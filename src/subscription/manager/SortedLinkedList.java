package subscription.manager;

import java.util.LinkedList;

/**
 * A generic subclass of LinkedList that is used for storing and lexicographic sorting of Subscriber, Meal, or Subscription objects.
 * <p>A SortedLinkedList can be sorted in ascending lexicographic order with insertSort() which uses insertion sort sorting algorithm to sort list's elements.
 * @param <E> specifies the type of objects stored in the list
 * @author Robert Petecki
 * @version 1.0 Date created: 01/11/2023
 */
public class SortedLinkedList<E extends Comparable<E>> extends LinkedList<E> {

    /**
     * This generic method sorts a SortedLinkedList in ascending lexicographic order using insertion sort sorting algorithm.
     * <p>This insertion sort sorting algorithm was adapted using lecture material provided by Dr Konrad Dąbrowski at Newcastle University.
     * @param a name of the list to be sorted
     */
    public void insertSort(SortedLinkedList<E> a) {
        for (int i = 1; i < a.size(); i++) {
            E value = a.get(i);
            int j;
            for (j = i; j > 0; j--) {
                if (value.compareTo(a.get(j - 1)) > 0) {
                    break;
                }
                else
                {
                    a.set(j, a.get(j - 1));
                }
            }
            a.set(j, value);
        }
    }
}
