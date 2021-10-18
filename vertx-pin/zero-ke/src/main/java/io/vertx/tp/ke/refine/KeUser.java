package io.vertx.tp.ke.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class KeUser {

    static <I> void audit(final I output, final JsonObject serialized, final boolean isCreated) {
        /*
         * - sigma
         * - language
         * - active
         * - createdBy
         * - updatedBy
         * - createdAt ( Calculate )
         * - updatedAt ( Calculate )
         */
        Ut.field(output, KName.SIGMA, serialized.getString(KName.SIGMA));
        Ut.field(output, KName.LANGUAGE, serialized.getString(KName.LANGUAGE));
        Ut.field(output, KName.ACTIVE, serialized.getBoolean(KName.ACTIVE, Boolean.TRUE));
        final String updatedBy = serialized.getString(KName.UPDATED_BY);
        final LocalDateTime now = LocalDateTime.now();
        if (isCreated) {
            // Create Only
            final String created = serialized.getString(KName.CREATED_BY);
            Ut.field(output, KName.CREATED_AT, now);
            Ut.field(output, KName.CREATED_BY, created);
            Ut.field(output, KName.UPDATED_AT, now);
            Ut.field(output, KName.UPDATED_BY, created);
        } else {
            // Update Only
            Ut.field(output, KName.UPDATED_AT, now);
            Ut.field(output, KName.UPDATED_BY, updatedBy);
        }
    }

    static <T, I> void audit(final I output, final T input, final boolean isCreated, final String pojo) {
        // If contains pojo, must be deserialized for auditor information
        final JsonObject serialized = Ux.toJson(input, pojo);
        audit(output, serialized, isCreated);
    }
}
