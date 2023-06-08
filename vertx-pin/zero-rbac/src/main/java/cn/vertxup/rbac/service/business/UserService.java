package cn.vertxup.rbac.service.business;

import cn.vertxup.rbac.domain.tables.daos.OUserDao;
import cn.vertxup.rbac.domain.tables.daos.RUserGroupDao;
import cn.vertxup.rbac.domain.tables.daos.RUserRoleDao;
import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.horizon.atom.common.Refer;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.acl.relation.Junc;
import io.vertx.mod.rbac.cv.AuthKey;
import io.vertx.mod.rbac.refine.Sc;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.Objects;

public class UserService implements UserStub {

    // ================== Type Part for S_USER usage ================

    /*
     * Async for user information
     * 1) Fetch information from S_USER
     * 2) Re-calculate the information by `modelId/modelKey` instead of ...
     *    -- Employee: modelId = employee
     *    -- Member:   modelId = member
     * 3) Fetch secondary information based on configuration by
     *    key = modelKey
     *
     * The whole level should be
     *
     */
    @Override
    public Future<JsonObject> fetchInformation(final String userId) {
        return Ux.Jooq.on(SUserDao.class)
            /* User Information */
            .<SUser>fetchByIdAsync(userId)
            /* Employee Information */
            .compose(Junc.refExtension()::identAsync)
            /* Relation for roles / groups */
            .compose(Junc.refRights()::identAsync);
    }

    // ================== Basic Part of S_User ================

    @Override
    public Future<JsonObject> fetchOUser(final String userKey) {
        return Ux.Jooq.on(OUserDao.class)
            .fetchOneAsync(AuthKey.F_CLIENT_ID, userKey)
            .compose(Ux::futureJ);
    }

    /*
     * 只附加更新关联对象，该API不更新和账号本身相关的内容如
     * -- Role
     * -- Group
     * -- OUser
     */
    @Override
    public Future<JsonObject> updateInformation(final String userId, final JsonObject params) {
        final SUser user = Ux.fromJson(params, SUser.class);
        user.setKey(userId);
        return Ux.Jooq.on(SUserDao.class).updateAsync(userId, user)
            .compose(userInfo -> Junc.refExtension().identAsync(userInfo, params));
    }

    @Override
    public Future<JsonObject> createUser(final JsonObject params) {
        final SUser user = Ux.fromJson(params, SUser.class);
        /*
         * 创建账号时如果没有密码则设置初始密码
         * 初始密码配置位置：plugin/rbac/configuration.json
         */
        if (Objects.isNull(user.getPassword())) {
            user.setPassword(Sc.valuePassword());
        }
        final Refer refer = new Refer();
        return Ux.Jooq.on(SUserDao.class).insertAsync(user)
            .compose(refer::future)
            // 创建认证信息
            .compose(inserted -> Sc.valueAuth(inserted, params))
            // Insert new OUser Record
            .compose(oUser -> Ux.Jooq.on(OUserDao.class).insertAsync(oUser))
            // delete attribute: password from user information To avoid update to EMPTY string
            .compose(entity -> Ux.futureJ(refer.<SUser>get().setPassword(null)));
    }

    @Override
    public Future<Boolean> deleteUser(final String userKey) {
        final UxJooq sUserDao = Ux.Jooq.on(SUserDao.class);
        final UxJooq oUserDao = Ux.Jooq.on(OUserDao.class);
        final UxJooq rUserRoleDao = Ux.Jooq.on(RUserRoleDao.class);
        final UxJooq rUserGroupDao = Ux.Jooq.on(RUserGroupDao.class);

        return oUserDao.fetchOneAsync(new JsonObject().put(KName.CLIENT_ID, userKey))
            /* delete OUser record */
            .compose(item -> oUserDao.deleteByIdAsync(Ux.toJson(item).getString(KName.KEY)))
            /* delete related role records */
            .compose(oUserFlag -> rUserRoleDao.deleteByAsync(new JsonObject().put(KName.USER_ID, userKey)))
            /* delete related group records */
            .compose(rUserRoleFlag -> rUserGroupDao.deleteByAsync(new JsonObject().put(KName.USER_ID, userKey)))
            /* delete SUser record */
            .compose(rUserGroupFlag -> sUserDao.deleteByIdAsync(userKey));
    }
}
