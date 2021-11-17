package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigRecord;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

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
        final ChangeFlag flag = record.getFlag();
        if (ChangeFlag.ADD == flag) {
            /*
             * Record serial when Insert, this action should
             * happen when ADD new record here.
             */
            return this.insertAsync(params, config)
                // Todo Processing
                .compose(processed -> this.moveAsync(processed, instance, config));
        } else {
            return this.updateAsync(params, config)
                // Todo Processing
                .compose(processed -> this.moveAsync(processed, instance, config));
        }
    }

    protected Future<WTodo> traceAsync(final WTodo todo, final ProcessInstance instance) {
        final EventOn event = EventOn.get();
        return event.taskActive(instance).compose(task -> {
            todo.setInstance(Boolean.TRUE);                   // Camunda Engine
            todo.setTraceId(instance.getId());                // Trace ID Related
            todo.setTraceTaskId(task.getId());                // Trace Task ID
            return Ux.future(todo);
        });
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
