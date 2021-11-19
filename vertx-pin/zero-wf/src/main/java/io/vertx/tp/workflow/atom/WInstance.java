package io.vertx.tp.workflow.atom;

import io.vertx.core.Future;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.tp.workflow.uca.runner.IsOn;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WInstance {
    private transient ProcessInstance instance;
    private transient Task task;
    private transient WMove move;

    private WInstance() {
    }

    public static WInstance create() {
        return new WInstance();
    }

    public WInstance bind(final ProcessInstance instance) {
        this.instance = instance;
        return this;
    }

    public WInstance bind(final Task task) {
        this.task = task;
        return this;
    }

    public WInstance bind(final WMove move) {
        this.move = move;
        return this;
    }

    public Future<ProcessInstance> future(final ProcessInstance instance) {
        this.instance = instance;
        return Ux.future(instance);
    }

    public Future<Task> future(final Task task) {
        this.task = task;
        return Ux.future(task);
    }

    public Future<WMove> future(final WMove move) {
        this.move = move;
        return Ux.future(move);
    }

    public Future<WInstance> future() {
        return Ux.future(this);
    }

    public ProcessInstance instance() {
        return this.instance;
    }

    public Task task() {
        return this.task;
    }

    public WMove move() {
        return this.move;
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
