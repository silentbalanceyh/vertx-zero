package io.vertx.mod.ke.refine;

import io.horizon.atom.common.Refer;
import io.horizon.spi.business.ExUser;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;

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
        return umUserInternal(input, config)
            .compose(user -> {
                replaced.put(KName.USER, user);
                return Ux.futureT();
            })
            .compose(nil -> {
                output.put(KName.__.USER, replaced);
                return Ux.future(output);
            });
    }

    // =============================== User Extract =============================
    private static Future<JsonObject> umUserInternal(final JsonObject input, final JsonObject config) {
        final JsonObject dataO = Ut.aiDataO(input);
        final JsonObject dataN = Ut.aiDataN(input);
        final Refer userN = new Refer();
        final JsonArray users = Ut.valueJArray(config, KName.USER);
        return umUser(dataN, users)
            .compose(userN::future)
            .compose(nil -> umUser(dataO, users))
            .compose(userO -> {
                JsonObject userJ = userN.get();
                userJ = userJ.copy();
                userJ.put(KName.__.DATA, userO);
                return Ux.future(userJ);
            });
    }

    private static Future<JsonObject> umUser(final JsonObject input, final JsonArray users) {
        final Set<String> keySet = new HashSet<>();
        Ut.itJArray(users, String.class, (field, index) -> {
            final Object value = input.getValue(field);
            if (value instanceof JsonArray) {
                keySet.addAll(Ut.toSet((JsonArray) value));
            } else if (value instanceof String) {
                keySet.add((String) value);
            }
        });
        return Ux.channel(ExUser.class, () -> input, stub -> stub.mapUser(keySet, true).compose(userMap -> {
            final JsonObject normalized = new JsonObject();
            Ut.itJArray(users, String.class, (field, index) -> {
                final Object value = input.getValue(field);
                if (value instanceof JsonArray) {
                    final Set<String> vSet = Ut.toSet((JsonArray) value);
                    final JsonArray userA = new JsonArray();
                    vSet.forEach(userKey -> {
                        final JsonObject userJ = userMap.getOrDefault(userKey, null);
                        if (Ut.isNotNil(userJ)) {
                            userA.add(userJ);
                        }
                    });
                    normalized.put(field, userA);                       // Replace
                } else if (value instanceof String) {                   // Replace
                    final String userKey = (String) value;
                    final JsonObject userJ = userMap.getOrDefault(userKey, null);
                    if (Ut.isNotNil(userJ)) {
                        normalized.put(field, userJ);
                    } else {
                        normalized.put(field, new JsonObject());        // Replace
                    }
                } else {
                    normalized.put(field, new JsonObject());            // Empty Replace
                }
            });
            return Ux.future(normalized);
        }));
    }
}
