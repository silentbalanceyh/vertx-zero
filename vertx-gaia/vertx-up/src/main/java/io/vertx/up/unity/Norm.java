package io.vertx.up.unity;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.horizon.eon.em.ChangeFlag;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class Norm {

    /*
     * Output Normalized for new specification such as following:
     *
     * {
     *     "__data": "previous data before updating",
     *     "__flag": "current operation flag: ADD | UPDATE | DELETE "
     * }
     *
     * Here are specification that's stored in KName.__
     * - __metadata:    Metadata data that are stored ( field = type ) part
     * - __data:        This node stored previous data record
     * - __flag:        This node identified current operation: ADD | UPDATE | DELETE
     *
     * The final situation is as following
     * 1) Add new record:       __data = null,     __flag   ( Could ignored )
     * 2) Update record:        __data = previous, __flag   ( Could ignored )
     * 3) Delete record:        __data = previous, __flag   ( Must be DELETE )
     */
    static Future<JsonObject> effect(final JsonObject input, final JsonObject previous, final JsonObject current) {
        final JsonObject combine = effectInternal(previous, current);
        final JsonObject inputJ = Ut.valueJObject(input);
        combine.put(KName.__.INPUT, inputJ);
        return Ux.future(combine);
    }

    static Future<JsonArray> effect(final JsonArray previous, final JsonArray current, final String field) {
        if (Ut.isNil(current) && Ut.isNil(previous)) {
            // current = [], previous = []
            return Ux.futureA();
        } else {
            final JsonArray response = new JsonArray();
            if (Objects.isNull(current)) {

                // DELETE ( Whole ), All element is DELETE
                Ut.itJArray(previous)
                    .map(item -> effectInternal(item, null)).forEach(response::add);
            } else {
                if (Ut.isNil(previous)) {

                    // ADD ( Whole ), All element is ADD
                    Ut.itJArray(current)
                        .map(item -> effectInternal(null, item)).forEach(response::add);
                } else {

                    // UPDATE ( Json May be UPDATE, ADD, DELETE )
                    final ConcurrentMap<ChangeFlag, JsonArray> comparedMap
                        = CompareJ.compareJ(previous, current, field);
                    Ut.itJArray(comparedMap.get(ChangeFlag.ADD))
                        .map(item -> effectInternal(null, item)).forEach(response::add);
                    Ut.itJArray(comparedMap.get(ChangeFlag.DELETE))
                        .map(item -> effectInternal(item, null)).forEach(response::add);

                    // UPDATE ( prev / now )
                    Ut.itJArray(current).forEach(record -> {
                        // Found Must
                        // record = New
                        final JsonObject found = Ut.elementFind(previous, field, record.getValue(field));
                        if (Ut.notNil(found)) {
                            final JsonObject combine = effectInternal(found, record);
                            response.add(combine);
                        }
                    });
                }
            }
            return Ux.future(response);
        }
    }

    private static JsonObject effectInternal(final JsonObject previous, final JsonObject current) {
        if (Objects.isNull(current) && Objects.isNull(previous)) {
            // current = null, previous = null
            return new JsonObject();
        } else {
            final JsonObject response = new JsonObject();
            if (Objects.isNull(current)) {
                // DELETE
                response.mergeIn(previous, true);
                response.put(KName.__.DATA, previous.copy());
                response.put(KName.__.FLAG, ChangeFlag.DELETE.name());
            } else {
                if (Objects.isNull(previous)) {
                    // ADD
                    response.mergeIn(current, true);
                    response.putNull(KName.__.DATA);
                    response.put(KName.__.FLAG, ChangeFlag.ADD.name());
                } else {
                    // UPDATE
                    response.mergeIn(current, true);
                    response.put(KName.__.DATA, previous.copy());
                    response.put(KName.__.FLAG, ChangeFlag.UPDATE.name());
                }
            }
            return response;
        }
    }

    private static Future<JsonObject> combine(final JsonObject input, final JsonObject processed) {
        final JsonObject response = processed.copy();
        if (Ut.notNil(input)) {
            response.put(KName.__.INPUT, input);
        }
        return Ux.future(response);
    }

    private static Future<JsonArray> combine(final JsonArray input, final JsonArray processed) {
        final JsonArray array = new JsonArray();
        final int size = input.size();
        Ut.itJArray(processed, (item, index) -> {
            final JsonObject responseJ = item.copy();
            if (index < size) {
                final JsonObject inputJ = input.getJsonObject(index);
                if (Ut.notNil(inputJ)) {
                    responseJ.put(KName.__.INPUT, inputJ);
                }
            }
            array.add(responseJ);
        });
        return Ux.future(array);
    }

    @SuppressWarnings("unchecked")
    static <T> Future<T> combine(final T input, final T processed) {
        if (input instanceof final JsonObject inputJ) {
            final JsonObject processedJ = (JsonObject) processed;
            return combine(inputJ, processedJ)
                .compose(json -> To.future((T) json));
        } else if (input instanceof final JsonArray inputA) {
            final JsonArray processedA = (JsonArray) processed;
            return combine(inputA, processedA)
                .compose(json -> To.future((T) json));
        } else {
            return Ux.future(processed);
        }
    }
}
