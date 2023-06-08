package io.vertx.mod.workflow.uca.conformity;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.task.Task;

import java.util.UUID;

class GainGenerate extends AbstractGain {

    GainGenerate(final WTicket ticket) {
        super(ticket);
    }

    @Override
    public Future<WTodo> buildAsync(final JsonObject params, final Task task, final WTodo wTask) {
        // 1. Generate new WTodo
        final WTodo generated = Ux.fromJson(params, WTodo.class);
        {
            // Key Remove ( Comment Clear )
            generated.setKey(UUID.randomUUID().toString());

            // Comment Clear
            generated.setComment(null);
            generated.setCommentApproval(null);
            generated.setCommentReject(null);

            // Status by VM
            GVm.generate(generated, wTask, this.ticket);

            // Auditor Processing
            generated.setFinishedAt(null);
            generated.setFinishedBy(null);

            // Escalate Null
            generated.setEscalate(null);
            generated.setEscalateData(null);
        }

        // 2. Connect Camunda
        this.bridgeTask(generated, task, wTask.getTraceId());

        // 3. TraceOrder = original + 1 and generate serial/code
        generated.setTraceOrder(wTask.getTraceOrder() + 1);

        // 4. Set task auditor information
        this.bridgeAudit(generated, wTask);
        return Ux.future(generated);
    }
}
