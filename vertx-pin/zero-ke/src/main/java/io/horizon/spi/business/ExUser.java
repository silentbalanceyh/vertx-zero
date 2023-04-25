package io.horizon.spi.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/*
 * Channel for account update
 */
public interface ExUser {


    // ------------------ Common Processing --------------------
    /*
     * Nexus implementation by specific definition here.
     * You must set UxJooq instead of other Dao here.
     * 1) This interface is for `modelKey` and `modelId` only
     * 2) A Table defined `modelKey` and `modelId`, the B table
     * 3) When relation has been changed from B, the `modelId` and `modelKey` must be updated
     */

    /*
     * Fetch by `modelKey` & `modelId`
     * `key` is the entity primary key
     * This method will update nexues between
     * Host record and Slave record here.
     * 1) Get record by `key`
     * 2) Provider params such as
     * {
     *     "identifier": "model id",
     *     "key": "record id"
     * }
     */
    Future<JsonObject> rapport(String key, JsonObject params);

    /*
     * Fetch by `modelKey` & `modelId`
     *
     * `identifier` means model id refer to unique identifier of each App
     * `key` means model key refer to unique
     * {
     *     "identifier": "model id, this model is bind to UxJooq",
     *     "key": "record id",
     *     "sigma": "related nexus that bind to app sigma"
     * }
     */
    Future<JsonObject> rapport(JsonObject condition);

    /*
     * Fetch all user information by `modelKey` set
     * 1) Get records by ke set
     * 2) You can ignore other condition
     */
    Future<JsonArray> rapport(Set<String> keys);

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
    Future<ConcurrentMap<String, String>> mapAuditor(Set<String> keys);

    Future<ConcurrentMap<String, JsonObject>> mapUser(Set<String> keys, boolean extension);

    Future<JsonArray> userGroup(String key);

    Future<JsonArray> searchUser(String keyword);
}
