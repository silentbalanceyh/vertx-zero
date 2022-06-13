package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.runtime.WRecord;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.atom.runtime.WTransition;
import io.vertx.tp.workflow.uca.central.AbstractMoveOn;
import io.vertx.tp.workflow.uca.central.AidData;
import org.camunda.bpm.engine.task.Task;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MoveOnUser extends AbstractMoveOn {
    @Override
    public Future<WRecord> transferAsync(final WRequest request, final WTransition process) {
        /*
         * Process creation for new and next step here.
         */
        final WTransition next = this.createTransition(request);
        {
            /*
             * Processing WProcess creation based on process here
             * Bind Data
             * - Task
             * - WMove
             * - ProcessInstance
             */
            final Task task = process.from();
            // final WMove move = this.rule(task.getTaskDefinitionKey());
            // move.stored(request.request());
            // next.from(process.to()).bind(move);
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
