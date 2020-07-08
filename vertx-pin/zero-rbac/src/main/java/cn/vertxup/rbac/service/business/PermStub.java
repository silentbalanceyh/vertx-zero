package cn.vertxup.rbac.service.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public interface PermStub {
    /*
     * Get permission groups service
     * There are two fields that stored data
     *
     * group - Permission Group
     * identifier - Model Identifier
     */
    Future<JsonArray> groupAsync(String sigma);

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
     *     },
     *     "data":[]
     * }
     */
    Future<JsonObject> savingPerm(JsonObject processed, String sigma);
}
