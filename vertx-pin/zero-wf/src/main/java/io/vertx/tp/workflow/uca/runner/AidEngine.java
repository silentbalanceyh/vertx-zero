package io.vertx.tp.workflow.uca.runner;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AidEngine implements AidOn {
    private final transient KitEvent typed;
    private final transient KitHistory history;

    AidEngine() {
        this.typed = new KitEvent();
        this.history = new KitHistory();
    }

    @Override
    public String taskType(final Task task) {
        if (Objects.isNull(task)) {
            return null;
        }
        return this.typed.eventType(task);
    }

    @Override
    public boolean isEnd(final ProcessInstance instance) {
        return Objects.nonNull(this.history.instance(instance.getId()));
    }
}
