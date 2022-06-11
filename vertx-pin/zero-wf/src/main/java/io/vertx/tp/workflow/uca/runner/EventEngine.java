package io.vertx.tp.workflow.uca.runner;

import io.vertx.core.Future;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class EventEngine implements EventOn {

    private final transient KitTask tasker;
    private final transient KitHistory history;

    public EventEngine() {
        this.tasker = new KitTask();
        this.history = new KitHistory();
    }

    @Override
    public Future<Task> taskOldActive(final ProcessInstance instance) {
        return Ux.future(this.tasker.byOldInstanceId(instance.getId()));
    }

    @Override
    public Future<Task> taskOldActive(final String taskId) {
        return Ux.future(this.tasker.byTaskId(taskId));
    }

    @Override
    public Future<Task> taskOldSmart(final ProcessInstance instance, final String taskId) {
        return Objects.isNull(taskId) ? this.taskOldActive(instance) : this.taskOldActive(taskId);
    }

    @Override
    public Future<Set<String>> taskHistory(final ProcessInstance instance) {
        return Ux.future(this.history.activities(instance.getId()));
    }

    @Override
    public Future<Set<String>> taskHistory(final HistoricProcessInstance instance) {
        return Ux.future(this.history.activities(instance.getId()));
    }

    @Override
    public List<Task> taskActive(final ProcessInstance instance) {
        Objects.requireNonNull(instance);
        return this.tasker.byInstanceId(instance.getId());
    }

    @Override
    public Task taskActive(final String taskId) {
        Objects.requireNonNull(taskId);
        return this.tasker.byTaskId(taskId);
    }
}
