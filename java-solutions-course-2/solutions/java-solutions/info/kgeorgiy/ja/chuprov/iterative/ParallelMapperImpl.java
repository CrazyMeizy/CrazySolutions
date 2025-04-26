package info.kgeorgiy.ja.chuprov.iterative;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A {@link ParallelMapper} implementation that maintains a fixed pool of worker threads
 * to apply a function to each element of a list concurrently.
 * <p>
 * Tasks submitted via {@link #map(Function, List)} are enqueued and processed by the worker threads.
 * The {@link #close()} method shuts down the pool and waits for all workers to terminate.
 * </p>
 *
 * @see ParallelMapper
 */
public class ParallelMapperImpl implements ParallelMapper {
    private final Thread[] workers;
    private final Queue<Task> taskQueue = new LinkedList<>();
    private volatile boolean isClosed = false;

    /**
     * Creates a new {@code ParallelMapperImpl} with the specified number of worker threads.
     *
     * @param threads the number of worker threads in the pool; must be positive
     * @throws IllegalArgumentException if {@code threads <= 0}
     */
    public ParallelMapperImpl(int threads) {
        workers = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            workers[i] = new Worker();
            workers[i].start();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param <T>   the type of input elements
     * @param <R>   the type of result elements
     * @param f     the function to apply to each element; must not be {@code null}
     * @param items the list of input elements; must not be {@code null}
     * @return a list of results of applying {@code f} to each element of {@code items}, in order
     * @throws InterruptedException if the current thread is interrupted while waiting for tasks to complete
     * @throws RuntimeException     if applying {@code f} to any element throws an unchecked exception
     */
    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> f,
                              List<? extends T> items) throws InterruptedException {
        int size = items.size();
        List<Optional<R>> result = new ArrayList<>(Collections.nCopies(size, Optional.empty()));
        List<Task> tasks = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            final int index = i;
            Task task = new Task(() -> {
                R res = f.apply(items.get(index));
                result.set(index, Optional.of(res));
            });
            tasks.add(task);
            synchronized (taskQueue) {
                taskQueue.add(task);
                taskQueue.notify();
            }
        }

        for (Task task : tasks) {
            task.await();
        }

        return result.stream()
                .map(opt -> opt.orElseThrow(
                        () -> new RuntimeException("Failed to apply the function to some elements")))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     *
     * @throws RuntimeException if the current thread is interrupted while waiting for workers to terminate
     */
    @Override
    public void close() {
        if(isClosed) return;
        synchronized (taskQueue) {
            isClosed = true;
            taskQueue.notifyAll();
        }
        for (Thread worker : workers) {
            worker.interrupt();
        }
        try {
            ThreadJoiner.joinAll(workers);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private class Worker extends Thread {
        @Override
        public void run() {
            while (true) {
                Task task;
                synchronized (taskQueue) {
                    while (taskQueue.isEmpty() && !isClosed) {
                        try {
                            taskQueue.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (isClosed) {
                        break;
                    }
                    task = taskQueue.remove();
                }
                try {
                    task.run();
                } catch (RuntimeException _) {
                }
            }
        }
    }

    private class Task {
        private final Runnable action;
        private boolean done = false;

        public Task(Runnable action) {
            this.action = action;
        }

        public synchronized void run() {
            try {
                action.run();
            } finally {
                done = true;
                notify();
            }
        }

        public synchronized void await() throws InterruptedException {
            while (!done && !isClosed) {
                wait();
            }
        }
    }
}
