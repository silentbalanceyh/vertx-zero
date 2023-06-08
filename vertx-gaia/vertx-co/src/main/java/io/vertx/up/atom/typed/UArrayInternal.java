package io.vertx.up.atom.typed;

import io.horizon.eon.VValue;
import io.reactivex.rxjava3.core.Observable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("all")
class UArrayInternal {

    static JsonObject append(
        final JsonObject target,
        final JsonObject source,
        final boolean immutable
    ) {
        final JsonObject result = immutable ? target.copy() : target;
        Observable.fromIterable(source.fieldNames())
            .filter(key -> !target.containsKey(key))
            .subscribe(key -> result.put(key, source.getValue(key))).dispose();
        return result;
    }

    static JsonObject append(
        final JsonObject target,
        final JsonArray sources
    ) {
        Observable.fromIterable(sources)
            .map(item -> (JsonObject) item)
            .subscribe(item -> append(target, item, false)).dispose();
        return target;
    }

    static JsonArray zip(
        final JsonArray targetArray,
        final JsonArray sources
    ) {
        final JsonArray results = new JsonArray();
        Ut.itJArray(targetArray, JsonObject.class, (source, index) -> {
            final JsonObject target = sources.getJsonObject(index);
            final JsonObject result = null == target ? source :
                append(source, target, true);
            results.add(result);
        });
        targetArray.clear();
        return targetArray.addAll(results);
    }

    static JsonArray zip(
        final JsonArray target,
        final JsonArray sources,
        final String fromKey,
        final String toKey
    ) {
        final ConcurrentMap<Integer, Object> targetMap = mapIndex(target, fromKey);
        final ConcurrentMap<Object, JsonObject> sourceMap = mapZip(sources, toKey);
        final ConcurrentMap<Integer, JsonObject> merged = Ut.elementZip(targetMap, sourceMap);
        final JsonArray results = new JsonArray();
        for (int idx = 0; idx < target.size(); idx++) {
            final JsonObject targetItem = merged.get(idx);
            final JsonObject sourceItem = target.getJsonObject(idx);
            final JsonObject item = null == targetItem ? sourceItem :
                append(sourceItem, targetItem, true);
            results.add(item);
        }
        target.clear();
        return target.addAll(results);
    }

    private static ConcurrentMap<Object, JsonObject> mapZip(
        final JsonArray sources,
        final String field
    ) {
        final ConcurrentMap<Object, JsonObject> resultMap =
            new ConcurrentHashMap<>();
        Observable.fromIterable(sources)
            .map(item -> (JsonObject) item)
            .filter(item -> item.containsKey(field))
            .subscribe(item -> {
                if (item.containsKey(field)) {
                    final Object value = item.getValue(field);
                    if (null != value) {
                        resultMap.put(value, item);
                    }
                }
            }).dispose();
        return resultMap;
    }

    private static ConcurrentMap<Integer, Object> mapIndex(
        final JsonArray sources,
        final String field
    ) {
        final ConcurrentMap<Integer, Object> resultMap =
            new ConcurrentHashMap<>();
        for (int idx = VValue.IDX; idx < sources.size(); idx++) {
            final JsonObject item = sources.getJsonObject(idx);
            final Object value = item.getValue(field);
            if (null != value) {
                resultMap.put(idx, value);
            }
        }
        return resultMap;
    }
}
