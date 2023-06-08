package io.vertx.mod.workflow.uca.conformity;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.task.Task;

public class GainStart extends AbstractGain {

    GainStart(final WTicket ticket) {
        super(ticket);
    }

    @Override
    public Future<WTodo> buildAsync(final JsonObject params, final Task task, final WTodo wTask) {
        // 0. Keep the same acceptedBy / toUser value and do nothing
        // 1. Json -> WTodo
        final WTodo todo = Ux.fromJson(params, WTodo.class);

        // 2. Connect Camunda
        this.bridgeTask(todo, task, this.ticket.getKey());

        // 3. TraceOrder = 1 and generate serial/code
        todo.setTraceOrder(1);
        return Ux.future(todo);
    }
}
