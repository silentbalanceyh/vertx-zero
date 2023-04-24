package io.aeon.specification.query;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * 「查询条件处理器」
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HCond {
    /*
     * qr - 传入的配置信息
     * data - 输入专用参数（可执行表达式解析的核心参数）
     * 最终返回处理过后的查询条件
     */
    Future<JsonObject> compile(JsonObject data, JsonObject qr);
}
