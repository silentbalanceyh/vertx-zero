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
import io.vertx.tp.rbac.atom.ScConfig;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.init.ScPin;
import io.vertx.tp.rbac.logged.ScResource;
import io.vertx.tp.rbac.logged.ScUser;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.Refer;
import io.vertx.up.eon.KName;
import io.vertx.up.exception.WebException;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.Objects;

public class AccreditService implements AccreditStub {
    private final static Annal LOGGER = Annal.get(AccreditService.class);
    private final static ScConfig CONFIG = ScPin.getConfig();

    @Inject
    private transient ActionStub stub;
    @Inject
    private transient MatrixStub matrixStub;

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
        return scUser.permissions();
    }

    @Override
    public Future<JsonObject> resource(final JsonObject requestData) {
        final ScResource request = ScResource.create(requestData);
        // First Phase
        return Rapid.<String, JsonObject>t(CONFIG.getResourcePool()).cached(request.key(), () -> {
            /* Fetch Action */
            final Refer actionHod = new Refer();
            // Action Checking
            return this.fetchAction(request)
                .compose(action -> this.inspectAction(request, action))
                .compose(actionHod::future)
                // Resource Checking
                .compose(action -> this.stub.fetchResource(action.getResourceId()))
                .compose(resource -> this.inspectResource(request, actionHod.get(), resource))
                // Level Checking
                .compose(resource -> this.inspectLevel(resource, actionHod.get()))
                // Resource Data Processing
                .compose(resource -> this.inspectData(resource, actionHod.get()));
        }).compose(stored -> this.inspectView(requestData, request, stored)
            // Extract `data` node
        ).compose(stored -> Ux.future(stored.getJsonObject(KName.DATA)));
    }

    private Future<JsonObject> inspectView(final JsonObject requestData, final ScResource resource,
                                           final JsonObject response) {
        final String habitus = requestData.getString(KName.HABITUS);
        final String keyView = resource.keyView();
        final ScUser user = ScUser.login(habitus);
        if (Objects.isNull(user)) {
            return Future.succeededFuture(new JsonObject());
        }
        return user.view(keyView).compose(viewData -> {
            if (Objects.isNull(viewData)) {
                return this.matrixStub.fetchBound(user, resource)
                    .compose(bound -> user.view(keyView, bound.toJson()))
                    .compose(nil -> Ux.future(response));
            } else {
                return Ux.future(response);
            }
        });
    }

    /*
     * {
     *      "key": "profileKey",
     *      "data": {
     *          "profileKey": {
     *
     *          }
     *      },
     *      "record": {
     *          SResource Data Structure ( Json )
     *      }
     * }
     */
    private Future<JsonObject> inspectData(final SResource resource, final SAction action) {
        // profile
        final String profileKey = Sc.valueProfile(resource);
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
}
