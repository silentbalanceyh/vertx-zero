package io.vertx.tp.workflow.uca.runner;

import io.vertx.core.Future;
import io.vertx.tp.error._409UniqueStartEventException;
import io.vertx.tp.error._501ProcessStartException;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.up.eon.Values;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class EventTyped {

    String type(final String definitionId, final String taskKey) {
        final RepositoryService service = WfPin.camundaRepository();
        final BpmnModelInstance instance = service.getBpmnModelInstance(definitionId);
        final ModelElementInstance node = instance.getModelElementById(taskKey);
        return node.getElementType().getTypeName();
    }

    Future<Set<StartEvent>> startSet(final String definitionId) {
        final RepositoryService service = WfPin.camundaRepository();
        final BpmnModelInstance instance = service.getBpmnModelInstance(definitionId);
        final Collection<StartEvent> starts = instance.getModelElementsByType(StartEvent.class);
        if (starts.isEmpty()) {
            return Ux.thenError(_501ProcessStartException.class, this.getClass(), definitionId);
        }
        return Ux.future(new HashSet<>(starts));
    }

    Future<StartEvent> start(final String definitionId) {
        return this.startSet(definitionId).compose(set -> {
            final int size = set.size();
            if (Values.ONE == size) {
                return Ux.future(set.iterator().next());
            } else {
                return Ux.thenError(_409UniqueStartEventException.class, this.getClass(), size, definitionId);
            }
        });
    }
}
