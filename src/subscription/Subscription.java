package subscription;

/**
 * A Subscription can be created by clerk's interaction with the program and has subscriber's full name,
 * subscribed meal name, and quantity of the specified meal.
 * @author Robert Petecki
 * @version 1.0 Date created: 01/11/2023
 */
public class Subscription extends Subscriber {

    /**
     * name of the meal type in the subscription
     */
    private final String mealSubscribed;
    /**
     * quantity of the meals of a particular meal type in the subscription
     */
    private int mealSubscribedBalance;

    //Constructor
    /**
     * Subscription constructor specifying subscriber's full name, meal type and it's quantity in the subscription.
     * @param firstName first name of the subscriber
     * @param surname last name of the subscriber
     * @param mealName name of the meal type subscribed to
     * @param mealQuantity quantity of meals of that meal type subscribed to
     */
    public Subscription(String firstName, String surname, String mealName, int mealQuantity)
    {
        super(firstName, surname);
        mealSubscribed = mealName;
        mealSubscribedBalance = mealQuantity;
    }

    //Methods
    //getters
    /**
     * Getter for the name of the meal type.
     * @return String name of the meal type
     */
    public String getMealSubscribed() {
        return mealSubscribed;
    }

    /**
     * Getter for quantity of meals subscribed to.
     * @return int quantity of meals of particular type subscribed to
     */
    public int getMealSubscribedBalance()
    {
        return mealSubscribedBalance;
    }

    //setter
    /**
     * Changes quantity of meals subscribed to.
     * @param mealQuantity new quantity of meals subscribed to.
     */
    public void setMealSubscribedBalance(int mealQuantity) {
        mealSubscribedBalance = mealQuantity;
    }

    //toString
    /**
     * Returns full name of the subscriber, meal type name and the quantity of meals of that type in the subscription.
     * @return String in format: "Subscriber: Full Name Subscriptions: meal mealQuantity
     */
    @Override
    public String toString()
    {
        return "Subscriber: " + getFirstName() + " " + getSurname() + " Subscriptions: " + mealSubscribed +  " " + mealSubscribedBalance;
    }

    //equals
    /**
     * Checks if Subscription objects are the same based on the subscriber's full name and meal type name, i.e. logical equivalence.
     * @param otherObject other Subscription object to check against
     * @return boolean true if Subscription objects are the same based on their field variables, false if not matched
     */
    @Override
    public boolean equals(Object otherObject) {
        Subscription other = (Subscription) otherObject;
        return getFirstName().equals(other.getFirstName())
                && getSurname().equals(other.getSurname())
                && mealSubscribed.equals(other.mealSubscribed);
    }
}
