package io.vertx.tp.workflow.atom;

import io.vertx.core.Future;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.tp.workflow.uca.runner.IsOn;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WProcess {
    private transient ProcessInstance instance;
    private transient Task task;
    private transient WMove move;

    private WProcess() {
    }

    public static WProcess create() {
        return new WProcess();
    }

    public WProcess bind(final ProcessInstance instance) {
        this.instance = instance;
        return this;
    }

    public WProcess bind(final Task task) {
        this.task = task;
        return this;
    }

    public WProcess bind(final WMove move) {
        this.move = move;
        return this;
    }

    public Future<ProcessInstance> future(final ProcessInstance instance) {
        this.instance = instance;
        if (Objects.isNull(this.task) && Objects.nonNull(instance)) {


            /*
             * Get the first task of Active after process
             * instance started. Here after process instance started,
             * the workflow engine should set the task instance to
             * WProcess here
             */
            final EventOn event = EventOn.get();
            return event.taskActive(instance).compose(task -> {


                /*
                 * Here the WProcess should set `task` instance
                 * The task object must be located after workflow started.
                 */
                this.task = task;
                return Ux.future(instance);
            });
        } else {
            return Ux.future(instance);
        }
    }

    public Future<Task> future(final Task task) {
        this.task = task;
        return Ux.future(task);
    }

    public Future<WMove> future(final WMove move) {
        this.move = move;
        return Ux.future(move);
    }

    public Future<WProcess> future() {
        return Ux.future(this);
    }

    public ProcessInstance instance() {
        return this.instance;
    }

    public Task task() {
        return this.task;
    }

    public WMoveRule rule() {
        return Objects.requireNonNull(this.move).ruleFind();
    }

    public boolean isEnd() {
        final IsOn is = IsOn.get();
        return is.isEnd(this.instance);
    }

    public boolean isContinue() {
        return !this.isEnd();
    }

    public Future<Task> next() {
        final EventOn event = EventOn.get();
        return event.taskActive(this.instance);
    }
}
