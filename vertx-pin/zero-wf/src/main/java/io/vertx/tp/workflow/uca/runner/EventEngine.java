package io.vertx.tp.workflow.uca.runner;

import io.vertx.core.Future;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class EventEngine implements EventOn {

    private final transient KitTask tasker;

    public EventEngine() {
        this.tasker = new KitTask();
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
}
