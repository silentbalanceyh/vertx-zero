package io.vertx.tp.workflow.uca.modeling;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class TodoStart implements TodoOn {
    @Override
    public Future<WTodo> dataAsync(final JsonObject params, final JsonObject config) {
        return null;
    }
}
