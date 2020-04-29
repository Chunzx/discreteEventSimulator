package cs2030.simulator;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.PriorityQueue;

/**
 * A class for discrete event simulation.
 * Simulates Customers arriving and being served by Servers.
 */
public class Simulator {
    public static final int ARRIVED = 0;
    public static final int WAIT = 1;
    public static final int SERVED = 2;
    public static final int LEAVE = 3;
    public static final int DONE = 4;
    public static final int BACK = 5;
    private final List<Server> serverList;
    private final HashMap<Integer, List<Customer>> queue;

    /**
     * Creates a Simulator.
     * @param servers Number of servers.
     */
    public Simulator(int servers) {
        serverList = new ArrayList<>();
        queue = new HashMap<>();
        for (int i = 1; i <= servers; i++) {
            serverList.add(new Server(i));
            queue.put(i, new LinkedList<Customer>());
        }
    }

    /**
     * Creates a Simulator.
     * @param servers Number of servers.
     * @param selfCheck Number of self-checkout counters.
     */
    public Simulator(int servers, int selfCheck) {
        serverList = new ArrayList<>();
        queue = new HashMap<>();
        for (int i = 1; i <= servers; i++) {
            serverList.add(new Server(i));
            queue.put(i, new LinkedList<Customer>());
        }
        //add a unified queue for self checkout counters
        queue.put(servers + 1, new LinkedList<Customer>());
        //create self checkout counters
        for (int i = servers + 1; i <= servers + selfCheck; i++) {
            serverList.add(new SelfCheckout(i, servers + 1));
        }
    }

    /**
     * Finds the first available Server that can serve a Customer at the current time.
     * @param currentTime current time.
     * @return first available Server, null if all are unavailable.
     */
    public Server findServer(double currentTime) {
        for (int i = 0; i < serverList.size(); i++) {
            if (serverList.get(i).canServe(currentTime)) {
                return serverList.get(i);
            }
        }
        return null;
    }

    /**
     * Simulate the flow of Events when Customers arrive and add all Events to PriorityQueue.
     * @param count Number of Customers to be served.
     * @param limit Maximum queue length.
     * @param pq PriorityQueue of Events.
     * @param seed Seed for RandomGenerator object.
     * @param lambda Arrival rate.
     * @param mu Service rate.
     * @param rho Resting rate.
     * @param prob Resting probability.
     * @param greedyProb Probability of encountering a greedy Customer.
     * @return double Array which stores the number of Customers who left and total waiting time.
     */
    public double[] serve(int count, int limit, PriorityQueue<Event> pq, int seed, double lambda, 
            double mu, double rho, double prob, double greedyProb) {
        RandomGenerator rd = new RandomGenerator(seed, lambda, mu, rho);
        PriorityQueue<Contract> contracts = new PriorityQueue<>(1, new ContractComparator());
        double totalWaitingTime = 0.00;
        double customersLeft = 0.00;
        double currentTime = 0;
        for (int i = 1; i <= count; i++) {
            double greedy = rd.genCustomerType();
            boolean isGreedy = greedy < greedyProb;
            Customer c = isGreedy ? new GreedyCustomer(i, ARRIVED, currentTime) 
                : new Customer(i, ARRIVED, currentTime);
            Event arrival = new CustomerEvent(c,currentTime,ARRIVED);
            pq.add(arrival);
            Contract contract = new Contract(c);
            contracts.add(contract);
            currentTime += rd.genInterArrivalTime();
        }
        while (contracts.size() > 0) {
            Contract curr = contracts.peek();
            Customer c = curr.getCustomer();
            int status = curr.getStatus();
            if (status == ARRIVED) {
                double arrivalTime = curr.getTime();
                Server server = findServer(arrivalTime);
                //if Server is available, Customer is immediately served.
                if (server != null) {
                    Server s = server;
                    Event serve = new CustomerServerEvent(c, arrivalTime, s, SERVED);
                    pq.add(serve);
                    c = c.setStatus(SERVED, arrivalTime);
                    curr = new Contract(c, s);
                    contracts.poll();
                    contracts.add(curr);
                    continue;
                } else {
                    //if no Server is available, Customer will find the queue he wants to join 
                    //according to his preference (greedy or non-greedy).
                    Server s = c.findServer(serverList,queue,limit);
                    if (s != null) {
                        Event wait = new CustomerServerEvent(c, arrivalTime, s, WAIT);
                        pq.add(wait);
                        c = c.setStatus(WAIT, arrivalTime);
                        curr = new Contract(c,s);
                        contracts.poll();
                        contracts.add(curr);
                        continue;
                    }
                    //nothing left to do, remove from contracts
                    contracts.poll();
                    Event leave = new CustomerEvent(c,arrivalTime,LEAVE);
                    pq.add(leave);
                    customersLeft++;
                }
            } else if (status == WAIT) { 
                //if Customer is waiting, add him to the queue of the server
                Server s = curr.getServer();
                queue.get(s.getQueueID()).add(c);
                contracts.poll();
            } else if (status == SERVED) {
                Server s = curr.getServer();
                double endTime = c.getTime() + rd.genServiceTime();
                Event doneServe = new CustomerServerEvent(c, endTime,s,DONE);
                pq.add(doneServe);
                s = s.setNextTime(endTime);
                //update serverList
                serverList.set(s.getID() - 1, s);
                c = c.setStatus(DONE,endTime);
                Contract newContract = new Contract(c,s);
                contracts.poll();
                contracts.add(newContract);
            } else if (status == DONE) {
                Server s = curr.getServer();
                double endTime = s.getTime();
                contracts.poll();
                double restProb = s.genRestProb(rd);
                if (restProb != -1 && restProb < prob) {    
                    double nextTime = endTime + rd.genRestPeriod();
                    s = s.setNextTime(nextTime); //rest until nextTime
                    serverList.set(s.getID() - 1, s);
                    //create new contract with resting Server
                    Contract newContract = new Contract(s);
                    contracts.add(newContract);
                    continue;
                }
                if (queue.get(s.getQueueID()).size() > 0) {
                    List<Customer> next = queue.get(s.getQueueID());
                    Customer nextInLine = next.remove(0);
                    queue.put(s.getID(), next);
                    totalWaitingTime += endTime - nextInLine.getTime();
                    Event serve = new CustomerServerEvent(nextInLine, endTime,s,SERVED);
                    pq.add(serve);
                    nextInLine = nextInLine.setStatus(SERVED, endTime); 
                    Contract newContract = new Contract(nextInLine, s);
                    contracts.add(newContract);
                }
            } else if (status == BACK) {
                Server s = curr.getServer();
                contracts.poll();
                double endTime = s.getTime();
                if (queue.get(s.getQueueID()).size() > 0) {
                    List<Customer> next = queue.get(s.getQueueID());
                    Customer nextInLine = next.remove(0);
                    queue.put(s.getID(), next);
                    totalWaitingTime += endTime - nextInLine.getTime();
                    Event serve = new CustomerServerEvent(nextInLine,endTime,s,SERVED);
                    pq.add(serve);
                    nextInLine = nextInLine.setStatus(SERVED, endTime);
                    Contract newContract = new Contract(nextInLine, s);
                    contracts.add(newContract);
                }
            }
        }
        return new double[]{customersLeft, totalWaitingTime};
    }
}


