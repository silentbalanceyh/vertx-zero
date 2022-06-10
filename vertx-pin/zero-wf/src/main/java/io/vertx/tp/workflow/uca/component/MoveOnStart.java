package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.error._409InValidStartException;
import io.vertx.tp.workflow.atom.WMove;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRequest;
import io.vertx.tp.workflow.uca.central.AbstractMoveOn;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.tp.workflow.uca.runner.RunOn;
import io.vertx.up.experiment.specification.KFlow;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MoveOnStart extends AbstractMoveOn {
    @Override
    public Future<WProcess> moveAsync(final WRequest request, final WProcess process) {
        final KFlow key = request.workflow();
        final String definitionKey = key.definitionKey();

        if (process.isStart()) {
            // Error-80604: The process has been started, could not call current divert
            return Ux.thenError(_409InValidStartException.class, this.getClass(), definitionKey);
        }
        /*
         * instance does not exist in your system here, it means that you must do as following:
         * 1. Call Start Process
         * 2. Get new WMove of started and Bind Moving
         * 3. Camunda Instance Moving to next
         */
        // Engine Connect
        final String definitionId = key.definitionId();
        final EventOn eventOn = EventOn.get();
        return eventOn.start(definitionId)
            .compose(event -> {
                // WMove Get
                final WMove move = this.rule(event.getId()).stored(request.request());
                // Camunda Workflow Running
                final RunOn runOn = RunOn.get();
                return runOn.startAsync(definitionKey, move);
            })
            .compose(process::future /* WProcess -> Bind Process */)
            .compose(nil -> process.future());
    }
}
