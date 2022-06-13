package io.vertx.tp.workflow.atom.runtime;

import io.vertx.core.Future;
import io.vertx.tp.error._409InValidInstanceException;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.camunda.Io;
import io.vertx.up.experiment.specification.KFlow;
import io.vertx.up.uca.sectio.AspectConfig;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WTransition {

    /*
     * Workflow Definition Level
     * 1. ProcessDefinition reference
     * 2. ConcurrentMap<String, WMove> for Transition Only
     */
    private final WTransitionDefine define;

    private final ProcessInstance instance;

    private transient Task from;
    private transient Task to;
    private transient WMove move;

    private WTransition(final KFlow workflow, final ConcurrentMap<String, WMove> move) {
        this.define = new WTransitionDefine(workflow, move);
        final Io<Void> io = Io.io();
        this.instance = io.inInstance(workflow.instanceId());
    }

    public static WTransition create(final WRequest request, final ConcurrentMap<String, WMove> move) {
        final KFlow workflow = request.workflow();
        return new WTransition(workflow, move);
    }

    public WTransition from(final Task task) {
        this.from = task;
        return this;
    }

    @Deprecated
    public WTransition bind(final WMove move) {
        this.move = move;
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

    public ProcessInstance instance() {
        return this.instance;
    }

    public ProcessDefinition definition() {
        return this.define.definition();
    }

    // --------------------- Move Rule Processing ------------------

    @Deprecated
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

    // --------------------- WTransition Action for Task Data ------------------
    public Future<WTransition> start() {
        final KFlow flow = this.define.workflow();
        final String taskId = flow.taskId();
        if (Ut.isNil(taskId)) {
            /*
             * The instance has not bee started, the `WMove` should be calculated by
             * Start event instead of taskId directly, it means that after current process
             * 1) The from = null ( Task )
             * 2) The move = value ( Not be null, at least Empty )
             */
            final ProcessDefinition definition = this.definition();
            Wf.Log.infoTransition(this.getClass(),
                "Flow Not Started, rule fetched by definition = {0}", definition.getId());
            final Io<StartEvent> io = Io.ioEventStart();
            return io.child(definition.getId()).compose(event -> {
                // e.start ( StartEvent )
                this.move = this.define.rule(event.getId());
                return Ux.future(this);
            });
        } else {
            /*
             * Ths instance has been started, it means that `WMove` should be calculated by
             * task definition key instead of ( StartEvent ), after current process
             * 1) The from = value
             * 2) The move = value
             */
            Objects.requireNonNull(this.instance);
            final Io<Task> ioTask = Io.ioTask();
            return ioTask.run(flow.taskId()).compose(task -> {
                this.from(task);
                if (Objects.isNull(task)) {
                    return Ux.thenError(_409InValidInstanceException.class, this.getClass(), this.instance.getId());
                } else {
                    // Task Definition Key ( e.xxx )
                    this.move = this.define.rule(this.from.getTaskDefinitionKey());
                    return Ux.future(this);
                }
            });
        }
    }

    public AspectConfig configAop() {
        return Objects.isNull(this.move) ? AspectConfig.create() : this.move.configAop();
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

class WTransitionDefine {
    private final ProcessDefinition definition;

    private final KFlow workflow;

    private final ConcurrentMap<String, WMove> move = new ConcurrentHashMap<>();

    WTransitionDefine(final KFlow workflow, final ConcurrentMap<String, WMove> move) {
        this.workflow = workflow;
        // Io<Void> io when create the new Transaction
        final Io<Void> io = Io.io();
        /*
         * ProcessDefinition
         * ProcessInstance
         */
        this.definition = io.inProcess(workflow.definitionId());
        if (Objects.nonNull(move)) {
            this.move.clear();
            this.move.putAll(move);
        }
    }

    ProcessDefinition definition() {
        return this.definition;
    }

    KFlow workflow() {
        return this.workflow;
    }

    WMove rule(final String node) {
        return this.move.getOrDefault(node, WMove.empty());
    }
}
