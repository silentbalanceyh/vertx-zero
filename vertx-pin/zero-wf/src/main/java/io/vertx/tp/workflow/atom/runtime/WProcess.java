package io.vertx.tp.workflow.atom.runtime;

import io.vertx.core.Future;
import io.vertx.tp.workflow.uca.camunda.Io;
import io.vertx.tp.workflow.uca.conformity.Gear;
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
    private transient Task taskNext;
    private transient WMove move;

    private transient Gear scatter;

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
        this.scatter = Gear.instance(move);
        return this;
    }

    public Future<WProcess> future() {
        return Ux.future(this);
    }


    // --------------------- Task & Instance ------------------

    public Future<ProcessInstance> instance(final ProcessInstance instance) {
        this.instance = instance;
        return Ux.future(instance);
    }

    /*
     * This task is for active task extracting, the active task should be following
     *
     * 1) When the process is not moved, active task is current one
     * 2) After current process moved, the active task may be the next active task instead
     */
    @Deprecated
    public Future<Task> taskActive() {
        final Io<Task> ioTask = Io.ioTask();
        return ioTask.child(this.instance.getId()).compose(taskThen -> {
            if (Objects.nonNull(this.task) && Objects.nonNull(taskThen)) {
                /*
                 * Task & TaskThen are different, it means that there exist
                 * workflow moving operation.
                 * if `TaskThen` is null, it means that workflow has been finished
                 */
                if (!this.task.getId().equals(taskThen.getId())) {
                    this.taskNext = taskThen;
                }
            }
            return Ux.future(this.taskNext);
        });
    }

    public Task task() {
        return this.task;
    }

    public Task taskNext() {
        return this.taskNext;
    }

    public ProcessInstance instance() {
        return this.instance;
    }

    // --------------------- Move Rule Processing ------------------
    public WMoveRule ruleFind() {
        /*
         * Fix: java.lang.NullPointerException
	        at java.base/java.util.Objects.requireNonNull(Objects.java:221)
         */
        if (Objects.nonNull(this.move)) {
            return this.move.ruleFind();
        } else {
            return null;
        }
    }

    public WMove rule() {
        return this.move;
    }

    // --------------------- Process Status ------------------
    public boolean isStart() {
        return Objects.nonNull(this.instance);
    }

    public boolean isEnd() {
        return this.instance.isEnded();
    }

    public boolean isContinue(final Task task) {
        final boolean isEnd = this.isEnd();
        if (isEnd) {
            return Boolean.FALSE;
        }
        if (Objects.isNull(task)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
