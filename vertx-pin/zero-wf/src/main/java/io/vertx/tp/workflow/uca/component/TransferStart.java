package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.tp.workflow.atom.WInstance;
import io.vertx.tp.workflow.atom.WRecord;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TransferStart extends AbstractTodo implements Transfer {
    @Override
    public Future<WRecord> moveAsync(final JsonObject params, final WInstance wInstance) {
        /*
         * Record processing first, here the parameters are following:
         *
         * 1. Process Record
         * 2. Todo Record
         *
         * Record support ADD / UPDATE operation combined
         */
        final ConfigTodo config = this.config();
        return this.recordSave(params, config).compose(processed -> {
            /*
             * Todo Inserting here
             */
            final ProcessInstance instance = wInstance.instance();
            return this.insertAsync(params, instance);
        });
    }
}
