package subscription.manager.exceptions;

import java.util.NoSuchElementException;

/**
 * A NoSuchSubscriptionException can be thrown if a subscription from clerk's keyboard input is not a valid existing subscription.
 * @author Robert Petecki
 * @version 1.0 Date created: 01/11/2023
 */
public class NoSuchSubscriptionException extends NoSuchElementException {
    /**
     * Exception constructor specifying the message attached to this exception object.
     * @param message exception message attached to the exception object
     */
    public NoSuchSubscriptionException(String message)
    {
        super(message);
    }
}
