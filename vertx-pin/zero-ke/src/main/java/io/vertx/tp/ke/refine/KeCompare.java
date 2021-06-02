package io.vertx.tp.ke.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.optic.fantom.Fabric;
import io.vertx.up.atom.record.Apt;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

class KeCompare {

    static Function<JsonObject, Future<JsonObject>> combineAsync(final String field) {
        return json -> {
            if (Ut.isNil(json) || !json.containsKey(field)) {
                return Ux.future(json);
            } else {
                final Class<?> clazz = Ut.clazz(json.getString(field));
                final Fabric<JsonObject> fabric = Ut.instance(clazz);
                return fabric.combine(json);
            }
        };
    }

    /*
     * ADD / UPDATE
     * insert
     * update
     */
    static Apt compared(final Apt apt, final String fieldName, final String user) {
        final JsonArray inserted = new JsonArray();
        final JsonArray updated = new JsonArray();
        final JsonArray originalArray = new JsonArray();

        final JsonArray original = apt.dataO();
        final JsonArray latest = apt.dataN();

        Ut.itJArray(latest).forEach(each -> {
            final String value = each.getString(fieldName);
            final JsonObject item = Ut.elementFind(original, fieldName, value);
            if (Objects.isNull(item)) {
                final JsonObject record = each.copy();
                record.put(KeField.CREATED_AT, Instant.now());
                record.put(KeField.CREATED_BY, user);
                inserted.add(record);
            } else {
                final JsonObject dataItem = new JsonObject();
                dataItem.mergeIn(item.copy(), true).mergeIn(each.copy());
                dataItem.put(KeField.UPDATED_AT, Instant.now());
                dataItem.put(KeField.UPDATED_BY, user);
                originalArray.add(item.copy());
                updated.add(dataItem);
            }
        });
        return Apt.create(originalArray, latest).add(inserted).edit(updated);
    }

    static BiFunction<Function<JsonArray, Future<JsonArray>>, Function<JsonArray, Future<JsonArray>>, Future<JsonArray>> atomyFn(
            final Class<?> clazz,
            final Apt compared) {
        return (iFun, uFun) -> {
            final JsonArray inserted = compared.dataAdd();
            final JsonArray updated = compared.dataEdit();

            final Annal LOGGER = Annal.get(clazz);
            KeLog.infoKe(LOGGER, "Result of calculated, Insert = {0}, Update = {1}",
                    String.valueOf(inserted.size()),
                    String.valueOf(updated.size()));

            final List<Future<JsonArray>> futures = new ArrayList<>();
            futures.add(Ut.ifJEmpty(iFun).apply(inserted));
            futures.add(Ut.ifJEmpty(uFun).apply(updated));
            return Ux.thenCombineArray(futures);
        };
    }
}
