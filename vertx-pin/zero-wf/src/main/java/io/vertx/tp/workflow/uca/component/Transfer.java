package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * Todo Generation
 * 1. Start Component
 * 2. Generate Component
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Transfer {
    Future<WTodo> run(JsonObject params, JsonObject config);
}
