package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.tp.workflow.atom.WInstance;
import io.vertx.tp.workflow.atom.WMove;
import io.vertx.tp.workflow.atom.WMoveRule;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class KitTodo {
    /*
     * Close current
     * {
     *      "status": "FINISHED",
     *      "finishedAt": "",
     *      "finishedBy": "",
     *      "traceEnd": "Depend on instance",
     *      "active": true
     * }
     */
    static JsonObject inputClose(final JsonObject params, final WInstance wInstance) {
        final JsonObject updatedData = params.copy();
        updatedData.put(KName.STATUS, TodoStatus.FINISHED);
        final String user = params.getString(KName.UPDATED_BY);
        updatedData.put(KName.Flow.Auditor.FINISHED_AT, Instant.now());
        updatedData.put(KName.Flow.Auditor.FINISHED_BY, user);
        // updatedAt / updatedBy contain values
        updatedData.put(KName.ACTIVE, Boolean.TRUE);

        if (wInstance.isEnd()) {
            updatedData.put(KName.Flow.TRACE_END, Boolean.TRUE);
        }
        return updatedData;
    }

    /*
     * Modification
     * {
     *      "traceEnd": true
     *      "traceExtra":{
     *          "history": "ADD"
     *      },
     *      "status": "CANCELED",
     *      "finishedAt": "",
     *      "finishedBy": ""
     * }
     */
    static JsonObject inputCancel(final JsonObject params, final WInstance wInstance, final Set<String> historySet) {
        final JsonObject todoData = params.copy();
        /*
         * History building
         */
        final Set<String> traceSet = new HashSet<>(historySet);
        final Task task = wInstance.task();
        if (Objects.nonNull(task)) {
            traceSet.add(task.getTaskDefinitionKey());
        }
        /*
         * Todo Processing（Record Keep）
         */
        {
            final JsonObject history = new JsonObject();
            history.put(KName.HISTORY, Ut.toJArray(traceSet));
            todoData.put(KName.Flow.TRACE_EXTRA, history.encode());

            final String user = todoData.getString(KName.UPDATED_BY);
            todoData.put(KName.STATUS, TodoStatus.CANCELED.name());
            todoData.put(KName.Flow.Auditor.FINISHED_AT, Instant.now());
            todoData.put(KName.Flow.Auditor.FINISHED_BY, user);
            todoData.put(KName.Flow.TRACE_END, Boolean.TRUE);
        }
        return todoData;
    }

    /*
     * Next
     * {
     *      "key": "New generation",
     *      "serial": "Re-calculation",
     *      "code": "synced with serial",
     *      "traceOrder": "Add 1 on original",
     *
     *      "traceId": "Trace Id",
     *      "traceTaskId": "Trace Task Id ( Next )",
     *      "status":  "PENDING",
     *
     *      "finishedBy": null,
     *      "finishedAt": null,
     *      "createdBy": "Original created by",
     *      "createdAt": Now,
     *      "updatedBy": Current User
     *      "updatedAt": Now
     * }
     * Extra combine
     * WMove todo part such as
     * {
     *      "status": "DRAFT",
     *      "toUser": null
     * }
     */
    static WTodo inputNext(final WTodo todo, final WInstance wInstance, final Task nextTask) {
        // Todo New
        final JsonObject newJson = Ux.toJson(todo);
        WTodo entity = Ux.fromJson(newJson, WTodo.class);
        {
            // key remove
            entity.setKey(UUID.randomUUID().toString());
            // major ticket number
            final String serialPrev = todo.getSerial();
            final String serial = serialPrev.substring(0, serialPrev.lastIndexOf('-'));
            entity.setTraceOrder(todo.getTraceOrder() + 1);
            entity.setSerial(serial + "-" + entity.getTraceOrder());
            entity.setCode(entity.getSerial());
        }
        {
            entity.setTraceId(nextTask.getProcessInstanceId());
            entity.setTraceTaskId(nextTask.getId());
            entity.setStatus(TodoStatus.PENDING.name());           // Force Pending
        }
        {
            // Auditor Processing
            entity.setFinishedAt(null);
            entity.setFinishedBy(null);

            entity.setCreatedAt(LocalDateTime.now());
            entity.setCreatedBy(todo.getCreatedBy());

            entity.setUpdatedAt(LocalDateTime.now());
            entity.setUpdatedBy(todo.getUpdatedBy());
        }
        final WMove move = wInstance.move();
        final WMoveRule rule = move.ruleFind();
        if (Objects.nonNull(rule)) {
            final JsonObject todoUpdate = rule.getTodo();
            entity = Ux.updateT(entity, todoUpdate);
        }
        return entity;
    }

    Future<WTodo> updateAsync(final JsonObject params) {
        // Todo Updating
        final UxJooq jooq = Ux.Jooq.on(WTodoDao.class);
        final String key = params.getString(KName.KEY);
        return jooq.<WTodo>fetchByIdAsync(key).compose(query -> {
            final WTodo updated = Ux.updateT(query, params);
            return jooq.updateAsync(updated);
        });
    }

    Future<WTodo> insertAsync(final JsonObject params, final ConfigTodo config,
                              final ProcessInstance instance) {
        // Todo Build
        final UxJooq jooq = Ux.Jooq.on(WTodoDao.class);
        return config.generate(params.getString(KName.KEY)).compose(entity -> {
            // Order Re-Calculate
            if (Objects.isNull(entity.getTraceOrder())) {
                entity.setTraceOrder(1);
            }
            entity.setSerial(entity.getSerial() + "-" + entity.getTraceOrder());
            // Code Synced with Serial
            if (Objects.isNull(entity.getCode())) {
                entity.setCode(entity.getSerial());
            }
            // Owner is as created todo here.d
            entity.setOwner(entity.getCreatedBy());
            Objects.requireNonNull(entity.getKey());
            return this.traceAsync(entity, instance)
                .compose(jooq::insertAsync);
        });
    }

    /*
     *  Connect WTodo and ProcessInstance
     *  1. instance = true
     *  2. traceId = instanceId
     *  3. traceTaskId = taskId
     */
    private Future<WTodo> traceAsync(final WTodo todo, final ProcessInstance instance) {
        final EventOn event = EventOn.get();
        return event.taskActive(instance).compose(task -> {
            todo.setInstance(Boolean.TRUE);                   // Camunda Engine
            todo.setTraceId(instance.getId());                // Trace ID Related
            todo.setTraceTaskId(task.getId());                // Trace Task ID
            return Ux.future(todo);
        });
    }
}
