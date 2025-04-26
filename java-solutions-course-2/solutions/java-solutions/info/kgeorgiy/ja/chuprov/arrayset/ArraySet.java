package info.kgeorgiy.ja.chuprov.arrayset;

import java.util.*;

public class ArraySet<E> extends AbstractSet<E> implements SortedSet<E> {

    private List<E> array;
    private final Comparator<? super E> comparator;
    private final static Comparator<Object> NATURAL_ORDER = Collections.reverseOrder().reversed();

    public ArraySet() {
        this(List.of(), null);
    }

    public ArraySet(Collection<? extends E> collection) {
        this(collection, null);
    }

    public ArraySet(Collection<? extends E> collection, Comparator<? super E> comparator) {
        this.comparator = comparator;
        array = new ArrayList<>(collection);
        array.sort(comparator);
        distinct();
    }

    private ArraySet(List<E> subList, Comparator<? super E> comparator) {
        this.comparator = comparator;
        array = subList;
    }

    @Override
    public Iterator<E> iterator() {
        return Collections.unmodifiableList(array).iterator();
    }

    @Override
    public int size() {
        return array.size();
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    public int find(E element) {
        int index = Collections.binarySearch(array, element, comparator);
        return index >= 0 ? index : -index - 1;
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        if (compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException("<toElement> must be greater than <fromElement> while subset");
        }
        return tailSet(fromElement).headSet(toElement);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        List<E> subList = array.subList(0, find(toElement));
        return new ArraySet<>(subList, comparator);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        List<E> subList = array.subList(find(fromElement), array.size());
        return new ArraySet<>(subList, comparator);
    }

    @Override
    public E first() {
        return array.getFirst();
    }

    @Override
    public E last() {
        return array.getLast();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return Collections.binarySearch(array, (E) o, comparator) >= 0;
    }

    private int compare(E e1, E e2) {
        if (comparator == null) {
            return NATURAL_ORDER.compare(e1, e2);
        }
        return comparator.compare(e1, e2);
    }

    private void distinct() {
        List<E> newArray = new ArrayList<>();
        for (int i = array.size() - 1; i >= 0; i--) {
            if (i == 0 || compare(array.get(i), array.get(i - 1)) != 0) {
                newArray.add(array.get(i));
            }
        }
        array = newArray.reversed();
    }
}
