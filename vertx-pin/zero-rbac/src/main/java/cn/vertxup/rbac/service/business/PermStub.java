package cn.vertxup.rbac.service.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface PermStub {

    /*
     * Permission removed here
     * The data structure is as following:
     * {
     *     "removed": [
     *         "action1",
     *         "action2"
     *     ],
     *     "relation":{
     *         "action3": "permission1",
     *         "action4": "permission1",
     *         "action5": "permission2"
     *     }
     * }
     *
     * Process relation between `Action` and `Permission`
     * -- Action & Permission
     */
    Future<JsonObject> syncAsync(JsonArray removed, JsonObject relation, String userKey);

    /*
     * Permission Sync with `roleId` provided
     * Process relation between `Role` and `Permission`
     * -- Role & Permission
     */
    Future<JsonArray> syncAsync(JsonArray permissions, String roleId);

    // ======================= CRUD Replace =============================
    /*
     * Query engine processing
     * - sigma for S_PERM_SET table here
     */
    Future<JsonObject> searchAsync(JsonObject query, String sigma);

    /*
     * Crud
     */
    Future<JsonObject> fetchAsync(String key);

    Future<JsonObject> createAsync(JsonObject body);

    Future<JsonObject> updateAsync(String key, JsonObject body);

    Future<Boolean> deleteAsync(String key, String userKey);
}
