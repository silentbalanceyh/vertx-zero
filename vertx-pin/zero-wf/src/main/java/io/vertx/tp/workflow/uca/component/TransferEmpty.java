package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TransferEmpty extends AbstractTransfer implements Transfer {
    @Override
    public Future<WTodo> startAsync(final JsonObject params) {
        return Ux.future();
    }
}
