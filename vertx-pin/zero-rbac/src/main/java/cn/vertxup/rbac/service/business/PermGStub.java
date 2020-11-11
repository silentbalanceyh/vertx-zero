package cn.vertxup.rbac.service.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * PERM_SET table management processing
 * 1) The code logical for permission fetching
 * 2) The code logical of major definition
 */
public interface PermGStub {
    /*
     * Get permission groups service
     * There are two fields that stored data
     *
     * group - Permission Group
     * identifier - Model Identifier
     */
    Future<JsonArray> fetchAsync(String sigma);

    /*
     * Permission Sync with `group` provided
     * {
     *     "group": "xxx",
     *     "data": []
     * }
     */
    Future<JsonArray> saveDefinition(JsonArray permission, String group, String sigma, String userKey);
}
