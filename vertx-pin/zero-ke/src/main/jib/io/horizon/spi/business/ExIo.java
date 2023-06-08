package io.horizon.spi.business;

import io.horizon.atom.common.Kv;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 1. Storage of Default
 * 2. Storage of FTP
 * 3. Storage of SSH
 * -- dirXx,  For Directory Only, it supports directory feature ( Directory + Io )
 * ---- JsonArray include X_DIRECTORY data only
 * -- fsXx,   For Attachment Standalone operation ( Io Only )
 * ---- JsonArray include X_ATTACHMENT data only
 * -- mixXx,  Support Directory/Attachment both ( Directory + Io )
 * ---- JsonArray include X_DIRECTORY data + storePath data structure
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface ExIo {

    // ----------------- Document Management Platform ----------------------
    /*
     * File System Processing based on Document Management
     * 1. Fetch all data from database `X_DIRECTORY` table
     * 2. Compare the input data and stored data for `UPDATE, ADD, DELETE`
     */
    Future<JsonArray> docInitialize(JsonArray data, JsonObject config);


    // ----------------- Directory Interface ----------------------
    /*
     * Query Data Only, the condition should be
     * 1. parentId = null ( WHERE SIGMA = ? )
     * -- application scope
     * 2. parentId is not null ( WHERE SIGMA = ? AND PARENT_ID = ? )
     * -- parent directory scope
     *
     * -- dirRun:                    active = true
     * -- dirTrash:                  active = false
     */
    Future<JsonArray> dirRun(String sigma, String parentId);

    Future<JsonArray> dirTrash(String sigma);

    Future<JsonArray> dirTree(String sigma, List<String> paths);

    /*
     * Fetch dir by `code` ( MD5 )
     */
    Future<JsonObject> dirBy(String sigma, String directory);

    Future<ConcurrentMap<String, JsonObject>> dirBy(Set<String> keys);

    // ----------------- File Interface ----------------------
    /*
     * Upload files to directory
     *
     * Here the directoryJ is as following structure
     */
    Future<Boolean> fsUpload(String directoryId, ConcurrentMap<String, String> fileMap);

    Future<Boolean> fsRemove(String directoryId, ConcurrentMap<String, String> fileMap);

    Future<Buffer> fsDownload(String directoryId, ConcurrentMap<String, String> fileMap);

    Future<Buffer> fsDownload(String directoryId, String storePath);

    // ----------------- Mix Interface ----------------------

    /*
     * The directoryJ data structure is as following:
     * - storePath
     * - sigma
     * - updatedBy
     *
     * The returned is directoryId
     *
     * 1. Step 1
     * - 1) Query I_DIRECTORY by `storePath` and `sigma` to unique one
     * - 2) If Null, go to Step 2
     *      If Data, terminal
     * 2. Step 2
     * - 1) Split `storePath` by `/` to List<String> ( Sorted by length )
     * - 2) Iterator for each level directory making
     * - 3) Returned the last directory ( the storePath is the longest )
     */
    Future<JsonObject> verifyIn(JsonArray directoryA, JsonObject params);

    /*
     * Update the tree from `directoryId` for impacting
     * - updatedAt
     * - updatedBy
     * All above information must be updated in current interface
     */
    Future<JsonObject> update(String directoryId, String user);

    /*
     * File Map Data structure
     * -- storePath = directoryId
     *
     * Here directoryId will detect store integration part for under Fs interface switch
     */
    Future<JsonArray> trashIn(JsonArray directoryJ, ConcurrentMap<String, String> fileMap);

    Future<JsonArray> trashOut(JsonArray directoryJ, ConcurrentMap<String, String> fileMap);

    Future<JsonArray> purge(JsonArray directoryJ, ConcurrentMap<String, String> fileMap);

    Future<Boolean> rename(JsonObject directoryId, Kv<String, String> renameKv);
}
