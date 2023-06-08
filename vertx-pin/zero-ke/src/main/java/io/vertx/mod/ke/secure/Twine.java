package io.vertx.mod.ke.secure;

import io.horizon.exception.web._501NotSupportException;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
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
public interface Twine<ID> extends Tie<ID, JsonObject> {

    default Future<JsonArray> identAsync(final Collection<ID> keys) {
        return Fn.outWeb(_501NotSupportException.class, this.getClass());
    }

    default Future<JsonObject> searchAsync(final String identifier, final JsonObject criteria) {
        return Fn.outWeb(_501NotSupportException.class, this.getClass());
    }
}