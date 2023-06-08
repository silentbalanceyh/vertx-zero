package io.vertx.mod.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.runtime.WRequest;
import io.vertx.mod.workflow.atom.runtime.WTransition;
import io.vertx.mod.workflow.error._409InValidStartException;
import io.vertx.mod.workflow.uca.camunda.RunOn;
import io.vertx.mod.workflow.uca.central.AbstractMoveOn;
import io.vertx.up.fn.Fn;
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
            return Fn.outWeb(_409InValidStartException.class, this.getClass(), definition.getKey());
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
             *
             * Be careful about the definition of `data` of current node:
             *
             * Example:
             * {
             *     "data": {
             *         "draft": "draft"
             *     }
             * }
             */
            final JsonObject parameters = wTransition.moveParameter(request);
            final RunOn runOn = RunOn.get();
            return runOn.startAsync(parameters, wTransition);
        }).compose(wTransition::end);
    }
}
