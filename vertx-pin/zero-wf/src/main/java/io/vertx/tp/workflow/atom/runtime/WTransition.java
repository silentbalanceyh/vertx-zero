package io.vertx.tp.workflow.atom.runtime;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.PassWay;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._409InValidInstanceException;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.camunda.Io;
import io.vertx.tp.workflow.uca.conformity.Gear;
import io.vertx.up.experiment.specification.KFlow;
import io.vertx.up.uca.sectio.AspectConfig;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

import java.util.List;
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

    private final JsonObject moveData = new JsonObject();
    private ProcessInstance instance;
    private Task from;

    private WTask to;
    private WMove move;

    private PassWay way;

    private WTransition(final KFlow workflow, final ConcurrentMap<String, WMove> move) {
        this.define = new WTransitionDefine(workflow, move);
        final Io<Void> io = Io.io();
        /*
         * Fix Issue: 「Camunda Exception」
         * org.camunda.bpm.engine.exception.NullValueException: Process instance id is null
         */
        if (Ut.notNil(workflow.instanceId())) {
            this.instance = io.inInstance(workflow.instanceId());
        }
    }

    public static WTransition create(final WRequest request, final ConcurrentMap<String, WMove> move) {
        final KFlow workflow = request.workflow();
        final WTransition transition = new WTransition(workflow, move);
        /* Private Scope for PassWay of request */
        transition.way = Wf.inGateway(request.request());
        return transition;
    }

    // --------------------- From/To Task ------------------
    public Task from() {
        return this.from;
    }

    public WTask to() {
        return this.to;
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
        return null;
    }

    // --------------------- High Level Configuration ------------------
    /*
     * 1. instance()        -> Running ProcessInstance
     * 2. definition()      -> Running ProcessDefinition
     *
     * By WMove
     * 3. aspect()          -> Running Aspect based on `WMove`
     */
    public ProcessInstance instance() {
        return this.instance;
    }

    public ProcessDefinition definition() {
        return this.define.definition();
    }

    public AspectConfig aspect() {
        return Objects.isNull(this.move) ? AspectConfig.create() : this.move.configAop();
    }
    // --------------------- Move Rule Processing ------------------

    public JsonObject rule(final JsonObject requestJ) {
        if (Objects.isNull(this.move)) {
            return new JsonObject();
        }
        final JsonObject parsed = this.move.inputMovement(requestJ);
        this.moveData.clear();
        this.moveData.mergeIn(parsed, true);
        return this.moveData.copy();
    }

    public JsonObject rule(final JsonObject requestJ, final boolean isRecord) {
        final WRule rule = this.move.inputTransfer(this.moveData.copy());
        /* Modify the input JsonObject of requestJ */
        return null;
    }

    @Deprecated
    public WRule rule() {
        return this.move.inputTransfer(this.moveData.copy());
    }

    public PassWay vague() {
        /*
         * java.lang.NullPointerException
         * 	at io.vertx.tp.workflow.atom.runtime.WTransition.vague(WTransition.java:127)
         */
        return Objects.isNull(this.to) ? null : this.to.vague();
    }

    // --------------------- WTransition Action for Task Data ------------------

    public Future<WTransition> end(final ProcessInstance instance) {
        Objects.requireNonNull(this.move);
        this.instance = instance;
        final Gear gear = this.move.inputGear(this.way);
        return gear.taskAsync(instance).compose(wTask -> {
            /*
             * 0 == size, End
             * 1 == size, Standard
             * 1 <  size, Fork/Join, Multi
             */
            this.to = wTask;
            return Ux.future(this);
        });
    }

    public Future<List<WTodo>> end(final JsonObject parameters, final WTicket ticket) {
        Objects.requireNonNull(this.move);
        final Gear gear = this.move.inputGear(this.way);
        return gear.todoAsync(parameters, ticket, this.to);
    }

    public Future<WTransition> start() {
        // Here the move means that the transition has been bind to `from`
        if (Objects.nonNull(this.move)) {
            return Ux.future(this);
        }
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
                Objects.requireNonNull(this.move);
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
                if (Objects.isNull(task)) {
                    return Ux.thenError(_409InValidInstanceException.class, this.getClass(), this.instance.getId());
                } else {
                    this.from = task;
                    // Task Definition Key ( e.xxx )
                    this.move = this.define.rule(task.getTaskDefinitionKey());
                    Objects.requireNonNull(this.move);
                    return Ux.future(this);
                }
            });
        }
    }

    // --------------------- WTransition Checking for Status ------------------
    public boolean isStarted() {
        return Objects.nonNull(this.instance);
    }

    public boolean isEnded() {
        Objects.requireNonNull(this.instance);
        return this.instance.isEnded();
    }

    public boolean isRunning() {
        final boolean isEnd = this.isEnded();
        if (isEnd) {
            return Boolean.FALSE;
        }
        if (Objects.isNull(this.to)) {
            return Boolean.FALSE;
        }
        if (this.to.isEmpty()) {
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
