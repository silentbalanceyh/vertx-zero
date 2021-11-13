package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WInstance;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TransferStart extends AbstractTransfer implements Transfer {
    @Override
    public Future<WTodo> startAsync(final JsonObject params, final WInstance instance) {
        // 1. Generate Todo

        // 2. Execute record
        return null;
    }
}
