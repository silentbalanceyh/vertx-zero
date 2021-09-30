package io.vertx.up.secure;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * ZaaS -> Zero Authenticate and Authorization Service
 * Interface defined for component
 */
public interface ZaaS {
    /**
     * 1. When you login into system successfully, you can store token in to:
     * 1) Redis
     * 2) Database
     * 3) Etcd
     * As you want.
     *
     * @param data Stored token information
     */
    default Future<JsonObject> store(final JsonObject data) {
        /*
         * 「Optional」
         * Default for optional, Not Implement Situation:
         * 1. When micro service api gateway use security interface
         *  -- The store code logical will call remote Rpc service
         *     or Http service to store authenticate information
         * 2. Sometimes the storage could not be implemented in
         *  default situation.
         */
        return Future.succeededFuture(data);
    }

    /**
     * 2. 401 Access, verify the token that you provided.
     * 1) Correct ?
     * 2) Expired ?
     * 3) Signature valid ?
     *
     * @param data The stored token data here
     *
     * @return Whether it's valid
     */
    default Future<Boolean> verify(final JsonObject data) {
        /*
         * The default 401 is false, it means that when you set the wall, you must
         * provide simple authenticate method
         */
        return Future.succeededFuture(Boolean.FALSE);
    }

    /**
     * 3. 403 Access, verify the resource access
     * Optional workflow: default return true means no access
     */
    default Future<Boolean> authorize(final JsonObject user) {
        /*
         * 「Optional」
         * For default situation, 403 issue won't throw, it means that
         * There is no 403 access issue in default situation.
         */
        return Future.succeededFuture(Boolean.TRUE);
    }
}
