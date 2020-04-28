package cs2030.simulator;
/**
 * An interface that will be used in PriorityQueue.
 * Represent events generated when a Customer arrives, leaves, waits, is served or done serving.
 */

public interface Event {

    /**
     * Gets the ID of the Event.
     * @return ID of event.
     */
    int getID();

    /** 
     * Gets the state of the Event.
     * @return State of Event.
     */
    int getState();
    
    /**
     * Gets the time of the event.
     * @return Time of the Event.
     */
    double getTime();

}
