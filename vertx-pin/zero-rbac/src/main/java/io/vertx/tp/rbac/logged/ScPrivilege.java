package io.vertx.tp.rbac.logged;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.AuthKey;
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
     *  Open existing habitus ( After initialized )
     */
    public static ScPrivilege open(final String habitusId) {
        return new ScPrivilege(habitusId).open();
    }

    /*
     * Uniform workflow here.
     */
    private ScPrivilege open() {
        /*
         * Deny to initialize `ScHabitus` object here.
         */
        this.habitus = ScHabitus.initialize(this.habitusId);
        /*
         * Open current `ScPrivilege` as session habitus
         */
        return this;
    }

    /*
     * Profile ( Json ) get / set
     * */
    private Future<JsonObject> fetchProfile() {
        return this.habitus.<JsonObject>get("profile")
            /*
             * JsonObject safe calling here
             */
            .compose(Ut::ifJNil);
    }

    /*
     * Permission get
     */
    public Future<JsonArray> fetchPermissions(final String profileKey) {
        return this.fetchProfile()
            .compose(this.extractAsync(profileKey, AuthKey.PROFILE_PERM));
    }

    /*
     * Role get
     */
    public Future<JsonArray> fetchRoles(final String profileKey) {
        return this.fetchProfile()
            .compose(this.extractAsync(profileKey, AuthKey.PROFILE_ROLE));
    }

    /*
     * Authorized get
     */
    public Future<Boolean> fetchAuthorized(final String authorizeKey) {
        return this.habitus.<Boolean>get(authorizeKey)
            .compose(result -> Objects.isNull(result)
                ? Future.succeededFuture(Boolean.FALSE)
                : Future.succeededFuture(result)
            );
    }


    public Future<JsonObject> storedBound(final String key, final JsonObject data) {
        return this.habitus.set(key, data);
    }

    public Future<Boolean> storedAuthorized(final String key, final Boolean result) {
        return this.habitus.set(key, result);
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
