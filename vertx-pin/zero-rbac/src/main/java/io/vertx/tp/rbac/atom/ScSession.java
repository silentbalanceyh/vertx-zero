package io.vertx.tp.rbac.atom;


import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.authorization.Align;
import io.vertx.tp.rbac.authorization.ScDetent;
import io.vertx.tp.rbac.permission.ScPrivilege;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.Refer;
import io.vertx.up.atom.unity.Uson;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
 * Profile information to normalize all permission data
 * 1) After logged into the system , this class stored token information into AGHAS
 * 2) Get the json data and singleton fetchProfile information
 * {
 *     "user": "user id",
 *     "role": [
 *          {
 *              "roleId": "role id",
 *              "priority": "user role relation"
 *          },
 *          ...
 *     ],
 *     "group":[
 *          {
 *              "groupId": "group id",
 *              "priority": "user group relation",
 *              "role":[
 *                  {
 *                      "roleId": "role id",
 *                      "priority": "group role relation"
 *                  },
 *                  ...
 *              ]
 *          },
 *          ...
 *     ]
 * }
 * 3) Normalize the data to calculated permission pool
 *
 * The result should be:
 * 1) User Single Profile
 * 2) Group Multi Profile
 * 3) The final result should be role / permission
 *
 * Calculate different fetchProfile type
 */
public class ScSession {
    private static final Annal LOGGER = Annal.get(ScSession.class);

    /*
     * Entry of authorization session.
     */
    public static Future<Boolean> initAuthorization(final JsonObject data) {
        /*
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
         *      "session": "session id that vert.x generated"
         * }
         **/
        return ScPrivilege.init(data).compose(reference ->
            /*
             * ScPrivilege evaluation for current logged user
             */
            reference.evaluate(profile -> ScSession.initRoles(profile, data.getJsonArray("role"))
                /* Initialize group information */
                .compose(processed -> ScSession.initGroups(processed, data.getJsonArray("group")))
                /* Refresh Cache */
                .compose(reference::storeProfile)
                /* Result Report */
                .compose(result -> Uson.create(data).append("profile", profile).toFuture())
                .compose(ScSession::onReport)
                .compose(nil -> Ux.future(Boolean.TRUE))));
    }

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

    private static Future<JsonObject> onReport(final JsonObject result) {
        /*
         * To avoid log more, here must use `encode` instead of `encodePrettily`
         */
        Sc.infoAuth(ScSession.LOGGER, "Permissions: {0}", result.encode());
        return Future.succeededFuture(result);
    }
}
