package cn.vertxup.rbac.service.business;

import cn.vertxup.rbac.domain.tables.daos.OUserDao;
import cn.vertxup.rbac.domain.tables.daos.RUserGroupDao;
import cn.vertxup.rbac.domain.tables.daos.RUserRoleDao;
import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import cn.vertxup.rbac.domain.tables.pojos.OUser;
import cn.vertxup.rbac.domain.tables.pojos.RUserGroup;
import cn.vertxup.rbac.domain.tables.pojos.RUserRole;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.AuthKey;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserService implements UserStub {
    private static final Annal LOGGER = Annal.get(UserService.class);

    @Override
    public Future<JsonObject> fetchOUser(final String userKey) {
        return Ux.Jooq.on(OUserDao.class)
            .fetchOneAsync(AuthKey.F_CLIENT_ID, userKey)
            .compose(Ux::futureJ);
    }

    @Override
    public Future<JsonArray> fetchRoleIds(final String userKey) {
        Sc.infoAuth(LOGGER, AuthMsg.RELATION_USER_ROLE, userKey);
        return Sc.relation(AuthKey.F_USER_ID, userKey, RUserRoleDao.class);
    }

    @Override
    public Future<JsonArray> fetchGroupIds(final String userKey) {
        Sc.infoAuth(LOGGER, AuthMsg.RELATION_GROUP, userKey);
        return Sc.relation(AuthKey.F_USER_ID, userKey, RUserGroupDao.class);
    }

    @Override
    public Future<JsonObject> fetchEmployee(final String userId) {
        return Ux.Jooq.on(SUserDao.class)
            /* User Information */
            .<SUser>fetchByIdAsync(userId)
            /* Employee Information */
            .compose(UserHelper::fetchEmployee);
    }

    @Override
    public Future<JsonObject> updateUser(final String userId, final JsonObject params) {
        final JsonArray roles = params.getJsonArray("roles");
        final JsonArray groups = params.getJsonArray("groups");
        /* Merge original here */
        final UxJooq jooq = Ux.Jooq.on(SUserDao.class);
        return jooq.fetchJByIdAsync(userId).compose(original -> {
            final JsonObject dataJson = original.mergeIn(params);
            final SUser user = Ux.fromJson(dataJson, SUser.class);
            /* User Saving here */
            return jooq.updateAsync(userId, user)
                .compose(entity -> this.updateRoles(userId, Ux.toJson(entity), roles))
                .compose(entity -> this.updateGroups(userId, Ux.toJson(entity), groups));
        });
    }

    @Override
    public Future<JsonObject> updateEmployee(final String userId, final JsonObject params) {
        final SUser user = Ux.fromJson(params, SUser.class);
        user.setKey(userId);
        return Ux.Jooq.on(SUserDao.class)
            .updateAsync(userId, user)
            .compose(userInfo -> UserHelper.updateEmployee(userInfo, params));
    }

    @Override
    public Future<JsonObject> fetchUser(final String userKey) {
        return Ux.Jooq.on(SUserDao.class)
            .fetchByIdAsync(userKey)
            .compose(userInfo -> this.fulfillUserWithRolesAndGroups(userKey, Ux.toJson(userInfo)));
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
        return Ux.Jooq.on(SUserDao.class)
            .insertAsync(user)
            .compose(this::createOUser);
    }

    @Override
    public Future<Boolean> deleteUser(final String userKey) {
        final UxJooq sUserDao = Ux.Jooq.on(SUserDao.class);
        final UxJooq oUserDao = Ux.Jooq.on(OUserDao.class);
        final UxJooq rUserRoleDao = Ux.Jooq.on(RUserRoleDao.class);
        final UxJooq rUserGroupDao = Ux.Jooq.on(RUserGroupDao.class);

        return oUserDao.fetchOneAsync(new JsonObject().put("CLIENT_ID", userKey))
            /* delete OUser record */
            .compose(item -> oUserDao.deleteByIdAsync(Ux.toJson(item).getString("key")))
            /* delete related role records */
            .compose(oUserFlag -> rUserRoleDao.deleteByAsync(new JsonObject().put("USER_ID", userKey)))
            /* delete related group records */
            .compose(rUserRoleFlag -> rUserGroupDao.deleteByAsync(new JsonObject().put("USER_ID", userKey)))
            /* delete SUser record */
            .compose(rUserGroupFlag -> sUserDao.deleteByIdAsync(userKey));
    }

    /**
     * TODO: replace the fixed value with real value
     * create OUser record
     *
     * @param user SUser entity
     *
     * @return SUser entity
     */
    private Future<JsonObject> createOUser(final SUser user) {
        final OUser oUser = new OUser()
            .setClientId(user.getKey())
            .setClientSecret(Ut.randomString(64))
            .setScope("vie.app.ox")
            .setGrantType("authorization_code")
            .setLanguage("cn")
            .setActive(Boolean.TRUE)
            .setKey(UUID.randomUUID().toString());

        return Ux.Jooq.on(OUserDao.class)
            .insertAsync(oUser)
            // delete attribute: password from user information
            // To avoid update to EMPTY string
            .compose(entity -> Ux.futureJ(user.setPassword(null)));
    }

    /**
     * get related groups and roles of user
     *
     * @param userKey user key
     * @param user    user entity
     *
     * @return user entity
     */
    private Future<JsonObject> fulfillUserWithRolesAndGroups(final String userKey, final JsonObject user) {
        /* delete attribute: password from user information */
        // user.put("password", "")
        user.remove(KName.PASSWORD);
        user.put("roles",
                Ux.Jooq.on(RUserRoleDao.class)
                    .fetch(AuthKey.F_USER_ID, userKey)
                    .stream()
                    .sorted(Comparator.comparing(item -> ((RUserRole) item).getPriority()))
                    .map(item -> Ux.toJson(item).getString("roleId"))
                    .collect(Collectors.toList())
            )
            .put("groups", Ux.Jooq.on(RUserGroupDao.class)
                .fetch(AuthKey.F_USER_ID, userKey)
                .stream()
                .sorted(Comparator.comparing(item -> ((RUserGroup) item).getPriority()))
                .map(item -> Ux.toJson(item).getString("groupId"))
                .collect(Collectors.toList())
            );
        return Ux.future(user);
    }

    @SuppressWarnings("all")
    private Future<JsonObject> updateRoles(final String userKey, final JsonObject user, final JsonArray roles) {
        /* execute this branch when only update user information */
        if (Objects.isNull(roles)) {
            return Ux.future(user);
        } else {
            /* execute this branch when update user and related information */
            final List<String> roleIds = roles.getList();
            return Ux.Jooq.on(RUserRoleDao.class)
                /* delete related roles */
                .deleteByAsync(new JsonObject().put("userId", userKey))
                /* insert related roles */
                .compose(roleFlag -> Ux.future(roleIds.stream().map((roleId -> {
                    RUserRole rUserRole = new RUserRole()
                        .setUserId(userKey)
                        .setRoleId(roleId)
                        .setPriority(roleIds.indexOf(roleId));
                    return Ux.Jooq.on(RUserRoleDao.class).insert(rUserRole);
                })).collect(Collectors.toList())).compose(inserted -> Ux.future(user.put("roles", roles))));
        }
    }

    @SuppressWarnings("all")
    private Future<JsonObject> updateGroups(final String userKey, final JsonObject user, final JsonArray groups) {
        if (Objects.isNull(groups)) {
            return Ux.future(user);
        } else {
            final List<String> groupIds = groups.getList();
            return Ux.Jooq.on(RUserGroupDao.class)
                .deleteByAsync(new JsonObject().put("userId", userKey))
                /* insert related roles */
                .compose(groupFlag -> Ux.future(groupIds.stream().map(groupId -> {
                    RUserGroup rUserGroup = new RUserGroup()
                        .setUserId(userKey)
                        .setGroupId(groupId)
                        .setPriority(groupIds.indexOf(groupId));
                    return Ux.Jooq.on(RUserGroupDao.class).insert(rUserGroup);
                }).collect(Collectors.toList()))).compose(inserted -> Ux.future(user.put("groups", groups)));
        }
    }
}
