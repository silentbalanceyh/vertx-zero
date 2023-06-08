package io.horizon.specification.action;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * - Async Action
 * 「查询条件编译器」
 * <hr/>
 * 针对两个 {@link JsonObject} 执行计算：
 * <pre><code>
 *     1. data - 传入的数据参数
 *     2. qr - 传入的 Qr 相关配置信息
 * </code></pre>
 * 该接口的核心目的在于将 data 和 qr 两个参数合并，最终生成查询条件，实现内部可使用不同的方式解析
 * 表达式定义。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HQR {

    /**
     * 根据配置定义和传入数据解析查询引擎模板
     *
     * @param data 传入数据
     * @param qr   查询引擎模板
     *
     * @return 返回异步结果
     */
    Future<JsonObject> compile(JsonObject data, JsonObject qr);
}
