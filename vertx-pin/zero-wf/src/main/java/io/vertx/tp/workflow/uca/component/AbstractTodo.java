package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.workflow.atom.ConfigRecord;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.tp.workflow.uca.modeling.ActionOn;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractTodo extends AbstractTransfer implements Transfer {
    @Override
    public Future<WTodo> moveAsync(final JsonObject params, final ProcessInstance instance) {
        // Record processing first
        final ConfigRecord record = this.configR();
        final ConfigTodo config = this.configT(params);
        /*
         * 1. Process Record
         * 2. Todo Record
         */
        final ActionOn action = ActionOn.action(record.getMode());
        final ChangeFlag flag = record.getFlag();
        if (ChangeFlag.ADD == flag) {
            /*
             * Record serial when Insert, this action should
             * happen when ADD new record here.
             */
            final JsonObject recordData = this.dataR(params, true);
            return Ke.umIndent(recordData, record.getIndent())
                // ADD processing
                .compose(processed -> action.createAsync(processed, config))
                // Todo Processing
                .compose(processed -> this.moveAsync(processed, instance, config));
        } else {
            final JsonObject recordData = this.dataR(params, false);
            final String key = record.unique(recordData);
            Objects.requireNonNull(key);
            // UPDATE processing
            return action.updateAsync(key, recordData, config)
                // Todo Processing
                .compose(processed -> this.moveAsync(processed, instance, config));
        }
    }

    /*
     * {
     *     "todo": "",
     *     "record": ""
     * }
     */
    protected ConfigTodo configT(final JsonObject params) {
        final JsonObject request = params.copy();
        request.remove(KName.RECORD);
        request.remove(KName.Flow.WORKFLOW);
        request.put(KName.Flow.TODO, this.config.getJsonObject(KName.Flow.TODO, new JsonObject()));
        return new ConfigTodo(request);
    }

    /*
     * moveAsync that could be overwritten by sub-class
     */
    protected Future<WTodo> moveAsync(final JsonObject record, final ProcessInstance instance,
                                      final ConfigTodo todo) {
        // Default Implementation
        return Ux.future();
    }
}
