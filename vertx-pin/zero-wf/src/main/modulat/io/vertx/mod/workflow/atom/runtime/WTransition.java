package io.vertx.mod.workflow.atom.runtime;

import cn.vertxup.workflow.cv.em.PassWay;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.horizon.uca.aop.AspectRobin;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.error._409InValidInstanceException;
import io.vertx.mod.workflow.refine.Wf;
import io.vertx.mod.workflow.uca.camunda.Io;
import io.vertx.mod.workflow.uca.conformity.Gear;
import io.vertx.up.atom.extension.KFlow;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
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

import static io.vertx.mod.workflow.refine.Wf.LOG;

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

    private ProcessInstance instance;
    private Task from;

    private WRule rule = WMove.RULE_EMPTY;

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
        if (Ut.isNotNil(workflow.instanceId())) {
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

    public AspectRobin aspect() {
        return Objects.isNull(this.move) ? AspectRobin.create() : this.move.configAop();
    }

    public PassWay vague() {
        /*
         * java.lang.NullPointerException
         * 	at io.vertx.mod.runtime.atom.workflow.WTransition.vague(WTransition.java:127)
         */
        return Objects.isNull(this.to) ? null : this.to.vague();
    }
    // --------------------- Move Rule Processing ------------------

    public JsonObject moveParameter(final WRequest request) {
        final JsonObject requestJ = request.request();
        if (Objects.isNull(this.move)) {
            return new JsonObject();
        }
        final JsonObject moveData = this.move.inputMovement(requestJ);
        this.rule = this.move.inputTransfer(moveData);
        return moveData;
    }

    public JsonObject moveTicket(final JsonObject requestJ) {
        // WTodo will be used in generation workflow
        final JsonObject todoJ = Ut.fromExpression(this.rule.getTodo(), requestJ);
        requestJ.mergeIn(todoJ, true);
        // WTicket, Extension
        requestJ.mergeIn(Ut.fromExpression(this.rule.getTicket(), requestJ), true);
        requestJ.mergeIn(Ut.fromExpression(this.rule.getExtension(), requestJ), true);

        // Record Processing
        return this.moveRecord(requestJ);
    }

    @SuppressWarnings("all")
    public JsonObject moveRecord(final JsonObject requestJ) {

        // Record Processing
        final Object recordRef = requestJ.getValue(KName.RECORD);
        if (Objects.isNull(recordRef)) {
            return requestJ;
        }
        final JsonObject recordData = Ut.fromExpression(this.rule.getRecord(), requestJ);
        if (recordRef instanceof JsonObject) {
            final JsonObject recordJ = ((JsonObject) recordRef);
            recordJ.mergeIn(recordData, true);
            requestJ.put(KName.RECORD, recordJ);
        } else if (recordRef instanceof JsonArray) {
            final JsonArray recordA = ((JsonArray) recordRef);
            Ut.itJArray(recordA).forEach(recordJ -> recordJ.mergeIn(recordData, true));
            requestJ.put(KName.RECORD, recordA);
        }
        return requestJ;
    }

    // --------------------- WTransition Action for Task Data ------------------

    /*
     * Camunda Engine Ran and after this method, the `to` variable ( WTask )
     * will be filled the value based on different Gear
     * 1) Standard
     * 2) Fork/Join
     * 3) Multi
     * 4) Grid
     *
     * For different PassWay here
     * 1. Before Camunda Engine, the PassWay will be calculated by data format.
     * 2. After Camunda Engine, the PassWay could be passed directly
     */
    public Future<WTransition> end(final ProcessInstance instance) {
        Objects.requireNonNull(this.move);
        this.instance = instance;
        final Gear gear = Gear.instance(this.way);
        gear.configuration(this.move.configWay());
        return gear.taskAsync(instance, this.from).compose(wTask -> {
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
        final Gear gear = Gear.instance(this.vague());
        gear.configuration(this.move.configWay());
        return gear.todoAsync(parameters, this.to, ticket);
    }

    public Future<List<WTodo>> end(final JsonObject parameters, final WTicket ticket, final WTodo todo) {
        Objects.requireNonNull(this.move);
        final Gear gear = Gear.instance(this.vague());
        gear.configuration(this.move.configWay());
        return gear.todoAsync(parameters, this.to, ticket, todo);
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
            LOG.Move.info(this.getClass(),
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
                    return Fn.outWeb(_409InValidInstanceException.class, this.getClass(), this.instance.getId());
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
        if (Objects.isNull(this.instance)) {
            return true;
        }
        /*
         * To Avoid Camunda Engine ProcessInstance state
         * Sync here. Here are some situations for end ProcessInstance checking
         * 1. When the instance is finished. ( There will be no ProcessInstance )
         * 2. The history instance has been created and could be find
         *    But for new version, the history instance could not determine the process finished or not
         */
        final Io<Void> io = Io.io();
        final ProcessInstance instance = io.inInstance(this.instance.getId());
        return Objects.isNull(instance);
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
