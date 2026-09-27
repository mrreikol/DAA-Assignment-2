package structures;

import java.util.NoSuchElementException;

public class MinHeap {
    public long comparisons = 0;
    public long movements = 0;

    private int[] data;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public MinHeap() {
        this(DEFAULT_CAPACITY);
    }

    public MinHeap(int capacity) {
        int initialCapacity = capacity < 1 ? DEFAULT_CAPACITY : capacity;
        this.data = new int[initialCapacity];
        this.size = 0;
    }

    public void resetMetrics() {
        this.comparisons = 0;
        this.movements = 0;
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
                movements++;
                newData[i] = data[i];
            }
            data = newData;
        }
    }

    public void insert(int x) {
        ensureCapacity(size + 1);
        data[size] = x;
        movements++;
        siftUp(size);
        size++;
    }

    public int peekMin() {
        if (size == 0) {
            throw new NoSuchElementException("Heap is empty");
        }
        return data[0];
    }

    public int extractMin() {
        if (size == 0) {
            throw new NoSuchElementException("Heap is empty");
        }
        int minVal = data[0];
        data[0] = data[size - 1];
        movements++;
        size--;
        if (size > 0) {
            siftDown(0);
        }
        return minVal;
    }

    public void siftUp(int i) {
        int key = data[i];
        while (i > 0) {
            int parent = (i - 1) >>> 1;
            comparisons++;
            if (key < data[parent]) {
                data[i] = data[parent];
                movements++;
                i = parent;
            } else {
                break;
            }
        }
        data[i] = key;
        movements++;
    }

    public void siftDown(int i) {
        int key = data[i];
        int half = size >>> 1;
        while (i < half) {
            int leftChild = (i << 1) + 1;
            int rightChild = leftChild + 1;
            int smallerChild = leftChild;

            if (rightChild < size) {
                comparisons++;
                if (data[rightChild] < data[leftChild]) {
                    smallerChild = rightChild;
                }
            }

            comparisons++;
            if (key <= data[smallerChild]) {
                break;
            }

            data[i] = data[smallerChild];
            movements++;
            i = smallerChild;
        }
        data[i] = key;
        movements++;
    }

    public boolean isHeapPropertyMaintained() {
        for (int i = 0; i <= (size - 2) / 2; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < size && data[i] > data[left]) {
                return false;
            }
            if (right < size && data[i] > data[right]) {
                return false;
            }
        }
        return true;
    }
}