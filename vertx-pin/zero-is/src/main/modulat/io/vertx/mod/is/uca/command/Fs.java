package io.vertx.mod.is.uca.command;

import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.horizon.atom.common.Kv;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * File System Here for integration
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Fs {
    /*
     * Tree Directory Initialize
     * Trash Directory Initialize
     */
    IDirectory initTree(JsonObject directory);

    void initTrash();

    /*
     * 1. Sync Data between ( Actual / Database )
     * 2. Command: mkdir
     */
    Future<JsonArray> synchronize(JsonArray data, JsonObject config);

    // ------------------- Cmd --------------------
    /*
     * Command: mkdir
     * - JsonArray
     * - JsonObject
     */
    Future<JsonArray> mkdir(JsonArray data);

    Future<JsonObject> mkdir(JsonObject data);

    /*
     * Command: rm
     * - JsonArray
     */
    Future<JsonArray> rm(JsonArray data);

    Future<JsonObject> rm(JsonObject data);

    Future<Boolean> rm(Collection<String> storeSet);

    /*
     * Command: none
     * - Rename folder
     */
    Future<Boolean> rename(String from, String to);

    Future<Boolean> rename(Kv<String, String> kv);

    Future<Boolean> rename(ConcurrentMap<String, String> transfer);

    /*
     * Read to Buffer
     */
    Future<Boolean> upload(ConcurrentMap<String, String> transfer);

    Future<Buffer> download(String storePath);

    Future<Buffer> download(Set<String> storeSet);

}
