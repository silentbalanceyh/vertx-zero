package cn.vertxup.rbac.service.login;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface LoginStub {
    /*
     * Login workflow
     * {
     *      "username": "xxx",
     *      "password": "xxx"
     * }
     */
    Future<JsonObject> execute(final String username, final String password);

    /*
     * Logout workflow
     * {
     *      "user": "uid",
     *      "habitus": "session key"
     * }
     */
    Future<Boolean> logout(final String user, final String habitus);
}
