package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TransferEmpty extends AbstractTodo implements Transfer {
    @Override
    public Future<WTodo> moveAsync(final JsonObject params, final ProcessInstance instance) {
        Wf.Log.warnMove(this.getClass(), "[ Empty ] `Transfer` component has not been configured. ");
        return Ux.future();
    }
}
