package io.vertx.tp.workflow.uca.modeling;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WRecord;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Respect {
    /*
     * Sync Respect
     * 1 - XLinkage
     * 2 - XAttachment
     */
    Future<JsonArray> syncAsync(JsonArray data, JsonObject params, WRecord record);

    Future<JsonArray> fetchAsync(WRecord record);
}
