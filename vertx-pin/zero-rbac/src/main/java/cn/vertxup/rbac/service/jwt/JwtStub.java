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
    /**
     * 1. When you login into system successfully, you can store token in to:
     * 1) Redis
     * 2) Database
     * 3) Etcd
     * As you want.
     *
     * 「Optional」
     * Default for optional, Not Implement Situation:
     * 1. When micro service api gateway use security interface
     * -- The store code logical will call remote Rpc service
     * or Http service to store authenticate information
     * 2. Sometimes the storage could not be implemented in
     * default situation.
     *
     * @param data Stored token information
     */
    Future<JsonObject> store(String userKey, JsonObject data);

    /*
     *
     * 2. 401 Access, verify the token that you provided.
     * 1) Correct ?
     * 2) Expired ?
     * 3) Signature valid ?
     * {
     *      "access_token": "xxx",
     *      "user": "xxxx",
     *      "habitus": "xxxx"
     * }
     */
    Future<Boolean> verify(String userKey, String token);
}
