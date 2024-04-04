package subscription.manager.exceptions;

import java.util.NoSuchElementException;

/**
 * A NoSuchSubscriberException can be used when the subscriber from clerk's keyboard input is not a valid registered subscriber.
 * @author Robert Petecki
 * @version 1.0 Date created: 01/11/2023
 */
public class NoSuchSubscriberException extends NoSuchElementException {
    /**
     * Exception constructor specifying the message attached to this exception object.
     * @param message exception message attached to the exception object
     */
    public NoSuchSubscriberException(String message)
    {
        super(message);
    }
}
