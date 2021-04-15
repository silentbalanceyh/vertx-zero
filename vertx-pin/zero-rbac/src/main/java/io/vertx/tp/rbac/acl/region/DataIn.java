package io.vertx.tp.rbac.acl.region;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.secure.Acl;
import io.vertx.up.eon.em.AclPhase;

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
        JsonArray projection = matrix.getJsonArray(Qr.KEY_PROJECTION);
        /*
         * Execute when phase = EAGER
         */
        final Acl acl = envelop.acl();
        if (Objects.nonNull(acl) && AclPhase.EAGER == acl.phase()) {
            /*
             * original + acl projection
             */
            projection = Sc.aclOn(projection, acl);
        }
        if (Objects.nonNull(projection) && !projection.isEmpty()) {
            /*
             * apply projection based on view
             */
            envelop.onProjection(projection);
        }
    }

    /*
     * Before criteria is not ok when
     * EAGER phase ( IGNORED )
     */
    static void visitCriteria(final Envelop envelop, final JsonObject matrix) {
        /* Criteria Modification */
        final JsonObject criteria = matrix.getJsonObject(Qr.KEY_CRITERIA);
        /* ACL do not control criteria */
        if (Objects.nonNull(criteria) && !criteria.isEmpty()) {
            envelop.onCriteria(criteria);
        }
    }

}
