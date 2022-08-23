package io.vertx.tp.optic.environment;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.fn.Fn;

import java.util.Collection;

/**
 * 新接口，连通接口
 * 1. 「找用户」被 ExUserEpic -> ExUser 通道调用，在 EmployeeService 中会调用 ExUser
 * 2. 「找关联」原始的 UserExtension，正向查找
 * 3. 「元数据」字段级的底层调用，执行字段转换
 * 4. 「找关联」原始的 Role / Group 多向查找
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Connex<ID> {
    /*
     * 参数转换成查询条件
     * {
     *     "identifier": "modelId",
     *     "key":        "modelKey",
     *     "sigma":      "sigma"
     * }
     */
    default Future<JsonObject> identAsync(final JsonObject condition) {
        return Fn.error(_501NotSupportException.class, this.getClass());
    }

    Future<JsonObject> identAsync(ID key);

    Future<JsonObject> identAsync(ID key, JsonObject updatedData);

    Future<JsonArray> identAsync(Collection<ID> keys);

    default Future<JsonObject> searchAsync(final String identifier, final JsonObject criteria) {
        return Fn.error(_501NotSupportException.class, this.getClass());
    }
}