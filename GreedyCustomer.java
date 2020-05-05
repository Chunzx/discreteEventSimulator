package cs2030.simulator;

import java.util.List;
import java.util.Map;

/**
 * A GreedyCustomer is a Customer that looks for the shortest queue to join.
 */
class GreedyCustomer extends Customer {

    /**
     * Creates a GreedyCustomer with an Id,status, time.
     * @param id Identification of Customer.
     * @param status Status is his most recent activity, i.e. arrival, wait, serve, done or leave.
     * @param time Time of his most recent activity, i.e. arrival, wait, serve, done or leave.
     */
    GreedyCustomer(int id, int status, double time) {
        super(id,status,time);
    }


    @Override
    GreedyCustomer setStatus(int nextStatus, double nextTime) {
        return new GreedyCustomer(getID(), nextStatus, nextTime);
    }

    /**
     * Since this is a GreedyCustomer, findServer finds the Server with smallest queue size.
     * @param servers List of Servers.
     * @param queue All queues in the store.
     * @param limit Maximum queue length.
     * @return Server with smallest queue length, null if all are full.
     */
    @Override
    Server findServer(List<Server> servers, Map<Integer,List<Customer>> queue, int limit) {
        int leastQueue = -1;
        for (int i = 0; i < servers.size(); i++) {
            if (queue.get(servers.get(i).getQueueID()).size() < limit) {
                if (leastQueue == -1) {
                    leastQueue = servers.get(i).getQueueID();
                } else {
                    leastQueue = queue.get(servers.get(i).getQueueID()).size() < 
                    queue.get(leastQueue).size() ? 
                    servers.get(i).getQueueID() : leastQueue;
                }
            }
        }
        return leastQueue == -1 ? null : servers.get(leastQueue - 1);
    }

    /**
     * Returns a String representation of a GreedyCustomer which contains his Id and greedy label.
     * @return String representaton of GreedyCustomer.
     */
    @Override
    public String toString() {
        return String.format("%d(greedy)", getID());
    }
}
