package io.horizon.util;

import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;

/**
 * @author lang : 2023/5/1
 */
class _End extends _Element {
    protected _End() {
    }

    /**
     * 从 JsonObject 中提取 String 类型的结果，转换成 Result 结果
     * SUCCESS 和 FAILURE
     * 1. SUCCESS -> true
     * 2. FAILURE -> false
     *
     * @param input 传入的 JsonObject
     * @param field 传入的字段名
     *
     * @return Result<Boolean>
     */
    public static Boolean endBool(final JsonObject input, final String field) {
        return HJson.endBool(input, field);
    }

    /**
     * 根据 checked 封装处理布尔结果类型为 JsonObject
     * <pre><code>
     *     checked = true
     *     {
     *         "field": "SUCCESS"
     *     }
     *     checked = false
     *     {
     *         "field": "FAILURE"
     *     }
     * </code></pre>
     *
     * @param checked 是否成功
     * @param field   字段名
     *
     * @return JsonObject
     */
    public static JsonObject endBool(final boolean checked, final String field) {
        return HJson.endBool(checked, field);
    }

    /**
     * 构造包装过的JsonObject结果
     *
     * <pre><code>
     *     {
     *         "field": data
     *     }
     * </code></pre>
     *
     * @param field 字段名
     * @param data  数据
     *
     * @return JsonObject
     */
    public static JsonObject endJObject(final String field, final ClusterSerializable data) {
        return HJson.endJObject(field, data);
    }
}
