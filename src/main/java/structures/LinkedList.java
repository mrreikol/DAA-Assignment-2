package structures;

import java.util.NoSuchElementException;

public class LinkedList {
    public long elementAccesses = 0;
    public long elementMovements = 0;
    public long comparisons = 0;

    public static class Node {
        public int val;
        public Node prev;
        public Node next;

        public Node(Node prev, Node next, int val) {
            this.prev = prev;
            this.next = next;
            this.val = val;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public LinkedList() {
        this.head = null;
        this.tail = null;
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

    public void add(int x) {
        Node oldTail = tail;
        Node newNode = new Node(oldTail, null, x);
        tail = newNode;
        elementMovements++;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == size) {
            add(x);
            return;
        }
        Node succ = getNode(index);
        Node pred = succ.prev;
        Node newNode = new Node(pred, succ, x);
        succ.prev = newNode;
        elementMovements++;
        if (pred == null) {
            head = newNode;
        } else {
            pred.next = newNode;
        }
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node target = getNode(index);
        int value = target.val;
        Node pred = target.prev;
        Node succ = target.next;

        if (pred == null) {
            head = succ;
        } else {
            pred.next = succ;
            target.prev = null;
        }

        if (succ == null) {
            tail = pred;
        } else {
            succ.prev = pred;
            target.next = null;
        }

        elementMovements++;
        size--;
        return value;
    }

    public int get(int index) {
        return getNode(index).val;
    }

    public boolean contains(int x) {
        Node curr = head;
        while (curr != null) {
            elementAccesses++;
            comparisons++;
            if (curr.val == x) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    private Node getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node curr;
        if (index < (size >> 1)) {
            curr = head;
            for (int i = 0; i < index; i++) {
                elementAccesses++;
                curr = curr.next;
            }
        } else {
            curr = tail;
            for (int i = size - 1; i > index; i--) {
                elementAccesses++;
                curr = curr.prev;
            }
        }
        elementAccesses++;
        return curr;
    }

    public int getFirst() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        elementAccesses++;
        return head.val;
    }

    public int getLast() {
        if (tail == null) {
            throw new NoSuchElementException("List is empty");
        }
        elementAccesses++;
        return tail.val;
    }
}