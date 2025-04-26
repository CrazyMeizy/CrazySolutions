package queue;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

// Model: a[1]..a[n] && n - size of queue
// Inv: n>=0 && forall i=1..n: a[i] != null
// Let: immutable(x,y): forall i=x..y: a'[i] = a[i]
abstract class AbstractQueue implements Queue {

    // Pred: element != null
    // Post: n' = n + 1 && a'[n'] = element && immutable(1,n)
    public void enqueue(Object element) {
        Objects.requireNonNull(element);
        enqueueImpl(element);
    }

    // Pred: true
    // Post: n' = n + 1 && a'[n'] = element && immutable(1,n)
    abstract void enqueueImpl(Object element);

    // Pred: n > 0
    // Post: R = a[1] && n' = n - 1 &&
    //  forall i=2..n a'[i-1]=a[i] && a'[n] = null
    public Object dequeue() {
        assertNotEmpty();
        Object result = element();
        remove();
        return result;
    }

    // Pre: n > 0
    // Post: R = a[1] && n' = n && immutable(1,n)
    public Object element() {
        assertNotEmpty();
        return elementImpl();
    }

    // Pre: n > 0
    // Post: R = a[1] && n' = n && immutable(1,n)
    abstract Object elementImpl();

    // Pre: true
    // Post: n' : forall i,j=1..n' && i!=j && a'[i]!=a'[j] &&
    // forall i=1..n' exists j=1..n : a'[i]=a[j] &&
    // forall i=1..n exists j=1..n' : a[i]=a'[j] &&
    // ∀ i,j=1..n : i<j && (∀ i1=1..i-1,j1=1..j-1  a[i]!=a[i1] && a[j]!=a[j1])
    // ∃! k,c : a'[k]=a[i] && a'[c]=a[j] && k<c
    public void distinct() {
        Set<Object> set = new HashSet<>();
        int size = size();
        for (int i = 0; i < size; i++) {
            Object temp = dequeue();
            if (!set.contains(temp)) {
                set.add(temp);
                enqueue(temp);
            }
        }
    }

    // Pred: n > 0
    // Post:  n' = n - 1 && forall i=1..n' a'[i]=a[i+1] && a'[n] = null
    abstract void remove();

    // Pred: n>0
    // Post: true
    private void assertNotEmpty() {
        if (isEmpty()) {
            throw new NullPointerException();
        }
    }

    // Pred: true
    // Post: R = (n == 0)) && immutable1,n) && n' = n
    public boolean isEmpty() {
        return size() == 0;
    }
}
