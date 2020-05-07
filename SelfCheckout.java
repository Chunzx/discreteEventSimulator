package cs2030.simulator;

/**
 * A class representing a self-checkout counter.
 * A self-checkout counter can perform all functions a Server can perform, 
 * the only difference is it shares a unified queue with all other self-checkout counters.
 */
class SelfCheckout extends Server {

    /**
     *Queue number shared by all SelfCheckout counters.
     * Since this value is identical for all SelfCheckout objects, 
     * it is a static variable.
     */
    private static int queueId;
    private static boolean hasValue = false; 
    
    /**
     * Creates a new SelfCheckout object able to serve Customers immediately.
     * @param id ID of counter.
     */
    SelfCheckout(int id) {
        super(id);
    }


    /**
     * Creates a new Selfcheckout object able to serve Customers after a specified time.
     * @param id ID of counter.
     * @param time Time counter is available to serve Customers.
     */
    SelfCheckout(int id,double time) {
        super(id, time);
    }
    
    /**
     * Gets the lane number of the queue this counter is serving.
     * @return Lane number among all the queues in the store that the counter is serving.
     */
    @Override
    int getQueueId() {
        return queueId;
    }

    /**
     * Updates counter's next available time to serve a Customer.
     * @param time Next time the counter is available.
     * @return new SelfCheckout object.
     */
    @Override
    SelfCheckout setNextTime(double time) {
        return new SelfCheckout(getID(),time);
    }

    /**
     * Sets the value of queueID, if it hasn't been set yet.
     * @param id Queue id shared by all Self-Checkout counters.
     */
    static void setQueueId(int id) {
        if (!hasValue) {
            hasValue = true;
            queueId = id;
        }
    }
        

    /**
     * Generates resting probability. Since this is a Self-checkout counter, it never rests.
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
