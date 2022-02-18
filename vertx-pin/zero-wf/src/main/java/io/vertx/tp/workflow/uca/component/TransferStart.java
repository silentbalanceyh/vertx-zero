package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRecord;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TransferStart extends AbstractTodo implements Transfer {
    @Override
    public Future<WRecord> moveAsync(final JsonObject params, final WProcess wProcess) {
        /*
         * Record processing first, here the parameters are following:
         *
         * 1. Process Record
         * 2. Todo Record
         *
         * Record support ADD / UPDATE operation combined
         */
        return this.saveAsync(params, this.metadataConfigured()).compose(processed -> {
            /*
             * Todo Inserting here
             */
            final ProcessInstance instance = wProcess.instance();
            return this.insertAsync(params, instance);
        });
    }
}
