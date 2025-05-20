package info.kgeorgiy.ja.chuprov.lambda;

import info.kgeorgiy.java.advanced.lambda.EasyLambda;
import info.kgeorgiy.java.advanced.lambda.Trees;

import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;

public class Lambda implements EasyLambda {
    @Override
    public <T> Spliterator<T> binaryTreeSpliterator(Trees.Binary<T> tree) {
        return new BinaryTreeSpliterator<>(tree);
    }

    @Override
    public <T> Spliterator<T> sizedBinaryTreeSpliterator(Trees.SizedBinary<T> tree) {
        return new SizedBinaryTreeSpliterator<>(tree);
    }

    @Override
    public <T> Spliterator<T> naryTreeSpliterator(Trees.Nary<T> tree) {
        return new NaryTreeSpliterator<>(tree);
    }

    @Override
    public <T> Collector<T, ?, Optional<T>> first() {
        return Collector.of(
                AtomicReference<T>::new,
                (acc, t) -> {
                    if (acc.get() == null) acc.set(t);
                },
                (acc1, acc2) -> acc1.get() != null ? acc1 : acc2,
                acc -> Optional.ofNullable(acc.get())
        );
    }

    @Override
    public <T> Collector<T, ?, Optional<T>> last() {
        return Collector.of(
                AtomicReference<T>::new,
                AtomicReference::set,
                (acc1, acc2) -> acc2.get() != null ? acc2 : acc1,
                acc -> Optional.ofNullable(acc.get())
        );
    }

    @Override
    public <T> Collector<T, ?, Optional<T>> middle() {
        return Collector.of(MiddleAccumulator<T>::new,
                MiddleAccumulator::accumulate,
                MiddleAccumulator::combine,
                MiddleAccumulator::finish);
    }

    @Override
    public Collector<CharSequence, ?, String> commonPrefix() {
        return commonCollector(UnaryOperator.identity(), CharSequence::charAt);
    }

    @Override
    public Collector<CharSequence, ?, String> commonSuffix() {
        return commonCollector(StringBuilder::reverse, (s, i) -> s.charAt(s.length() - 1 - i));
    }

    private Collector<CharSequence, ?, String> commonCollector(
            Function<StringBuilder, StringBuilder> converse,
            BiFunction<CharSequence, Integer, Character> charExtractor) {
        return Collector.of(
                AtomicReference::new,
                accumulate(converse, charExtractor),
                this::mergeCommon,
                ref -> Optional.ofNullable(ref.get())
                        .map(converse)
                        .map(StringBuilder::toString)
                        .orElse("")
        );
    }

    private BiConsumer<AtomicReference<StringBuilder>, CharSequence> accumulate(
            Function<StringBuilder, StringBuilder> converse,
            BiFunction<CharSequence, Integer, Character> charExtractor) {
        return (ref, seq) -> {
            Objects.requireNonNull(seq);
            if (ref.get() == null) {
                ref.set(converse.apply(new StringBuilder(seq)));
            } else {
                updateCommon(ref.get(), seq, charExtractor);
            }
        };
    }

    private AtomicReference<StringBuilder> mergeCommon(AtomicReference<StringBuilder> ref1,
                                                       AtomicReference<StringBuilder> ref2) {
        if (ref1.get() == null) return ref2;
        if (ref2.get() == null) return ref1;
        updateCommon(ref1.get(), ref2.get(), CharSequence::charAt);
        return ref1;
    }

    private static void updateCommon(StringBuilder sb, CharSequence seq,
                                     BiFunction<CharSequence, Integer, Character> charExtractor) {
        int minL = Math.min(sb.length(), seq.length());
        sb.setLength(minL);
        for (int i = 0; i < minL; i++) {
            if (sb.charAt(i) != charExtractor.apply(seq, i)) {
                sb.setLength(i);
                break;
            }
        }
    }
}
