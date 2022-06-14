package io.vertx.tp.workflow.uca.conformity;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.PassWay;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.runtime.WTask;
import io.vertx.tp.workflow.uca.camunda.Io;
import io.vertx.up.unity.Ux;
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
    public Future<List<WTodo>> createAsync(final JsonObject parameters, final WTicket ticket, final WTask wTask) {
        final Task task = wTask.standard();
        if (Objects.isNull(task)) {
            return Ux.futureL();
        }
        // `key` - todoKey is inner parameters
        final WTodo todo = Ux.fromJson(parameters, WTodo.class);

        // Normalize for common
        this.todoNorm(todo, ticket, task);

        // traceOrder = 1 when create
        todo.setTraceOrder(1);
        return Ux.futureL(todo);
    }
}
