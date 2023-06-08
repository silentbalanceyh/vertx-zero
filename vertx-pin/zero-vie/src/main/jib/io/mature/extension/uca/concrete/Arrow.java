package io.mature.extension.uca.concrete;

import io.horizon.exception.web._501NotSupportException;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/*
 * 单操作专用接口
 * 1）每一种操作对应一个接口
 * 2）由 PreAtomSwitcher 进行置换选择
 * 3）同时处理 JsonObject / JsonArray 两种
 */
public interface Arrow {
    /*
     * 操作单条记录
     */
    Future<JsonObject> processAsync(final JsonObject record);

    /*
     * 操作批量记录（默认设置异常抛出）
     */
    default Future<JsonArray> processAsync(final JsonArray records) {
        throw new _501NotSupportException(this.getClass());
    }
}
