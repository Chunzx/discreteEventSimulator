package cs2030.simulator;

/**
 * A class to represent an Event involving a Customer, but without a Server.
 * Created whenever a customer arrives or leaves.
 */
class CustomerEvent implements Event {
    static final int ARRIVED = 0;
    static final int LEAVE = 3;
    private final int id;
    private final double time;
    private final int state;  
    private final boolean isGreedy;

    /**
     * Creates a new CustomerEvent with id, time, state and whether it is greedy.
     * @param id Id of Customer.
     * @param time Time of Event.
     * @param state State of the Event, ARRIVED (0) or LEAVE (3).
     * @param isGreedy whether Customer is greedy.
     */
    CustomerEvent(int id, double time, int state, boolean isGreedy) {
        this.id = id;
        this.time = time;
        this.state = state;
        this.isGreedy = isGreedy;
    }

    /**
     * Gets Id of Customer.
     * @return id of CustomerEvent.
     */
    @Override
    public int getID() {
        return this.id;
    }

    /**
     * Gets time of Event.
     * @return Time of CustomerEvent.
     */
    @Override
    public double getTime() {
        return this.time;
    }

    /**
     * Gets state of Event.
     * @return state of CustomerEvent.
     */
    @Override
    public int getState() {
        return state;
    }

    /**
     * Checks whether Customer is greedy.
     * @return whether Customer is greedy.
     */
    public boolean isGreedy() {
        return isGreedy;
    }

    @Override
    public String toString() {
        if (isGreedy) {
            if (state == LEAVE) {
                return String.format("%.3f %d(greedy) leaves", time, id);
            } else {
                return String.format("%.3f %d(greedy) arrives", time, id);
            }
        }
        if (state == LEAVE) {
            return String.format("%.3f %d leaves", time, id);
        } else {
            return String.format("%.3f %d arrives", time, id);
        }
    }
}
