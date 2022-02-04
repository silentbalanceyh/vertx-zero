package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.tp.workflow.atom.WInstance;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.up.eon.em.ChangeFlag;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TransferStart extends AbstractTodo implements Transfer {
    @Override
    public Future<WRecord> moveAsync(final JsonObject params, final WInstance wInstance) {
        // Record processing first
        final ConfigTodo config = this.configuration(params);
        final ProcessInstance instance = wInstance.instance();
        /*
         * 1. Process Record
         * 2. Todo Record
         */
        final ChangeFlag flag = this.recordMode();
        if (ChangeFlag.ADD == flag) {
            /*
             * Record serial when Insert, this action should
             * happen when ADD new record here.
             */
            return this.recordInsert(params, config)
                // Todo Processing
                .compose(processed -> this.insertAsync(processed, config, instance));
        } else {
            return this.recordUpdate(params, config)
                // Todo Processing
                .compose(processed -> this.insertAsync(processed, config, instance));
        }
    }
}
