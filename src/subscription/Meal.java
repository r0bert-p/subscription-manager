package subscription;

/**
 * A Meal is read from the input file and has a name of the meal type and changeable balance of available meals of the particular meal type.
 * <p>Meal implements Comparable interface with overridden compareTo(Meal m) to allow for lexicographic sorting if meals are stored in the {@link subscription.manager.SortedLinkedList sorted linked list}.
 * <p>Balance of particular meal type can be changed with setMealBalance method which is called in the MainProgram class when subscribing or unsubscribing to meals.
 * @author Robert Petecki
 * @version 1.0 Date created: 01/11/2023
 */
public class Meal implements Comparable<Meal> {
    //Fields
    /**
     * Name of the meal type.
     */
    private final String mealName;
    /**
     * Balance of the meals available.
     */
    private int mealBalance;

    //Constructors

    /**
     * Meal constructor specifying meal type name and available balance of that meal type.
     * @param name name of the meal type
     * @param balance available quantity of the meals of that meal type
     */
    public Meal(String name, int balance)
    {
        mealName = name;
        mealBalance = balance;
    }

    /**
     * Meal constructor specifying only meal type name, useful when validating input meal type name.
     * @param name name of the meal type
     */
    public Meal(String name)
    {
        mealName = name;
    }

    //Methods
    //setter
    /**
     * Changes balance of available meals.
     * @param balance new balance of the meals available
     */
    public void setMealBalance(int balance)
    {
        mealBalance = balance;
    }

    //getters
    /**
     * Getter for the name of the meal type.
     * @return String name of the meal type
     */
    public String getMealName()
    {
        return mealName;
    }

    /**
     * Getter for balance of meals available.
     * @return int balance of the meals available
     */
    public int getMealBalance()
    {
        return mealBalance;
    }

    //compareTo
    /**
     * Overriden to compare Meal objects based on their mealName field, used for lexicographic ordering.
     * @param m the Meal object to be compared.
     * @return same as compareTo(): "a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object"
     */
    @Override
    public int compareTo(Meal m) {
        int mNCmp = mealName.compareTo(m.mealName);
        if (mNCmp != 0) return mNCmp;
        return 0;
    }

    //toString
    /**
     * Overriden to return name of the meal type and the balance of available meals of that type.
     * @return String name of the meal type and balance of available meals of that meal type
     */
    @Override
    public String toString()
    {
        return getMealName() + " " + getMealBalance();
    }

    //equals
    /**
     * Overriden to check if Meal objects are the same based on the mealName.
     * @param otherObject other Meal object to check against
     * @return boolean true if Meal objects are the same based on their field variables, false if not matched
     */
    @Override
    public boolean equals(Object otherObject) {
        Meal other = (Meal) otherObject;
        return mealName.equals(other.mealName);
    }
}
