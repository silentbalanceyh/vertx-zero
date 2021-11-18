package io.vertx.tp.workflow.uca.runner;

import io.vertx.core.Future;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.impl.BpmnModelConstants;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class EventEngine implements EventOn {

    private final transient EventTask tasker;
    private final transient EventTyped typed;

    public EventEngine() {
        this.tasker = new EventTask();
        this.typed = new EventTyped();
    }

    @Override
    public Future<Set<StartEvent>> startSet(final String definitionId) {
        return this.typed.startSet(definitionId);
    }

    @Override
    public Future<StartEvent> start(final String definitionId) {
        return this.typed.start(definitionId);
    }

    @Override
    public boolean isUserEvent(final Task task) {
        if (Objects.isNull(task)) {
            return Boolean.FALSE;
        } else {
            final String eventType = this.typed.type(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
            return BpmnModelConstants.BPMN_ELEMENT_USER_TASK.equals(eventType);
        }
    }

    @Override
    public Future<Task> taskActive(final ProcessInstance instance) {
        return Ux.future(this.tasker.byInstanceId(instance.getId()));
    }

    @Override
    public Future<Task> taskActive(final String taskId) {
        return Ux.future(this.tasker.byTaskId(taskId));
    }

    @Override
    public Future<Task> taskSmart(final ProcessInstance instance, final String taskId) {
        return Objects.isNull(taskId) ? this.taskActive(instance) : this.taskActive(taskId);
    }

    @Override
    public Future<Set<String>> taskHistory(final ProcessInstance instance) {
        return Ux.future(this.tasker.histories(instance.getId()));
    }
}
