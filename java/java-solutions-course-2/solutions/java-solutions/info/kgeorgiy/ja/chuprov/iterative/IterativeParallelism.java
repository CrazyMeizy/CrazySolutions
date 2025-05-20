package info.kgeorgiy.ja.chuprov.iterative;

import info.kgeorgiy.java.advanced.iterative.ScalarIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * IterativeParallelism provides a parallel implementation of scalar operations on lists.
 * <p>
 * This class implements the {@link ScalarIP} interface and supports operations such as finding the index
 * of the maximum or minimum element, searching for an element by a predicate, finding the last occurrence
 * of an element matching a predicate, and summing the indices of elements that satisfy a predicate.
 * The operations are performed by splitting the list into segments and processing them concurrently using
 * a specified number of threads.
 * </p>
 *
 * @see ScalarIP
 */
public class IterativeParallelism implements ScalarIP {

    ParallelMapper mapper;

    /**
     * Constructs an {@code IterativeParallelism} instance without a custom mapper.
     * <p>
     * Operations will be executed by spawning the specified number of threads
     * for each call, using internally created {@link Thread} objects to process
     * list segments in parallel.
     * </p>
     */
    public IterativeParallelism() {}

    /**
     * Constructs an {@code IterativeParallelism} instance that uses the given mapper.
     * <p>
     * All operations will be dispatched to and executed by the supplied
     * {@link ParallelMapper}, which manages its own thread pool for parallel execution.
     * </p>
     *
     * @param mapper the {@code ParallelMapper} used to execute tasks in parallel;
     *               must not be {@code null}
     * @throws IllegalArgumentException if {@code mapper} is {@code null}
     */
    public IterativeParallelism(ParallelMapper mapper) {
        this.mapper = mapper;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <T> int argMax(int threads, List<T> values, Comparator<? super T> comparator) throws InterruptedException {
        return processInParallel(threads, values,
                (start, end) -> reduceStreamToMax(IntStream.range(start, end).boxed(), values, comparator),
                results -> reduceStreamToMax(results.stream(), values, comparator)
        );
    }

    private <T> Integer reduceStreamToMax(Stream<Integer> stream, List<T> values, Comparator<? super T> comparator) {
        return stream.reduce(0, (acc, x) ->
                (comparator.compare(values.get(x), values.get(acc)) > 0 ? x : acc)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> int argMin(int threads, List<T> values, Comparator<? super T> comparator) throws InterruptedException {
        return argMax(threads, values, comparator.reversed());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> int indexOf(int threads, List<T> values, Predicate<? super T> predicate) throws InterruptedException {
        int n = values.size();
        if (n == 0) {
            return -1;
        }
        return processInParallel(threads, values,
                (start, end) ->
                        IntStream.range(start, end)
                                .reduce(-1, (acc, j) ->
                                        (acc != -1 ? acc : (predicate.test(values.get(j)) ? j : -1))
                                )
                ,
                results -> results.stream()
                        .reduce(-1, (acc, x) -> (x != -1 && (acc == -1 || x < acc) ? x : acc)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> int lastIndexOf(int threads, List<T> values, Predicate<? super T> predicate) throws InterruptedException {
        int ans = indexOf(threads, values.reversed(), predicate);
        return ans != -1 ? values.size() - ans - 1 : -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> long sumIndices(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        int n = values.size();
        if (n == 0) {
            return 0L;
        }
        return processInParallel(threads, values,
                (start, end) ->
                        LongStream.range(start, end).filter(j -> predicate.test(values.get((int) j))).sum(),
                results -> results.stream().reduce(0L, Long::sum)
        );
    }

    private <T, N> N processInParallel(int threads, List<T> values,
                                       BiFunction<Integer, Integer, N> reducer,
                                       Function<List<N>, N> combiner) throws InterruptedException {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("List is empty");
        }
        if (threads <= 0) {
            throw new IllegalArgumentException("threads number must be positive");
        }

        int len = values.size();
        int treadNum = Math.min(threads, values.size());
        int rem = len % treadNum;
        int baseSize = len / treadNum;

        if (!Objects.isNull(mapper)) {
            List<Integer> localResultsList = IntStream.range(0, treadNum).boxed().toList();
            return combiner.apply(mapper.map(i -> {
                        int start = i * baseSize + Math.min(i, rem);
                        int end = start + baseSize + (i < rem ? 1 : 0);
                        return reducer.apply(start, end);
                    }, localResultsList));
        }

        Thread[] threadPool = new Thread[treadNum];
        List<N> localResults = new ArrayList<>(Collections.nCopies(treadNum, null));

        for (int i = 0; i < treadNum; i++) {
            final int start = i * baseSize + Math.min(i, rem);
            final int end = start + baseSize + (i < rem ? 1 : 0);
            final int threadIndex = i;

            threadPool[i] = new Thread(() -> localResults.set(threadIndex, reducer.apply(start, end)));
            threadPool[i].start();
        }

        ThreadJoiner.joinAll(threadPool);

        return combiner.apply(localResults);
    }
}
