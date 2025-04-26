package info.kgeorgiy.ja.chuprov.lambda;

import info.kgeorgiy.java.advanced.lambda.Trees.Binary;
import info.kgeorgiy.java.advanced.lambda.Trees.Binary.Branch;
import info.kgeorgiy.java.advanced.lambda.Trees.Leaf;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Spliterator;
import java.util.function.Consumer;

public class BinaryTreeSpliterator<T> implements Spliterator<T> {
    private Deque<Binary<T>> nodes;
    private int characteristics = ORDERED | IMMUTABLE;
    private long size = Long.MAX_VALUE;

    public BinaryTreeSpliterator(Binary<T> tree) {
        nodes = new ArrayDeque<>();
        this.nodes.add(tree);
        setSizeOneIfKnown();
    }

    private BinaryTreeSpliterator(Deque<Binary<T>> nodes) {
        this.nodes = nodes;
        setSizeOneIfKnown();
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (nodes.isEmpty()) return false;
        switch (nodes.removeLast()) {
            case Leaf<T>(T value) -> {
                action.accept(value);
                setSizeOneIfKnown();
                setSizeIfKnown(nodes.isEmpty(), 0);
                return true;
            }
            case Branch<T> branch -> {
                nodes.add(branch.right());
                nodes.add(branch.left());
                return tryAdvance(action);
            }
        }
    }

    @Override
    public Spliterator<T> trySplit() {
        if (nodes.isEmpty()) return null;
        if (nodes.size() == 1) {
            switch (nodes.getLast()) {
                case Branch<T> branch -> {
                    nodes.removeLast();
                    nodes.add(branch.right());
                    nodes.add(branch.left());
                }
                case Leaf<T> leaf -> {
                    return null;
                }
            }
        }
        return split();
    }

    @Override
    public long estimateSize() {
        return size;
    }

    @Override
    public int characteristics() {
        return characteristics;
    }

    private Spliterator<T> split() {
        Deque<Binary<T>> temp = new ArrayDeque<>();
        temp.add(nodes.removeFirst());
        Deque<Binary<T>> newNodes = nodes;
        nodes = temp;
        setSizeOneIfKnown();
        return new BinaryTreeSpliterator<>(newNodes);
    }

    private void setSizeOneIfKnown() {
        setSizeIfKnown(nodes.size() == 1 && nodes.getLast() instanceof Leaf<T>, 1);
    }

    private void setSizeIfKnown(boolean flag, int size) {
        if (flag) {
            characteristics |= SIZED | SUBSIZED;
            this.size = size;
        }
    }
}
