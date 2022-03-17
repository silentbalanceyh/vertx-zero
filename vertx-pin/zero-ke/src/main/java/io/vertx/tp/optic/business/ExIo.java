package io.vertx.tp.optic.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * 1. Storage of Default
 * 2. Storage of FTP
 * 3. Storage of SSH
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface ExIo {
    /*
     * File System Processing
     */
    Future<JsonArray> dirMk(JsonArray data, JsonObject config);

    /*
     * Query Data Only, the condition should be
     * 1. parentId = null ( WHERE SIGMA = ? )
     * -- application scope
     * 2. parentId is not null ( WHERE SIGMA = ? AND PARENT_ID = ? )
     * -- parent directory scope
     */
    Future<JsonArray> dirLs(String sigma, String parentId);

    Future<JsonArray> dirTrashed(String sigma);
}
