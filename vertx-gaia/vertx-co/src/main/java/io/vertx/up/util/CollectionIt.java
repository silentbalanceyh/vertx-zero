package io.vertx.up.util;

import io.horizon.eon.VValue;
import io.horizon.uca.log.Annal;
import io.horizon.util.HUt;
import io.vertx.core.json.JsonObject;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * For collection
 */
final class CollectionIt {
    private static final Annal LOGGER = Annal.get(CollectionIt.class);

    private CollectionIt() {
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
        for (int idx = VValue.IDX; idx < size; idx++) {
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

    @SuppressWarnings("unchecked")
    static <T> void exec(final JsonObject data, final String flag,
                         final Boolean sec, // S - Start, E - End, C - Contains
                         final BiConsumer<T, String> consumer) {
        if (HUt.isNil(flag)) {
            HUt.itJObject(data, consumer);
        } else {
            data.stream().filter(entry -> {
                final String field = entry.getKey();
                if (Objects.isNull(sec)) {
                    return field.contains(flag);
                } else if (sec) {
                    return field.startsWith(flag);
                } else {
                    return field.endsWith(flag);
                }
            }).forEach(entry -> {
                final Object value = entry.getValue();
                if (Objects.nonNull(value)) {
                    consumer.accept((T) value, entry.getKey());
                }
            });
        }
    }
}
