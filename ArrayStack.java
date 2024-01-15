public class ArrayStack<T> implements StackADT<T> {

    // Array to hold the items in the stack of Type <T>
    private T[] array;
    // TOP variable to track the top of the stack , -1 means e -1 when the stack is
    // empty
    private int top;

    // Constructor Method
    // Initialize the array with a capacity of 10
    // Initialize top to -1
    public ArrayStack() {
        // Step 1 Create the object of array with 10 length
        // Step 2 Cast the object to T type in array
        this.array = (T[]) new Object[10];

        // Set Top to -1 as stack is empty
        this.top = -1;
    }

    // Method to add an element to the top of the stack
    @Override
    public void push(T element) {

        // increase total count
        this.top = this.top + 1;

        this.array[top] = element; // Add the element to the Top
        // Expend Capacity
        this.expandCapacity();
        // System.out.println("Dhruv >> " + element);

    }

    @Override
    public T pop() throws StackException {
        // step 1
        // Check if it's empty by calling IsEmpty method
        if (this.isEmpty()) {
            throw new StackException("Stack is empty");
        }
        // step 2
        // shrink the capacity
        this.shrinkCapacity();

        // Step 3
        // Read the Top one and Remove
        T TopElement = this.array[top]; // Get the TOP object
        this.array[top] = null; // set the Top to Null to remove the element
        this.top = this.top - 1; // decrase total elements counts

        return TopElement;
    }

    @Override
    public T peek() throws StackException {
        // Check if it's empty by calling IsEmpty method
        if (this.isEmpty()) {
            throw new StackException("Stack is empty");
        }

        return this.array[top]; // return top stack value
    }

    @Override
    public int size() {
        return top + 1; // +1 because top start from -1
    }

    // Method to return the capacity of the array
    public int getCapacity() {
        return this.array.length;
    }

    // Checking STack is empty or not
    @Override
    public boolean isEmpty() {

        if (this.top == -1) {
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        // Step 1 Create the object of array with 10 length
        // Step 2 Cast the object to T type in array
        T[] clearArray = (T[]) new Object[10];

        // Replace the current stack to empty one
        this.array = clearArray;

        // Set Top to -1 as stack is empty
        this.top = -1;
    }

    // Method to return the top index
    public int getTop() {
        return top;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "Empty stack.";
        }

        String OutputString = "Stack: ";

        // Read stack from Top to Bottom
        for (int i = top; i >= 0; i--) {

            OutputString = OutputString + this.array[i]; // Start from Top and then keep adding
            if (i > 0) {

                OutputString = OutputString + ", "; // comma and 1 Space
            } else {
                OutputString = OutputString + "."; // period and No Space

            }
        }
        return OutputString.toString();
    }

    /*
     * Method to expand the capacity of the array when
     * is being used (number of items in the array divided
     * by the capacity). If that fraction is less than 0.75, do nothing and skip
     * this step.
     * If at least 0.75 (75%) is being used, expand the capacity by adding 10
     * additional
     * spots in the array.
     */
    private void expandCapacity() {
        int currentCapcity = this.getCapacity();
        double currentlyUsed = (double) (((double) size()) / ((double) currentCapcity));

        if (currentlyUsed >= 0.75) {

            int newCapacity = currentCapcity + 10;
            // Create new array to copy into original with new capacity
            T[] newArray = (T[]) new Object[newCapacity];

            // Loop it and copy the data into new array
            for (int i = 0; i < this.array.length; i++) {
                newArray[i] = this.array[i];

            }
            // Replace original array
            this.array = newArray;

        }
    }

    // Shrink the capacity
    private void shrinkCapacity() {
        // check usage
        int currentCapcity = this.getCapacity();
        double currentlyUsed = (double) (((double) size()) / ((double) currentCapcity));
        // if capcity is > .25 or capacity > 20 then do nothing
        if (currentlyUsed <= 0.25 && currentCapcity > 20) {
            int newCapacity = currentCapcity - 10;
            // Create new array to copy into original with new capacity
            T[] newArray = (T[]) new Object[newCapacity];

            // Loop it and copy the data into new array
            for (int i = 0; i < newArray.length; i++) {
                newArray[i] = this.array[i];

            }
            // Replace original array
            this.array = newArray;
        }
    }

}
