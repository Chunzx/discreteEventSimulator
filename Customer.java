package cs2030.simulator;

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
    private final Event currentEvent;
    private final boolean isGreedy;

    /**
     *Creates a Customer with an Id,status, Event.
     *Customer can be greedy or non-greedy.
     */
    Customer(int id,int status, Event first, boolean isGreedy) {
        this.id = id;
        this.status = status;
        this.currentEvent = first;
        this.isGreedy = isGreedy;
    }

    /**
     *Sets status of Customer and Event to nextStatus and current.
     *@param nextStatus next Status of Customer.
     *@param nextEvent next Event of customer.
     */
    Customer setStatus(int nextStatus, Event nextEvent) {
        return new Customer(id, nextStatus, nextEvent, isGreedy);
    }

    /**
     * Gets the time of Customer's most recent Event.
     *@return time of Customer's most recent Event.
     */
    double getTime() {
        return currentEvent.getTime();
    }

    /**
     * Gets the Id of Customer.
     *@return Id of Customer.
     */
    int getID() {
        return id;
    }

    /**
     * Gets the status of Customer.
     *@return status of Customer.
     */
    int getStatus() {
        return status;
    }

    /**
     * Checks whether Customer is greedy.
     *@return whether Customer is greedy
     */
    boolean isGreedy() {
        return isGreedy;
    }

    /**
     * Gets the most recent Event.
     *@return Customer's most recent Event.
     */
    Event getEvent() {
        return currentEvent;
    }
}
