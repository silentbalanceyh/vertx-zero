package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.MetaInstance;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class StaySave extends AbstractTodo implements Stay {
    @Override
    public Future<WRecord> keepAsync(final JsonObject params, final WProcess instance) {
        // Todo Updating
        return this.updateAsync(params).compose(record -> {
            final MetaInstance metadata = MetaInstance.output(record);
            // Record Updating
            return this.updateAsync(params, metadata)
                .compose(nil -> Ux.future(record));
        });
    }
}
