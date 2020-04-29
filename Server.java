package cs2030.simulator;

/**
 * A class representing a Server.
 */
class Server {
    private final int id;
    private final double time;
    
    /**
     * Creates a Server that is able to serve immediately.
     * @param id Id of Server.
     */
    Server(int id) {
        this.id = id;
        this.time = 0;
    }

    /**
     * Creates a Server able to serve Customers after a specified time.
     * @param id Id of Server.
     * @param time Time Server is available.
     */
    Server(int id, double time) {
        this.id = id;
        this.time = time;
    }
    
    /**
     * Gets next available time this Server can serve a Customer.
     * @return Time server can serve the next Customer.
     */
    double getTime() {
        return time;
    }

    /**
     * Gets Id of Server.
     * @return Id of Server.
     */
    int getID() {
        return id;
    }

    /**
     * Gets the lane number of the queue this Server is serving.
     * @return Which lane among all the queues this Server is serving.
     */
    int getQueueID() {
        return id;
    }

    /**
     * Sets the next available time of this Server.
     * @param time Next available time.
     * @return A new Server with the next available time updated.
     */
    Server setNextTime(double time) {
        return new Server(id, time);
    }

    /**
     * Checks if Server is available to serve at the curent time.
     * @param currentTime current time.
     * @return true if Server is available.
     */
    boolean canServe(double currentTime) {
        return time <= currentTime;
    }

    /**
     *Generates probability that this Server will rest.
     *@param rd RandomGenerator object used for generating probability.
     *@return Probability Server will rest.
     */
    double genRestProb(RandomGenerator rd) {
        return rd.genRandomRest();
    }

    public String toString() {
        return String.format("server %d",id);
    }
}


