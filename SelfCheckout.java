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
     * Generates resting probability, since this is a Self-checkout counter, it never rests.
     * @return -1
     */
    @Override
    double genRestProb(RandomGenerator rd) {
        return -1;
    }

    /**
     * Returns a String description of a SelfCheckout object.
     * Description contains the Selfcheckout label and its Id.
     * @return String representaton of a SelfCheckout object.
     */
    public String toString() {
        return String.format("self-check %d", getID());
    }
}
