package cs2030.simulator;

/**
 * A class representing a self-checkout counter.
 * A self-checkout counter can perform all functions a Server can perform, 
 * the only difference is it shares a unified queue with all other self-checkout counters.
 */
class SelfCheckout extends Server {
    private final int queueID;
    
    /**
     * Creates a new SelfCheckout object able to serve Customers immediately.
     * @param id ID of counter.
     * @param queueID Lane number that the counter is serving.
     */
    SelfCheckout(int id, int queueID) {
        super(id);
        this.queueID = queueID;
    }

    /**
     * Creates a new Selfcheckout object able to serve Customers after a specified time.
     * @param id ID of counter.
     * @param queueID Lane number that the counter is serving.
     * @param time Time counter is available to serve Customers.
     */
    SelfCheckout(int id, int queueID, double time) {
        super(id, time);
        this.queueID = queueID;
    }
    
    /**
     * Gets the lane number of the queue this counter is serving.
     * @return Lane number among all the queues in the store that the counter is serving
     */
    @Override
    int getQueueID() {
        return queueID;
    }

    /**
     * Updates counter's next available time to serve a Customer.
     * @param time Next time the counter is available.
     * @return new SelfCheckout object.
     */
    @Override
    SelfCheckout setNextTime(double time) {
        return new SelfCheckout(getID(),queueID,time);
    }

    /**
     * Checks whether the Server is a self-checkout object.
     * @return true since this is a selfCheckout object.
     */
    @Override
    boolean isSelf() {
        return true;
    }
}
