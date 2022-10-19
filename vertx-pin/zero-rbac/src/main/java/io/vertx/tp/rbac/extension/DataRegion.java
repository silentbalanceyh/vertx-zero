package io.vertx.tp.rbac.extension;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.tp.rbac.acl.region.CommonCosmo;
import io.vertx.tp.rbac.acl.region.Cosmo;
import io.vertx.tp.rbac.acl.region.SeekCosmo;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.extension.AbstractRegion;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.unity.Ux;

/*
 * Extension in RBAC module
 * 1) Region calculation
 * 2) Visitant calculation ( Extension More )
 */
public class DataRegion extends AbstractRegion {
    private static final Cc<String, Cosmo> CC_COSMO = Cc.openThread();

    @Override
    public Future<Envelop> before(final RoutingContext context, final Envelop envelop) {
        if (!this.isEnabled(context)) {
            // Data Region disabled
            return Ux.future(envelop);
        }

        /* Get Critical parameters */
        return Sc.cacheView(context, envelop.habitus()).compose(matrix -> {
            if (this.isRegion(matrix)) {
                Sc.infoAuth(this.getLogger(), AuthMsg.REGION_BEFORE,
                    context.request().path(), matrix.encode());
                /*
                 * Select cosmo by matrix
                 */
                final Cosmo cosmo = this.cosmo(matrix);
                return cosmo.before(envelop, matrix);
            } else {
                /*
                 * Matrix null or empty
                 */
                return Ux.future(envelop);
            }
        }).otherwise(Ux.otherwise(envelop));
    }

    @Override
    public Future<Envelop> after(final RoutingContext context, final Envelop response) {
        if (!this.isEnabled(context)) {
            // Data Region disabled
            return Ux.future(response);
        }

        /* Get Critical parameters */
        return Sc.cacheView(context, response.habitus()).compose(matrix -> {
            if (this.isRegion(matrix)) {
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
        }).otherwise(Ux.otherwise(response));
    }

    private Cosmo cosmo(final JsonObject matrix) {
        /* Build DataCosmo */
        if (matrix.containsKey(KName.SEEKER)) {
            /*
             * Virtual resource region calculation
             */
            return CC_COSMO.pick(SeekCosmo::new, SeekCosmo.class.getName());
        } else {
            /*
             * Actual resource region calculation
             */
            return CC_COSMO.pick(CommonCosmo::new, CommonCosmo.class.getName());
        }
    }
}
