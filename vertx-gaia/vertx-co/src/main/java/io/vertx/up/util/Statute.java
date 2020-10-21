package io.vertx.up.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Do some specification statute operations
 */
final class Statute {

    private static final Annal LOGGER = Annal.get(Statute.class);

    private Statute() {
    }

    /**
     * @param list     The target list
     * @param fnFilter the filter for list search.
     * @param <T>      The generic type of list element.
     *
     * @return Found type for target generic type.
     */
    static <T> T find(final List<T> list, final Predicate<T> fnFilter) {
        return Fn.getNull(() -> {
            final List<T> filtered = list.stream().filter(fnFilter).collect(Collectors.toList());
            return Fn.getSemi(filtered.isEmpty(), LOGGER,
                    () -> null,
                    () -> filtered.get(Values.IDX));
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

    static <F, T> ConcurrentMap<F, T> zipper(final List<F> keys, final List<T> values) {
        final ConcurrentMap<F, T> result = new ConcurrentHashMap<>();
        Ut.itList(keys, (key, index) -> {
            final T value = getEnsure(values, index);
            // Ignore for null element
            if (null != key && null != value) {
                result.put(key, value);
            }
        });
        return result;
    }

    static JsonObject subset(final JsonObject input, final String... pickedKeys) {
        return subset(input, new HashSet<>(Arrays.asList(pickedKeys)));
    }

    static JsonObject subset(final JsonObject input, final Set<String> pickedKeys) {
        if (Objects.isNull(input)) {
            return new JsonObject();
        } else {
            final JsonObject normalized = new JsonObject(); // input.copy();
            pickedKeys.forEach(field -> normalized.put(field, input.getValue(field)));
            return normalized;
        }
    }

    static JsonArray subset(final JsonArray array, final Function<JsonObject, Boolean> fnFilter) {
        return Fn.getNull(new JsonArray(), () -> {
            final JsonArray subset = new JsonArray();
            It.itJArray(array).filter(fnFilter::apply).forEach(subset::add);
            return subset;
        }, array, fnFilter);
    }

    static <K, V, E> ConcurrentMap<K, V> zipper(final Collection<E> object, final Function<E, K> keyFn, final Function<E, V> valueFn) {
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

    static JsonObject zipper(final JsonArray array, final String field) {
        final JsonObject grouped = new JsonObject();
        array.stream().map(item -> (JsonObject) item)
                .filter(Objects::nonNull)
                .filter(item -> Objects.nonNull(item.getValue(field)))
                .forEach(item -> grouped.put(item.getString(field), item.copy()));
        return grouped;
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

    static <K, V, E> ConcurrentMap<K, List<V>> group(final Collection<E> object, final Function<E, K> keyFn, final Function<E, V> valueFn) {
        final ConcurrentMap<K, List<V>> ret = new ConcurrentHashMap<>();
        if (Objects.nonNull(object) && !object.isEmpty()) {
            for (final E item : object) {
                if (null != item) {
                    final K key = keyFn.apply(item);
                    if (null != key) {
                        // Extract List
                        List<V> reference = null;
                        if (ret.containsKey(key)) {
                            reference = ret.get(key);
                        }
                        // Double check
                        if (null == reference) {
                            reference = new ArrayList<>();
                        }
                        final V value = valueFn.apply(item);
                        if (null != value) {
                            reference.add(value);
                        }
                        // Replace finally
                        ret.put(key, reference);
                    }
                }
            }
        }
        return ret;
    }

    static <K, V, E> ConcurrentMap<K, V> map(final List<E> list, final Function<E, K> keyFn, final Function<E, V> valueFn) {
        final ConcurrentMap<K, V> grouped = new ConcurrentHashMap<>();
        if (Objects.nonNull(list)) {
            list.stream().filter(Objects::nonNull).forEach(each -> {
                final K key = keyFn.apply(each);
                final V value = valueFn.apply(each);
                if (Objects.nonNull(key) && Objects.nonNull(value)) {
                    grouped.put(key, value);
                }
            });
        }
        return grouped;
    }

    static ConcurrentMap<String, JsonObject> map(final JsonArray data, final String field) {
        final ConcurrentMap<String, JsonObject> mapped = new ConcurrentHashMap<>();
        It.itJArray(data).forEach(json -> {
            final String key = json.getString(field);
            if (Ut.notNil(key)) {
                mapped.put(key, json);
            }
        });
        return mapped;
    }

    static ConcurrentMap<String, JsonArray> group(final JsonArray source, final Function<JsonObject, String> executor) {
        final ConcurrentMap<String, JsonArray> ret = new ConcurrentHashMap<>();
        if (Objects.nonNull(source) && !source.isEmpty()) {
            source.stream().filter(Objects::nonNull)
                    .map(item -> (JsonObject) item)
                    .filter(item -> Objects.nonNull(executor.apply(item)))
                    .forEach(item -> {
                        /*
                         * JsonArray get
                         */
                        final String key = executor.apply(item);
                        /*
                         * `key` calculated here for map final `key`
                         */
                        JsonArray reference = ret.get(key);
                        if (Objects.isNull(reference)) {
                            reference = new JsonArray();
                            ret.put(key, reference);
                        }
                        /*
                         * Add new Object
                         */
                        reference.add(item.copy());
                    });
        }
        return ret;
    }

    static ConcurrentMap<String, JsonArray> group(final JsonArray source, final String field) {
        return group(source, item -> item.getString(field));
    }

    static <K, T, V> ConcurrentMap<K, V> zipper(final ConcurrentMap<K, T> source, final ConcurrentMap<T, V> target) {
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

    static <F, S, T> List<T> zipper(final List<F> first, final List<S> second, final BiFunction<F, S, T> function) {
        final List<T> result = new ArrayList<>();
        Ut.itList(first, (key, index) -> {
            final S value = getEnsure(second, index);
            final T merged = function.apply(key, value);
            if (null != merged) {
                result.add(merged);
            }
        });
        return result;
    }

    private static <T> T getEnsure(final List<T> list, final int index) {
        return (0 <= index) && (index < list.size()) ? list.get(index) : null;
    }
}
