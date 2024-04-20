// This class represents a Stack collection implemented using an array. It implements the StackADT interface and the
// top variable is an integer that keeps track of where the top of the stack is.
public class ArrayStack<T> implements StackADT<T> {

    // Declaring the following private instance variables.
    private T[] array; // This array holds the items in the stack.
    private int top; // Keeps track of the top of the stack.

    // Constructor method that initializes the array size to 10 and top to -1.
    public ArrayStack() {
        array = (T[]) new Object[10];
        top = -1;
    }

    // Expands the capacity of the array by adding 10 more spots if we're using more than or equal to 75% of the
    // array capacity.
    public void push(T element) {
        // Checking if we need to expand the capacity of the array if we're using more than or equal to 75% of the
        // array capacity.
        if (size() >= (int)(array.length * 0.75)) { expandCapacity(); }
        // Adding the element to the top and updating the value of top.
        top = top + 1;
        array[top] = element;
    }

    // Shrinks the capacity of the array by removing 10 spots if we're using less than or equal to 25% of the
    // array's capacity and keeping in mind the capacity is greater than oe equal to 20.
    public T pop() throws StackException {
        // If the stack is empty then we throw a StackException.
        if (isEmpty()) { throw new StackException("Stack is empty"); }
        // Checking if the stack needs to shrink if we're using less than or equal to 25% of the array capacity
        // the capacity is greater than or equal to 20.
        if (size() <= (int)(array.length * 0.25) && array.length >= 20) { shrinkCapacity(); }
        // Decrementing the top value since an element has been reduced.
        top = top - 1;
        return array[top+1];
    }

    // Returns the element at the top of the stack without removing it.
    public T peek() throws StackException {
        // If the stack is empty then a StackException is thrown.
        if (isEmpty()) { throw new StackException("Stack is empty"); }
        // Returning top most element.
        return array[top];
    }

    // Returns true if the stack is empty or false otherwise.
    // The stack is empty if it's size is 0.
    public boolean isEmpty() { return size() == 0; }

    // Returns the number of elements in the stack.
    public int size() { return top + 1; }

    // Clears out the elements from the stack and restores it to it's original state
    public void clear() {
        array = (T[]) new Object[10];
        top = -1;
    }

    // Returns the length of the array.
    public int getCapacity() { return array.length; }

    // Returns the top index.
    public int getTop() { return top; }

    // Prints the stack.
    public String toString() {
        // If the stack is empty then we print the following.
        if (isEmpty()) { return "Empty stack."; }
        // Creating a stack string that will show the items in the stack
        String stackString = "Stack: ";
        // Iterating through the stack and adding the elements to the final string.
        for (int i = top; i >= 0; i--) {
            stackString += array[i];
            // Adding a comma if the element isn't the last element.
            if (i > 0) {
                stackString += ", ";
            }
        }
        // Adding a period at the end and then returning the final string.
        stackString += ".";
        return stackString;
    }

    // If atleast 75% of the array's capacity is being used then we expand the capacity of the array by adding 10 more
    // spots in the array.
    private void expandCapacity() {
        T[] newArray = (T[]) new Object[array.length + 10];
        for (int i = 0; i < size(); i++) newArray[i] = array[i];
        array = newArray;
    }

    // If atmost 25% of the array's capacity is being used then we shrink the capacity of the array by reducing 10
    // spots from the array.
    private void shrinkCapacity() {
        T[] newArray = (T[]) new Object[array.length - 10]; // reducing capacity by 10
        for (int i = 0; i < size(); i++) newArray[i] = array[i];
        array = newArray;
    }
}


