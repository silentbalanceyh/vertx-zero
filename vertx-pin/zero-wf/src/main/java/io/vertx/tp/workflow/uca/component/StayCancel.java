package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.tp.workflow.uca.runner.StoreOn;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class StayCancel extends AbstractTodo implements Stay {
    @Override
    public Future<WTodo> keepAsync(final JsonObject params, final ProcessInstance instance) {
        /*
         * Instance deleting, but fetch the history and stored into `metadata` field as the final processing
         * Cancel for W_TODO and Camunda
         */
        final EventOn event = EventOn.get();
        return event.taskHistory(instance).compose(historySet -> event.taskActive(instance).compose(task -> {
            final Set<String> traceSet = new HashSet<>(historySet);
            if (Objects.nonNull(task)) {
                traceSet.add(task.getTaskDefinitionKey());
            }
            /*
             * Todo Processing（Record Keep）
             */
            {
                final String user = params.getString(KName.UPDATED_BY);
                params.put(KName.STATUS, TodoStatus.CANCELED.name());
                params.put(KName.Flow.Auditor.FINISHED_AT, Instant.now());
                params.put(KName.Flow.Auditor.FINISHED_BY, user);
                params.put(KName.Flow.TRACE_END, Boolean.TRUE);
                final JsonObject history = new JsonObject();
                history.put(KName.HISTORY, Ut.toJArray(traceSet));
                params.put(KName.Flow.TRACE_EXTRA, history.encode());
            }
            return this.todoUpdate(params);
        })).compose(todo -> {
            // Remove ProcessDefinition
            final StoreOn storeOn = StoreOn.get();
            return storeOn.instanceEnd(instance).compose(removed -> Ux.future(todo));
        });
    }
}
