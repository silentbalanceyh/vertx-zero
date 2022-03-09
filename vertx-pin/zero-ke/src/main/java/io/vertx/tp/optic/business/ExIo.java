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
     * Query Data Only
     */
    Future<JsonArray> dirLs(String directoryId);

    Future<JsonArray> dirLsR(String sigma);
}
