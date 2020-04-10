package io.vertx.tp.plugin.history;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/*
 * Trash Client for
 * 1) backup
 * 2) restore
 */
public interface TrashClient {
    /*
     * Backup record for each deleting
     */
    Future<JsonObject> backupAsync(JsonObject record, MultiMap params);

    Future<JsonObject> backupAsync(JsonObject record);

    Future<JsonArray> backupAsync(JsonArray records, MultiMap params);

    Future<JsonArray> backupAsync(JsonArray records);

    /*
     * Restore record for each deleting
     */
    Future<JsonObject> restoreAsync(JsonObject record, MultiMap params);
}
