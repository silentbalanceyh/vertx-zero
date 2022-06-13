package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.runtime.WMove;
import io.vertx.tp.workflow.atom.runtime.WProcess;
import io.vertx.tp.workflow.atom.runtime.WRecord;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.uca.central.AbstractMoveOn;
import io.vertx.tp.workflow.uca.central.AidData;
import org.camunda.bpm.engine.task.Task;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MoveOnUser extends AbstractMoveOn {
    @Override
    public Future<WRecord> transferAsync(final WRequest request, final WProcess process) {
        /*
         * Process creation for new and next step here.
         */
        final WProcess next = WProcess.create();
        {
            /*
             * Processing WProcess creation based on process here
             * Bind Data
             * - Task
             * - WMove
             * - ProcessInstance
             */
            final Task task = process.task();
            final WMove move = this.rule(task.getTaskDefinitionKey());
            move.stored(request.request());
            next.bind(process.taskNext()).bind(move).bind(process.instance());
        }
        // Record and instance
        final WRecord generated = AidData.nextJ(request.record(), next);
        // TodoKit generateAsync
        final JsonObject params = request.request();
        return Objects.requireNonNull(this.todoKit)
            .generateAsync(generated)
            .compose(record -> Objects.requireNonNull(this.linkageKit)
                .syncAsync(params, record)
            );
    }
}
