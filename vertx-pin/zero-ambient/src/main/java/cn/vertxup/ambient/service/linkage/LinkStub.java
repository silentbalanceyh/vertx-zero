package cn.vertxup.ambient.service.linkage;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface LinkStub {
    /*
     * Where by `sigma` and `type`
     */
    Future<JsonArray> fetchByType(String type, String sigma);

    Future<JsonArray> fetchNorm(String sourceKey, String targetKey);

    /*
     * Batch Saving
     */
    Future<JsonArray> saving(JsonArray batchData, boolean vector);

    Future<JsonObject> create(JsonObject data, boolean vector);

    Future<JsonArray> syncB(JsonArray data, JsonArray removed);
}
