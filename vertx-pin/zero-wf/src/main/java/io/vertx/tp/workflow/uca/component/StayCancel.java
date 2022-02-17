package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WInstance;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.tp.workflow.uca.runner.StoreOn;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class StayCancel extends AbstractTodo implements Stay {
    @Override
    public Future<WRecord> keepAsync(final JsonObject params, final WInstance wInstance) {
        /*
         * Instance deleting, but fetch the history and stored into `metadata` field as the final processing
         * Cancel for W_TODO and Camunda
         */
        final EventOn event = EventOn.get();
        final ProcessInstance instance = wInstance.instance();
        return event.taskHistory(instance).compose(historySet -> {
            // Cancel data processing
            final JsonObject todoData = KtTodo.cancelJ(params, wInstance, historySet);
            return this.updateAsync(todoData);
        }).compose(record -> {
            // Remove ProcessDefinition
            final StoreOn storeOn = StoreOn.get();
            return storeOn.instanceEnd(instance)
                .compose(removed -> Ux.future(record));
        });
    }
}
