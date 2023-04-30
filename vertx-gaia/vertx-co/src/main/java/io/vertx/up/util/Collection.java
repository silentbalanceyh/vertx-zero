package io.vertx.up.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

final class Collection {

    private Collection() {
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


    static <T, V> Set<V> set(final List<T> listT, final Function<T, V> executor) {
        final Set<V> valueSet = new HashSet<>();
        listT.stream()
            .filter(Objects::nonNull)
            .map(executor)
            .filter(Objects::nonNull)
            .forEach(valueSet::add);
        return valueSet;
    }
}
