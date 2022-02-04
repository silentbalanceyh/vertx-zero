package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.tp.workflow.atom.WInstance;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class StaySave extends AbstractTodo implements Stay {
    @Override
    public Future<WRecord> keepAsync(final JsonObject params, final WInstance instance) {
        // Todo Updating
        return this.updateAsync(params).compose(record -> {
            final ConfigTodo configTodo = new ConfigTodo(record);
            // Record Updating
            return this.recordUpdate(params, configTodo)
                .compose(nil -> Ux.future(record));
        });
    }
}
