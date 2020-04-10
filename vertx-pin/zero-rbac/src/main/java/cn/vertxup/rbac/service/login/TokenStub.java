package cn.vertxup.rbac.service.login;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Session;

/*
 * Token exchange interface
 * 1. exchange token by authorization code
 */
public interface TokenStub {

    Future<JsonObject> execute(String clientId, String code, Session session);
}
