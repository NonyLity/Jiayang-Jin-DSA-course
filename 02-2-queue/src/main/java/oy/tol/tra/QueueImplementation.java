
package oy.tol.tra;
public class QueueImplementation<E> implements QueueInterface<E> {

    private static final int INITIAL_CAPACITY = 10;
    private Object[] itemArray;
    private int size;
    private int front;
    private int rear;
    private int capacity;

    public QueueImplementation() throws QueueAllocationException {
        this(INITIAL_CAPACITY);
    }

    public QueueImplementation(int capacity) throws QueueAllocationException {
        if (capacity < 2) {
            throw new IllegalArgumentException("Capacity must be at least 2");
        }
        itemArray = new Object[capacity];
        size = 0;
        front = 0;
        rear = -1;
        this.capacity = capacity;
    }

    public int capacity() {
        return capacity;
    }

    public void enqueue(E element) throws QueueAllocationException, NullPointerException {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }
        if (size == itemArray.length) {
            reallocate();
        }

        rear = (rear + 1) % itemArray.length;
        itemArray[rear] = element;
        size++;
    }

    public E dequeue() throws QueueIsEmptyException {
        if (isEmpty()) {
            throw new QueueIsEmptyException("Queue is empty");
        }

        E element = (E) itemArray[front];
        itemArray[front] = null; // clear the element
        front = (front + 1) % itemArray.length;
        size--;

        return element;
    }

    public E element() throws QueueIsEmptyException {
        if (isEmpty()) {
            throw new QueueIsEmptyException("Queue is empty");
        }

        return (E) itemArray[front];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;
        front = 0;
        rear = -1;
    }

    private void reallocate() {
        int newCapacity = itemArray.length * 2;
        Object[] newArray = new Object[newCapacity];

        for (int i = 0; i < size; i++) {
            newArray[i] = itemArray[(front + i) % itemArray.length];
        }

        itemArray = newArray;
        front = 0;
        rear = size - 1;
        capacity = newCapacity;
    }

    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            int index = (front + i) % itemArray.length;
            sb.append(itemArray[index]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");

        return sb.toString();
    }
}