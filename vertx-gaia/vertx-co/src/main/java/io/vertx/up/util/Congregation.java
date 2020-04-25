package io.vertx.up.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.fn.Actuator;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * For collection
 */
final class Congregation {
    private static final Annal LOGGER = Annal.get(Congregation.class);

    private Congregation() {
    }

    /**
     * @param map      Iterated hash map
     * @param consumer kv consumer
     * @param <K>      key type
     * @param <V>      value type
     */
    static <K, V> void exec(final ConcurrentMap<K, V> map, final BiConsumer<K, V> consumer) {
        map.forEach((key, value) -> {
            if (null != key && null != value) {
                consumer.accept(key, value);
            }
        });
    }

    /**
     * @param list     Iterated list
     * @param consumer element index consumer
     * @param <V>      value type
     */
    static <V> void exec(final List<V> list, final BiConsumer<V, Integer> consumer) {
        final int size = list.size();
        for (int idx = Values.IDX; idx < size; idx++) {
            final V item = list.get(idx);
            if (null != item) {
                consumer.accept(item, idx);
            }
        }
    }

    /**
     * @param matrix   Iterated matrix
     * @param consumer item consumer
     * @param <V>      value type
     */
    static <V> void exec(final V[][] matrix, final Consumer<V> consumer) {
        for (final V[] arr : matrix) {
            for (final V item : arr) {
                if (null != item) {
                    consumer.accept(item);
                }
            }
        }
    }

    static <F, S> void exec(final Collection<F> firsts, final Function<F, Collection<S>> seconds, final BiConsumer<F, S> consumer) {
        firsts.forEach(first -> seconds.apply(first)
                .forEach(second -> consumer.accept(first, second)));
    }

    static <F, S> void exec(final Collection<F> firsts, final Function<F, Collection<S>> seconds, final BiConsumer<F, S> consumer, final BiPredicate<F, S> predicate) {
        firsts.forEach(first -> seconds.apply(first)
                .stream().filter(second -> predicate.test(first, second))
                .forEach(second -> consumer.accept(first, second)));
    }

    static void exec(final Integer times, final Actuator actuator) {
        int start = 0;
        while (start < times) {
            actuator.execute();
            start++;
        }
    }

    /**
     * @param data     JsonObject that will be iterated
     * @param consumer consumer of field, value
     * @param <T>      Value type
     */
    static <T> void exec(final JsonObject data,
                         final BiConsumer<T, String> consumer) {
        try {
            Fn.etJObject(data, consumer::accept);
        } catch (final ZeroException ex) {
            LOGGER.jvm(ex);
        }
    }

    /**
     * @param dataArray JsonArray that will be iterated
     * @param clazz     element expected type ( Others will be ignored )
     * @param consumer  consumer of element
     * @param <T>       element expected
     */
    static <T> void exec(final JsonArray dataArray, final Class<T> clazz, final BiConsumer<T, Integer> consumer) {
        try {
            Fn.etJArray(dataArray, clazz, consumer::accept);
        } catch (final ZeroException ex) {
            LOGGER.jvm(ex);
        }
    }
}
