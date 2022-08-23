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
import io.vertx.tp.rbac.acl.relation.Junc;
import io.vertx.tp.rbac.atom.ScConfig;
import io.vertx.tp.rbac.cv.AuthKey;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.init.ScPin;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.eon.Constants;
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
    private static final ScConfig CONFIG = ScPin.getConfig();

    // ================== Type Part for S_USER usage ================
    @Override
    public Future<JsonObject> searchUser(final String identifier, final JsonObject criteria) {
        return Junc.extension().searchAsync(identifier, criteria);
    }

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
            .compose(Junc.extension()::identAsync)
            /* Relation for roles / groups */
            .compose(this::fetchRelation);
    }

    // ================== Basic Part of S_User ================

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


    private Future<JsonObject> fetchRelation(final JsonObject userJson) {
        final String key = userJson.getString(KName.KEY);
        return this.fetchRoleIds(key).compose(roles -> {
            userJson.put(KName.ROLE, Ut.encryptBase64(roles.encodePrettily()));
            return Ux.future();
        }).compose(nil -> this.fetchGroupIds(key)).compose(groups -> {
            userJson.put(KName.GROUP, Ut.encryptBase64(groups.encodePrettily()));
            return Ux.future(userJson);
        });
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
    public Future<JsonObject> updateInformation(final String userId, final JsonObject params) {
        final SUser user = Ux.fromJson(params, SUser.class);
        user.setKey(userId);
        return Ux.Jooq.on(SUserDao.class)
            .updateAsync(userId, user)
            .compose(userInfo -> Junc.extension().identAsync(userInfo, params));
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
        return Ux.Jooq.on(SUserDao.class).insertAsync(user)
            // 创建认证信息
            .compose(inserted -> this.createOUser(inserted, params));
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

    /**
     * create OUser record
     *
     * @param user SUser entity
     *
     * @return SUser entity
     */
    private Future<JsonObject> createOUser(final SUser user, final JsonObject input) {
        final String language = input.getString(KName.LANGUAGE, Constants.DEFAULT_LANGUAGE);
        final JsonObject initializeJ = CONFIG.getInitialize();
        final OUser oUser = Ux.fromJson(initializeJ, OUser.class);
        oUser.setClientId(user.getKey())
            .setClientSecret(Ut.randomString(64))
            .setLanguage(language)
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
