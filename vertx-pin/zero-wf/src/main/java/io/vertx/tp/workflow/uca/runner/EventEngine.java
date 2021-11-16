package io.vertx.tp.workflow.uca.runner;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.error._409UniqueStartEventException;
import io.vertx.tp.error._501ProcessStartException;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.up.eon.Values;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class EventEngine implements EventOn {

    @Override
    public Future<Set<StartEvent>> startSet(final String definitionId) {
        final RepositoryService service = WfPin.camundaRepository();
        final BpmnModelInstance instance = service.getBpmnModelInstance(definitionId);
        final Collection<StartEvent> starts = instance.getModelElementsByType(StartEvent.class);
        if (starts.isEmpty()) {
            return Ux.thenError(_501ProcessStartException.class, this.getClass(), definitionId);
        }
        return Ux.future(new HashSet<>(starts));
    }

    @Override
    public Future<StartEvent> start(final String definitionId) {
        return this.startSet(definitionId).compose(set -> {
            final int size = set.size();
            if (Values.ONE == size) {
                return Ux.future(set.iterator().next());
            } else {
                return Ux.thenError(_409UniqueStartEventException.class, this.getClass(), size, definitionId);
            }
        });
    }

    @Override
    public Future<Task> task(final ProcessInstance instance) {
        final TaskService service = WfPin.camundaTask();
        final Task task = service.createTaskQuery()
            .initializeFormKeys()
            .processInstanceId(instance.getId())
            .active().singleResult();
        return Ux.future(task);
    }

    @Override
    public Future<JsonArray> taskHistory(final ProcessInstance instance) {
        final HistoryService service = WfPin.camundaHistory();
        final HistoricProcessInstanceQuery query = service.createHistoricProcessInstanceQuery()
            .finished()
            .processInstanceId(instance.getId());
        final List<HistoricProcessInstance> instances = query.list();
        // Set<String>
        final Set<String> activitySet = new HashSet<>();
        instances.forEach(each -> activitySet.add(each.getStartActivityId()));
        return this.start(instance.getProcessDefinitionId()).compose(startEvent -> {
            activitySet.add(startEvent.getId());
            return Ux.future(Ut.toJArray(activitySet));
        });
    }
}
