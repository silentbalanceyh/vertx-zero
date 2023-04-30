package io.horizon.util;

import io.horizon.eon.VValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author lang : 2023/4/30
 */
class CZip {

    static <K, V, E> ConcurrentMap<K, V> zip(final Collection<E> object, final Function<E, K> keyFn, final Function<E, V> valueFn) {
        final ConcurrentMap<K, V> ret = new ConcurrentHashMap<>();
        if (0 < object.size()) {
            for (final E item : object) {
                if (null != item) {
                    final K key = keyFn.apply(item);
                    final V value = valueFn.apply(item);
                    if (null != key && null != value) {
                        ret.put(key, value);
                    }
                }
            }
        }
        return ret;
    }

    static <K, T, V> ConcurrentMap<K, V> zip(final ConcurrentMap<K, T> source, final ConcurrentMap<T, V> target) {
        final ConcurrentMap<K, V> resultMap = new ConcurrentHashMap<>();
        if (Objects.nonNull(source) && Objects.nonNull(target)) {
            source.forEach((k, t) -> {
                final V value = target.get(t);
                if (Objects.nonNull(value)) {
                    resultMap.put(k, value);
                }
            });
        }
        return resultMap;
    }

    static <F, S, T> List<T> zip(final List<F> first, final List<S> second, final BiFunction<F, S, T> function) {
        final List<T> result = new ArrayList<>();
        final int length = first.size();
        for (int idx = VValue.IDX; idx < length; idx++) {
            final F key = first.get(idx);
            final S value = valueSure(second, idx);
            final T combine = function.apply(key, value);
            if (null != key && null != combine) {
                result.add(combine);
            }
        }
        return result;
    }

    static <F, T> ConcurrentMap<F, T> zip(final List<F> keys, final List<T> values) {
        final ConcurrentMap<F, T> result = new ConcurrentHashMap<>();
        final int length = keys.size();
        for (int idx = VValue.IDX; idx < length; idx++) {
            final F key = keys.get(idx);
            final T value = valueSure(values, idx);
            if (null != key && null != value) {
                result.put(key, value);
            }
        }
        return result;
    }

    private static <T> T valueSure(final List<T> list, final int index) {
        return (0 <= index) && (index < list.size()) ? list.get(index) : null;
    }
}
