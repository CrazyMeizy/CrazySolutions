package info.kgeorgiy.ja.chuprov.lambda;

import info.kgeorgiy.java.advanced.lambda.Trees.Leaf;
import info.kgeorgiy.java.advanced.lambda.Trees.SizedBinary;
import info.kgeorgiy.java.advanced.lambda.Trees.SizedBinary.Branch;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Spliterator;
import java.util.function.Consumer;

public class SizedBinaryTreeSpliterator<T> implements Spliterator<T> {
    private final Deque<SizedBinary<T>> nodes;
    private long size;

    public SizedBinaryTreeSpliterator(SizedBinary<T> tree) {
        nodes = new ArrayDeque<>();
        this.nodes.add(tree);
        size = tree.size();
    }

    private SizedBinaryTreeSpliterator(Deque<SizedBinary<T>> nodes, long size) {
        this.nodes = nodes;
        this.size = size;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (nodes.isEmpty()) return false;
        switch (nodes.removeLast()) {
            case Leaf<T>(T value) -> {
                action.accept(value);
                size--;
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
        if (size <= 1) return null;
        long splitSize = 0;
        Deque<SizedBinary<T>> newNodes = new ArrayDeque<>();
        while (splitSize < size / 2) {
            while (splitSize + nodes.getLast().size() > size / 2) {
                if (nodes.getLast() instanceof Branch<T> branch) {
                    nodes.removeLast();
                    nodes.add(branch.right());
                    nodes.add(branch.left());
                }
            }
            SizedBinary<T> lastNode = nodes.removeLast();
            newNodes.addFirst(lastNode);
            splitSize += lastNode.size();
            size -= lastNode.size();
        }
        return new SizedBinaryTreeSpliterator<>(newNodes, splitSize);
    }

    @Override
    public long estimateSize() {
        return size;
    }

    @Override
    public int characteristics() {
        return ORDERED | SIZED | SUBSIZED | IMMUTABLE;
    }
}
