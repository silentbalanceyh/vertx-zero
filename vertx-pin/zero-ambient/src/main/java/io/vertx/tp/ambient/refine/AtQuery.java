package io.vertx.tp.ambient.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.util.Ut;

class AtQuery {

    static JsonObject filters(final String appId, final JsonArray types, final String code) {
        final JsonObject filters = new JsonObject();
        filters.put(KName.APP_ID, appId);
        filters(filters, types, code);
        return filters.put(Strings.EMPTY, Boolean.TRUE);
    }

    static JsonObject filtersSigma(final String sigma, final JsonArray types, final String code) {
        final JsonObject filters = new JsonObject();
        filters.put(KName.SIGMA, sigma);
        filters(filters, types, code);
        return filters.put(Strings.EMPTY, Boolean.TRUE);
    }

    private static void filters(final JsonObject filters, final JsonArray types, final String code) {
        filters.put(KName.ACTIVE, Boolean.TRUE);
        if (Values.ONE == types.size()) {
            final String firstArg = types.getString(Values.IDX);
            filters.put(KName.TYPE, firstArg);
            /* Conflict to multi types */
            if (Ut.notNil(code)) {
                filters.put(KName.CODE, code);
            }
        } else {
            filters.put(KName.TYPE + ",i", types);
        }
    }
}
