package io.vertx.mod.rbac.acl.relation;

import cn.vertxup.rbac.domain.tables.daos.RUserGroupDao;
import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ke.secure.Twine;
import io.vertx.mod.rbac.cv.AuthKey;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Collection;
import java.util.Objects;

public class TwineRights implements Twine<String> {
    /*
     * userJ -> User + Extension JsonObject
     * This method will extract `roles` & `groups` from system
     */
    @Override
    public Future<JsonObject> identAsync(final JsonObject userJ) {
        final String key = Ut.valueString(userJ, KName.KEY);
        return Junc.role().identAsync(key).compose(roles -> {
            userJ.put(KName.ROLE, Ut.encryptBase64(roles.encodePrettily()));
            return Ux.future();
        }).compose(nil -> Junc.group().identAsync(key)).compose(groups -> {
            userJ.put(KName.GROUP, Ut.encryptBase64(groups.encodePrettily()));
            return Ux.future(userJ);
        });
    }

    @Override
    public Future<JsonObject> identAsync(final String key) {
        return Ux.Jooq.on(SUserDao.class).fetchJByIdAsync(key)
                .compose(userJ -> {
                    /* delete attribute: password from user information */
                    userJ.remove(KName.PASSWORD);
                    return Ux.future(userJ);
                })
                .compose(userJ -> Junc.role().identAsync(userJ)
                        // roles -> JsonArray
                        .compose(roles -> Ux.future(userJ.put(KName.ROLES, roles))))
                .compose(userJ -> Junc.group().identAsync(userJ)
                        // groups -> JsonArray
                        .compose(groups -> Ux.future(userJ.put(KName.GROUPS, groups)))
                );
    }

    @Override
    public Future<JsonObject> identAsync(final String key, final JsonObject updatedData) {
        return this.updateAsync(key, updatedData)
                .compose(userJ -> Junc.role().identAsync(key, userJ)
                        // roles -> JsonArray
                        .compose(roles -> Ux.future(userJ.put(KName.ROLES, roles))))
                .compose(userJ -> Junc.group().identAsync(key, userJ)
                        // groups -> JsonArray
                        .compose(groups -> Ux.future(userJ.put(KName.GROUPS, groups)))
                );
    }

    // ----------------------- Extract Data -----------------------
    /*
     * 此处读取只读取 groups，不读取 roles，最终数据格式如：
     * {
     *     "userId": "xxx",
     *     "groupId": "xxx"
     * }
     */
    @Override
    public Future<JsonArray> identAsync(final Collection<String> keys) {
        return Ux.Jooq.on(RUserGroupDao.class).fetchJInAsync(AuthKey.F_USER_ID, keys);
    }

    private Future<JsonObject> updateAsync(final String userKey, final JsonObject params) {
        /* Merge original here */
        final UxJooq jq = Ux.Jooq.on(SUserDao.class);
        return jq.<SUser>fetchByIdAsync(userKey).compose(queried -> {
            if (Objects.isNull(queried)) {
                return Ux.futureJ();
            }
            final SUser updated = Ux.updateT(queried, params);
            /* User Saving here */
            return jq.updateJAsync(userKey, updated).compose(userJ -> {
                // Be sure the response contains `roles` and `groups`
                userJ.put(KName.ROLES, params.getValue(KName.ROLES));
                userJ.put(KName.GROUPS, params.getValue(KName.GROUPS));
                return Ux.future(userJ);
            });
        });
    }
}
