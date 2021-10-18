package io.vertx.up.commune.envelop;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.eon.ID;
import io.vertx.up.eon.Strings;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class RibIr {

    private static final Annal LOGGER = Annal.get(RibIr.class);

    static void irProjection(final JsonObject reference, final JsonArray projection, final boolean clear) {
        if (clear) {
            /* Overwrite Mode */
            reference.put(Qr.KEY_PROJECTION, projection.copy());
        } else {
            /* Update Mode */
            final Set<String> originalSet = originalProjection(reference);
            /* Add New */
            projection.copy().stream().filter(Objects::nonNull)
                .map(item -> (String) item)
                .forEach(originalSet::add);
            /* Replace */
            reference.put(Qr.KEY_PROJECTION, Ut.toJArray(originalSet));
        }
        LOGGER.info("Final query ( projection ): \n{0}", reference.encodePrettily());
    }

    static void irCriteria(final JsonObject reference, final JsonObject criteria, final boolean clear) {
        if (clear) {
            /* Overwrite Mode */
            reference.put(Qr.KEY_CRITERIA, criteria.copy());
        } else {
            /* Update Mode */
            final JsonObject originalCriteria = reference.getJsonObject(Qr.KEY_CRITERIA);
            /* Original Is Null */
            final JsonObject criteriaResult = new JsonObject();
            if (Ut.isNil(originalCriteria)) {
                /* New Criteria is the major criteria */
                criteriaResult.mergeIn(criteria.copy());
            } else {
                /* Build new Tree of criteria, join 2 criterias */
                criteriaResult.put(Strings.EMPTY, Boolean.TRUE);
                criteriaResult.put(ID.TREE_ORIGINAL, originalCriteria.copy());
                criteriaResult.put(ID.TREE_MATRIX, criteria.copy());
            }
            /* Replace */
            reference.put(Qr.KEY_CRITERIA, criteriaResult);
        }
        LOGGER.info("Final query ( criteria ): \n{0}", reference.encodePrettily());
    }

    private static Set<String> originalProjection(final JsonObject reference) {
        final Set<String> projectionSet = new HashSet<>();
        if (reference.containsKey(Qr.KEY_PROJECTION)) {
            reference.getJsonArray(Qr.KEY_PROJECTION).stream()
                .map(item -> (String) item).forEach(projectionSet::add);
        }
        return projectionSet;
    }
}
