package cs2030.simulator;

import java.util.Map;
import java.util.List;

/**
 * A class representing a Customer.
 * A Customer has a status and an Event associated with it.
 */
class Customer {
    static final int ARRIVED = 0;
    static final int WAIT = 1;
    static final int SERVED = 2;
    static final int DONE = 3;
    static final int LEAVE = 4;
    private final int status;
    private final int id;
    private final double time;

    /**
     * Creates a Customer with an Id,status, time.
     * @param id Identification of Customer.
     * @param status Status is his most recent activity, i.e. arrival, wait, serve, done or leave.
     * @param time Time of his most recent activity, i.e. arrival, wait, serve, done or leave.
     */
    Customer(int id,int status, double time) {
        this.id = id;
        this.status = status;
        this.time = time;
    }

    /**
     *Sets status of Customer and time to nextStatus and nextTime.
     *@param nextStatus next Status of Customer.
     *@param nextTime Time of Customer's most recent activity.
     * @return Customer with updated time and status.
     */
    Customer setStatus(int nextStatus, double nextTime) {
        return new Customer(id, nextStatus, nextTime);
    }

    /**
     * Gets the time of Customer's most recent Event.
     *@return time of Customer's most recent Event.
     */
    double getTime() {
        return time;
    }

    /**
     * Gets the Id of Customer.
     *@return Id of Customer.
     */
    int getID() {
        return id;
    }

    /**
     * Finds the first server whose queue is not full.
     * @param servers List of Servers.
     * @param queue All queues in the store.
     * @param limit Maximum queue length
     * @return First server whose queue is not full, null if all are full.
     */
    Server findServer(List<Server> servers, Map<Integer, List<Customer>> queue, int limit) {
        for (int i = 0; i < servers.size(); i++) {
            if (queue.get(servers.get(i).getQueueID()).size() < limit) {
                return servers.get(i);
            }
        }
        return null;
    }

    /**
     * Gets the status of Customer.
     *@return status of Customer.
     */
    int getStatus() {
        return status;
    }

    /**
     * Returns a String representation of a Customer which is simply his Id.
     * @return Strng representaton of a Customer.
     */
    public String toString() {
        return String.format("%d", getID());
    }

}
