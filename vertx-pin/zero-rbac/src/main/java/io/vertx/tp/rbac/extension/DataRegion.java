package io.vertx.tp.rbac.extension;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.tp.rbac.acl.region.CommonCosmo;
import io.vertx.tp.rbac.acl.region.Cosmo;
import io.vertx.tp.rbac.acl.region.SeekCosmo;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.commune.Envelop;
import io.vertx.up.extension.region.AbstractRegion;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

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

    @Override
    public Future<Envelop> before(final RoutingContext context, final Envelop envelop) {
        if (this.isEnabled(context)) {
            /* Get Critical parameters */
            return Sc.cacheBound(context, envelop).compose(matrix -> {
                if (Objects.nonNull(matrix)) {
                    Sc.infoAuth(this.getLogger(), context.request().path(),
                            AuthMsg.REGION_BEFORE, matrix.encode());
                    /*
                     * Select cosmo by matrix
                     */

                    final HttpMethod method = envelop.getMethod();
                    if (HttpMethod.POST == method || HttpMethod.PUT == method) {
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
            return Sc.cacheBound(context, response).compose(matrix -> {
                if (Objects.nonNull(matrix)) {
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
