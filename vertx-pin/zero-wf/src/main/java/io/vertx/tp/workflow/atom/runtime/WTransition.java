package io.vertx.tp.workflow.atom.runtime;

import io.vertx.core.Future;
import io.vertx.tp.workflow.uca.camunda.Io;
import io.vertx.tp.workflow.uca.conformity.Gear;
import io.vertx.up.experiment.specification.KFlow;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WTransition {
    private final transient ProcessDefinition definition;
    private final transient ProcessInstance instance;
    private transient Task from;
    private transient Task to;
    private transient WMove move;
    private transient Gear scatter;

    private WTransition(final KFlow workflow) {
        // Io<Void> io when create the new Transaction
        final Io<Void> io = Io.io();
        /*
         * ProcessDefinition
         * ProcessInstance
         */
        this.definition = io.inProcess(workflow.definitionId());
        this.instance = io.inInstance(workflow.instanceId());
    }

    public static WTransition create(final WRequest request) {
        final KFlow workflow = request.workflow();
        return new WTransition(workflow);
    }

    public WTransition from(final Task task) {
        this.from = task;
        return this;
    }

    public WTransition bind(final WMove move) {
        this.move = move;
        this.scatter = Gear.instance(move);
        return this;
    }


    // --------------------- Task & Instance ------------------

    /*
     * This from is for active from extracting, the active from should be following
     *
     * 1) When the process is not moved, active from is current one
     * 2) After current process moved, the active from may be the next active from instead
     */
    @Deprecated
    public Future<Task> taskActive() {
        final Io<Task> ioTask = Io.ioTask();
        return ioTask.child(this.instance.getId()).compose(taskThen -> {
            if (Objects.nonNull(this.from) && Objects.nonNull(taskThen)) {
                /*
                 * Task & TaskThen are different, it means that there exist
                 * workflow moving operation.
                 * if `TaskThen` is null, it means that workflow has been finished
                 */
                if (!this.from.getId().equals(taskThen.getId())) {
                    this.to = taskThen;
                }
            }
            return Ux.future(this.to);
        });
    }

    public Task from() {
        return this.from;
    }

    public Task to() {
        return this.to;
    }

    public ProcessInstance flowInstance() {
        return this.instance;
    }

    public ProcessDefinition flowDefinition() {
        return this.definition;
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

    // --------------------- WTransition Checking for Status ------------------
    public boolean isStarted() {
        return Objects.nonNull(this.instance);
    }

    public boolean isEnded() {
        Objects.requireNonNull(this.instance);
        return this.instance.isEnded();
    }

    public boolean isRunning(final Task task) {
        final boolean isEnd = this.isEnded();
        if (isEnd) {
            return Boolean.FALSE;
        }
        if (Objects.isNull(task)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}

class WTransitionInternal {

}
