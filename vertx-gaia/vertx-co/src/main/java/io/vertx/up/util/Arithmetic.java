package io.vertx.up.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

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

    static <T> Collection<T> each(final Collection<T> source, final Consumer<T>... consumers) {
        if (0 < consumers.length) {
            source.forEach(item -> Arrays.stream(consumers).forEach(consumer -> consumer.accept(item)));
        }
        return source;
    }
}
