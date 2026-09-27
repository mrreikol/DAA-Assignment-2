package structures;

public class DynamicArray {
    public long elementAccesses = 0;
    public long elementMovements = 0;
    public long comparisons = 0;

    private int[] data;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public DynamicArray() {
        this(DEFAULT_CAPACITY);
    }

    public DynamicArray(int capacity) {
        int initialCapacity = capacity < 1 ? DEFAULT_CAPACITY : capacity;
        this.data = new int[initialCapacity];
        this.size = 0;
    }

    public void resetMetrics() {
        this.elementAccesses = 0;
        this.elementMovements = 0;
        this.comparisons = 0;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > data.length) {
            int newCapacity = data.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            int[] newData = new int[newCapacity];
            for (int i = 0; i < size; i++) {
                elementAccesses++;
                elementMovements++;
                newData[i] = data[i];
            }
            data = newData;
        }
    }

    public void add(int element) {
        ensureCapacity(size + 1);
        elementAccesses++;
        data[size] = element;
        size++;
    }

    public void add(int index, int element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            elementAccesses++;
            elementMovements++;
            data[i] = data[i - 1];
        }
        elementAccesses++;
        data[index] = element;
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        elementAccesses++;
        int removedValue = data[index];
        for (int i = index; i < size - 1; i++) {
            elementAccesses++;
            elementMovements++;
            data[i] = data[i + 1];
        }
        size--;
        return removedValue;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        elementAccesses++;
        return data[index];
    }

    public boolean contains(int value) {
        for (int i = 0; i < size; i++) {
            elementAccesses++;
            comparisons++;
            if (data[i] == value) {
                return true;
            }
        }
        return false;
    }
}