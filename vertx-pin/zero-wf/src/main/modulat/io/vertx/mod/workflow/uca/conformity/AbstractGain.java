package io.vertx.mod.workflow.uca.conformity;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import org.camunda.bpm.engine.task.Task;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class AbstractGain implements Gain {

    protected final WTicket ticket;

    AbstractGain(final WTicket ticket) {
        Objects.requireNonNull(ticket);
        this.ticket = ticket;
    }

    protected void bridgeTask(final WTodo todo, final Task task, final String ticketKey) {
        // 2. Set relation between WTodo and Camunda Task
        {
            todo.setTraceId(ticketKey);
            // Camunda Engine
            /*
             *  Connect WTodo and ProcessInstance
             *  1. taskId = Task, getId
             *  2. taskKey = Task, getTaskDefinitionKey
             */
            todo.setTaskId(task.getId());
            todo.setTaskKey(task.getTaskDefinitionKey());
        }
    }

    protected void bridgeAudit(final WTodo generated, final WTodo todo) {
        generated.setCreatedAt(LocalDateTime.now());
        generated.setCreatedBy(todo.getUpdatedBy());
        generated.setUpdatedAt(LocalDateTime.now());
        generated.setUpdatedBy(todo.getUpdatedBy());
    }
}
