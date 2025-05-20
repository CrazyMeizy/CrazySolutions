package queue;

import java.util.function.Predicate;

// Model: a[1]..a[n] && n - size of queue
// Inv: n>=0 && forall i=1..n: a[i] != null
// Let: immutable(x,y): forall i=x..y: a'[i] = a[i]
public interface Queue {
    // Pred: element != null
    // Post: n' = n + 1 && a'[n'] = element && immutable(1,n)
    void enqueue(Object element);

    // Pre: n > 0
    // Post: R = a[1] && n' = n && immutable(1,n)
    Object element();

    // Pred: n > 0
    // Post: R = a[1] && n' = n - 1 &&
    //  forall i=2..n a'[i-1]=a[i] && a'[n] = null
    Object dequeue();
    // :NOTE: контракт странный
    // Pre: true
    // Post: n' : forall i,j=1..n' && i!=j && a'[i]!=a'[j] &&
    // forall i=1..n' exists j=1..n : a'[i]=a[j] &&
    // forall i=1..n exists j=1..n' : a[i]=a'[j] &&
    // ∀ i,j=1..n : i<j && (∀ i1=1..i-1,j1=1..j-1  a[i]!=a[i1] && a[j]!=a[j1])
    // ∃! k,c : a'[k]=a[i] && a'[c]=a[j] && k<c
    void distinct();

    // Pred: true
    // Post: R = n && n' = n && immutable(1,n)
    int size();

    // Pred: true
    // Post: R = (n == 0)) && immutable1,n) && n' = n
    boolean isEmpty();

    // Pred: true
    // Post: forall i=1..n: a'[i]=null n' = 0
    void clear();
}
