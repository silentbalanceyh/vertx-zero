package io.vertx.mod.rbac.acl.relation;

import cn.vertxup.rbac.domain.tables.daos.RUserGroupDao;
import cn.vertxup.rbac.domain.tables.pojos.RUserGroup;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.mod.ke.secure.Tie;
import io.vertx.mod.rbac.cv.AuthKey;
import io.vertx.mod.rbac.cv.AuthMsg;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.stream.Collectors;

import static io.vertx.mod.rbac.refine.Sc.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TieGroup implements Tie<String, JsonArray> {
    @Override
    public Future<JsonArray> identAsync(final JsonObject userJ) {
        final String userKey = Ut.valueString(userJ, KName.KEY);
        return Ke.umALink(AuthKey.F_USER_ID, userKey, RUserGroupDao.class, RUserGroup::getPriority)
            .compose(result -> {
                final JsonArray groups = new JsonArray();
                result.stream().map(RUserGroup::getGroupId).forEach(groups::add);
                return Ux.future(groups);
            });
    }

    @Override
    public Future<JsonArray> identAsync(final String userKey) {
        LOG.Auth.debug(this.getClass(), AuthMsg.RELATION_GROUP, userKey);
        return Ke.umALink(AuthKey.F_USER_ID, userKey, RUserGroupDao.class);
    }

    @Override
    @SuppressWarnings("all")
    public Future<JsonArray> identAsync(final String userKey, final JsonObject userJ) {
        // Update Related Groups
        final JsonArray groups = Ut.valueJArray(userJ, KName.GROUPS);
        if (Ut.isNil(groups)) {
            return Ux.futureA();
        }

        final JsonObject conditionJ = new JsonObject()
            .put(AuthKey.F_USER_ID, userKey);
        /* Remove & Insert */
        final UxJooq jq = Ux.Jooq.on(RUserGroupDao.class);
        /* Delete Related Groups */
        return jq.deleteByAsync(conditionJ).compose(nil -> {
            /* Insert Related Groups */
            final List<String> groupIds = groups.getList();
            final List<RUserGroup> inserted = groupIds.stream()
                .map(groupId -> new RUserGroup()
                    .setUserId(userKey)
                    .setGroupId(groupId)
                    .setPriority(groupIds.indexOf(groupId)))
                .collect(Collectors.toList());
            return jq.insertAsync(inserted);
        }).compose(nil -> Ux.future(groups));
    }
}
