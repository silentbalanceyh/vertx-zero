package cn.vertxup.fm.service.end;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface PayStub {
    /*
     * Debt / Refund Creating
     */
    Future<JsonObject> createAsync(JsonObject data);

    Future<Boolean> deleteByItem(String itemKey);
}
