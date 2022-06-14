package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._409InValidStartException;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.atom.runtime.WTransition;
import io.vertx.tp.workflow.uca.camunda.RunOn;
import io.vertx.tp.workflow.uca.central.AbstractMoveOn;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.repository.ProcessDefinition;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MoveOnStart extends AbstractMoveOn {
    @Override
    public Future<WTransition> moveAsync(final WRequest request, final WTransition wTransition) {
        final ProcessDefinition definition = wTransition.definition();

        if (wTransition.isStarted()) {
            // Error-80604: The wTransition has been started, could not call current divert
            return Ux.thenError(_409InValidStartException.class, this.getClass(), definition.getKey());
        }
        return wTransition.start().compose(started -> {
            /*
             * Input `taskId` is null, in this kind of situation the workflow has not been
             * started, the `WMove` is based on `StartEvent`
             *
             * After start() calling, the `move` has the configured value but
             * `ProcessInstance/Task` are both null.
             *
             * Following code should locate the correct `WRule` for parameters, the collection
             * should be
             *
             * [
             *     WRule,
             *     WRule,
             *     WRule,
             *     ...
             * ]
             *
             * Based on configuration the `WRule` must be mutual exclusion
             */
            final JsonObject parameters = wTransition.rule(request.request());
            final RunOn runOn = RunOn.get();
            return runOn.startAsync(parameters, wTransition);
        }).compose(wTransition::end);
    }
}
