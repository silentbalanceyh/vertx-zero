package io.vertx.mod.workflow.uca.conformity;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.camunda.bpm.engine.task.Task;

/*
 * Task Gain that based on different lifecycle
 *
 * The constructor could be bind to ticket
 */
public interface Gain {

    static Gain starter(final WTicket ticket) {
        return new GainStart(ticket);
    }

    static Gain generator(final WTicket ticket) {
        return new GainGenerate(ticket);
    }

    Future<WTodo> buildAsync(JsonObject params, Task task, WTodo wTask);
}
