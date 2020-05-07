package cs2030.simulator;
/**
 *A Contract is an interaction between a Customer and a Server.
 */

class Contract {
    static final int BACK = 5;
    private final Server s;
    private final Customer c;
    
    /**
     *Creates a Contract without a Server yet.
     */
    Contract(Customer c) {
        this.s = null;
        this.c = c;
    }
    
    /**
     *Creates a Contract where a Server is assigned to a Customer.
     */
    Contract(Customer c, Server s) {
        this.s = s;
        this.c = c;
    }
    
    /**
     *Creates a Contract with a Server without a Customer yet.
     */
    Contract(Server s) {
        this.s = s;
        this.c = null;
    }
    
    /**
     * Gets the Server associated with this Contract.
     @return Server
     */
    Server getServer() {
        return s;
    }

    /**
     * Gets the Customer associated with this Contract.
     @return Customer
     */
    Customer getCustomer() {
        return c;
    }

    /**
     *Assigns a new Server to the current Customer.
     * @param newS Server to be assigned to.
     */
    Contract setServer(Server newS) {
        return new Contract(c, newS);
    }

    /**
     * Gets the time of this Contract.
     *@return Time of this Contract.
     */
    double getTime() {
        return c == null ? s.getTime() : c.getTime();
    }

    /**
     * Gets the Id of the Customer, -1 if there is no Customer.
     *@return Id of Customer.
     */
    int getID() {
        return c == null ? -1 : c.getID();
    }

    /**
     * Gets the status of the Contract.
     *@return status of Contract.
     */
    Status getStatus() {
        if (c == null) {
            return Status.BACK;
        }
        return c.getStatus();
    }
}
