package io.vertx.up.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

final class ArrayUtil {

    private ArrayUtil() {
    }

    @SuppressWarnings("all")
    private static Object copyArrayGrow1(final Object array, final Class<?> newArrayComponentType) {
        if (array != null) {
            final int arrayLength = Array.getLength(array);
            final Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
            System.arraycopy(array, 0, newArray, 0, arrayLength);
            return newArray;
        }
        return Array.newInstance(newArrayComponentType, 1);
    }

    static JsonArray flat(final JsonArray item) {
        final JsonArray normalized = new JsonArray();
        item.stream().filter(Objects::nonNull).forEach(each -> {
            if (each instanceof JsonArray) {
                normalized.addAll(flat((JsonArray) each));
            } else {
                normalized.add(each);
            }
        });
        return normalized;
    }

    static <T> T[] add(final T[] array, final T element) {
        final Class<?> type;
        if (array != null) {
            type = array.getClass().getComponentType();
        } else if (element != null) {
            type = element.getClass();
        } else {
            throw new IllegalArgumentException("Arguments cannot both be null");
        }
        @SuppressWarnings("unchecked") // type must be T
        final T[] newArray = (T[]) copyArrayGrow1(array, type);
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    /**
     * Replaced duplicated by key
     * This method will modify input array.
     *
     * @param array      source
     * @param jsonObject element that will be added.
     *
     * @return the new json array
     */
    static JsonArray add(final JsonArray array,
                         final JsonObject jsonObject,
                         final String field) {
        // counter
        int targetIndex = Values.UNSET;
        for (int idx = 0; idx < array.size(); idx++) {
            final JsonObject element = array.getJsonObject(idx);
            if (null != element) {
                final Object elementValue = element.getValue(field);
                final Object value = jsonObject.getValue(field);
                if (Objects.nonNull(elementValue) && Objects.nonNull(value)
                        && elementValue.equals(value)) {
                    targetIndex = idx;
                    break;
                }
            }
        }
        if (Values.ZERO < targetIndex) {
            array.getJsonObject(targetIndex).clear().mergeIn(jsonObject);
        } else {
            array.add(jsonObject);
        }
        return array;
    }

    static JsonArray climb(final JsonArray children, final JsonArray tree, final JsonObject options) {
        final JsonArray result = new JsonArray();
        /*
         * Array to Map
         */
        final JsonObject opts = treeOption(options);
        final ConcurrentMap<String, JsonObject> maps = new ConcurrentHashMap<>();
        children.stream().filter(Objects::nonNull).map(each -> (JsonObject) each).forEach(child -> {
            /*
             * 当前 child
             */
            final JsonArray parents = climb(child, tree, options);
            final String idField = opts.getString("key");
            parents.stream().filter(Objects::nonNull).map(found -> (JsonObject) found)
                    .filter(found -> Objects.nonNull(found.getString(idField)))
                    .forEach(found -> maps.put(found.getString(idField), found));
        });
        maps.values().forEach(result::add);
        return result;
    }

    static JsonArray child(final JsonArray current, final JsonArray tree, final JsonObject options) {
        final JsonArray result = new JsonArray();
        /*
         * Array to Map
         */
        final JsonObject opts = treeOption(options);
        final ConcurrentMap<String, JsonObject> maps = new ConcurrentHashMap<>();
        current.stream().filter(Objects::nonNull).map(each -> (JsonObject) each).forEach(child -> {
            final JsonArray children = child(child, tree, options);
            final String idField = opts.getString("key");
            children.stream().filter(Objects::nonNull).map(found -> (JsonObject) found)
                    .filter(found -> Objects.nonNull(found.getString(idField)))
                    .forEach(found -> maps.put(found.getString(idField), found));
        });
        maps.values().forEach(result::add);
        return result;
    }

    static JsonObject find(final JsonArray array, final String field, final Object value) {
        return It.itJArray(array)
                .filter(item -> {
                    if (Objects.isNull(value)) {
                        return Objects.isNull(item.getValue(field));
                    } else {
                        return value.equals(item.getValue(field));
                    }
                })
                .findAny().orElse(null);
    }

    static JsonArray save(final JsonArray array, final JsonArray input, final String field) {
        Ut.itJArray(input).forEach(json -> save(array, json, field));
        return array;
    }

    static JsonArray save(final JsonArray array, final JsonObject json, final String field) {
        return Fn.getNull(new JsonArray(), () -> {
            final AtomicBoolean isFound = new AtomicBoolean(Boolean.FALSE);
            It.itJArray(array).forEach(each -> {
                final boolean isSame = Is.isSame(each, json, field);
                if (isSame) {
                    each.mergeIn(json, true);
                    isFound.set(Boolean.TRUE);
                }
            });
            if (!isFound.get()) {
                /*
                 * Not found, add
                 */
                array.add(json.copy());
            }
            return array;
        }, array, json, field);
    }

    static JsonArray child(final JsonObject current, final JsonArray tree, final JsonObject options) {
        final JsonArray result = new JsonArray();
        /*
         * Wether it contains current node
         */
        final Boolean include = Objects.nonNull(options) ? options.getBoolean("include") : Boolean.TRUE;
        if (include) {
            result.add(current);
        }
        /*
         * Children find
         */
        final JsonArray children = elementChild(current, tree, options);
        if (!children.isEmpty()) {
            result.addAll(child(children, tree, options));
        }
        return result;
    }

    static JsonArray climb(final JsonObject child, final JsonArray tree, final JsonObject options) {
        final JsonArray result = new JsonArray();
        /*
         * Wether it contains current node
         */
        final Boolean include = Objects.nonNull(options) ? options.getBoolean("include") : Boolean.TRUE;
        if (include) {
            result.add(child);
        }
        /*
         * Parent Find
         */
        final JsonObject parent = elementParent(child, tree, options);
        if (Objects.nonNull(parent)) {
            result.addAll(climb(parent, tree, options));
        }
        return result;
    }

    private static JsonArray elementChild(final JsonObject item, final JsonArray tree, final JsonObject options) {
        final JsonObject opts = treeOption(options);
        final String parentField = opts.getString("parent");
        final String idField = opts.getString("key");
        final List<JsonObject> children = tree.stream().filter(Objects::nonNull).map(each -> (JsonObject) each)
                .filter(each -> Objects.nonNull(each.getString(parentField)))
                .filter(each -> each.getString(parentField).equals(item.getString(idField)))
                .collect(Collectors.toList());
        return new JsonArray(children);
    }

    private static JsonObject elementParent(final JsonObject item, final JsonArray tree, final JsonObject options) {
        final JsonObject opts = treeOption(options);
        final String parentField = opts.getString("parent");
        final String idField = opts.getString("key");
        return tree.stream().filter(Objects::nonNull).map(each -> (JsonObject) each)
                .filter(each -> Objects.nonNull(each.getString(idField)))
                .filter(each -> each.getString(idField).equals(item.getString(parentField)))
                .findFirst().orElse(null);
    }


    private static JsonObject treeOption(final JsonObject options) {
        final JsonObject target = new JsonObject()
                .put("parent", "parentId")
                .put("key", "key")
                .put("include", Boolean.TRUE);
        if (Objects.nonNull(options)) {
            target.mergeIn(options);
        }
        return target;
    }
}
