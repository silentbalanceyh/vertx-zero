package cn.vertxup.rbac.service.login;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/*
 * Authorization Code Api
 * 1) Generate authorization code
 * 2) Verify authorization code
 */
public interface CodeStub {
    /*
     * Get new authorization code / generate new code
     */
    Future<JsonObject> authorize(String clientId);

    /*
     * Verify authorization code
     * {
     *      "code":"xxx",
     *      "client_id":"xxx"
     * }
     */
    Future<String> verify(String clientId, String code);
}
