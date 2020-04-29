package cs2030.simulator;

/**
 * A class to represent an Event involving a Customer, but without a Server.
 * Created whenever a customer arrives or leaves.
 */
class CustomerEvent implements Event {
    static final int ARRIVED = 0;
    static final int LEAVE = 3;
    private final Customer customer;
    private final double time;
    private final int state;  

    /**
     * Creates a new CustomerEvent with id, time, state and whether it is greedy.
     * @param customer Customer in the Event.
     * @param time Time of the Event.
     * @param state State of the Event, ARRIVED (0) or LEAVE (3).
     */
    CustomerEvent(Customer customer, double time, int state) {
        this.customer = customer;
        this.time = time;
        this.state = state;
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
     * Gets Id of Customer.
     *@return Id of Customer.
     */
    @Override
    public int getID() {
        return customer.getID();
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
     * Returns a String description of the CustomerEvent
     * Description contains the time and Customer involved.
     * @return String representaton of CustomerEvent.
     */
    @Override
    public String toString() {
        if (state == ARRIVED) {
            return String.format("%.3f %s arrives", time, customer.toString());
        } else {
            return String.format("%.3f %s leaves", time, customer.toString());
        }
    }
}
