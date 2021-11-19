package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Stay extends Behaviour {

    Future<WTodo> keepAsync(JsonObject params, WInstance instance);
}
