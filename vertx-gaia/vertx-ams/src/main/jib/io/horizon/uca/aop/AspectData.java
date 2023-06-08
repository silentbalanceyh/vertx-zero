package io.horizon.uca.aop;

import io.horizon.eon.VName;
import io.horizon.util.HUt;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author lang : 2023-05-27
 */
class AspectData {
    
    @SuppressWarnings("all")
    static <T> Future<T> build(final T input, final T processed) {
        if (input instanceof JsonObject) {
            final JsonObject inputJ = (JsonObject) input;
            final JsonObject processedJ = (JsonObject) processed;
            return build(inputJ, processedJ)
                .compose(json -> Future.succeededFuture((T) json));
        } else if (input instanceof JsonArray) {
            final JsonArray inputA = (JsonArray) input;
            final JsonArray processedA = (JsonArray) processed;
            return build(inputA, processedA)
                .compose(json -> Future.succeededFuture((T) json));
        } else {
            return Future.succeededFuture(processed);
        }
    }

    private static Future<JsonObject> build(final JsonObject input, final JsonObject processed) {
        final JsonObject response = processed.copy();
        if (HUt.isNotNil(input)) {
            response.put(VName.__.INPUT, input);
        }
        return Future.succeededFuture(response);
    }

    private static Future<JsonArray> build(final JsonArray input, final JsonArray processed) {
        final JsonArray array = new JsonArray();
        final int size = input.size();
        HUt.itJArray(processed, (item, index) -> {
            final JsonObject responseJ = item.copy();
            if (index < size) {
                final JsonObject inputJ = input.getJsonObject(index);
                if (HUt.isNotNil(inputJ)) {
                    responseJ.put(VName.__.INPUT, inputJ);
                }
            }
            array.add(responseJ);
        });
        return Future.succeededFuture(array);
    }
}
