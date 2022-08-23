package io.vertx.tp.rbac.acl.relation;

import cn.vertxup.rbac.domain.tables.daos.RUserGroupDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.ke.secure.Tie;
import io.vertx.tp.rbac.cv.AuthKey;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.refine.Sc;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TieGroup implements Tie<String, JsonArray> {
    @Override
    public Future<JsonArray> identAsync(final String userKey) {
        Sc.infoAuth(this.getClass(), AuthMsg.RELATION_GROUP, userKey);
        return Ke.umALink(AuthKey.F_USER_ID, userKey, RUserGroupDao.class);
    }

    @Override
    public Future<JsonArray> identAsync(final String key, final JsonObject updatedJ) {
        return null;
    }
}
