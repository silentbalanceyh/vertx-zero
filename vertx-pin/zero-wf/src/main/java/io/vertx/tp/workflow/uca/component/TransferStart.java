package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
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
        final ConfigTodo config = new ConfigTodo(params);
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
                .compose(processed -> this.todoInsert(processed, config, instance));
        } else {
            return this.recordUpdate(params, config)
                // Todo Processing
                .compose(processed -> this.todoInsert(processed, config, instance));
        }
    }
}
