package oy.tol.tra;

/**
 * An implementation of the StackInterface.
 */
public class StackImplementation<E> implements StackInterface<E> {

    private Object[] itemArray;
    private int capacity;
    private int currentIndex = -1;
    private static final int DEFAULT_STACK_SIZE = 10;

    public StackImplementation() throws StackAllocationException {
        this(DEFAULT_STACK_SIZE);
    }

    public StackImplementation(int capacity) throws StackAllocationException {
        if (capacity < 2) {
            throw new StackAllocationException("Capacity should be at least 2.");
        }
        this.capacity = capacity;
        itemArray = new Object[capacity];
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public void push(E element) throws StackAllocationException, NullPointerException {
        if (currentIndex == capacity - 1) {
            // Stack is full, need to resize
            int newCapacity = capacity * 2;
            Object[] newArray = new Object[newCapacity];
            System.arraycopy(itemArray, 0, newArray, 0, capacity);
            itemArray = newArray;
            capacity = newCapacity;
        }

        if (element == null) {
            throw new NullPointerException("Cannot push null element into the stack.");
        }

        itemArray[++currentIndex] = element;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E pop() throws StackIsEmptyException {
        if (isEmpty()) {
            throw new StackIsEmptyException("Stack is empty, cannot pop elements.");
        }

        E element = (E) itemArray[currentIndex];
        itemArray[currentIndex--] = null; // clear reference
        return element;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E peek() throws StackIsEmptyException {
        if (isEmpty()) {
            throw new StackIsEmptyException("Stack is empty, cannot peek elements.");
        }

        return (E) itemArray[currentIndex];
    }

    @Override
    public int size() {
        return currentIndex + 1;
    }

    @Override
    public void clear() {
        for (int i = 0; i <= currentIndex; i++) {
            itemArray[i] = null;
        }
        currentIndex = -1;
    }

    @Override
    public boolean isEmpty() {
        return currentIndex == -1;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        for (int index = 0; index <= currentIndex; index++) {
            builder.append(itemArray[index].toString());
            if (index < currentIndex) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
