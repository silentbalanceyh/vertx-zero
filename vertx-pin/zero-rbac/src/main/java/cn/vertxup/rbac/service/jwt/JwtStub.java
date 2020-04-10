package cn.vertxup.rbac.service.jwt;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/*
 * Wall processing of Jwt Token
 * This component will be used by @Wall class
 * 1) @Wall store code logical
 *    - After login, stored critical information of current user
 *    - After login, stored role information of current user
 *    - If group supported, stored group information of current user
 */
public interface JwtStub {

    Future<JsonObject> store(String userKey, JsonObject data);

    Future<Boolean> verify(String userKey, String token);
}
