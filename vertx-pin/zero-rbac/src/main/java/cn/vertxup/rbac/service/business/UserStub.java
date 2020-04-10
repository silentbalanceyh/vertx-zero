package cn.vertxup.rbac.service.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/*
 * Basic user interface
 * 1) Get relations between user / role by user key
 * 2) Get OAuth user account information by user key ( client_id )
 */
public interface UserStub {

    /**
     * Fetch ouser by client_id
     */
    Future<JsonObject> fetchOUser(String userKey);

    /**
     * R_USER_ROLE
     * <p>
     * userKey -> Relation to Role
     */
    Future<JsonArray> fetchRoleIds(String userKey);

    /**
     * R_USER_GROUP
     * <p>
     * userKey -> Relation to Group
     */
    Future<JsonArray> fetchGroupIds(String userKey);

    /**
     *
     */
    Future<JsonObject> fetchEmployee(String userId);

    /**
     * Update user information
     */
    Future<JsonObject> updateUser(String userId, JsonObject params);

    /**
     * Update employee information
     */
    Future<JsonObject> updateEmployee(String userId, JsonObject params);

    /**
     * modified by Hongwei at 2019/12/06
     * add fetchUser method to get user information: user information and related roles and groups
     * add createUser method to create user entity: create user record in SUser and OUser tables
     * modify updateUser method: save user information and related roles and groups
     * add deleteUser method to delete user information: delete user information and related roles and groups
     */
    Future<JsonObject> fetchUser(String userKey);

    /**
     * create user: SUser and OUser
     */
    Future<JsonObject> createUser(JsonObject params);

    /**
     * delete user including related roles and groups
     */
    Future<Boolean> deleteUser(String userKey);
}
