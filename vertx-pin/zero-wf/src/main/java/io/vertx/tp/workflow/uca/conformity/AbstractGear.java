package io.vertx.tp.workflow.uca.conformity;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.PassWay;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.runtime.WTask;
import io.vertx.tp.workflow.uca.camunda.Io;
import io.vertx.up.atom.Kv;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

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

    protected WTodo todoGenerate(final JsonObject parameters) {

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

    protected void todoComplete(final WTodo todo, final Task task,
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
}
