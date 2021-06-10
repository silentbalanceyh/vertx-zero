package io.vertx.up.util;

import io.vertx.up.commune.compare.Vs;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Collection calculation for Set
 * Here List is not needed for this arithmetic because all the calculation method require your
 * collection support non-duplicated and non-sequence.
 */
final class Arithmetic {
    private Arithmetic() {
    }

    /**
     * Collection intersect ( HashSet / TreeSet )
     * A = {1, 2}
     * B = {1, 3}
     * The result should be {1}
     *
     * @param left  First Set
     * @param right Second Set
     * @param <T>   The element type in Set
     *
     * @return The result set
     */
    static <T> Set<T> intersect(final Set<T> left,
                                final Set<T> right) {
        final Set<T> ret = new HashSet<>(left);
        ret.retainAll(right);
        return ret;
    }

    /*
     * Collection intersect ( HashSet / TreeSet )
     *
     * @param left First Set
     * @param right Second Set
     * @param fnGet The method of java bean
     * @param <T> The element entity type in Set
     *
     * @return The result set
     */
    static <T, V> Set<T> intersect(final Set<T> left, final Set<T> right, final Function<T, V> fnGet) {
        /*
         * Iterate left to pick up that element in right
         */
        final Set<T> result = new HashSet<>();
        left.stream()
                .map(original -> findBy(right, original, fnGet))
                .filter(Objects::nonNull)
                .forEach(result::add);
        return result;
    }

    /**
     * Collection union ( HashSet / TreeSet )
     * A = {1, 2}
     * B = {1, 3}
     * The result should be {1, 2, 3}
     *
     * @param left  First Set
     * @param right Second Set
     * @param <T>   The element type in Set
     *
     * @return The result Set
     */
    static <T> Set<T> union(final Set<T> left,
                            final Set<T> right) {
        final Set<T> ret = new HashSet<>();
        ret.addAll(left);
        ret.addAll(right);
        return ret;
    }

    static <T, V> Set<T> union(final Set<T> left, final Set<T> right, final Function<T, V> fnGet) {
        final Set<T> result = new HashSet<>(left);
        right.forEach(original -> {
            /*
             * Search T in `left`
             * If returned reference is null, means that original could be added
             * into result because `original` belong to `right` but not belong to `left`
             */
            final T found = findBy(left, original, fnGet);
            if (Objects.isNull(found)) {
                result.add(original);
            }
        });
        return result;
    }

    /**
     * Collection removing ( HashSet / TreeSet )
     * A = {1, 2}
     * B = {1, 3}
     * The result should be {2}
     *
     * @param subtrahend Subtrahend set
     * @param minuend    Minuend set
     * @param <T>        The element type in Set
     *
     * @return The result SEt
     */
    static <T> Set<T> diff(final Set<T> subtrahend,
                           final Set<T> minuend) {
        final Set<T> ret = new HashSet<>(subtrahend);
        ret.removeAll(minuend);
        return ret;
    }

    static <T, V> Set<T> diff(final Set<T> subtrahend, final Set<T> minuend, final Function<T, V> fnGet) {
        final Set<T> result = new HashSet<>();
        subtrahend.forEach(original -> {
            final T found = findBy(minuend, original, fnGet);
            if (Objects.isNull(found)) {
                result.add(original);
            }
        });
        return result;
    }

    @SafeVarargs
    static <T> Collection<T> each(final Collection<T> source, final Consumer<T>... consumers) {
        if (0 < consumers.length) {
            source.forEach(item -> Arrays.stream(consumers).forEach(consumer -> consumer.accept(item)));
        }
        return source;
    }

    private static <T, V> T findBy(final Set<T> source, final T original, final Function<T, V> fnGet) {
        return source.stream()
                .filter(current -> Vs.isSame(original, current, fnGet))
                .findAny().orElse(null);
    }
}
