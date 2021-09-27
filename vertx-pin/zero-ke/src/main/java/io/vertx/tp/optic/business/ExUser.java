package io.vertx.tp.optic.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/*
 * Channel for account update
 */
public interface ExUser {
    /*
     * Fetch by `modelKey` & `modelId`
     */
    Future<JsonObject> fetchRef(JsonObject filters);

    /*
     * Fetch by `modelKey` & `modelId`
     */
    Future<JsonObject> updateRef(String key, JsonObject params);

    /*
     * Fetch all user information by `modelKey` set
     */
    Future<JsonArray> fetchRef(Set<String> keys);

    /*
     * Fetch all auditor information by `keys` ( createdBy / updatedBy )
     */
    Future<ConcurrentMap<String, String>> transAuditor(Set<String> keys);
}
