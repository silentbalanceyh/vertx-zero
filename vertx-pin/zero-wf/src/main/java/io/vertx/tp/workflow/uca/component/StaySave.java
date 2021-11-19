package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class StaySave extends AbstractTodo implements Stay {
    @Override
    public Future<WTodo> keepAsync(final JsonObject params, final ProcessInstance instance) {
        // Todo Updating
        return this.todoUpdate(params).compose(todo -> {
            final ConfigTodo configTodo = new ConfigTodo(todo);
            // Record Updating
            return this.recordUpdate(params, configTodo).compose(nil -> Ux.future(todo));
        });
    }
}
