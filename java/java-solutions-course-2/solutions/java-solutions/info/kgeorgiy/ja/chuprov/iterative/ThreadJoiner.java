package info.kgeorgiy.ja.chuprov.iterative;

/**
 * Utility class for waiting on a group of threads to terminate.
 * <p>
 * Provides a method to sequentially join multiple threads, collecting
 * any {@link InterruptedException InterruptedExceptions} that occur during
 * the joining process and re-throwing them once all threads have been joined.
 * </p>
 */
public class ThreadJoiner {

    /**
     * Waits for all threads in the provided array to finish execution.
     * <p>
     * For each thread, this method invokes {@link Thread#join()}. If the
     * current thread is interrupted while waiting, the {@link InterruptedException}
     * is caught and accumulated. The join attempt is retried until the thread
     * terminates. All caught interruptions are added as suppressed exceptions
     * to a single {@link InterruptedException} which is thrown after every
     * thread has been joined.
     * </p>
     *
     * @param threadPool an array of threads to join, must contain no {@code null}
     *                   elements and each thread should have been started prior
     *                   to calling this method
     * @throws InterruptedException if the current thread was interrupted while
     *                              waiting for any of the threads; in this case,
     *                              the interrupt status is restored and all
     *                              interruptions are attached as suppressed exceptions
     */
    public static void joinAll(Thread[] threadPool) throws InterruptedException {
        InterruptedException combinedException = null;
        for (Thread thread : threadPool) {
            while (true) {
                try {
                    thread.join();
                    break;
                } catch (InterruptedException e) {
                    if (combinedException == null) {
                        combinedException = e;
                    } else {
                        combinedException.addSuppressed(e);
                    }
                }
            }
        }

        if (combinedException != null) {
            Thread.currentThread().interrupt();
            throw combinedException;
        }
    }
}
