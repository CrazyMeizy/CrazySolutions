package queue;

// Model: a[1]..a[n] && n - size of queue
// Inv: n>=0 && forall i=1..n: a[i] != null
// Let: immutable(x,y): forall i=x..y: a'[i] = a[i]
public class LinkedQueue extends AbstractQueue {
    private Node tail = new Node(null, null);
    private final Node head = new Node(null, tail);
    private int size = 0;

    // Pred: true
    // Post: n' = n + 1 && a'[n'] = element && immutable(1,n)
    protected void enqueueImpl(Object element) {
        tail.value = element;
        tail.next = new Node(null, null);
        tail = tail.next;
        size++;
    }

    // Pre: n > 0
    // Post: R = a[1] && n' = n && immutable(1,n)
    protected Object elementImpl() {
        return head.next.value;
    }

    // Pred: n > 0
    // Post:  n' = n - 1 && forall i=1..n' a'[i]=a[i+1] && a'[n] = null
    protected void remove() {
        size--;
        head.next = head.next.next;
    }

    // Pred: true
    // Post: R = n && n' = n && immutable(1,n)
    public int size() {
        return size;
    }

    // Pred: true
    // Post: forall i=1..n: a'[i]=null n' = 0
    public void clear() {
        head.next = tail;
        size = 0;
    }

    private static class Node {
        private Object value;
        private Node next;

        private Node(Object value, Node next) {
            this.value = value;
            this.next = next;
        }
    }
}