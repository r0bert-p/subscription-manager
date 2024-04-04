package subscription.manager.exceptions;

/**
 * An IllegalSubscriptionQuantityException can be thrown if clerk attempts to add subscription of another meal for a subscriber that has already reached the limit of 3 different subscriptions.
 * @author Robert Petecki
 * @version 1.0 Date created: 01/11/2023
 */
public class IllegalSubscriptionQuantityException extends IllegalArgumentException {
    /**
     * Exception constructor specifying the message attached to this exception object.
     * @param message exception message attached to the exception object
     */
    public IllegalSubscriptionQuantityException(String message)
    {
        super(message);
    }
}
