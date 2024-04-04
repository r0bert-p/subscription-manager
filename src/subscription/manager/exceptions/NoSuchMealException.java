package subscription.manager.exceptions;

import java.util.NoSuchElementException;

/**
 * A NoSuchMealException can be thrown when the meal from clerk's keyboard input is not a valid offered meal.
 * @author Robert Petecki
 * @version 1.0 Date created: 01/11/2023
 */
public class NoSuchMealException extends NoSuchElementException {
    /**
     * Exception constructor specifying the message attached to this exception object.
     * @param message exception message attached to the exception object
     */
    public NoSuchMealException(String message)
    {
        super(message);
    }
}