package subscription;

/**
 * Subscriber class - representing a subscriber that has a first name and a last name.
 * <p>Subscriber class implements Comparable interface with overridden compareTo(Subscriber s) to allow for lexicographic sorting in a SortedLinkedList, see
 * <p>Subscriber's first name and surname are read from the input file once the program is started using MainProgram driver class and cannot be changed in the program.
 * @author Robert Petecki
 * @version 1.0 Date created: 01/11/2023
 */
public class Subscriber implements Comparable<Subscriber> {
    //Fields
    /**
     * First name of the subscriber.
     */
    private final String firstName;
    /**
     * Last name (surname) of the subscriber.
     */
    private final String surname;

    //Constructor

    /**
     * Subscriber constructor specifying subscriber's first name and surname.
     * @param fName subscriber's first name
     * @param lName subscriber's surname
     */
    public Subscriber(String fName, String lName)
    {
        this.firstName = fName;
        this.surname = lName;
    }

    //Methods
    //getters
    /**
     * Getter for the first name of the subscriber.
     * @return String with first name of the subscriber
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Getter for the last name of the subscriber.
     * @return String with surname of the subscriber
     */
    public String getSurname()
    {
        return surname;
    }

    //toString
    /**
     * Overriden to return full name of the subscriber.
     * @return String with the full name of the subscriber
     */
    @Override
    public String toString()
    {
        return firstName + " " + surname;
    }

    //compareTo
    /**
     * Overriden to compare Meal objects based on their mealName field, used for lexicographic ordering based on surname then first name of the subscriber.
     * @param s the Subscriber object to be compared.
     * @return same as compareTo(): "a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object"
     */
    @Override
    public int compareTo(Subscriber s) {
        int lNCmp = surname.compareTo(s.surname);
        if (lNCmp != 0) return lNCmp;
        int fNCmp = firstName.compareTo(s.firstName);
        if (fNCmp != 0) return fNCmp;
        else return 0;
    }

    //equals
    /**
     * Overriden to check if Subscriber objects are the same based on the firstName and surname.
     * @param otherObject other Subscriber object to check against
     * @return boolean true if Subscriber objects are the same based on their field variables, false if not matched
     */
    @Override
    public boolean equals(Object otherObject) {
        Subscriber other = (Subscriber) otherObject;
        return firstName.equals(other.firstName)
                && surname.equals(other.surname);
    }
}
