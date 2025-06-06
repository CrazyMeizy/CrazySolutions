package info.kgeorgiy.java.advanced.implementor.advanced;

import java.util.List;

/**
 * Class with array arguments and return types.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public abstract class ArraysTest<T> {
    public ArraysTest(final T[] elements) {
    }

    abstract T[] getElements();
    abstract void setElements(T[] elements);

    abstract List<T>[] getList();
    abstract void setList(List<T>[] elements);
}
