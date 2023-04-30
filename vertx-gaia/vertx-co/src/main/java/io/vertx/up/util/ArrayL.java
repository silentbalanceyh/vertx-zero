package io.vertx.up.util;

import io.horizon.eon.VValue;
import io.horizon.uca.log.Annal;
import io.vertx.up.fn.Fn;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Do some specification statute operations
 */
final class ArrayL {

    private static final Annal LOGGER = Annal.get(ArrayL.class);

    private ArrayL() {
    }

    /**
     * @param list     The target list
     * @param fnFilter the filter for list search.
     * @param <T>      The generic type of list element.
     *
     * @return Found type for target generic type.
     */
    static <T> T find(final List<T> list, final Predicate<T> fnFilter) {
        return Fn.runOr(() -> {
            final List<T> filtered = list.stream().filter(fnFilter).toList();
            return Fn.runOr(filtered.isEmpty(), LOGGER,
                () -> null,
                () -> filtered.get(VValue.IDX));
        }, list, fnFilter);
    }

    static <K, T, V> ConcurrentMap<K, V> reduce(final ConcurrentMap<K, T> from, final ConcurrentMap<T, V> to) {
        final ConcurrentMap<K, V> result = new ConcurrentHashMap<>();
        from.forEach((key, middle) -> {
            final V value = to.get(middle);
            if (null != value) {
                result.put(key, value);
            }
        });
        return result;
    }

    static <K, V> ConcurrentMap<K, V> reduce(final Set<K> from, final ConcurrentMap<K, V> to) {
        final ConcurrentMap<K, V> result = new ConcurrentHashMap<>();
        from.forEach((key) -> {
            final V value = to.get(key);
            if (null != value) {
                result.put(key, value);
            }
        });
        return result;
    }

    static <T, V> Set<V> set(final List<T> listT, final Function<T, V> executor) {
        final Set<V> valueSet = new HashSet<>();
        listT.stream()
            .filter(Objects::nonNull)
            .map(executor)
            .filter(Objects::nonNull)
            .forEach(valueSet::add);
        return valueSet;
    }

    static <K, V> ConcurrentMap<K, List<V>> compress(final List<ConcurrentMap<K, List<V>>> dataList) {
        final ConcurrentMap<K, List<V>> resultMap = new ConcurrentHashMap<>();
        dataList.forEach(each -> each.forEach((k, vList) -> {
            /*
             * Original Key Extraction
             */
            if (Objects.nonNull(k)) {
                /*
                 * Contains k checking for result map
                 */
                if (!resultMap.containsKey(k)) {
                    resultMap.put(k, new ArrayList<>());
                }
                final List<V> ref = resultMap.get(k);
                vList.stream().filter(Objects::nonNull).forEach(ref::add);
                resultMap.put(k, ref);  // Replace
            }
        }));
        return resultMap;
    }
}
