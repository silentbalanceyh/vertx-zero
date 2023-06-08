package cn.vertxup.rbac.service.login;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Session;

/*
 * Major interface that has been used in Worker component
 */
public interface AuthStub {

    /**
     * Exchange authorization code
     * by filters ( JsonObject )
     */
    Future<JsonObject> authorize(JsonObject filters);

    /**
     * Exchange token with authorization code
     */
    Future<JsonObject> token(JsonObject filters, Session session);

    /**
     * Login with "username/password"
     */
    Future<JsonObject> login(JsonObject params);
}
