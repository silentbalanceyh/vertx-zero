package io.vertx.tp.rbac.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.up.commune.secure.Acl;
import io.vertx.up.eon.em.AclPhase;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class ScAcl {

    static JsonArray aclProjection(final JsonArray original, final Acl acl) {
        final JsonArray projection = Ut.sureJArray(original);
        if (Objects.isNull(acl)) {
            /*
             * No acl, default projection defined in S_VIEW
             */
            return projection;
        } else {
            /*
             * Delay for acl control in phase
             */
            if (AclPhase.EAGER == acl.phase()) {
                final Set<String> aclProjection = acl.projection();
                if (aclProjection.isEmpty()) {
                    /*
                     * acl is empty, it means no definition found in our system
                     * If the acl will be modified / configured, it must contains some default fields
                     * such as:
                     * - active
                     * - key
                     * - language
                     * - sigma
                     */
                    return projection;
                } else {
                    /*
                     * Mix calculation
                     * 1) Acl Projection is major control
                     * 2) Matrix Projection is the secondary here, it means that all aclProjection must contains
                     */
                    final Set<String> replaced = new HashSet<>(aclProjection);
                    Ut.itJArray(projection, String.class, (field, index) -> {
                        if (aclProjection.contains(field)) {
                            replaced.add(field);
                        }
                    });
                    return Ut.toJArray(replaced);
                }
            } else {
                /*
                 * Delay and pass acl only for record
                 * Here the situation is form control
                 */
                return projection;
            }
        }
    }
}
