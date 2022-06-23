package io.vertx.tp.workflow.uca.conformity;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.PassWay;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.runtime.WTask;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.camunda.Io;
import io.vertx.up.atom.Kv;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

interface GearSupplier {
    ConcurrentMap<PassWay, Kv<String, Supplier<Gear>>> SUPPLIERS = new ConcurrentHashMap<>() {
        {
            this.put(PassWay.Standard, Kv.create(GearStandard.class.getName(), GearStandard::new));
            this.put(PassWay.Fork, Kv.create(GearForkJoin.class.getName(), GearForkJoin::new));
            this.put(PassWay.Multi, Kv.create(GearMulti.class.getName(), GearMulti::new));
            this.put(PassWay.Grid, Kv.create(GearGrid.class.getName(), GearGrid::new));
        }
    };
}

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractGear implements Gear {

    protected final JsonObject configuration = new JsonObject();
    protected PassWay type;

    protected AbstractGear(final PassWay type) {
        this.type = type;
    }

    @Override
    public Gear configuration(final JsonObject config) {
        if (Ut.notNil(config)) {
            this.configuration.mergeIn(config, true);
        }
        return this;
    }

    /*
     * Node -> Node ( Single )
     * The next task is only one
     */
    @Override
    public Future<WTask> taskAsync(final ProcessInstance instance, final Task from) {
        /*
         * One Size
         */
        final Io<Task> io = Io.ioTask();
        return io.children(instance.getId()).compose(taskList -> {
            final WTask wTask = new WTask(this.type);
            if (Objects.isNull(from)) {
                // Start Point Here
                taskList.forEach(wTask::add);
            } else {
                // Search the next task and find into `taskList` to determine the running
                Wf.taskNext(from, taskList).forEach(wTask::add);
            }
            return Ux.future(wTask);
        });
    }

    protected WTodo todoGenerate(final JsonObject parameters, final WTicket ticket, final Task task, final WTodo todo) {
        // 1. Generate new WTodo
        final WTodo generated = this.todoGenerate(parameters);

        // 2. Set relation between WTodo and Camunda Task
        this.todoTask(generated, task, todo.getTraceId());

        // 3. traceOrder = original + 1 and generate serial/code
        generated.setTraceOrder(todo.getTraceOrder() + 1);

        // 4. Set todo auditor information
        this.todoAuditor(generated, todo);
        return generated;
    }

    protected WTodo todoStart(final JsonObject parameters, final WTicket ticket, final Task task) {
        // 0. Keep the same acceptedBy / toUser value and do nothing
        // 1. Deserialize new WTodo
        final WTodo todo = Ux.fromJson(parameters, WTodo.class);

        // 2. Set relation between WTodo and Camunda Task
        this.todoTask(todo, task, ticket.getKey());

        // 3. traceOrder = 1 and generate serial/code
        todo.setTraceOrder(1);

        return todo;
    }

    /*
     * Start Serial generation here
     */
    protected void todoSerial(final WTodo todo, final WTicket ticket, final Integer sequence) {
        // Based On SerialFork
        final String serialFork = todo.getSerialFork();
        final StringBuilder serialBuf = new StringBuilder();
        serialBuf.append(ticket.getSerial()).append(Strings.DASH);
        serialBuf.append(Ut.fromAdjust(todo.getTraceOrder(), 2));
        if (Ut.isNil(serialFork)) {
            serialBuf.append(Values.ZERO);
            if (Objects.nonNull(sequence)) {
                // XXX-010-01
                serialBuf.append(Strings.DASH).append(Ut.fromAdjust(sequence, 2));
            } // else = XXX-010
        } else {
            if (Objects.isNull(sequence)) {
                // XXX-011
                serialBuf.append(serialFork);
            } else {
                // XXX-011-01
                serialBuf.append(serialFork).append(Strings.DASH).append(Ut.fromAdjust(sequence, 2));
            }
        }

        todo.setCode(serialBuf.toString());
        todo.setSerial(todo.getCode());
    }

    // --------------- Private Method ------------------
    private void todoTask(final WTodo todo, final Task task,
                          final String traceId) {
        todo.setTraceId(traceId);
        /*
         *  Connect WTodo and ProcessInstance
         *  1. taskId = Task, getId
         *  2. taskKey = Task, getTaskDefinitionKey
         */
        // Camunda Engine
        todo.setTaskId(task.getId());
        todo.setTaskKey(task.getTaskDefinitionKey());        // Task Key/Id
    }

    private WTodo todoGenerate(final JsonObject parameters) {

        final WTodo generated = Ux.fromJson(parameters, WTodo.class);
        // Key Remove ( Comment Clear )
        generated.setKey(UUID.randomUUID().toString());

        // Comment Clear
        generated.setComment(null);
        generated.setCommentApproval(null);
        generated.setCommentReject(null);

        // Status Force to PENDING
        generated.setStatus(TodoStatus.PENDING.name());

        // Auditor Processing
        generated.setFinishedAt(null);
        generated.setFinishedBy(null);

        // Escalate Null
        generated.setEscalate(null);
        generated.setEscalateData(null);
        return generated;
    }

    private void todoAuditor(final WTodo todo, final WTodo input) {
        todo.setCreatedAt(LocalDateTime.now());
        todo.setCreatedBy(input.getUpdatedBy());
        todo.setUpdatedAt(LocalDateTime.now());
        todo.setUpdatedBy(input.getUpdatedBy());
    }
}
