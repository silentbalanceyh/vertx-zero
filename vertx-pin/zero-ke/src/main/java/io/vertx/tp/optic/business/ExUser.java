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
    Future<JsonObject> update(String key, JsonObject params);

    // ------------------ Information --------------------
    /*
     * Fetch by `modelKey` & `modelId`
     */
    Future<JsonObject> fetch(JsonObject filters);

    /*
     * Fetch all user information by `modelKey` set
     */
    Future<JsonArray> fetch(Set<String> keys);

    // ------------------ Auditor --------------------
    /*
     * Fetch all auditor information by `keys` ( createdBy / updatedBy )
     * Result
     * {
     *      "key1": "realname1",
     *      "key2": "realname2"
     * }
     *
     * List Result
     *
     * realname ->
     * [
     *      "key1",
     *      "key2",
     *      "..."
     * ]
     */
    Future<ConcurrentMap<String, String>> auditor(Set<String> keys);

    Future<ConcurrentMap<String, JsonObject>> auditor(ConcurrentMap<String, String> keyMap);

    Future<JsonArray> search(String keyword);
}
