package io.vertx.tp.workflow.uca.runner;

import io.vertx.core.Future;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class EventEngine implements EventOn {

    private final transient KitTask tasker;
    private final transient KitHistory history;
    private final transient KitEvent typed;

    public EventEngine() {
        this.tasker = new KitTask();
        this.typed = new KitEvent();
        this.history = new KitHistory();
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
    public Future<Set<EndEvent>> endSet(final String definitionId) {
        return this.typed.endSet(definitionId);
    }

    @Override
    public Future<EndEvent> end(final String definitionId) {
        return this.typed.end(definitionId);
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
        return Ux.future(this.history.activities(instance.getId()));
    }

    @Override
    public Future<Set<String>> taskHistory(final HistoricProcessInstance instance) {
        return Ux.future(this.history.activities(instance.getId()));
    }
}
