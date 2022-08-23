package io.vertx.tp.rbac.acl.relation;

import cn.vertxup.rbac.domain.tables.daos.RUserRoleDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.ke.secure.Tie;
import io.vertx.tp.rbac.cv.AuthKey;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TieRole implements Tie<String, JsonArray> {
    @Override
    public Future<JsonArray> identAsync(final String userKey) {
        // Fetch related role
        Sc.infoAuth(this.getClass(), AuthMsg.RELATION_USER_ROLE, userKey);
        return Ke.umALink(AuthKey.F_USER_ID, userKey, RUserRoleDao.class);
    }

    /*
     * updatedJ
     * {
     *     "...",
     *     "roles": []
     * }
     */
    @Override
    public Future<JsonArray> identAsync(final String userKey, final JsonObject updatedJ) {
        // Update Related Roles
        final JsonObject userJ = Ut.valueJObject(updatedJ, true);
        userJ.remove(KName.ROLES);
        final JsonArray roles = Ut.valueJArray(updatedJ, KName.ROLES);
        if (Ut.isNil(roles)) {
            // Execute this branch when only update user information
            return Ux.futureA();
        }
        // Remove & Insert

        return null;
    }
}
