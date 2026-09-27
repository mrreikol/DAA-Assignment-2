import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import structures.DynamicArray;
import structures.LinkedList;
import structures.MinHeap;

import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    @Nested
    @DisplayName("DynamicArray Verification")
    class DynamicArrayTests {
        private DynamicArray array;

        @BeforeEach
        void setUp() {
            array = new DynamicArray(4);
        }

        @Test
        void testEmptyStructure() {
            assertEquals(0, array.size());
            assertTrue(array.isEmpty());
            assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
            assertThrows(IndexOutOfBoundsException.class, () -> array.remove(0));
            assertFalse(array.contains(10));
        }

        @Test
        void testSingleElement() {
            array.add(42);
            assertEquals(1, array.size());
            assertFalse(array.isEmpty());
            assertEquals(42, array.get(0));
            assertTrue(array.contains(42));
            assertFalse(array.contains(99));

            int removed = array.remove(0);
            assertEquals(42, removed);
            assertEquals(0, array.size());
            assertTrue(array.isEmpty());
        }

        @Test
        void testInsertionsAtBoundaryIndices() {
            array.add(0, 100);
            array.add(0, 50);
            array.add(2, 200);
            array.add(1, 75);

            assertEquals(4, array.size());
            assertEquals(50, array.get(0));
            assertEquals(75, array.get(1));
            assertEquals(100, array.get(2));
            assertEquals(200, array.get(3));
        }

        @Test
        void testDuplicatesAndRemovalBoundaries() {
            array.add(10);
            array.add(20);
            array.add(10);
            array.add(20);
            array.add(30);

            assertEquals(5, array.size());
            assertTrue(array.contains(10));
            assertTrue(array.contains(20));

            assertEquals(10, array.remove(0));
            assertEquals(30, array.remove(array.size() - 1));
            assertEquals(3, array.size());
            assertEquals(20, array.get(0));
            assertEquals(10, array.get(1));
            assertEquals(20, array.get(2));
        }

        @Test
        void testOutOfBoundsExceptions() {
            array.add(1);
            array.add(2);
            assertThrows(IndexOutOfBoundsException.class, () -> array.get(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> array.get(2));
            assertThrows(IndexOutOfBoundsException.class, () -> array.add(-1, 10));
            assertThrows(IndexOutOfBoundsException.class, () -> array.add(3, 10));
            assertThrows(IndexOutOfBoundsException.class, () -> array.remove(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> array.remove(2));
        }

        @Test
        void testLargeInputsCapacityExpansion() {
            int total = 10000;
            for (int i = 0; i < total; i++) {
                array.add(i);
            }
            assertEquals(total, array.size());
            for (int i = 0; i < total; i++) {
                assertEquals(i, array.get(i));
            }
        }
    }

    @Nested
    @DisplayName("LinkedList Verification")
    class LinkedListTests {
        private LinkedList list;

        @BeforeEach
        void setUp() {
            list = new LinkedList();
        }

        @Test
        void testEmptyStructure() {
            assertEquals(0, list.size());
            assertTrue(list.isEmpty());
            assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
            assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
            assertThrows(NoSuchElementException.class, () -> list.getFirst());
            assertThrows(NoSuchElementException.class, () -> list.getLast());
            assertFalse(list.contains(5));
        }

        @Test
        void testSingleElement() {
            list.add(100);
            assertEquals(1, list.size());
            assertEquals(100, list.getFirst());
            assertEquals(100, list.getLast());
            assertEquals(100, list.get(0));
            assertTrue(list.contains(100));

            int removed = list.remove(0);
            assertEquals(100, removed);
            assertEquals(0, list.size());
            assertTrue(list.isEmpty());
        }

        @Test
        void testInsertionsAtBoundaryIndices() {
            list.add(0, 10);
            list.add(0, 5);
            list.add(2, 20);
            list.add(1, 7);

            assertEquals(4, list.size());
            assertEquals(5, list.get(0));
            assertEquals(7, list.get(1));
            assertEquals(10, list.get(2));
            assertEquals(20, list.get(3));
            assertEquals(5, list.getFirst());
            assertEquals(20, list.getLast());
        }

        @Test
        void testRemovalBoundaries() {
            list.add(1);
            list.add(2);
            list.add(3);
            list.add(4);

            assertEquals(1, list.remove(0));
            assertEquals(4, list.remove(list.size() - 1));
            assertEquals(2, list.remove(0));
            assertEquals(3, list.remove(0));
            assertTrue(list.isEmpty());
        }

        @Test
        void testOutOfBoundsExceptions() {
            list.add(10);
            assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
            assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 5));
            assertThrows(IndexOutOfBoundsException.class, () -> list.add(2, 5));
            assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
        }

        @Test
        void testLargeInputs() {
            int total = 5000;
            for (int i = 0; i < total; i++) {
                list.add(i);
            }
            assertEquals(total, list.size());
            assertEquals(0, list.get(0));
            assertEquals(total / 2, list.get(total / 2));
            assertEquals(total - 1, list.get(total - 1));
        }
    }

    @Nested
    @DisplayName("MinHeap Verification")
    class MinHeapTests {
        private MinHeap heap;

        @BeforeEach
        void setUp() {
            heap = new MinHeap(4);
        }

        @Test
        void testEmptyStructure() {
            assertEquals(0, heap.size());
            assertTrue(heap.isEmpty());
            assertTrue(heap.isHeapPropertyMaintained());
            assertThrows(NoSuchElementException.class, () -> heap.peekMin());
            assertThrows(NoSuchElementException.class, () -> heap.extractMin());
        }

        @Test
        void testSingleElement() {
            heap.insert(42);
            assertEquals(1, heap.size());
            assertFalse(heap.isEmpty());
            assertTrue(heap.isHeapPropertyMaintained());
            assertEquals(42, heap.peekMin());
            assertEquals(42, heap.extractMin());
            assertEquals(0, heap.size());
            assertTrue(heap.isEmpty());
        }

        @Test
        void testDuplicateValues() {
            heap.insert(10);
            heap.insert(5);
            heap.insert(10);
            heap.insert(5);
            heap.insert(1);

            assertTrue(heap.isHeapPropertyMaintained());
            assertEquals(1, heap.extractMin());
            assertTrue(heap.isHeapPropertyMaintained());
            assertEquals(5, heap.extractMin());
            assertTrue(heap.isHeapPropertyMaintained());
            assertEquals(5, heap.extractMin());
            assertTrue(heap.isHeapPropertyMaintained());
            assertEquals(10, heap.extractMin());
            assertTrue(heap.isHeapPropertyMaintained());
            assertEquals(10, heap.extractMin());
            assertTrue(heap.isEmpty());
        }

        @Test
        void testParityWithPriorityQueueLargeInput() {
            PriorityQueue<Integer> pq = new PriorityQueue<>();
            Random rng = new Random(42);
            int total = 10000;

            for (int i = 0; i < total; i++) {
                int val = rng.nextInt(1000000);
                heap.insert(val);
                pq.add(val);
            }

            assertEquals(pq.size(), heap.size());
            assertTrue(heap.isHeapPropertyMaintained());

            int prev = Integer.MIN_VALUE;
            while (!pq.isEmpty()) {
                int pqVal = pq.poll();
                int heapVal = heap.extractMin();
                assertEquals(pqVal, heapVal);
                assertTrue(heapVal >= prev);
                prev = heapVal;
            }

            assertTrue(heap.isEmpty());
            assertTrue(heap.isHeapPropertyMaintained());
        }
    }
}