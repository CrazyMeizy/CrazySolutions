package info.kgeorgiy.ja.chuprov.lambda;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public final class MiddleAccumulator<T> {
    private final Deque<T> queue = new ArrayDeque<>();
    private int count = 0;

    public void accumulate(T element) {
        queue.addFirst(element);
        count++;
        if (count % 2 == 0) {
            queue.removeLast();
        }
    }

    public MiddleAccumulator<T> combine(MiddleAccumulator<T> element) {
        throw new UnsupportedOperationException("Parallel streams not supported by middle");
    }

    public Optional<T> finish() {
        return queue.isEmpty() ? Optional.empty() : Optional.of(queue.peekLast());
    }
}
