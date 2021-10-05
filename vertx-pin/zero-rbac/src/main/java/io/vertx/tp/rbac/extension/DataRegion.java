package io.vertx.tp.rbac.extension;

import cn.vertxup.rbac.service.accredit.MatrixStub;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.rbac.acl.region.CommonCosmo;
import io.vertx.tp.rbac.acl.region.Cosmo;
import io.vertx.tp.rbac.acl.region.SeekCosmo;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.logged.ScUser;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.extension.region.AbstractRegion;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Extension in RBAC module
 * 1) Region calculation
 * 2) Visitant calculation ( Extension More )
 */
public class DataRegion extends AbstractRegion {
    private static final ConcurrentMap<String, Cosmo> POOL_COMMON =
        new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Cosmo> POOL_SEEK =
        new ConcurrentHashMap<>();
    @Inject
    private transient MatrixStub stub;

    @Override
    public Future<Envelop> before(final RoutingContext context, final Envelop envelop) {
        if (this.isEnabled(context)) {
            /* Get Critical parameters */
            return this.cacheView(context, envelop).compose(matrix -> {
                if (this.hasValue(matrix)) {
                    Sc.infoAuth(this.getLogger(), AuthMsg.REGION_BEFORE,
                        context.request().path(), matrix.encode());
                    /*
                     * Select cosmo by matrix
                     */
                    final HttpMethod method = envelop.method();
                    if (HttpMethod.POST == method || HttpMethod.GET == method) {
                        final Cosmo cosmo = this.cosmo(matrix);
                        return cosmo.before(envelop, matrix);
                    } else {
                        /*
                         * DELETE / PUT has no before
                         */
                        return Ux.future(envelop);
                    }
                } else {
                    /*
                     * Matrix null or empty
                     */
                    return Ux.future(envelop);
                }
            });
        } else {
            // Data Region disabled
            return Ux.future(envelop);
        }
    }

    @Override
    public Future<Envelop> after(final RoutingContext context, final Envelop response) {
        if (this.isEnabled(context)) {
            /* Get Critical parameters */
            return this.cacheView(context, response).compose(matrix -> {
                if (this.hasValue(matrix)) {
                    Sc.infoAuth(this.getLogger(), AuthMsg.REGION_AFTER, matrix.encode());
                    /*
                     * Select cosmo by matrix
                     */
                    final Cosmo cosmo = this.cosmo(matrix);
                    return cosmo.after(response, matrix);
                } else {
                    /*
                     * Matrix null or empty
                     */
                    return Ux.future(response);
                }
            });
        } else {
            // Data Region disabled
            return Ux.future(response);
        }
    }

    private Future<JsonObject> cacheView(final RoutingContext context, final Envelop envelop) {
        final String habitus = envelop.token(KName.HABITUS);
        if (Ut.isNil(habitus)) {
            /*
             * Empty bound in current interface instead of other
             */
            return Ux.future(new JsonObject());
        } else {
            final String viewKey = Ke.keyView(context);
            final ScUser scUser = ScUser.login(habitus);
            return scUser.view(viewKey);
        }
    }

    private boolean hasValue(final JsonObject matrix) {
        if (Objects.isNull(matrix)) {
            return false;
        }
        boolean hasValue = Ut.notNil(matrix.getJsonArray(Qr.KEY_PROJECTION, new JsonArray()));
        if (hasValue) {
            return true;
        }
        hasValue = Ut.notNil(matrix.getJsonArray("credit", new JsonArray()));
        if (hasValue) {
            return true;
        }
        hasValue = Ut.notNil(matrix.getJsonObject("rows", new JsonObject()));
        if (hasValue) {
            return true;
        }
        return Ut.notNil(matrix.getJsonObject(Qr.KEY_CRITERIA, new JsonObject()));
    }

    private Cosmo cosmo(final JsonObject matrix) {
        /* Build DataCosmo */
        if (matrix.containsKey("seeker")) {
            /*
             * Virtual resource region calculation
             */
            return Fn.poolThread(POOL_SEEK, SeekCosmo::new);
        } else {
            /*
             * Actual resource region calculation
             */
            return Fn.poolThread(POOL_COMMON, CommonCosmo::new);
        }
    }
}
