package cn.vertxup.rbac.service.accredit;

import cn.vertxup.rbac.domain.tables.pojos.SAction;
import cn.vertxup.rbac.domain.tables.pojos.SResource;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.tp.error._403ActionDinnedException;
import io.vertx.tp.error._404ActionMissingException;
import io.vertx.tp.error._404ResourceMissingException;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.logged.ScResource;
import io.vertx.tp.rbac.logged.ScUser;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.Refer;
import io.vertx.up.eon.KName;
import io.vertx.up.exception.WebException;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Objects;

public class AccreditService implements AccreditStub {
    private final static Annal LOGGER = Annal.get(AccreditService.class);

    @Inject
    private transient ActionStub stub;

    /*
     * Permission
     * {
     *      "profile1": [],
     *      "profile2": []
     * }
     */
    @Override
    public Future<JsonObject> profile(final User user) {
        final ScUser scUser = ScUser.login(user);
        return scUser.profile();
    }

    @Override
    public Future<JsonObject> resource(final JsonObject requestData) {
        final ScResource request = ScResource.create(requestData);
        return request.resource().compose(data -> {
            if (Ut.isNil(data)) {
                /* Fetch Action */
                final Refer actionHod = new Refer();
                // Action Checking
                return this.fetchAction(request)
                    .compose(action -> this.inspectAction(request, action))
                    // Resource Checking
                    .compose(action -> this.stub.fetchResource(action.getResourceId()))
                    .compose(actionHod::future)
                    .compose(resource -> this.inspectResource(request, actionHod.get(), resource))
                    // Level Checking
                    .compose(resource -> this.inspectLevel(resource, actionHod.get()))
                    // Resource Data Processing
                    .compose(resource -> this.inspectData(resource, actionHod.get()))
                    .compose(request::resource)
                    // Extract data: profileKey = permissionIds ( [] )
                    .compose(stored -> Ux.future(stored.getJsonObject(KName.DATA)));
            } else {
                /* Resource Processing */
                return Ux.future(data);
            }
        });
    }

    /*
     * {
     *      "key": "profileKey",
     *      "data": {
     *          "profileKey": ["p1", "p2"]
     *      },
     *      "record": {
     *          SResource Data Structure ( Json )
     *      }
     * }
     */
    private Future<JsonObject> inspectData(final SResource resource, final SAction action) {
        // profile
        final String profileKey = this.profileKey(resource);
        final JsonArray permissions = new JsonArray().add(action.getPermissionId());
        // resource data
        final JsonObject stored = new JsonObject();
        stored.put(KName.RECORD, Ut.serializeJson(resource));
        stored.put(KName.KEY, profileKey);
        stored.put(KName.DATA, new JsonObject().put(profileKey, permissions));

        return Ux.future(stored);
    }

    private Future<SResource> inspectLevel(final SResource resource, final SAction action) {
        final Integer required = resource.getLevel();
        final Integer actual = action.getLevel();
        if (actual < required) {
            final WebException error = new _403ActionDinnedException(this.getClass(), required, actual);
            return Future.failedFuture(error);
        } else {
            Sc.debugCredit(LOGGER, AuthMsg.CREDIT_LEVEL, action.getLevel(), resource.getLevel());
            return Future.succeededFuture(resource);
        }
    }

    /*
     * 1. Whether action is existing
     * If action missing, throw 404 exception
     */
    private Future<SAction> inspectAction(final ScResource request, final SAction action) {
        if (Objects.isNull(action)) {
            final String requestUri = request.method() + " " + request.uri();
            final WebException error = new _404ActionMissingException(this.getClass(), requestUri);
            return Future.failedFuture(error);
        } else {
            Sc.debugCredit(LOGGER, AuthMsg.CREDIT_ACTION, request.uriRequest(), request.method(), request.uri());
            return Future.succeededFuture(action);
        }
    }

    /*
     * 2. Whether resource is existing
     * If resource missing, throw 404 exception
     */
    private Future<SResource> inspectResource(final ScResource request, final SAction action, final SResource resource) {
        if (Objects.isNull(resource)) {
            final String requestUri = request.method() + " " + request.uri();
            final WebException error = new _404ResourceMissingException(this.getClass(), action.getResourceId(), requestUri);
            return Future.failedFuture(error);
        } else {
            Sc.debugCredit(LOGGER, AuthMsg.CREDIT_RESOURCE, resource.getKey());
            return Future.succeededFuture(resource);
        }
    }

    private Future<SAction> fetchAction(final ScResource resource) {
        return this.stub.fetchAction(
            resource.uri(),                         // Normalized Uri
            resource.method(),
            resource.sigma()
        ).compose(action -> {
            if (Objects.nonNull(action)) {
                /* Action Found */
                return Ux.future(action);
            }
            /* Check Normalized, action = null */
            if (resource.isNormalized()) {
                return this.stub.fetchAction(
                    resource.uriRequest(),          // Request Uri
                    resource.method(),
                    resource.sigma()
                );
            }
            return Ux.future();
        });
    }

    private String profileKey(final SResource resource) {
        /*
         * Get Role/Group/Tree modes
         */
        final String modeRole = resource.getModeRole();
        final String modeGroup = resource.getModeGroup();
        if (Ut.isNil(modeGroup)) {
            /*
             * User Mode
             *
             * USER_UNION
             * USER_INTERSECT
             * USER_EAGER
             * USER_LAZY
             */
            return "USER_" + modeRole.toUpperCase(Locale.getDefault());
        } else {
            final String modeTree = resource.getModeTree();
            final String group = modeGroup.toUpperCase(Locale.getDefault()) +
                "_" + modeRole.toUpperCase(Locale.getDefault());
            if (Ut.isNil(modeTree)) {
                /*
                 * Group Mode
                 * HORIZON_XXX
                 * CRITICAL_XXX
                 * OVERLOOK_XXX
                 */
                return group;
            } else {
                /*
                 * Inherit / Child / Parent/ Extend
                 * EXTEND_XXX
                 * PARENT_XXX
                 * CHILD_XXX
                 * INHERIT_XXX
                 */
                return modeTree.toUpperCase(Locale.getDefault()) +
                    "_" + group;
            }
        }
    }
}
