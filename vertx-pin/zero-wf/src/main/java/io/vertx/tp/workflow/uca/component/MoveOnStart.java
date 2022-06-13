package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.error._409InValidStartException;
import io.vertx.tp.workflow.atom.runtime.WMove;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.atom.runtime.WTransition;
import io.vertx.tp.workflow.uca.camunda.Io;
import io.vertx.tp.workflow.uca.camunda.RunOn;
import io.vertx.tp.workflow.uca.central.AbstractMoveOn;
import io.vertx.up.experiment.specification.KFlow;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MoveOnStart extends AbstractMoveOn {
    @Override
    public Future<WTransition> moveAsync(final WRequest request, final WTransition wTransition) {
        final KFlow key = request.workflow();
        final String definitionKey = key.definitionKey();

        if (wTransition.isStarted()) {
            // Error-80604: The wTransition has been started, could not call current divert
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
        final Io<StartEvent> io = Io.ioEventStart();
        return io.child(definitionId)
            .compose(event -> {
                // WMove Get
                final WMove move = this.rule(event.getId()).stored(request.request());
                wTransition.bind(move);
                // Camunda Workflow Running
                final RunOn runOn = RunOn.get();
                return runOn.startAsync(definitionKey, move);
            })

            /*
             * 「Engine 1」:
             * 1) Start the workflow here
             * 2) Bind the WMove to WProcess first and then bind the ProcessInstance
             * 3) Each workflow will start here for continue wTransitioning
             */
            .compose(nil -> Ux.future(wTransition));
    }
}
