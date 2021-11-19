package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.tp.workflow.atom.WInstance;
import io.vertx.tp.workflow.uca.runner.IsOn;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TransferStandard extends AbstractTodo implements Transfer {
    @Override
    public Future<WTodo> moveAsync(final JsonObject params, final WInstance wInstance) {
        // Update current todo information
        final TodoStatus status = Ut.toEnum(TodoStatus.class, params.getString(KName.STATUS));
        final JsonObject updatedData = KitTodo.inputClose(params, wInstance);

        return this.todoUpdate(updatedData).compose(todo -> {
            if (TodoStatus.DRAFT == status) {
                // Draft -> Pending, we should update record
                final ConfigTodo configTodo = new ConfigTodo(todo);
                return this.recordUpdate(params, configTodo).compose(nil -> Ux.future(todo));
            }
            return Ux.future(todo);
        }).compose(todo -> wInstance.next().compose(taskNext -> {
            /*
             * Todo Generation Condition
             * 1. Instance is not ended
             * 2. Next task is UserEvent
             */
            final IsOn is = IsOn.get();
            if (!wInstance.isContinue() && is.isUserEvent(taskNext)) {
                return this.todoGenerate(todo, wInstance, taskNext);
            } else {
                return Ux.future(todo);
            }
        }));
    }
}
