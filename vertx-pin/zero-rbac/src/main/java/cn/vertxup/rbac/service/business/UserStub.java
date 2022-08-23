package cn.vertxup.rbac.service.business;

import io.vertx.core.Future;
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
     * create user: SUser and OUser
     */
    Future<JsonObject> createUser(JsonObject params);

    /**
     * delete user including related roles and groups
     */
    Future<Boolean> deleteUser(String userKey);


    // ====================== Information ( By Type ) =======================


    /**
     * Update employee information
     */
    Future<JsonObject> updateInformation(String userId, JsonObject params);

    /**
     *
     */
    Future<JsonObject> fetchInformation(String userId);
}
