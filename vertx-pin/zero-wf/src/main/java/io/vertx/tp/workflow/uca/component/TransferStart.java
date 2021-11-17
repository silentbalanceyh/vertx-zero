package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigRecord;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.up.eon.em.ChangeFlag;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TransferStart extends AbstractTodo {
    @Override
    public Future<WTodo> moveAsync(final JsonObject params, final ProcessInstance instance) {
        // Record processing first
        final ConfigRecord record = this.configR();
        final ConfigTodo config = this.configT(params);
        /*
         * 1. Process Record
         * 2. Todo Record
         */
        final ChangeFlag flag = record.getFlag();
        if (ChangeFlag.ADD == flag) {
            /*
             * Record serial when Insert, this action should
             * happen when ADD new record here.
             */
            return this.insertAsync(params, config)
                // Todo Processing
                .compose(processed -> this.insertAsync(processed, config, instance));
        } else {
            return this.updateAsync(params, config)
                // Todo Processing
                .compose(processed -> this.insertAsync(processed, config, instance));
        }
    }
}
