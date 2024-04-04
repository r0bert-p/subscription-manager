package subscription.manager.exceptions;

/**
 * An IllegalMealQuantityException can be thrown when either
 * there is not enough meals available to add to a subscription
 * or the quantity of meals to be removed exceeds the quantity subscribed.
 * @author Robert Petecki
 * @version 1.0 Date created: 01/11/2023
 */
public class IllegalMealQuantityException extends IllegalArgumentException {
    /**
     * Exception constructor specifying the message attached to this exception object.
     * @param message exception message attached to the exception object
     */
    public IllegalMealQuantityException(String message)
    {
        super(message);
    }
}
