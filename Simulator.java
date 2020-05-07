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
        //instantiate queue number for all Selfcheckout counters
        SelfCheckout.setQueueId(servers + 1);
        //create self checkout counters
        for (int i = servers + 1; i <= servers + selfCheck; i++) {
            serverList.add(new SelfCheckout(i));
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
     * Simulate the flow of Events when Customers arrive and add all Events to
     * PriorityQueue pq.
     * @param count Number of Customers to be served.
     * @param limit Maximum queue length.
     * @param seed Seed for RandomGenerator object.
     * @param lambda Arrival rate.
     * @param mu Service rate.
     * @param rho Resting rate.
     * @param prob Resting probability.
     * @param greedyProb Probability of encountering a greedy Customer.
     * @return double Array which stores the number of Customers who left and total waiting time.
     */
    public double[] serve(int count, int limit, int seed, double lambda, 
            double mu, double rho, double prob, double greedyProb) {
        RandomGenerator rd = new RandomGenerator(seed, lambda, mu, rho);
        PriorityQueue<Contract> contracts = new PriorityQueue<>(1, new ContractComparator());
        double totalWaitingTime = 0.00;
        double customersLeft = 0.00;
        double currentTime = 0;
        for (int i = 1; i <= count; i++) {
            double greedy = rd.genCustomerType();
            boolean isGreedy = greedy < greedyProb;
            Customer c = isGreedy ? new GreedyCustomer(i, Status.ARRIVED, currentTime) 
                : new Customer(i, Status.ARRIVED, currentTime);
            Contract contract = new Contract(c);
            contracts.add(contract);
            currentTime += rd.genInterArrivalTime();
        }
        while (contracts.size() > 0) {
            Contract curr = contracts.poll();
            Customer c = curr.getCustomer();
            Status status = curr.getStatus();
            if (status == Status.ARRIVED) {
                double arrivalTime = c.getTime();
                System.out.println(String.format("%.3f %s arrives", arrivalTime, c));
                Server server = findServer(arrivalTime);
                //if Server is available, Customer is immediately served.
                if (server != null) {
                    Server s = server;
                    System.out.println(String.format("%.3f %s served by %s", arrivalTime, c, s));
                    c = c.setStatus(Status.SERVED, arrivalTime);
                    curr = new Contract(c, s);
                    contracts.add(curr);
                    continue;
                } else {
                    //if no Server is available, Customer will find the queue he wants to join 
                    //according to his preference (greedy or non-greedy).
                    Server s = c.findServer(serverList,queue,limit);
                    if (s != null) {
                        System.out.println(String.format("%.3f %s waits to be served by %s", 
                            arrivalTime, c, s));
                        c = c.setStatus(Status.WAIT, arrivalTime);
                        curr = new Contract(c,s);
                        contracts.add(curr);
                        continue;
                    }
                    //nothing left to do, CUstomer leaves.
                    System.out.println(String.format("%.3f %s leaves", arrivalTime, c));
                    customersLeft++;
                }
            } else if (status == Status.WAIT) { 
                //if Customer is waiting, add him to the queue of the server
                Server s = curr.getServer();
                queue.get(s.getQueueId()).add(c);
            } else if (status == Status.SERVED) {
                //if Customer is served, generate time he is done serving
                // and update the Server availabilty
                Server s = curr.getServer();
                double endTime = c.getTime() + rd.genServiceTime();
                s = s.setNextTime(endTime);
                //update serverList
                serverList.set(s.getID() - 1, s);
                c = c.setStatus(Status.DONE,endTime);
                Contract newContract = new Contract(c,s);
                contracts.add(newContract);
            } else if (status == Status.DONE) {
                //if Customer is done serving,
                //generate probability Server will rest,
                //and if the Server does not rest,
                //Serve the next Customer in line.
                //Otherwise update Server availability to time he is done resting.
                Server s = curr.getServer();
                double endTime = s.getTime();
                System.out.println(String.format("%.3f %s done serving by %s", endTime, c, s));
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
                if (queue.get(s.getQueueId()).size() > 0) {
                    List<Customer> next = queue.get(s.getQueueId());
                    Customer nextInLine = next.remove(0);
                    queue.put(s.getID(), next);
                    totalWaitingTime += endTime - nextInLine.getTime();
                    System.out.println(String.format("%.3f %s served by %s", 
                        endTime, nextInLine, s));
                    nextInLine = nextInLine.setStatus(Status.SERVED, endTime); 
                    Contract newContract = new Contract(nextInLine, s);
                    contracts.add(newContract);
                }
            } else if (status == Status.BACK) {
                //if Server is back from resting, serve the next Customer in line.
                Server s = curr.getServer();
                double endTime = s.getTime();
                if (queue.get(s.getQueueId()).size() > 0) {
                    List<Customer> next = queue.get(s.getQueueId());
                    Customer nextInLine = next.remove(0);
                    queue.put(s.getID(), next);
                    totalWaitingTime += endTime - nextInLine.getTime();
                    System.out.println(String.format("%.3f %s served by %s", 
                        endTime, nextInLine, s));
                    nextInLine = nextInLine.setStatus(Status.SERVED, endTime);
                    Contract newContract = new Contract(nextInLine, s);
                    contracts.add(newContract);
                }
            }
        }
        return new double[]{customersLeft, totalWaitingTime};
    }
}


