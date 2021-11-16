package io.vertx.tp.workflow.uca.modeling;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface TodoOn {

    static TodoOn todo(final Boolean start) {
        if (start) {
            return Fn.poolThread(WfPool.POOL_TODO, TodoStart::new, TodoStart.class.getName());
        } else {
            return Fn.poolThread(WfPool.POOL_TODO, TodoNext::new, TodoNext.class.getName());
        }
    }

    Future<WTodo> dataAsync(JsonObject params, JsonObject config);
}
