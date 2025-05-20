package queue;

import java.util.Objects;
import java.util.function.Predicate;

// Model: a[1]..a[n] && n - size of queue
// Inv: n>=0 && forall i=1..n: a[i] != null
// Let: immutable(x,y): forall i=x..y: a'[i] = a[i]
public class ArrayQueueADT {

    private int tail;
    private int head;
    private Object[] elements = new Object[5];

    // Pred: true
    // Post: R.n = 0
    public static ArrayQueueADT create() {
        return new ArrayQueueADT();
    }

    // Pred: element != null
    // Post: n' = n + 1 && a'[n'] = element && immutable(1,n)
    public static void enqueue(ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(queue, size(queue) + 1);
        queue.elements[queue.tail] = element;
        queue.tail = (queue.tail + 1) == queue.elements.length ? 0 : queue.tail + 1;
    }

    // Pre: true
    // Post: n' = n && immutable(1,n)
    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        if (capacity > queue.elements.length - 1) {
            Object[] temp = new Object[queue.elements.length * 2];
            System.arraycopy(queue.elements, queue.head, temp, 0,
                    queue.tail >= queue.head ? queue.tail - queue.head : queue.elements.length - queue.head);
            System.arraycopy(queue.elements, 0, temp,
                    queue.elements.length - queue.head, queue.tail >= queue.head ? 0 : queue.tail);
            queue.tail = size(queue);
            queue.head = 0;
            queue.elements = temp;
        }
    }

    // Pre: n > 0
    // Post: R = a[1] && n' = n && immutable(1,n)
    public static Object element(ArrayQueueADT queue) {
        if (size(queue) <= 0) {
            throw new NullPointerException();
        }
        return queue.elements[queue.head];
    }

    // Pred: n > 0
    // Post: R = a[1] && n' = n - 1 &&
    //  forall i=2..n a'[i-1]=a[i] && a'[n] = null
    public static Object dequeue(ArrayQueueADT queue) {
        if (size(queue) <= 0) {
            throw new NullPointerException();
        }
        Object answer = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        queue.head = queue.head + 1 == queue.elements.length ? 0 : queue.head + 1;
        return answer;
    }

    // Pred: predicate != null
    // Post: R = min(i=1..n) : predicate(a[R]) = true && n' = n && immutable(1,n)
    public static int indexIf(ArrayQueueADT queue, Predicate<Object> predicate) {
        return findIndex(queue, predicate, queue.head, queue.tail, 1);
    }

    // Pred: predicate != null
    // Post: R = max(i=1..n) : predicate(a[R]) = true && n' = n && immutable(1,n)
    public static int lastIndexIf(ArrayQueueADT queue, Predicate<Object> predicate) {
        return findIndex(queue, predicate, queue.head, queue.tail, -1);
    }

    // Pred: direction = 1 or -1 && predicate != null
    // Post: if direction == 1 then 
    //   R = min(i=1..n) : predicate(a[R]) = true && n' = n && immutable(1,n)
    //   else
    //   R = max(i=1..n) : predicate(a[R]) = true && n' = n && immutable(1,n)
    public static int findIndex(ArrayQueueADT queue, Predicate<Object> predicate, int start, int end, int direction) {
        if (direction == -1) {
            int temp = start;
            start = next(queue, end, -1);
            end = next(queue, temp, -1);
        }
        while (start != end) {
            if (predicate.test(queue.elements[start])) {
                return start - queue.head < 0 ? (queue.elements.length + start - queue.head) : start - queue.head;
            }
            start = next(queue, start, direction);
        }
        return -1;
    }

    // Pred: true
    // Post: R = n && n' = n && immutable(1,n)
    public static int size(ArrayQueueADT queue) {
        return queue.tail >= queue.head ? queue.tail - queue.head : queue.elements.length - queue.head + queue.tail;
    }

    // Pred: true
    // Post: R = (n == 0)) && immutable1,n) && n' = n
    public static boolean isEmpty(ArrayQueueADT queue) {
        return size(queue) == 0;
    }

    // Pred: true
    // Post: forall i=1..n: a'[i]=null n' = 0
    public static void clear(ArrayQueueADT queue) {
        queue.tail = 0;
        queue.head = 0;
        queue.elements = new Object[5];
    }

    // Pred: index -- int && direction == -1, 0 or 1
    // Post: (index+direction) % queue.elements.length
    private static int next(ArrayQueueADT queue, int index, int direction) {
        return index == (queue.elements.length - 1) * ((1 + direction) >> 1) ?
                (queue.elements.length - 1) * ((1 - direction) >> 1) : index + direction;
    }
}