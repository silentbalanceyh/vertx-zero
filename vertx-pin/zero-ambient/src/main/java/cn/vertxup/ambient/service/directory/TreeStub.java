package cn.vertxup.ambient.service.directory;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface TreeStub {
    /*
     * - appId
     * - type
     */
    Future<JsonArray> seekAsync(String appId, String type);
}
