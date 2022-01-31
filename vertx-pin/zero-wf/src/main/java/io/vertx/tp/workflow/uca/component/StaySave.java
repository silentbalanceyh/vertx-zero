package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.tp.workflow.atom.WInstance;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class StaySave extends AbstractTodo implements Stay {
    @Override
    public Future<WTodo> keepAsync(final JsonObject params, final WInstance instance) {
        // Todo Updating
        return this.todoUpdate(params).compose(todo -> {
            // TODO:
            final ConfigTodo configTodo = new ConfigTodo(new JsonObject());
            // Record Updating
            return this.recordUpdate(params, configTodo).compose(nil -> Ux.future(todo));
        });
    }
}
