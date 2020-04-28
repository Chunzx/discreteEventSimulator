package cs2030.simulator;

import java.util.Comparator;

/**
 * Comparator class to compare different Contracts.
 */
class ContractComparator implements Comparator<Contract> {
    static final int ARRIVED = 0;
    static final int WAIT = 1;
    static final int SERVED = 2;
    static final int DONE = 3;
    static final int LEAVE = 4;

    /**
     * A contract may not contain a Customer. 
     * If both contracts have no customers, compare the time of the Server's next available time. 
     * If they are equal, return 0.
     * If only one Contract has no Customer, compare the time of the Server's next available time 
     * with time of Customer's most recent Event.
     * Otherwise, compare the time of the Customer's most recent Event.
     * If the time is the same, Customer id is compared.
     * If the id is the same, Customer status is compared.
     * @param a Contract
     * @param b Other Contract
     * @return -1 If a occured before b, 1 if a occured after b, 0 otherwise.
     */
    public int compare(Contract a, Contract b) {
        Customer firstCustomer = a.getCustomer();
        Customer secondCustomer = b.getCustomer();
        double at = a.getTime();
        double bt = b.getTime();
        if (firstCustomer == null && secondCustomer == null) {
            return at - bt == 0.00 ? 0 : at < bt ? -1 : 1;
        } else if (firstCustomer == null) {
            return at - bt == 0.00 ? 0 : at < bt ? -1 : 1;
        } else if (secondCustomer == null) {
            return at - bt == 0.00 ? 0 : at < bt ? -1 : 1;
        } else if (firstCustomer != null && secondCustomer != null) {
            if (at - bt != 0) {
                return at < bt ? -1 : 1;
            }
            double firstId = firstCustomer.getID();
            double secondId = secondCustomer.getID();
            if (firstId - secondId != 0) {
                return firstId < secondId ? -1 : 1;
            }
            int firstStatus = a.getStatus();
            int secondStatus = b.getStatus();
            if (firstStatus - secondStatus != 0) {
                return firstStatus < secondStatus ? -1 : 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
}

