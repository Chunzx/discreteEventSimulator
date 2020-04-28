package cs2030.simulator;
/**
 * A class to represent an Event between a Customer and a Server.
 * Created whenever a Customer waits, is served, or is done serving.
 */

class CustomerServerEvent implements Event {
    static final int WAIT = 1;
    static final int SERVED = 2;
    static final int DONE = 4;
    private final int id;
    private final int serverID;
    private final double time;
    private final int state;
    private final boolean isSelf;
    private final boolean isGreedy;

    /**
     * Creates a new CustomerServerEvent.
     * @param id Id of Customer.
     * @param time Time of Event.
     * @param serverID Server ID.
     * @param state State of CustomerServerEvent, can be WAIT (1), SERVED (2) or DONE (4).
     * @param isSelf Whether Server is a self-checkout machine.
     * @param isGreedy Whether Customer is greedy.
     */
    CustomerServerEvent(int id, double time, int serverID, int state, 
        boolean isSelf, boolean isGreedy) {
        this.id = id;
        this.serverID = serverID;
        this.time = time;
        this.state = state;
        this.isSelf = isSelf;
        this.isGreedy = isGreedy;
    }

    /**
     * Gets Id of Customer.
     * @return Id of Customer.
     */
    @Override
    public int getID() {
        return this.id;
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
     * Gets state of this CustomerServerEvent.
     * @return State of CustomerServerEvent, can be WAIT (1), SERVED (2) or DONE (4).
     */
    @Override 
    public int getState() {
        return state;
    }

    @Override
    public String toString() {
        if (!isSelf) {
            if (state == WAIT) {
                return isGreedy ?  String.format("%.3f %d(greedy) waits to be served by server %d", 
                time, id, serverID) : 
                    String.format("%.3f %d waits to be served by server %d", time, id, serverID);
            } else if (state == SERVED) {
                return isGreedy ? String.format("%.3f %d(greedy) served by server %d", 
                    time, id, serverID) : 
                    String.format("%.3f %d served by server %d", time, id, serverID);
            } else {
                return isGreedy ? String.format("%.3f %d(greedy) done serving by server %d", 
                    time, id, serverID) : 
                String.format("%.3f %d done serving by server %d", time, id, serverID);
            }
        } else {
            if (state == WAIT) {
                return isGreedy ? 
                String.format("%.3f %d(greedy) waits to be served by self-check %d", 
                    time, id, serverID) : 
                    String.format("%.3f %d waits to be served by self-check %d", 
                    time, id, serverID);
            } else if (state == SERVED) {
                return isGreedy ? String.format("%.3f %d(greedy) served by self-check %d", 
                time, id, serverID) :
                String.format("%.3f %d served by self-check %d", time, id, serverID);
            } else {
                return isGreedy ? String.format("%.3f %d(greedy) done serving by self-check %d", 
                time, id, serverID) : 
                String.format("%.3f %d done serving by self-check %d", time, id, serverID);
            }
        }
    }
}
