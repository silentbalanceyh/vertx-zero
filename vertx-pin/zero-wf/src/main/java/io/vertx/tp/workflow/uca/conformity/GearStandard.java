package io.vertx.tp.workflow.uca.conformity;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.PassWay;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.runtime.WTask;
import io.vertx.tp.workflow.uca.camunda.Io;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class GearStandard extends AbstractGear {

    public GearStandard() {
        super(PassWay.Standard);
    }

    /*
     * Node -> Node ( Single )
     * The next task is only one
     */
    @Override
    public Future<WTask> taskAsync(final ProcessInstance instance) {
        /*
         * One Size
         */
        final Io<Task> io = Io.ioTask();
        return io.child(instance.getId()).compose(task -> {
            final WTask wTask = new WTask(PassWay.Standard);
            wTask.add(task);
            return Ux.future(wTask);
        });
    }

    @Override
    public Future<List<WTodo>> todoAsync(final JsonObject parameters, final WTask wTask, final WTicket ticket) {
        final Task task = wTask.standard();
        if (Objects.isNull(task)) {
            return Ux.futureL();
        }
        // 0. Keep the same acceptedBy / toUser value and do nothing
        // 1. Deserialize new WTodo
        final WTodo todo = Ux.fromJson(parameters, WTodo.class);

        // 2. Set relation between WTodo and Camunda Task
        this.todoTask(todo, task, ticket.getKey());

        // 3. traceOrder = 1 and generate serial/code
        todo.setTraceOrder(1);
        this.todoSerial(todo, ticket);

        return Ux.futureL(todo);
    }

    @Override
    public Future<List<WTodo>> todoAsync(final JsonObject parameters, final WTask wTask, final WTicket ticket,
                                         final WTodo todo) {
        final Task task = wTask.standard();
        if (Objects.isNull(task)) {
            return Ux.futureL();
        }

        // 0. Pre-Assignment: toUser -> acceptedBy
        this.todoAssign(parameters);

        // 1. Generate new WTodo
        final WTodo generated = this.todoGenerate(parameters);

        // 2. Set relation between WTodo and Camunda Task
        this.todoTask(generated, task, todo.getTraceId());

        // 3. traceOrder = original + 1 and generate serial/code
        generated.setTraceOrder(todo.getTraceOrder() + 1);
        this.todoSerial(generated, ticket);

        // 4. Set todo auditor information
        this.todoAuditor(generated, todo);

        return Ux.futureL(generated);
    }


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

    private void todoAssign(final JsonObject parameters) {
        // toUser -> acceptedBy
        final String toUser = parameters.getString(KName.Flow.Auditor.TO_USER);
        parameters.put(KName.Flow.Auditor.ACCEPTED_BY, toUser);
        parameters.remove(KName.Flow.Auditor.TO_USER);
    }

    /*
     *  The format:  <Ticket Serial>-<traceOrder>
     */
    private void todoSerial(final WTodo todo, final WTicket ticket) {
        final String serial = ticket.getCode() + "-" + Ut.fromAdjust(todo.getTraceOrder(), 2);
        todo.setCode(serial);
        todo.setSerial(serial);
    }
}
