package io.vertx.up.unity;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.Set;
import java.util.stream.Collectors;

/*
 * 查询条件专用算法，用来合并两个维度的条件
 * 1. irV: 合并 projection
 * 2. irH: 合并 criteria
 */
final class Query {
    private static final Annal LOGGER = Annal.get(Query.class);

    /*
     * Move `RibIr` code to current method, complex algorithm to combine qr parts directly
     * 1. irV / irH:  3 arguments -> Qr Full
     * 2. irV / irH:  2 arguments -> Qr Node ( criteria, projection )
     */
    static JsonObject irV(final JsonObject query, final JsonArray projection, final boolean clear) {
        if (clear) {
            /* Overwrite Mode */
            query.put(Qr.KEY_PROJECTION, projection.copy());
        } else {
            /* Combine */
            final JsonArray original = Ut.valueJArray(query, Qr.KEY_PROJECTION);
            query.put(Qr.KEY_PROJECTION, irV(original, projection));
        }
        LOGGER.info("[Qr] Projection: \n{0}", query.encodePrettily());
        return query;
    }

    /*
     * Combine ( original, updated )
     */
    static JsonArray irV(final JsonArray original, final JsonArray projection) {
        final JsonArray originalA = Ut.valueJArray(original);
        final JsonArray projectionA = Ut.valueJArray(projection);

        // Original Set Conversation
        final Set<String> originalSet = originalA.stream()
                .filter(item -> item instanceof String)
                .map(item -> (String) item)
                .collect(Collectors.toSet());

        // Add New from
        projectionA.stream()
                .filter(item -> item instanceof String)
                .map(item -> (String) item)
                .forEach(originalSet::add);
        // Returned to Combined
        return Ut.toJArray(originalSet);
    }
}
