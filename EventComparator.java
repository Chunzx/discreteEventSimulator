package cs2030.simulator;

import java.util.Comparator;
/**
 * Comparator class to compare the events based on their time and order.
 */

public class EventComparator implements Comparator<Event> {
    /**
     * Compares two different Events to see which occured first.
     * First compare the time of the two Events.
     * If the time is the same, compare the ID of the Events.
     * If the ID is the same, compare the state of the Events.
     * @param a The first event.
     * @param b The second event.
     * @return -1 If a occured first or 1 if b occured first, 0 if they occur at the same time.
     */
    public int compare(Event a, Event b) {
        double atime = a.getTime();
        double btime = b.getTime();
        double difference = atime - btime;
        int aid = a.getID();
        int bid = b.getID();
        if (difference != 0) {
            return atime < btime ? -1 : 1;
        } else if (aid - bid == 0) {
            return a.getState() < b.getState() ? -1 : 1;
        } else if (aid - bid == 0) {
            return 0;
        } else {
            return aid < bid ? -1 : 1;
        }
    }
}

