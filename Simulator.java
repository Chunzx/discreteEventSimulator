package cs2030.simulator;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.PriorityQueue;

public class Simulator {
    public static final int ARRIVED = 0;
    public static final int WAIT = 1;
    public static final int SERVED = 2;
    public static final int LEAVE = 3;
    public static final int DONE = 4;
    public static final int BACK = 5;
    private final List<Server> serverList;
    private final List<Server> selfCheckout;
    private final HashMap<Integer, List<Customer>> queue;

    /**
     * Creates a Simulator.
     * @param servers Number of servers.
     */
    public Simulator(int servers) {
        serverList = new ArrayList<>();
        selfCheckout = new ArrayList<>();
        queue = new HashMap<>();
        for (int i = 1; i <= servers; i++) {
            serverList.add(new Server(i));
            queue.put(i, new LinkedList<Customer>());
        }
    }

    /**
     * Creatres a Simulator.
     * @param servers Number of servers.
     * @param selfCheck NUmber of self-checkout counters.
     */
    public Simulator(int servers, int selfCheck) {
        serverList = new ArrayList<>();
        selfCheckout = new ArrayList<>();
        queue = new HashMap<>();
        for (int i = 1; i <= servers; i++) {
            serverList.add(new Server(i));
            queue.put(i, new LinkedList<Customer>());
        }
        //add a unified queue for self checkout counters
        queue.put(servers + 1, new LinkedList<Customer>());
        //create self checkout counters
        for (int i = servers + 1; i <= servers + selfCheck; i++) {
            selfCheckout.add(new SelfCheckout(i, servers + 1));
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
     * Fnds the first available SelfCheckout counter at the current time.
     * @param currentTime current time.
     * @return First available self-checkout counter, null if all are unavailable.
     */
    public Server findSelfCheckout(double currentTime) {
        for (int i = 0; i < selfCheckout.size(); i++) {
            if (selfCheckout.get(i).canServe(currentTime)) { 
                return selfCheckout.get(i);
            }
        }
        return null;
    }

    /**
     * Finds the first Server whose queue is not full.
     * @param limit Maximum queue length
     * @return first Server whose queue is not full, null if all are full.
     */
    public Server findNotFullServer(int limit) {
        for (int i = 0; i < serverList.size(); i++) {
            if (queue.get(serverList.get(i).getQueueID()).size() < limit) {
                return serverList.get(i);
            }
        }
        return null;
    }

    /**
     * Finds the first SelfCheckout counter whose queue is not full.
     * Note that all counters serve the same queue, 
     * hence if the first counter is full, all other counters are full too.
     * @param limit Maximum queue length.
     * @return first SelfCheckout whose queue is not full.
     */
    public Server findNotFullSelfCheckout(int limit) {
        for (int i = 0; i < selfCheckout.size(); i++) {
            if (queue.get(selfCheckout.get(i).getQueueID()).size() < limit) {
                return selfCheckout.get(i);
            }
        }
        return null;
    }

    /**
     * Finds the Server or Selfcheckout counter whose queue size is the smallest.
     * If two or more Servers or Selfcheckout have the same queue size, return the first one.
     * @param limit Maximum queue length.
     * @return Server or Selfcheckout with least number of Customers in the queue, null if all full.
     */
    public Server findLeastQueue(int limit) {
        int leastQueue = -1;
        for (int i = 0; i < serverList.size(); i++) {
            if (queue.get(serverList.get(i).getQueueID()).size() < limit) {
                if (leastQueue == -1) {
                    leastQueue = serverList.get(i).getQueueID();
                } else {
                    leastQueue = queue.get(serverList.get(i).getQueueID()).size() < 
                    queue.get(leastQueue).size() ? 
                    serverList.get(i).getQueueID() : leastQueue;
                }
            }
        }
        for (int i = 0; i < selfCheckout.size(); i++) {
            if (queue.get(selfCheckout.get(i).getQueueID()).size() < limit) {
                if (leastQueue == -1) {
                    leastQueue = selfCheckout.get(i).getQueueID();
                } else {
                    leastQueue = queue.get(selfCheckout.get(i).getQueueID()).size() < 
                    queue.get(leastQueue).size() ? 
                    selfCheckout.get(i).getQueueID() : leastQueue;
                }
            } else {
                break;
            }
        }
        return leastQueue == -1 ? null : leastQueue <= serverList.size() ? 
        serverList.get(leastQueue - 1) : selfCheckout.get(0);
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
            Event arrival = new CustomerEvent(i,currentTime, ARRIVED, isGreedy);
            pq.add(arrival);
            Customer c = new Customer(i, ARRIVED, arrival, isGreedy);
            Contract contract = new Contract(c);
            contracts.add(contract);
            currentTime += rd.genInterArrivalTime();
        }
        while (contracts.size() > 0) {
            Contract curr = contracts.peek();
            Customer c = curr.getCustomer();
            int status = curr.getStatus();
            if (status == ARRIVED) {
                boolean isGreedy = c.isGreedy();
                double arrivalTime = c.getTime();
                Server server = findServer(arrivalTime);
                if (server == null) {
                    server = findSelfCheckout(arrivalTime);
                }
                //if server is available, Customer is immediately served
                if (server != null) {
                    Server s = server;
                    Event serve = new CustomerServerEvent(c.getID(), arrivalTime, 
                        s.getID(), SERVED, s.isSelf(), isGreedy);
                    pq.add(serve);
                    c = c.setStatus(SERVED, serve);
                    curr = new Contract(c, s);
                    contracts.poll();
                    contracts.add(curr);
                    continue;
                } else {
                    Server notFull = null;
                    if (isGreedy) {
                        notFull = findLeastQueue(limit);
                    } else {
                        notFull = findNotFullServer(limit);
                        if (notFull == null) {
                            notFull = findNotFullSelfCheckout(limit);
                        }
                    }
                    if (notFull != null) {
                        Server s = notFull;
                        Event wait = new CustomerServerEvent(c.getID(), arrivalTime, 
                            s.getQueueID(), WAIT, s.isSelf(), isGreedy);
                        pq.add(wait);
                        c = c.setStatus(WAIT, wait);
                        curr = new Contract(c,s);
                        contracts.poll();
                        contracts.add(curr);
                        continue;
                    } else {
                        //nothing left to do, remove from contracts
                        contracts.poll();
                        Event leave = new CustomerEvent(c.getID(),arrivalTime,LEAVE, isGreedy);
                        pq.add(leave);
                        customersLeft++;
                    }
                }
            } else if (status == WAIT) { 
                //if Customer is waiting, add him to the queue of the server
                Server s = curr.getServer();
                queue.get(s.getQueueID()).add(c);
                contracts.poll();
            } else if (status == SERVED) {
                boolean isGreedy = c.isGreedy();
                Server s = curr.getServer();
                double endTime = c.getTime() + rd.genServiceTime();
                Event doneServe = new CustomerServerEvent(c.getID(), endTime, 
                    s.getID(),DONE, s.isSelf(), isGreedy);
                pq.add(doneServe);
                s = s.setNextTime(endTime);
                if (!s.isSelf()) {
                    serverList.set(s.getID() - 1, s);
                } else {
                    int position = s.getID() - (serverList.size() + 1); //position in ArrayList
                    selfCheckout.set(position, s);
                }
                c = c.setStatus(DONE,doneServe);
                Contract newContract = new Contract(c,s);
                contracts.poll();
                contracts.add(newContract);
            } else if (status == DONE) {
                Server s = curr.getServer();
                double endTime = s.getTime();
                contracts.poll();
                if (!s.isSelf()) {
                    double restProb = rd.genRandomRest();
                    if (restProb < prob) {
                        double nextTime = endTime + rd.genRestPeriod();
                        s = s.setNextTime(nextTime); //rest until nextTime
                        if (!s.isSelf()) {
                            serverList.set(s.getID() - 1, s);
                        } else {
                            int position = s.getID() - (serverList.size() + 1); 
                            selfCheckout.set(position, s);
                        }
                        //create new contract with resting Server
                        Contract newContract = new Contract(s);
                        contracts.add(newContract);
                        continue;
                    }
                }
                if (queue.get(s.getQueueID()).size() > 0) {
                    List<Customer> next = queue.get(s.getQueueID());
                    Customer nextInLine = next.remove(0);
                    boolean isGreedy = nextInLine.isGreedy();
                    queue.put(s.getID(), next);
                    totalWaitingTime += endTime - nextInLine.getTime();
                    Event serve = new CustomerServerEvent(nextInLine.getID(), endTime, 
                        s.getID(),SERVED, s.isSelf(), isGreedy);
                    pq.add(serve);
                    nextInLine = nextInLine.setStatus(SERVED, serve); 
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
                    boolean isGreedy = nextInLine.isGreedy();
                    queue.put(s.getID(), next);
                    totalWaitingTime += endTime - nextInLine.getTime();
                    Event serve = new CustomerServerEvent(nextInLine.getID(), endTime, 
                        s.getID(),SERVED, s.isSelf(), isGreedy);
                    pq.add(serve);
                    nextInLine = nextInLine.setStatus(SERVED, serve);
                    Contract newContract = new Contract(nextInLine, s);
                    contracts.add(newContract);
                }
            }
        }
        return new double[]{customersLeft, totalWaitingTime};
    }
}


