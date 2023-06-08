package io.horizon.spi.modeler;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * 限定组件，用于根据输入生成最终查询条件
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Confine {

    Future<JsonObject> restrict(JsonObject request, JsonObject syntax);
}
