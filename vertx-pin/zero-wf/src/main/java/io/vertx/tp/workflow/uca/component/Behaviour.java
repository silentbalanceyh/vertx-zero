package io.vertx.tp.workflow.uca.component;

import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Behaviour {
    Behaviour bind(JsonObject config);
}
