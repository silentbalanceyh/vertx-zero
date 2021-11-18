package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.tp.workflow.uca.runner.IsOn;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.time.Instant;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TransferStandard extends AbstractTodo {
    @Override
    public Future<WTodo> moveAsync(final JsonObject params, final ProcessInstance instance) {
        // Update current todo information
        final JsonObject updatedData = Ut.sureJObject(params.copy());
        final String user = params.getString(KName.UPDATED_BY);
        final TodoStatus status = Ut.toEnum(TodoStatus.class, params.getString(KName.STATUS));
        {
            updatedData.put(KName.STATUS, TodoStatus.FINISHED);
            updatedData.put(KName.Flow.Auditor.FINISHED_AT, Instant.now());
            updatedData.put(KName.Flow.Auditor.FINISHED_BY, user);
            // updatedAt / updatedBy contain values
            updatedData.put(KName.ACTIVE, Boolean.TRUE);
        }
        final EventOn event = EventOn.get();
        final IsOn is = IsOn.get();
        if (is.isEnd(instance)) {
            updatedData.put("traceEnd", Boolean.TRUE);
        }
        return this.todoUpdate(updatedData).compose(todo -> {
            if (TodoStatus.DRAFT == status) {
                // Draft -> Pending, we should update record
                final ConfigTodo configTodo = new ConfigTodo(todo);
                return this.recordUpdate(params, configTodo).compose(nil -> Ux.future(todo));
            }
            return Ux.future(todo);
        }).compose(todo -> event.taskActive(instance).compose(task -> {
            /*
             * Todo Generation Condition
             * 1. Instance is not ended
             * 2. Next task is UserEvent
             */
            if (!is.isEnd(instance) && is.isUserEvent(task)) {
                return this.todoGenerate(todo, task);
            } else {
                return Ux.future(todo);
            }
        }));
    }
}
