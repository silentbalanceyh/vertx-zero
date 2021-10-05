package io.vertx.tp.rbac.logged;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.tp.rbac.authorization.Align;
import io.vertx.tp.rbac.authorization.ScDetent;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.Refer;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.eon.Constants;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.UxPool;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Data in Session for current user
 * Connect to Pool: vertx-web.sessions.habitus for each user session
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ScUser {
    private static final Annal LOGGER = Annal.get(ScUser.class);
    private static final ConcurrentMap<String, ScUser> USERS = new ConcurrentHashMap<>();
    private final transient UxPool pool;
    private final transient String habitus;

    private ScUser(final String habitus) {
        this.habitus = habitus;
        this.pool = Ux.Pool.on(Constants.Pool.HABITUS);
    }

    // ------------------------- Profile Method ------------------------
    /*
     * Initialization fetchProfile roles ( User )
     * 1) UNION
     * 2) EAGER
     * 3) LAZY
     * 4) INTERSECT
     */
    @SuppressWarnings("all")
    private static Future<JsonObject> initRoles(final JsonObject profile, final JsonArray roles) {
        Sc.infoAuth(LOGGER, "Roles : {0}", roles.encode());
        final List futures = new ArrayList<>();
        roles.stream().filter(Objects::nonNull)
            .map(item -> (JsonObject) item)
            .map(ProfileRole::new)
            .map(ProfileRole::initAsync)
            .forEach(futures::add);
        return CompositeFuture.join(futures)
            /* Composite Result */
            .compose(Sc::<ProfileRole>composite)
            /* User Process */
            .compose(ScDetent.user(profile)::procAsync);
    }

    @SuppressWarnings("all")
    private static Future<JsonObject> initGroups(final JsonObject profile, final JsonArray groups) {
        Sc.debugAuth(LOGGER, "Groups: {0}", groups.encode());
        final List futures = new ArrayList();
        groups.stream().filter(Objects::nonNull)
            .map(item -> (JsonObject) item)
            .map(ProfileGroup::new)
            .map(ProfileGroup::initAsync)
            .forEach(futures::add);
        final Refer parentHod = new Refer();
        final Refer childHod = new Refer();
        return CompositeFuture.join(futures).compose(Sc::<ProfileGroup>composite).compose(profiles -> Ux.future(profiles)
            /* Group Direct Mode */
            .compose(Align::flat)
            .compose(ScDetent.group(profile)::procAsync)
            .compose(nil -> Ux.future(profiles))

            /* Group Parent Mode */
            .compose(Align::parent)
            .compose(parentHod::future)
            /** Parent Only */
            .compose(parents -> ScDetent.parent(profile, profiles).procAsync(parents))
            /** Parent and Current */
            .compose(nil -> ScDetent.inherit(profile, profiles).procAsync(parentHod.get()))
            .compose(nil -> Ux.future(profiles))

            /* Group Child Mode */
            .compose(Align::children)
            .compose(childHod::future)
            /** Child Only */
            .compose(children -> ScDetent.children(profile, profiles).procAsync(children))
            /** Child and Current */
            .compose(nil -> ScDetent.extend(profile, profiles).procAsync(childHod.get()))
            .compose(nil -> Ux.future(profiles))
        ).compose(nil -> Ux.future(profile));
    }

    // ------------------------- Initialized Method ------------------------
    /*
     * Create ScUser for current Logged User
     * 1. The key is calculated with `habitus` value
     * 2. The data input contains
     *
     * Memory
     *      "habitus" = ScUser
     *
     * SharedPool
     * 1st Level
     *      "habitus" = {}
     * 2nd Level: {} Content
     * Create relation between session & user
     * {
     *      "user": "X_USER key field, client key/user id here",
     *      "role": [
     *          {
     *              "roleId": "X_ROLE key field",
     *              "priority": 0
     *          }
     *      ],
     *      "group":[
     *          {
     *              "groupId": "X_GROUP key field",
     *              "priority": 0
     *          }
     *      ],
     *      "habitus": "128 bit random string",
     *      "session": "session id that vert.x generated",
     *      "profile": {
     *      },
     *      "view": {
     *      }
     * }
     */
    public static Future<ScUser> login(final JsonObject data) {
        final String habitus = data.getString(KName.HABITUS);
        return Ux.future(Fn.pool(USERS, habitus, () -> new ScUser(habitus))).compose(user -> {
            final JsonObject stored = data.copy();
            stored.remove(KName.HABITUS);
            return user.set(stored);        // Start Async
        }).compose(user -> user.profile()
            // Role Profile initialized
            .compose(profile -> initRoles(profile, data.getJsonArray(KName.ROLE)))
            // Group Profile initialized
            .compose(profile -> initGroups(profile, data.getJsonArray(KName.GROUP)))
            // Stored
            .compose(user::profile)
            // Report
            .compose(user::report)
            // Final result
            .compose(nil -> Ux.future(user))
        );
    }

    public static ScUser login(final String habitus) {
        final ScUser user = USERS.get(habitus);
        Objects.requireNonNull(user);
        return user;
    }

    public static ScUser login(final User user) {
        final JsonObject principle = user.principal();
        final String habitus = principle.getString(KName.HABITUS);
        return login(habitus);
    }

    public static Future<Boolean> logout(final String habitus) {
        final ScUser user = USERS.get(habitus);
        Objects.requireNonNull(user);
        return user.logout();
    }

    // ------------------------- Session Method -----------------------
    public Future<JsonObject> view() {
        return this.<JsonObject>get(KName.VIEW).compose(Ut::ifJNil);
    }

    public Future<JsonObject> view(final String viewKey) {
        return this.view().compose(view -> Ux.future(view.getJsonObject(viewKey)));
    }

    public Future<JsonObject> view(final String viewKey, final JsonObject viewData) {
        return this.view().compose(view -> {
            final JsonObject stored = view.getJsonObject(viewKey, new JsonObject());
            stored.put(Qr.KEY_PROJECTION, viewData.getJsonArray(Qr.KEY_PROJECTION));
            stored.put(Qr.KEY_CRITERIA, viewData.getJsonObject(Qr.KEY_CRITERIA));
            view.put(viewKey, stored);
            return this.set(KName.VIEW, view);
        });
    }

    public Future<JsonObject> profile() {
        return this.<JsonObject>get(KName.PROFILE).compose(Ut::ifJNil);
    }

    public Future<JsonObject> profile(final JsonObject profileData) {
        return this.set(KName.PROFILE, profileData);
    }

    // ------------------------- Private Method ------------------------
    private Future<Boolean> logout() {
        /*
         * Remove reference pool first
         */
        USERS.remove(this.habitus);
        return this.pool.remove(this.habitus).compose(kv ->
            Ux.future(Boolean.TRUE));
    }

    private Future<JsonObject> report(final JsonObject result) {
        Sc.infoAuth(LOGGER, "Permissions: {0}", result.encode());
        return Ux.future(result);
    }

    private Future<ScUser> set(final JsonObject data) {
        return this.getStored().compose(stored -> {
            stored.mergeIn(data, true);
            return this.pool.put(this.habitus, stored)
                .compose(nil -> Ux.future(this));
        });
    }

    private <T> Future<T> set(final String dataKey, final T value) {
        return this.getStored().compose(stored -> {
            // dataKey = value, the T must be valid for JsonObject
            stored.put(dataKey, value);
            return this.pool.put(this.habitus, stored)
                .compose(nil -> Ux.future(value));
        });
    }

    private Future<JsonObject> getStored() {
        return this.pool.<String, JsonObject>get(this.habitus).compose(stored -> {
            // 1st time fetch data often return null here
            if (Ut.isNil(stored)) {
                stored = new JsonObject();
            }
            return Ux.future(stored);
        });
    }

    @SuppressWarnings("unchecked")
    private <T> Future<T> get(final String dataKey) {
        return this.getStored().compose(stored -> {
            if (Ut.isNil(stored)) {
                return Ux.future();
            } else {
                return Ux.future((T) stored.getValue(dataKey));
            }
        });
    }
}
