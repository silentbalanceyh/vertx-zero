package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TransferStart extends AbstractTodo {
    @Override
    public Future<WTodo> moveAsync(final JsonObject record, final ProcessInstance instance,
                                   final ConfigTodo todo) {
        // Todo Build
        final WTodo entity = Ux.fromJson(todo.data(), WTodo.class);
        entity.setInstance(Boolean.TRUE);                   // Camunda Engine
        entity.setTraceId(instance.getId());                // Instance ID Related
        // Key processing
        return null;
    }
}
