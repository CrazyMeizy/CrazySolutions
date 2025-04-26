package info.kgeorgiy.ja.chuprov.lambda;

import info.kgeorgiy.java.advanced.lambda.Trees.Leaf;
import info.kgeorgiy.java.advanced.lambda.Trees.Nary;
import info.kgeorgiy.java.advanced.lambda.Trees.Nary.Node;

import java.util.*;
import java.util.function.Consumer;

public class NaryTreeSpliterator<T> implements Spliterator<T> {
    private Deque<Map.Entry<Nary<T>, Integer>> entries;
    private int characteristics = ORDERED | IMMUTABLE;
    private long size = Long.MAX_VALUE;

    public NaryTreeSpliterator(Nary<T> tree) {
        entries = new ArrayDeque<>();
        this.entries.add(Map.entry(tree, 0));
        setSizeOneIfKnown();
    }

    private NaryTreeSpliterator(Deque<Map.Entry<Nary<T>, Integer>> newEntries) {
        entries = newEntries;
        setSizeOneIfKnown();
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (entries.isEmpty()) return false;
        switch (entries.getLast().getKey()) {
            case Leaf<T>(T value) -> {
                action.accept(value);
                entries.removeLast();
                setSizeOneIfKnown();
                setSizeIfKnown(entries.isEmpty(), 0);
                return true;
            }
            case Node<T>(List<Nary<T>> children) -> {
                substitudeLastWithChildren(children);
                return tryAdvance(action);
            }
        }
    }

    @Override
    public Spliterator<T> trySplit() {
        if (entries.isEmpty()) {
            return null;
        }
        while (entries.size() == 1) {
            switch (entries.getLast().getKey()) {
                case Leaf<T> leaf -> {
                    return null;
                }
                case Node<T>(List<Nary<T>> children) -> substitudeLastWithChildren(children);
            }
        }
        int withMinDepth = countElementsWithMinDepth();
        return split((withMinDepth + 1) / 2);
    }

    @Override
    public long estimateSize() {
        return size;
    }

    @Override
    public int characteristics() {
        return characteristics;
    }

    private void substitudeLastWithChildren(List<Nary<T>> children) {
        int newDepth = entries.removeLast().getValue() + 1;
        entries.addAll(children.reversed().stream()
                .map(tNary -> Map.entry(tNary, newDepth))
                .toList());
    }

    private int countElementsWithMinDepth() {
        int withinDepth = 1;
        var it = entries.iterator();
        int depth = it.next().getValue();
        while (it.hasNext() && it.next().getValue() == depth) {
            withinDepth++;
        }
        return withinDepth;
    }

    private Spliterator<T> split(int splitSize) {
        Deque<Map.Entry<Nary<T>, Integer>> temp = new ArrayDeque<>();
        for (int i = 0; i < splitSize; i++) {
            temp.addLast(entries.removeFirst());
        }
        var newEntries = entries;
        entries = temp;
        setSizeOneIfKnown();
        return new NaryTreeSpliterator<>(newEntries);
    }

    private void setSizeOneIfKnown() {
        setSizeIfKnown(entries.size() == 1 && entries.getLast().getKey() instanceof Leaf<T>, 1);
    }

    private void setSizeIfKnown(boolean flag, int size) {
        if (flag) {
            characteristics |= SIZED | SUBSIZED;
            this.size = size;
        }
    }
}
