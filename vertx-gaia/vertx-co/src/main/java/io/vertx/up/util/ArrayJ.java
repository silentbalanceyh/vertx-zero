package io.vertx.up.util;

import io.horizon.util.HaS;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

final class ArrayJ {

    private ArrayJ() {
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

    /**
     * Replaced duplicated by key
     * This method will modify input array.
     *
     * @param array      source
     * @param jsonObject element that will be added.
     *
     * @return the new json array
     */

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
        return HaS.itJArray(array).filter(item -> {
            if (Objects.isNull(value)) {
                return Objects.isNull(item.getValue(field));
            } else {
                return value.equals(item.getValue(field));
            }
        }).findAny().orElse(null);
    }

    static JsonObject find(final JsonArray array, final JsonObject subsetQ) {
        return HaS.itJArray(array).filter(item -> {
            final Set<String> keys = subsetQ.fieldNames();
            final JsonObject subset = HaS.elementSubset(item, keys);
            return subset.equals(subsetQ);
        }).findAny().orElse(new JsonObject());
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

    static boolean isSame(final JsonArray dataO, final JsonArray dataN,
                          final Set<String> fields, final boolean sequence) {
        final JsonArray arrOld = Ut.valueJArray(dataO);
        final JsonArray arrNew = Ut.valueJArray(dataN);
        if (arrOld.size() != arrNew.size()) {
            return false;
        }
        final JsonArray subOld = Ut.elementSubset(arrOld, fields);
        final JsonArray subNew = Ut.elementSubset(arrNew, fields);
        if (sequence) {
            return subOld.equals(subNew);
        } else {
            return Ut.itJArray(subOld).allMatch(jsonOld ->
                Ut.itJArray(subNew).anyMatch(jsonNew -> jsonNew.equals(jsonOld))
            );
        }
    }
}
