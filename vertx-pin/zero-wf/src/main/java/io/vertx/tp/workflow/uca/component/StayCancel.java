package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class StayCancel extends AbstractTodo implements Stay {
    @Override
    public Future<WTodo> keepAsync(final JsonObject params, final ProcessInstance instance) {

        return null;
    }
}
