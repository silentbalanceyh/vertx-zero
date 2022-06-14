package io.vertx.tp.workflow.uca.conformity;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.PassWay;
import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.runtime.WTask;
import io.vertx.tp.workflow.uca.camunda.Io;
import io.vertx.up.atom.Kv;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

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

    protected PassWay type;

    protected AbstractGear(final PassWay type) {
        this.type = type;
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
        return io.children(instance.getId()).compose(taskList -> {
            final WTask wTask = new WTask(this.type);
            taskList.forEach(wTask::add);
            return Ux.future(wTask);
        });
    }

    protected void todoNorm(final WTodo todo, final WTicket ticket, final Task task) {
        /*
         * null value when processed
         * 「Related」
         *  - traceId
         *  - traceOrder
         *  - parentId
         *
         * 「Camunda」
         *  - taskId
         *  - taskKey
         *
         * 「Flow」
         *  - assignedBy
         *  - assignedAt
         *  - acceptedBy
         *  - acceptedAt
         *  - finishedBy
         *  - finishedAt
         *  - comment
         *  - commentApproval
         *  - commentReject
         *
         * 「Future」
         *  - metadata
         *  - modelCategory
         *  - activityId
         */
        todo.setTraceId(ticket.getKey());
        todo.setCode(ticket.getCode() + "-" + Ut.fromAdjust(todo.getTraceOrder(), 2));
        todo.setSerial(ticket.getSerial() + "-" + Ut.fromAdjust(todo.getTraceOrder(), 2));


        /*
         *  Connect WTodo and ProcessInstance
         *  1. taskId = Task, getId
         *  2. taskKey = Task, getTaskDefinitionKey
         */
        // Camunda Engine
        todo.setTaskId(task.getId());
        todo.setTaskKey(task.getTaskDefinitionKey());        // Task Key/Id
    }
}
