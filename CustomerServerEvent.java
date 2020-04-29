package cs2030.simulator;
/**
 * A class to represent an Event between a Customer and a Server.
 * Created whenever a Customer waits, is served, or is done serving.
 */

class CustomerServerEvent implements Event {
    static final int WAIT = 1;
    static final int SERVED = 2;
    static final int DONE = 4;
    private final Customer customer;
    private final Server server;
    private final double time;
    private final int state;

    /**
     * Creates a new CustomerServerEvent.
     * @param customer Customer involved in this Event.
     * @param time Time of Event.
     * @param server Server involved in this Event.
     * @param state State of CustomerServerEvent, can be WAIT (1), SERVED (2) or DONE (4).
     */
    CustomerServerEvent(Customer customer, double time, Server server, int state) {
        this.customer = customer;
        this.server = server;
        this.time = time;
        this.state = state;
    }

    /**
     * Gets time of Event.
     * @return Time of Event.
     */
    @Override
    public double getTime() {
        return this.time;
    }

    /**
     *Gets Id of Customer.
     * @return Id of Customer.
     */
    @Override
    public int getID() {
        return customer.getID();
    }

    /**
     * Gets state of this CustomerServerEvent.
     * @return State of CustomerServerEvent, can be WAIT (1), SERVED (2) or DONE (4).
     */
    @Override 
    public int getState() {
        return state;
    }

    @Override
    public String toString() {
        if (state == WAIT) {
            return String.format("%.3f %s waits to be served by %s", time, customer, server);
        } else if (state == SERVED) {
            return String.format("%.3f %s served by %s", time, customer, server);
        } else {
            return String.format("%.3f %s done serving by %s", time, customer, server);
        }
    }
}
