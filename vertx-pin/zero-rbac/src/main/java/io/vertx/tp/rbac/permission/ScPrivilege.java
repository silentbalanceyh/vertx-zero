package io.vertx.tp.rbac.permission;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.rbac.cv.AuthKey;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

/*
 * Data in Session
 * 1) All the stored data is Buffer
 */
public class ScPrivilege implements Serializable {

    private final transient String habitusId;
    private transient ScHabitus habitus;

    private ScPrivilege(final String habitusId) {
        this.habitusId = habitusId;
    }

    /*
     *  Create new habitus ( First time initialize )
     */
    public static Future<ScPrivilege> init(final JsonObject data) {
        final String habitusId = data.getString(KName.HABITUS);
        return new ScPrivilege(habitusId).open().compose(self -> {
            final ScHabitus habitus = self.habitus;
            /*
             * Stored user
             */
            return habitus.set("user", data.getString("user"))
                /*
                 * Stored role
                 */
                .compose(nil -> habitus.set("role", data.getJsonArray("role")))
                /*
                 * Stored group
                 */
                .compose(nil -> habitus.set("group", data.getJsonArray("group")))
                /*
                 * Return self reference here
                 */
                .compose(nil -> Ux.future(self));
        });
    }

    /*
     *  Open existing habitus ( After initialized )
     */
    public static Future<ScPrivilege> open(final String habitusId) {
        return new ScPrivilege(habitusId).open();
    }

    /*
     * Uniform workflow here.
     */
    private Future<ScPrivilege> open() {
        /*
         * Deny to initialize `ScHabitus` object here.
         */
        habitus = ScHabitus.initialize(habitusId);
        /*
         * Open current `ScPrivilege` as session habitus
         */
        return Future.succeededFuture(this);
    }

    // ----------------
    // Public interface that provide for operations
    // ----------------
    public Future<Boolean> evaluate(final Function<JsonObject, Future<Boolean>> fnDirect) {
        return fetchProfile().compose(profile -> Ut.isNil(profile) ?
            /*
             * Profile does not exist and no cached, in this situation
             * the system should go the whole flow
             */
            fnDirect.apply(profile) :
            /*
             * Profile exist, it's not needed to go the whole flow.
             * Return True directly.
             */
            Future.succeededFuture(Boolean.TRUE));
    }

    /*
     * Profile ( Json ) get / set
     * */
    private Future<JsonObject> fetchProfile() {
        return habitus.<JsonObject>get("profile")
            /*
             * JsonObject safe calling here
             */
            .compose(Ke.Result::jsonAsync);
    }

    public Future<JsonObject> storeProfile(final JsonObject profiles) {
        return habitus.set("profile", profiles);
    }

    /*
     * Permission get
     */
    public Future<JsonArray> fetchPermissions(final String profileKey) {
        return fetchProfile()
            .compose(extractAsync(profileKey, AuthKey.PROFILE_PERM));
    }

    /*
     * Role get
     */
    public Future<JsonArray> fetchRoles(final String profileKey) {
        return fetchProfile()
            .compose(extractAsync(profileKey, AuthKey.PROFILE_ROLE));
    }

    /*
     * Authorized get
     */
    public Future<Boolean> fetchAuthorized(final String authorizeKey) {
        return habitus.<Boolean>get(authorizeKey)
            .compose(result -> Objects.isNull(result)
                ? Future.succeededFuture(Boolean.FALSE)
                : Future.succeededFuture(result)
            );
    }


    public Future<JsonObject> storedBound(final String key, final JsonObject data) {
        return habitus.set(key, data);
    }

    public Future<Boolean> storedAuthorized(final String key, final Boolean result) {
        return habitus.set(key, result);
    }

    public Future<Boolean> clear() {
        return habitus.clear();
    }

    private Function<JsonObject, Future<JsonArray>> extractAsync(
        final String profileKey, final String hitKey) {
        return profile -> {
            JsonObject single = profile.getJsonObject(profileKey);
            if (Ut.isNil(single)) {
                single = new JsonObject();
            }
            JsonArray value = single.getJsonArray(hitKey);
            if (Objects.isNull(value)) {
                value = new JsonArray();
            }
            return Ux.future(value);
        };
    }
}
