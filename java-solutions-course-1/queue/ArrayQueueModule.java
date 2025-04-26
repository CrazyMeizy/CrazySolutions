package queue;

import java.util.Objects;
import java.util.function.Predicate;

// Model: a[1]..a[n] && n - size of queue
// Inv: n>=0 && forall i=1..n: a[i] != null
// Let: immutable(x,y): forall i=x..y: a'[i] = a[i]
public class ArrayQueueModule {

    private static int tail;
    private static int head;
    private static Object[] elements = new Object[5];

    // Pred: element != null
    // Post: n' = n + 1 && a'[n'] = element && immutable(1,n)
    public static void enqueue(Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size() + 1);
        elements[tail] = element;
        tail = (tail + 1) == elements.length ? 0 : tail + 1;
    }

    // Pre: true
    // Post: n' = n && immutable(1,n)
    private static void ensureCapacity(int capacity) {
        if (capacity > elements.length - 1) {
            Object[] temp = new Object[elements.length * 2];
            System.arraycopy(elements, head, temp, 0,
                    tail >= head ? tail - head : elements.length - head);
            System.arraycopy(elements, 0, temp,
                    elements.length - head, tail >= head ? 0 : tail);
            tail = size();
            head = 0;
            elements = temp;
        }
    }

    // Pre: n > 0
    // Post: R = a[1] && n' = n && immutable(1,n)
    public static Object element() {
        if (size() <= 0) {
            throw new NullPointerException();
        }
        return elements[head];
    }

    // Pred: n > 0
    // Post: R = a[1] && n' = n - 1 &&
    //  forall i=2..n a'[i-1]=a[i] && a'[n] = null
    public static Object dequeue() {
        if (size() <= 0) {
            throw new NullPointerException();
        }
        Object answer = elements[head];
        elements[head] = null;
        head = head + 1 == elements.length ? 0 : head + 1;
        return answer;
    }

    // Pred: predicate != null
    // Post: R = min(i=1..n) : predicate(a[R]) = true && n' = n && immutable(1,n)
    public static int indexIf(Predicate<Object> predicate) {
        return findIndex(predicate, head, tail, 1);
    }

    // Pred: predicate != null
    // Post: R = max(i=1..n) : predicate(a[R]) = true && n' = n && immutable(1,n)
    public static int lastIndexIf(Predicate<Object> predicate) {
        return findIndex(predicate, head, tail, -1);
    }
    // Pred: direction = 1 or -1 && predicate != null
    // Post: if direction == 1 then 
    //   R = min(i=1..n) : predicate(a[R]) = true && n' = n && immutable(1,n)
    //   else
    //   R = max(i=1..n) : predicate(a[R]) = true && n' = n && immutable(1,n)
    public static int findIndex(Predicate<Object> predicate, int start, int end, int direction) {
        if (direction == -1) {
            int temp = start;
            start = next(end, -1);
            end = next(temp, -1);
        }
        while (start != end) {
            if (predicate.test(elements[start])) {
                return start - head < 0 ? (elements.length + start - head) : start - head;
            }
            start = next(start, direction);
        }
        return -1;
    }

    // Pred: true
    // Post: R = n && n' = n && immutable(1,n)
    public static int size() {
        return tail >= head ? tail - head : elements.length - head + tail;
    }

    // Pred: true
    // Post: R = (n == 0)) && immutable1,n) && n' = n
    public static boolean isEmpty() {
        return size() == 0;
    }

    // Pred: true
    // Post: forall i=1..n: a'[i]=null n' = 0
    public static void clear() {
        tail = 0;
        head = 0;
        elements = new Object[5];
    }
    // Pred: index -- int && direction == -1, 0 or 1
    // Post: (index+direction) % elements.length
    private static int next(int index, int direction) {
        return index == (elements.length - 1) * ((1 + direction) >> 1) ?
                (elements.length - 1) * ((1 - direction) >> 1) : index + direction;
    }
}