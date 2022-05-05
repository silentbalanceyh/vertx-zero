package io.vertx.tp.ke.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class KeUser {
    /*
     * Extract data for `__user`, it could not be core-framework, but for extension only
     * because it contains following tables:
     * - S_USER,
     * - S_GROUP,
     * - S_ROLE,
     * - E_TEAM,
     * - E_DEPT,
     * - E_EMPLOYEE
     *
     * The input Json is as following:
     *
     * {
     *     "user": [
     *     ],
     *     "role": [
     *     ],
     *     "group": [
     *     ],
     *     "dept": [
     *     ],
     *     "team": [
     *     ]
     * }
     *
     * To:
     * {
     *     "user": {},
     *     "role": [],
     *     "group": [],
     *     "dept": {},
     *     "team": {},
     *     "__data": {
     *         "user": {},
     *         "role": [],
     *         "group": [],
     *         "dept": {}
     *         "team": {}
     *     }
     * }
     */
    static Future<JsonObject> umUser(final JsonObject input, final JsonObject config) {
        /*
         * Replace the `__user` node for script usage etc.
         */
        final JsonObject output = Ut.valueJObject(input).copy();
        final JsonObject replaced = new JsonObject();
        final JsonObject data0 = Ut.aiDataO(input);
        final JsonObject dataN = Ut.aiDataN(input);
        return Ux.future(output);
    }

    private static Future<JsonObject> umUser(final JsonObject input, final JsonArray users) {
        final ConcurrentMap<String, String> paramMap = new ConcurrentHashMap<>();
        Ut.itJArray(users, String.class, (field, index) -> {
            final Object value = input.getValue(field);
        });
        return null;
    }
}
