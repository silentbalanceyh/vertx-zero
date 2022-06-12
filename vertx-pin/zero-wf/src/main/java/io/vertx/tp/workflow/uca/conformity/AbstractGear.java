package io.vertx.tp.workflow.uca.conformity;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractGear implements Gear {
    @Override
    public List<WTodo> todoInitialize(final ProcessInstance instance, final WTicket ticket) {
        return null;
    }

    @Override
    public Task taskActive(final String taskId) {
        return null;
    }

    @Override
    public List<Task> taskNext(final Task task) {
        return null;
    }

    @Override
    public boolean taskClose(final String taskId, final TodoStatus status) {
        return false;
    }
}
