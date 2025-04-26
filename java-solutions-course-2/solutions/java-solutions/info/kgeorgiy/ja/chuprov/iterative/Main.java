package info.kgeorgiy.ja.chuprov.iterative;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.List;
import java.util.stream.LongStream;

public class Main {
    public static void main(String[] ignored) {
        long start = System.currentTimeMillis();

        int threads = 16;
        List<Long> list = LongStream.range(0, 1000L).boxed().toList();
        try (ParallelMapper mapper = new ParallelMapperImpl(threads)) {
            mapper.close();
            mapper.map(Main::fib, list);
        } catch (Exception e) {
            System.err.println("Exception caught: ");
            System.err.format("%s%n", e.fillInStackTrace());
        }

        long end = System.currentTimeMillis();
        System.out.printf("Threads: %-3d  Time taken: %d мс%n", threads, (end - start));
    }

    public static long fib(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("Index must be non negative: " + n);
        }
        if (n <= 1) {
            return n;
        }
        long a = 0;
        long b = 1;
        long c;
        for (int i = 2; i <= n; i++) {
            c = a + b;
            a = b;
            b = c;
            if(c < 0){
                throw new RuntimeException("Fibonacci number is negative: " + c);
            }
        }
        return b;
    }
}
