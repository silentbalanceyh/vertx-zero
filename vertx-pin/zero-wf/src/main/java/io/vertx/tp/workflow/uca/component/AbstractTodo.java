package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractTodo extends AbstractTransfer implements Transfer {

    protected Future<WTodo> insertAsync(final JsonObject params, final ConfigTodo config,
                                        final ProcessInstance instance) {
        // Todo Build
        final UxJooq jooq = Ux.Jooq.on(WTodoDao.class);
        return config.generate(params.getString(KName.KEY)).compose(entity -> {
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

    protected Future<WTodo> updateAsync(final JsonObject params) {
        // Todo Updating
        final UxJooq jooq = Ux.Jooq.on(WTodoDao.class);
        final String key = params.getString(KName.KEY);
        return jooq.<WTodo>fetchByIdAsync(key).compose(query -> {
            final WTodo updated = Ux.combineT(query, params);
            return jooq.updateAsync(updated);
        });
    }

    /*
     *  Connect WTodo and ProcessInstance
     *  1. instance = true
     *  2. traceId = instanceId
     *  3. traceTaskId = taskId
     */
    protected Future<WTodo> traceAsync(final WTodo todo, final ProcessInstance instance) {
        final EventOn event = EventOn.get();
        return event.taskActive(instance).compose(task -> {
            todo.setInstance(Boolean.TRUE);                   // Camunda Engine
            todo.setTraceId(instance.getId());                // Trace ID Related
            todo.setTraceTaskId(task.getId());                // Trace Task ID
            return Ux.future(todo);
        });
    }

    /*
     * {
     *     "todo": "",
     *     "record": ""
     * }
     */
    protected ConfigTodo configT(final JsonObject params) {
        final JsonObject request = params.copy();
        request.remove(KName.RECORD);
        request.remove(KName.Flow.WORKFLOW);
        request.put(KName.Flow.TODO, this.config.getJsonObject(KName.Flow.TODO, new JsonObject()));
        return new ConfigTodo(request);
    }
}
