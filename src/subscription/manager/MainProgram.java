package subscription.manager;

import subscription.*;
import subscription.manager.exceptions.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Driver class with the {@link #main(String[]) main method} allowing for clerk's interaction with the food company subscription management program.
 * <p>At the program start there are no subscriptions, and input file is read to supply the information about subscribers and meals to the program.
 * <p>While the program is running, clerk can choose specified options from a displayed menu through keyboard input.
 * Output is displayed on the screen. If applicable, notes in form of letters are output to "letters.txt" file once the clerk chooses to finish running the program.
 *
 * <p><strong>References:</strong>
 * <p>Some parts of the code were inspired or adapted based on the code found in the following resources:
 * <ul>
 *     <li>
 *         Exception handling code examples found online - <a href="https://www.digitalocean.com/community/tutorials/exception-handling-in-java">LINK</a> ;
 *         Original Author - user "Pankaj";
 *         Modifying author - Robert Petecki.
 *     </li>
 *     <li>
 *         Code examples of methods ofPattern() from DateTimeFormatter and now() from LocalDateTime package - <a href="https://mkyong.com/java/java-how-to-get-current-date-time-date-and-calender/">LINK</a>;
 *         Original Author - user "mkyong";
 *         Modifying Author - Robert Petecki.
 *     </li>
 *     <li>
 *         Code examples in the textbook Big Java: Early Objects (2018);
 *         Book Author - Horstmann, C.S.
 *     </li>
 * </ul>
 *
 * @author Robert Petecki
 * @version 1.0 Date created: 01/11/2023
 */
public class MainProgram {
    /**
     * The SortedLinkedList for storing registered subscribers in ascending lexicographic order by surname then name.
     */
    private static SortedLinkedList<Subscriber> subscribersLinkedList;
    /**
     * The SortedLinkedList for storing registered meals in ascending lexicographic order by meal type name.
     */
    private static SortedLinkedList<Meal> mealsLinkedList;
    /**
     * The LinkedList for storing subscriptions.
     */
    private static LinkedList<Subscription> subscriptionsLinkedList;

    /**
     * Main method reads in information about registered subscribers and meals, prints menu with operation options to the display,
     * and prompts clerk to interact with the program using keyboard input.
     * <p>Through appropriate interaction clerk is able to print the list of subscribers or meals to the display,
     * and update the stored data when a registered subscriber changes (adds or removes) meals in their subscription.
     * @param args N/A
     * @throws FileNotFoundException when input file "input_data.txt" is not found in the current directory
     * @throws IllegalNumberInputException when the number from clerk's keyboard input is not a positive number and a positive number is expected (adding or removing subscriptions).
     * @throws IllegalMealQuantityException when either there is not enough meals available to add to a subscription or the quantity of meals to be removed exceeds the quantity subscribed.
     * @throws NoSuchMealException when meal from clerk's keyboard input isn't a registered meal
     * @throws NoSuchSubscriberException when subscriber from clerk's keyboard input isn't a registered subscriber
     * @throws NoSuchSubscriptionException when subscriber has no subscriptions
     * @throws IllegalSubscriptionQuantityException when attempts to add subscription of another meal for a subscriber that has already reached the limit of 3 different subscriptions.
     */
    public static void main(String[] args) throws FileNotFoundException, IllegalNumberInputException, IllegalMealQuantityException, IllegalSubscriptionQuantityException, NoSuchMealException, NoSuchSubscriberException, NoSuchSubscriptionException {

        //boolean variable programDone is used in a while loop that keeps the program running until clerk chooses to finish
        boolean programDone = false;

        //initialise SortedLinkedLists for storing subscriber, meal, subscription information
        subscribersLinkedList = new SortedLinkedList<Subscriber>();
        mealsLinkedList = new SortedLinkedList<Meal>();
        subscriptionsLinkedList = new LinkedList<Subscription>();

        //create PrintWriter object outLetters for writing notes to file letters.txt
        PrintWriter outLetters = new PrintWriter("src/subscription/manager/letters.txt");

        //calling method readIn to read in the input file
        readIn();

        //print welcome message at the start of the program only
        //ASCII Art was found here: https://www.asciiart.eu/food-and-drinks/other
        System.out.println("Welcome to the Food Company subscription management program!");
        System.out.println(
                "   (\\\n" +
                        "     \\ \\\n" +
                        " __    \\/ ___,.-------..__        __\n" +
                        "//\\\\ _,-'\\\\               `'--._ //\\\\\n" +
                        "\\\\ ;'      \\\\                   `: //\n" +
                        " `(          \\\\                   )'\n" +
                        "   :.          \\\\,----,         ,;\n" +
                        "    `.`--.___   (    /  ___.--','\n" +
                        "      `.     ``-----'-''     ,'\n" +
                        "         -.               ,-\n" +
                        "            `-._______.-'"
        );

        //while loop to keep the program running until clerk chooses to finish
        while (!programDone)
        {
            //initialise or declare variables
            //number of input attempts available
            int i = 3;

            String inFName = null;
            String inLName = null;
            String inMeal = null;
            int inMealQuantity = 0;
            boolean optionDone = false;
            boolean validatedS = false;
            boolean validatedM = false;

            //print program menu to display
            printMenu();

            //take keyboard input
            //create Scanner object "input" that takes keyboard input
            Scanner input = new Scanner(System.in);
            //assign keyboard input to variable "userMenuInput"
            String userMenuInput = input.next();

            //switch statement for program operation decision-making depending on clerk's keyboard input "userMenuInput"
            switch(userMenuInput) {

                //option "f" to finish running the program
                case "f":
                    //print goodbye message to display
                    System.out.println("Closing the program, see you later!");
                    //close PrintWriter to output notes to letters.txt file
                    outLetters.close();
                    //finish running the program using boolean programDone
                    programDone = true;
                    break;

                //option "m" for displaying a list of meals and their available quantity sorted in ascending lexicographic order by meal's name
                case "m":
                    //print to display
                    System.out.println("Information about all the meals and their availability:\n");
                    //print meals and their available quantity using for-each loop on mealsLinkedList
                    for (Meal meal : mealsLinkedList) {
                        System.out.println("Meal type: " + meal.getMealName() + "\nNumber available: " + meal.getMealBalance());
                        System.out.println();
                    }
                    break;

                //option "s" for displaying a list of subscribers and their subscriptions, sorted in ascending lexicographic order by subscriber's surname then name
                case "s":
                    //print to display
                    System.out.println("Information about all the subscribers:\n");
                    /*Print subscribers and their subscriptions to display in the following format using for-each loop on subscribersLinkedList
                    Full Name: X Y
                    Subscriptions: none - if no subscriptions
                    1. Meal type: Z, number of meals: # - numbered list if has subscriptions*/
                    //outer for-each loop
                    for (Subscriber subscriber : subscribersLinkedList) {
                        System.out.println("Full name: " + subscriber.getFirstName() + " " + subscriber.getSurname());
                        //try block
                        try
                        {
                            //check if subscriber has subscriptions using isSubscription()
                            isSubscription(subscriber.getFirstName(), subscriber.getSurname()); //throws NoSuchSubscriptionException if subscriber has no subscriptions
                            //print subscriber's subscriptions to display as a numbered list
                            System.out.println("Subscriptions:");
                            int count = 0; //numbered list counter
                            //inner for-each loop
                            for (Subscription subscription : subscriptionsLinkedList)
                            {
                                Subscriber subscribed = new Subscriber(subscription.getFirstName(), subscription.getSurname());
                                //match subscription to subscriber from the outer for-each loop
                                if (subscriber.equals(subscribed))
                                {
                                    //add to numbered list counter as each subscription is printed to display
                                    count++;
                                    System.out.println(count + ". Meal type: " + subscription.getMealSubscribed() + ", number of meals: " + subscription.getMealSubscribedBalance());
                                }
                            }
                        }
                        //catch block
                        catch (NoSuchSubscriptionException exception)
                        {
                            //prints to display when user has no subscriptions
                            System.out.println("Subscriptions: none");
                        }
                        System.out.println();
                    }
                    break;

                //option "a" for adding new subscriptions or adding meals to already existing subscriptions
                case "a":
                    //reassign values of boolean variables to false allowing for input validation throughout the option prompts
                    optionDone = false;
                    validatedS = false;
                    validatedM = false;
                    //while loop to keep the option running until the value of boolean optionDone is changed to "true"
                    while (!optionDone) {
                        try {
                            //prompt to enter subscriber's full name, validates if subscriber from keyboard input exists
                            if (!validatedS) //runs only at the start of the option when subscriber from clerk's input hasn't been validated yet
                            {
                                //prompt
                                System.out.println("Please type the full name of a registered subscriber separated by space and press enter:");
                                //take clerk's keyboard input
                                input = new Scanner(System.in);
                                //assign first name to inFName variable, surname to inLName variable
                                inFName = input.next();
                                inLName = input.next();
                                input.nextLine();
                                //validate if input subscriber is a registered subscriber
                                isSubscriber(inFName, inLName); //throws a NoSuchSubscriberException
                                //subscriber is validated
                                validatedS = true;
                                //reassign number of clerk's input attempts to 3
                                i = 3;
                            }
                            //prompt to enter meal's name, validates if meal from keyboard input exists
                            if (validatedS && !validatedM)
                            {
                                //prompt
                                System.out.println("Type the name of the meal type you would like to add and press enter:");
                                //take clerk's keyboard input
                                input = new Scanner(System.in);
                                //assign meal name to inMeal variable
                                inMeal = input.nextLine();
                                //validate if input meal is a registered meal
                                isMeal(inMeal); //throws a NoSuchMealException
                                //meal is validated
                                validatedM = true;
                                //reassign number of clerk's input attempts to 3
                                i = 3;
                            }
                            //prompt to enter the number of meals to be added to the subscription
                            if (validatedS && validatedM) //runs only when subscriber and meal from clerk's input are validated
                            {
                                //prompt
                                System.out.println("Type the number of " + inMeal + " meals you would like to add to subscription and press enter:");
                                //take clerk's keyboard input
                                input = new Scanner(System.in);
                                //assign input meal quantity to inMealQuantity variable
                                inMealQuantity = input.nextInt();
                                i = 3;
                                //check input format, expected positive number
                                if (inMealQuantity <= 0) {
                                    throw new IllegalNumberInputException("The number of meals you would like to add needs to be greater than 0."); //IllegalNumberException thrown to prevent adding subscription with quantity of meals 0 or of a negative number of meals.
                                }
                                //add subscription to input subscriber (inFName, inLName), input meal (inMeal) input meal quantity (inMealQuantity)
                                addMealSubscription(inFName, inLName, inMeal, inMealQuantity);
                                //exit option while loop
                                optionDone = true;
                            }
                        }
                        //catch blocks
                        //3 attempts for the clerk when validating subscriber, meal, or meal quantity input format
                        catch (NoSuchSubscriberException | NoSuchMealException | IllegalNumberInputException exception)
                        {
                            //substract one attempt if catch block is executed
                            i--;
                            //check if there are attempts left
                            if (i > 0) {
                                //print exception message to display
                                System.out.print(exception.getMessage());
                                //print number of attempts left
                                System.out.println(" You have " + i + " more chance(s)");
                            }
                            //no attempts left, print message to the display and break from the option while loop
                            else {
                                //reassign number of attempts left
                                i = 3;
                                System.out.println("Operation was unsuccessful, no changes have been made to subscriptions.");
                                break;
                            }
                        }
                        //1 attempt for the clerk when trying to add more than 3 subscriptions to a subscriber
                        catch (IllegalSubscriptionQuantityException exception)
                        {
                            //print exception message to display
                            System.out.println(exception.getMessage());
                            System.out.println("Operation was unsuccessful, no changes have been made to subscriptions.");
                            break;
                        }
                        //1 attempt for the clerk when trying to add more meals than available to a subscription
                        catch (IllegalMealQuantityException exception)
                        {
                            //print exception message to display
                            System.out.println(exception.getMessage());
                            /*
                            Create date time format object dtf that will set the format to Day/Month/Year Hour:minutes:seconds.
                            The following parts of code were adapted from the code by user "mkyong" found here: https://mkyong.com/java/java-how-to-get-current-date-time-date-and-calender/
                             */
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss");
                            //create date time object now with the current date and time, adapted code as stated above
                            LocalDateTime now = LocalDateTime.now();
                            //print a note in a form of letter to letters.txt file, letter informs that there is not enough meals of requested type
                            outLetters.println(dtf.format(now) + "\nFood Company Office\n" + "Dear " + inFName + " " + inLName + ",\n" +
                                    exception.getMessage() + " Therefore, we are sorry to let you know that this could not be added to your subscription and your request will not be fulfilled.\n" +
                                    "Please accept our apologies.\n" +
                                    "Sincerely,\n" +
                                    "Clerk on behalf of the Food Company\n" +
                                    "* * * * * * * * *");
                            //print unsuccessful operation message to display
                            System.out.println("Operation was unsuccessful, no changes have been made to subscriptions.\nYou can find an apology letter in the \"letters.txt\" file after you finish running this program.");
                            //exit option while loop
                            optionDone = true;
                        }
                        //handle incorrect input format when number input is expected
                        catch (InputMismatchException exception)
                        {
                            //substract one attempt if catch block is executed
                            i--;
                            //check if there are attempts left
                            if (i > 0) {
                                //print message to display informing of expected input format
                                System.out.print("Your input must be a number.");
                                //print message to display informing of attempts left
                                System.out.println(" You have " + i + " more chance(s)");
                            }
                            //no attempts left, print message to the display and break from the option while loop
                            else {
                                i = 3;
                                System.out.println("Operation was unsuccessful, no changes have been made to subscriptions.");
                                break;
                            }
                        }
                    }
                    break;

                //option "r" for removing meals from subscriptions
                case "r":
                    //reassign values of boolean variables to false allowing for input validation throughout the option prompts
                    optionDone = false;
                    validatedS = false;
                    validatedM = false;
                    //while loop to keep the option running until the value of boolean optionDone is changed to "true"
                    while (!optionDone) {
                        try
                        {
                            //prompt to enter subscriber's full name, validates if subscriber from keyboard input exists
                            if (!validatedS) //runs only at the start of the option when subscriber from clerk's input hasn't been validated yet
                            {
                                //prompt
                                System.out.println("Please type the full name of a registered subscriber separated by space and press enter:");
                                //take clerk's keyboard input
                                input = new Scanner(System.in);
                                //assign first name to inFName variable, surname to inLName variable
                                inFName = input.next();
                                inLName = input.next();
                                input.nextLine();
                                //validate if input subscriber is a registered subscriber
                                isSubscriber(inFName, inLName); //throws a NoSuchSubscriberException
                                //validate if input subscriber has a subscription
                                isSubscription(inFName, inLName); //throws NoSuchSubscriptionException
                                //subscriber is validated
                                validatedS = true;
                                //reassign number of clerk's input attempts to 3
                                i = 3;
                            }
                            //prompt to enter meal's name, validates if meal from keyboard input exists
                            if (validatedS && !validatedM) //runs only when subscriber is validated and meal from clerk's input hasn't been validated yet
                            {
                                //prompt
                                System.out.println("Type the name of the meal type you would like to remove from the subscription and press enter:");
                                //take clerk's keyboard input
                                input = new Scanner(System.in);
                                //assign meal name to inMeal variable
                                inMeal = input.nextLine();
                                //validate if input meal is a registered meal
                                isMeal(inMeal); //throws a NoSuchMealException
                                //meal is validated
                                validatedM = true;
                                //reassign number of clerk's input attempts to 3
                                i = 3;
                            }
                            //prompt to enter the number of meals to be removed from the subscription
                            if (validatedS && validatedM) //runs only when subscriber and meal from clerk's input are validated
                            {
                                //prompt
                                System.out.println("Type the number of " + inMeal + " meals you would like to remove from the subscription and press enter:");
                                //take clerk's keyboard input
                                input = new Scanner(System.in);
                                //assign input meal quantity to inMealQuantity variable
                                inMealQuantity = input.nextInt();
                                i = 3;
                                //check input format, expected positive number
                                if (inMealQuantity <= 0) {
                                    throw new IllegalNumberInputException("The number of meals you would like to add needs to be greater than 0.");
                                }
                                //remove subscription specified by clerk's input, subscriber (inFName, inLName), input meal (inMeal) input meal quantity (inMealQuantity)
                                removeMealSubscription(inFName, inLName, inMeal, inMealQuantity); //throws IllegalMealQuantityException if attempted to remove more meals than is subscribed to
                                //exit option while loop
                                optionDone = true;
                            }
                        }
                        //catch blocks
                        //3 attempts for the clerk when validating subscriber, meal, or meal quantity input format
                        catch (NoSuchSubscriberException | NoSuchSubscriptionException | NoSuchMealException | IllegalNumberInputException | IllegalMealQuantityException exception)
                        {
                            //substract one attempt if catch block is executed
                            i--;
                            //check if there are attempts left
                            if (i > 0) {
                                //print exception message to display
                                System.out.print(exception.getMessage());
                                System.out.println(" You have " + i + " more chance(s)");
                            }
                            //no attempts left, print message to the display and break from the option while loop
                            else {
                                //reassign number of attempts left
                                i = 3;
                                System.out.println("Operation was unsuccessful, no changes have been made to subscriptions.");
                                break;
                            }
                        }
                        catch (InputMismatchException exception)
                        {
                            //substract one attempt if catch block is executed
                            i--;
                            //check if there are attempts left
                            if (i > 0) {
                                System.out.print("Your input must be a number.");
                                System.out.println(" You have " + i + " more chance(s)");
                            }
                            //no attempts left, print message to the display and break from the option while loop
                            else {
                                i = 3;
                                System.out.println("Operation was unsuccessful, no changes have been made to subscriptions.");
                                break;
                            }
                        }
                    }
                    break;
                //if clerk's keyboard input doesn't match any options from the menu print message to display
                default:
                    System.out.println("The option you have chosen doesn't exist, try again.");
                    break;
            }
        }
    }

    //Methods
    /**
     * Reads data from input file "input_data.txt" in the current directory, i.e. src/subscription/manager/input_data.txt
     * containing information about registered subscriptions and meals, stores and sorts subscribers and meals in SortedLinkedLists.
     * Sorting is performed using insertion sort sorting algorithm insertSort() from SortedLinkedList class.
     * <p>Data in the input file follows the format below:
     * <p>The first line contains an integer representing the number of registered subscribers, followed by the
     * information about the subscribers (one line for every subscriber with their first name and surname). The
     * next line contains an integer representing the number of different types of meal available, and is followed by
     * the information about the meals (two lines for every meal: one line containing the name of the meal and the
     * second one containing the number of these meals available each week). See the example file in the current directory.
     * @throws FileNotFoundException when input file "input_data.txt" is not found in the current directory
     */
    private static void readIn() throws FileNotFoundException {
        //initialise file reader and scanner
        FileReader inFile = new FileReader("src/subscription/manager/input_data.txt"); //file needs to be in the same directory
        Scanner in = new Scanner(inFile);

        //initialise variables
        //index used for looping over lists, i
        int i = 0;
        //number of subscribers read from input file
        int noOfSubscribers = in.nextInt();
        in.nextLine();

        //while loop to read subscribers from lines containing subscriber information
        while (i < noOfSubscribers) {
            Subscriber subscriber = new Subscriber(in.next(), in.next());
            subscribersLinkedList.add(subscriber); //add subscriber to SortedLinkedList
            subscribersLinkedList.insertSort(subscribersLinkedList); //sort SortedLinkedList
            i++;
        }

        //variables
        //reassign index variable to 0
        i = 0;
        //number of meal type names read from input file
        int noOfMealTypes = in.nextInt();
        in.nextLine();

        //while loop to read subscribers from lines containing subscriber information
        while (i < noOfMealTypes) {
            Meal meal = new Meal(in.nextLine(), in.nextInt());
            in.nextLine();
            mealsLinkedList.add(meal); //add meal to SortedLinkedList
            mealsLinkedList.insertSort(mealsLinkedList); //sort SortedLinkedList
            i++;
        }
    }

    /**
     * Prints the program menu to the display.
     * The operations specified in this menu are the operations that clerk can perform when running the program via keyboard input:
     * <ul>
     *     <li>
     *         f - finish running the program
     *     </li>
     *     <li>
     *         m - display information about all the meals
     *     </li>
     *     <li>
     *         s - display information about all the subscribers
     *     </li>
     *     <li>
     *         a - update the stored data, add meals to a registered subscriber's subscription
     *     </li>
     *     <li>
     *         r - update the stored data, remove meals from a registered subscriber's subscription
     *     </li>
     * </ul>
     *
     */
    private static void printMenu(){
        printDivider(); //visually divides space on the display
        System.out.println("Choose one of the operations displayed below:");
        System.out.println("f - finish running the program");
        System.out.println("m - display information about all the meals");
        System.out.println("s - display information about all the subscribers");
        System.out.println("a - update the stored data, add meals to a registered subscriber's subscription");
        System.out.println("r - update the stored data, remove meals from a registered subscriber's subscription");
        printDivider();

    }

    /**
     * Prints a visual divider line to the display, useful when displaying menu.
     */
    private static void printDivider()
    {
        System.out.println("-------------------------------------------------------------------------------------------");
    }


    //methods for adding and removing subscriptions
    /**
     * Adds meal subscription to the Linked List of subscriptions, subscriptionsLinkedList.
     * @param fName subscriber's first name
     * @param lName subscriber's surname
     * @param mealType name of the meal type
     * @param mealQuantity quantity of meals of the specified meal type
     * @throws IllegalSubscriptionQuantityException when attempted to add subscription and subscriber has reached the limit of maximum 3 different subscriptions
     * @throws IllegalMealQuantityException when attempted to add subscription of more meals of particular type than available at the moment
     */
    private static void addMealSubscription(String fName, String lName, String mealType, int mealQuantity) throws IllegalSubscriptionQuantityException, IllegalMealQuantityException {
        //create Subscription and Meal objects from input parameters, subscription and inputMeal
        Subscription subscription = new Subscription(fName, lName, mealType, mealQuantity);
        Meal inputMeal = new Meal(mealType, mealQuantity);

        //max limit of subscriptions = 3 different meal types per subscriber
        //use variable numberMealsSubscribed to check number of existing subscriptions per subscriber
        int numberMealsSubscribed = 0;
        for (Subscription subscribed : subscriptionsLinkedList) //loop over subscriptions list
        {
            Subscriber subscriber = new Subscriber(subscription.getFirstName(), subscription.getSurname());
            if (subscriber.equals(subscribed))
            {
                numberMealsSubscribed++; //add to variable
            }
        }
        //max limit of subscriptions reached, throw exception
        if (numberMealsSubscribed == 3)
        {
            throw new IllegalSubscriptionQuantityException("Subscription cannot be added.\nEach subscriber is allowed to subscribe to a maximum of 3 different meal types and subscriber \"" + fName + " " + lName + "\" has already reached that limit.");
        }

        //validate if enough quantity of meals is available to be subscribed
        isEnoughMeal(mealType, mealQuantity); //throws IllegalMealQuantityException

        //adding subscription
        //add to existing subscription
        if (isSubscription(fName, lName, mealType))
        {
            for (Subscription subscriptionExisting : subscriptionsLinkedList)
            {
                if (subscriptionExisting.equals(subscription))
                {
                    int newQuantity = subscriptionExisting.getMealSubscribedBalance() + mealQuantity;
                    subscriptionExisting.setMealSubscribedBalance(newQuantity);
                }
            }
        }
        //add a new subscription if meal of specified type hasn't been subscribed to yet
        else
        {
            subscriptionsLinkedList.add(subscription);
        }

        //change balance of available meals
        for (Meal meal : mealsLinkedList)
        {
         if (inputMeal.equals(meal))
            {
                int newMealBalance = meal.getMealBalance() - mealQuantity;
                meal.setMealBalance(newMealBalance);
            }
        }

        //print confirmation of operation message to the display
        System.out.println("The " + mealType + " meal (number of meals: " + mealQuantity + ") was added to " + fName + " " + lName + "'s subscription.");
    }

    /**
     * Removes meal subscription or removes meals from subscription stored in the Linked List of subscriptions, subscriptionsLinkedList.
     * @param fName subscriber's first name
     * @param lName subscriber's surname
     * @param mealType name of the meal type
     * @param mealQuantity quantity of meals of the specified meal type
     * @throws IllegalMealQuantityException when attempted to remove more meals of particular type than there is subscribed to at the moment
     */
    private static void removeMealSubscription(String fName, String lName, String mealType, int mealQuantity) throws IllegalMealQuantityException
    {
        //create Subscription and Meal objects from input parameters, subscription and inputMeal
        Subscription subscription = new Subscription(fName, lName, mealType, mealQuantity);
        Meal inputMeal = new Meal(mealType, mealQuantity);

        //if subscription exists, remove specified quantity of meals (mealQuantity) from subscription
        if (isSubscription(fName, lName, mealType))
        {
            //using for-each loop here threw ConcurrentModificationException hence use for loop over the Linked List instead
            for (int i = 0; i < subscriptionsLinkedList.size(); i++)
            {
                Subscription subscribed = subscriptionsLinkedList.get(i);

                //specified with parameter quantity of meals (mealQuantity) needs to be equal or smaller than quantity of meals subscribed to (subscribed.getMealSubscribedBalance())
                if (subscribed.equals(subscription) && subscribed.getMealSubscribedBalance() >= mealQuantity)
                {
                    //temporary variable newQuantity to check what is the new quantity of meals in the subscription
                    int newQuantity = subscribed.getMealSubscribedBalance() - mealQuantity;
                    //if new quantity is 0, remove subscription from the Linked List of subscriptions (subscriptionsLinkedList)
                    if (newQuantity == 0)
                    {
                        subscriptionsLinkedList.remove(subscribed);
                    }
                    //if new quantity is still greater than 0, only change quantity of meals subscribed to in the subscription
                    else
                    {
                        subscribed.setMealSubscribedBalance(newQuantity);
                    }
                    //print confirmation of operation message to the display
                    System.out.println("The " + mealType + " meal (number of meals: " + mealQuantity + ") was removed from " + fName + " " + lName + "'s subscription.");

                    //update balance of available meals in SortedLinkedList (mealsLinkedList) after removing subscriptions
                    for (Meal meal : mealsLinkedList)
                    {
                        if (meal.equals(inputMeal))
                        {
                            //temporary variable of new meal balance
                            int newMealBalance = meal.getMealBalance() + mealQuantity;

                            meal.setMealBalance(newMealBalance);
                        }
                    }
                }
                //throw IllegalMealQuantityException if attempted to remove more meals than there is subscribed to
                if (subscribed.equals(subscription) && subscribed.getMealSubscribedBalance() < mealQuantity)
                {
                    throw new IllegalMealQuantityException("The quantity of meals you want to remove (" + mealQuantity + ") exceeds the number of meals " + fName + " " + lName + " is subscribed to (" + subscribed.getMealSubscribedBalance() + ").");
                }
            }
        }
    }

    //methods for validating input
    /**
     * Validates if subscription of a particular meal type for a particular subscriber specified with parameters exists.
     * @param fName subscriber's first name
     * @param lName subscriber's surname
     * @param mealType name of the meal type
     * @return boolean validSubscription = true if subscription specified with parameters exists (valid)
     */
    private static boolean isSubscription(String fName, String lName, String mealType)
    {
        boolean validSubscription = false; //boolean validSubscription to be returned by this method, starts as false

        Subscription inputSubscription = new Subscription(fName, lName, mealType, 1); //meal quantity is 1 as it's the minimum to be subscribed to

        //check if subscription specified with parameters (inputSubscription) exists in the Linked List of subscriptions (subscriptionsLinkedList)
        for (Subscription subscription : subscriptionsLinkedList) {
            if (inputSubscription.equals(subscription)) {
                validSubscription = true; //boolean value = true if exists (valid)
                break;
            }
        }
        return validSubscription;
    }

    //overloading isSubscription
    /**
     * Validates if a particular subscriber specified with parameters has subscription(s).
     * @param fName subscriber's first name
     * @param lName subscriber's surname
     * @return boolean validSubscription = true if subscriber specified with parameters has subscription(s) (valid)
     * @throws NoSuchSubscriptionException when subscriber specified with parameters has no subscriptions
     */
    private static boolean isSubscription(String fName, String lName) throws NoSuchSubscriptionException
    {
        boolean validSubscription = false; //boolean validSubscription to be returned by this method, starts as false

        Subscriber isSubscribed = new Subscriber(fName, lName);

        //check if subscriber specified with parameters (isSubscribed) has subscriptions in the Linked List of subscriptions (subscriptionsLinkedList)
        for (Subscription subscription : subscriptionsLinkedList) {
            Subscriber subscribed = new Subscriber(subscription.getFirstName(), subscription.getSurname()); //subscriber actually found in the list of subscriptions (subscribed)
            if (isSubscribed.equals(subscribed)) {
                validSubscription = true; //boolean true if subscriber specified with parameters (isSubscribed) is actually on the list of subscriptions (subscribed)
                break;
            }
        }
        //throw NoSuchSubscriptionException if no subscriptions for the subscriber specified with parameters
        if (!validSubscription)
        {
            throw new NoSuchSubscriptionException("Subscriber \"" + fName + " " + lName + "\" has no subscriptions.");
        }
        return validSubscription;
    }

    /**
     * Validates if subscriber specified with parameters is actually a registered subscriber.
     * @param fName subscriber's first name
     * @param lName subscriber's surname
     * @return boolean validSubscriber = true if subscriber specified with parameters is a registered subscriber (valid)
     * @throws NoSuchSubscriberException when subscriber specified with parameters is not a registered subscriber
     */
    private static boolean isSubscriber(String fName, String lName) throws NoSuchSubscriberException
    {
        boolean validSubscriber = false; // boolean validSubscriber to be returned by this method, starts as false

        Subscriber inputSubscriber = new Subscriber(fName, lName);

        //check if subscriber specified with parameters (inputSubscriber) exists in the SortedLinkedList of subscribers (subscribersLinkedList)
        for (Subscriber subscriber : subscribersLinkedList)
        {
            if (inputSubscriber.equals(subscriber))
            {
                validSubscriber = true; //true if exists (valid)
                break;
            }
        }
        //throw NoSuchSubscriberException when subscriber specified with parameters is not a registered subscriber
        if (!validSubscriber) {
            throw new NoSuchSubscriberException("Subscriber \"" + fName + " " + lName + "\" doesn't exist.");
        }
        return validSubscriber;

    }

    /**
     * Validates if meal type name specified with parameter is actually a registered meal available for subscriptions
     * @param mealName name of the meal type
     * @return boolean validMeal = true if meal specified with parameter is a registered meal available for subscriptions
     * @throws NoSuchMealException when meal type specified with parameter is not a registered meal
     */
    private static boolean isMeal(String mealName) throws NoSuchMealException
    {
        boolean validMeal = false; //boolean validMeal to be returned by this method, starts as false

        Meal inputMeal = new Meal(mealName);

        //check if meal specified with parameter (mealName) exists in the SortedLinkedList of meals (mealsLinkedList)
        for (Meal meal : mealsLinkedList) {
            if (inputMeal.equals(meal)) {
                validMeal = true; //true if exists (valid)
                break;
            }
        }
        //throw NoSuchMealException when meal specified with parameter is not a registered meal
        if (!validMeal) {
            throw new NoSuchMealException("Meal type \"" + mealName + "\" doesn't exist.");
        }
        return validMeal;
    }

    /**
     * Validates if there is enough meals of particular type specified with parameters to add to subscription.
     * @param mealName name of the meal type
     * @param mealQuantity quantity of meals of the specified meal type
     * @return boolean enoughMeal = true if there is enough meals of specified meal type to subscribe
     * @throws IllegalMealQuantityException when attempted to add more meals of particular type to a subscription than currently available
     */
    private static boolean isEnoughMeal(String mealName, int mealQuantity) throws IllegalMealQuantityException
    {
        boolean enoughMeal = false; //boolean enoughMeal to be returned by this method, starts as false

        int mealAvailableQuantity = 0; //integer for storing quantity of available meals of particular type, starts as 0

        Meal inputMeal = new Meal(mealName);

        //loop through SortedLinkedList of Meals (mealsLinkedList)
        for (Meal meal : mealsLinkedList)
        {
            if (inputMeal.equals(meal) && meal.getMealBalance() >= mealQuantity) //mealQuantity specified with parameter needs to be equal or smaller than current balance of meal
            {
                enoughMeal = true; //true if there is enough meals
                break;
            } else if (inputMeal.equals(meal) && meal.getMealBalance() < mealQuantity) {
                mealAvailableQuantity = meal.getMealBalance(); //store available meal balance
            }
        }
        //throw IllegalMealQuantityException if not enough meals, display the message with meal type name, requested quantity, available quantity
        if (!enoughMeal)
        {
            throw new IllegalMealQuantityException("The number of " + mealName + " meals you would like to add (" + mealQuantity + ") exceeds the number of " + mealName + " meals currently available (" + mealAvailableQuantity + ").");
        }
        return enoughMeal;
    }
}
