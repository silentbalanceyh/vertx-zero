package io.vertx.mod.rbac.acl.region;

import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.refine.Sc;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.em.EmSecure;
import io.vertx.up.specification.secure.Acl;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * In processing for analyzing `syntax` for calculation
 */
class DataIn {
    /*
     * Before projection could be impacted by ACL `aclVisible`
     * It means that you must get data based on ACL property
     */
    static void visitProjection(final Envelop envelop, final JsonObject matrix) {
        JsonArray projection = matrix.getJsonArray(Ir.KEY_PROJECTION);
        /*
         * Execute when phase = EAGER
         */
        final Acl acl = envelop.acl();
        if (Objects.nonNull(acl) && EmSecure.ActPhase.EAGER == acl.phase()) {
            /*
             * original + acl projection
             */
            projection = Sc.aclOn(projection, acl);
        }
        if (Objects.nonNull(projection) && !projection.isEmpty()) {
            /*
             * apply projection based on view
             */
            envelop.onV(projection);
        }
    }

    /*
     * Before criteria is not ok when
     * EAGER phase ( IGNORED )
     */
    static void visitCriteria(final Envelop envelop, final JsonObject matrix) {
        /* Criteria Modification */
        final JsonObject criteria = matrix.getJsonObject(Ir.KEY_CRITERIA);
        /* ACL do not control criteria */
        if (Objects.nonNull(criteria) && !criteria.isEmpty()) {
            envelop.onH(criteria);
        }
    }

}
