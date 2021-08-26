package cn.vertxup.rbac.service.accredit;

import cn.vertxup.rbac.domain.tables.pojos.SAction;
import cn.vertxup.rbac.domain.tables.pojos.SResource;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error.*;
import io.vertx.tp.rbac.atom.ScRequest;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.commune.secure.DataBound;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.log.Annal;

import java.util.Objects;

/*
 * Tool for authorization workflow
 */
class AccreditFlow {

    private static final Annal LOGGER = Annal.get(AccreditFlow.class);

    /*
     * 1. Whether action is existing
     * If action missing, throw 403 exception
     */
    static Future<SAction> inspectAction(
        final Class<?> clazz, final SAction action, final ScRequest request) {
        if (Objects.isNull(action)) {
            final String requestUri = request.getMethod() + " " + request.getNormalizedUri();
            final WebException error = new _403ActionMissingException(clazz, requestUri);
            return Future.failedFuture(error);
        } else {
            Sc.debugCredit(LOGGER, AuthMsg.CREDIT_ACTION,
                request.getRequestUri(), request.getMethod(), request.getNormalizedUri());
            return Future.succeededFuture(action);
        }
    }

    /*
     * 2. Whether resource is existing
     * If resource missing, throw 404 exception
     */
    static Future<SResource> inspectResource(
        final Class<?> clazz, final SResource resource, final ScRequest request, final SAction action) {
        if (Objects.isNull(resource)) {
            final String requestUri = request.getMethod() + " " + request.getNormalizedUri();
            final WebException error = new _404ResourceMissingException(clazz, action.getResourceId(), requestUri);
            return Future.failedFuture(error);
        } else {
            Sc.debugCredit(LOGGER, AuthMsg.CREDIT_RESOURCE, resource.getKey());
            return Future.succeededFuture(resource);
        }
    }

    /*
     * 3. Action Level / Resource Level comparing.
     */
    static Future<SResource> inspectLevel(
        final Class<?> clazz, final SResource resource, final SAction action
    ) {
        final Integer required = resource.getLevel();
        final Integer actual = action.getLevel();
        if (actual < required) {
            final WebException error = new _403ActionDinnedException(clazz, required, actual);
            return Future.failedFuture(error);
        } else {
            Sc.debugCredit(LOGGER, AuthMsg.CREDIT_LEVEL, action.getLevel(), resource.getLevel());
            return Future.succeededFuture(resource);
        }
    }

    /*
     * 4. Extract fetchProfile information from Cached
     */
    static Future<JsonArray> inspectPermission(
        final Class<?> clazz, final SResource resource, final ScRequest request
    ) {
        final String profileKey = Sc.generateProfileKey(resource);
        return request.openSession()
            /* Get Permission from Privilege */
            .compose(privilege -> privilege.fetchPermissions(profileKey))
            .compose(permissions -> {
                /* Profile Key */
                if (Objects.isNull(permissions) || permissions.isEmpty()) {
                    final WebException error = new _403NoPermissionException(clazz, request.getUser(), profileKey);
                    return Future.failedFuture(error);
                } else {
                    Sc.debugCredit(LOGGER, AuthMsg.CREDIT_PERMISSION, profileKey);
                    return Future.succeededFuture(permissions);
                }
            });
    }

    /*
     * 5. Permission / Action comparing
     */
    static Future<Boolean> inspectAuthorized(
        final Class<?> clazz, final SAction action, final JsonArray permission
    ) {
        final String permissionId = action.getPermissionId();
        if (Objects.nonNull(permissionId)) {
            if (permission.contains(permissionId)) {
                Sc.debugCredit(LOGGER, AuthMsg.CREDIT_AUTHORIZED, permissionId);
                return Future.succeededFuture(Boolean.TRUE);
            } else {
                final WebException error = new _403PermissionLimitException(clazz, action.getCode(), permissionId);
                return Future.failedFuture(error);
            }
        } else {
            final WebException error = new _500InternalServerException(clazz, "Permission Id Null");
            return Future.failedFuture(error);
        }
    }

    /*
     * 6. Final refresh session data for request
     */
    static Future<JsonObject> inspectBound(final DataBound bound, final ScRequest request) {
        /* Stored bound data into current session */
        final JsonObject data = bound.toJson();
        final String cacheKey = request.getCacheKey();
        Sc.debugCredit(LOGGER, AuthMsg.CREDIT_BOUND, data.encode(), cacheKey);
        return request.openSession()
            .compose(privilege -> privilege.storedBound(cacheKey, data));
    }

    /*
     * 7. Last step for stored current uri information into cache.
     */
    static Future<Boolean> inspectAuthorized(final ScRequest request) {
        /* Stored authorized */
        final String authorizedKey = request.getAuthorizedKey();
        Sc.debugCredit(LOGGER, AuthMsg.CREDIT_SUCCESS, authorizedKey, request.getSessionId());
        return request.openSession()
            .compose(privilege -> privilege.storedAuthorized(authorizedKey, Boolean.TRUE));
    }
}
