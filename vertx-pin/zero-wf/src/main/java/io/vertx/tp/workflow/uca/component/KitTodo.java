package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class KitTodo {

    Future<WTodo> updateAsync(final JsonObject params) {
        // Todo Updating
        final UxJooq jooq = Ux.Jooq.on(WTodoDao.class);
        final String key = params.getString(KName.KEY);
        return jooq.<WTodo>fetchByIdAsync(key).compose(query -> {
            final WTodo updated = Ux.combineT(query, params);
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

    Future<WTodo> nextAsync(final WTodo todo, final Task task) {
        // Todo New
        final JsonObject newJson = Ux.toJson(todo);
        final WTodo entity = Ux.fromJson(newJson, WTodo.class);
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
            entity.setTraceId(task.getProcessInstanceId());
            entity.setTraceTaskId(task.getId());
            entity.setStatus(TodoStatus.PENDING.name());           // Force Pending
        }
        {
            // Auditor Processing
            entity.setFinishedAt(null);
            entity.setFinishedBy(null);

            entity.setCreatedAt(LocalDateTime.now());
            entity.setCreatedBy(todo.getUpdatedBy());

            entity.setUpdatedAt(LocalDateTime.now());
            entity.setUpdatedBy(todo.getUpdatedBy());
        }

        final UxJooq jooq = Ux.Jooq.on(WTodoDao.class);
        return jooq.insertAsync(entity);
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
