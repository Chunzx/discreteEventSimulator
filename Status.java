package cs2030.simulator;

/**
 * An enum class to represent all the possible states of interaction 
 * between a Server and/or Customer.
 */
public enum Status {
    ARRIVED(0),
    WAIT(1),
    SERVED(2),
    DONE(3),
    LEAVE(4),
    BACK(5);

    private final int statusCode;

    Status(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getCode() {
        return statusCode;
    }
}
