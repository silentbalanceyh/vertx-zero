package io.vertx.tp.workflow.uca.component;

import io.aeon.experiment.specification.power.KFlow;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._409InValidInstanceException;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.atom.runtime.WTransition;
import io.vertx.tp.workflow.uca.camunda.RunOn;
import io.vertx.tp.workflow.uca.central.AbstractMoveOn;
import io.vertx.up.fn.Fn;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MoveOnNext extends AbstractMoveOn {
    @Override
    public Future<WTransition> moveAsync(final WRequest request, final WTransition wTransition) {
        final ProcessInstance instance = wTransition.instance();
        final KFlow key = request.workflow();
        final String instanceId = key.instanceId();
        if (Objects.isNull(instance)) {
            return Fn.failure(_409InValidInstanceException.class, this.getClass(), instanceId);
        }
        return wTransition.start().compose(started -> {
            final JsonObject parameters = wTransition.moveParameter(request);
            final RunOn runOn = RunOn.get();
            return runOn.moveAsync(parameters, wTransition);
        }).compose(wTransition::end);
    }
}
