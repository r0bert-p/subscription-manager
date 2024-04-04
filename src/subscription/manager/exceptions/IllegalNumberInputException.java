package subscription.manager.exceptions;

/**
 * An IllegalNumberInputException can be thrown when the number from clerk's keyboard input is not a positive number and a positive number is expected (adding or removing subscriptions).
 * @author Robert Petecki
 * @version 1.0 Date created: 01/11/2023
 */
public class IllegalNumberInputException extends IllegalArgumentException {
    /**
     * Exception constructor specifying the message attached to this exception object.
     * @param message exception message attached to the exception object
     */
    public IllegalNumberInputException(String message)
    {
        super(message);
    }
}
