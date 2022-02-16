package io.vertx.tp.workflow.uca.component;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigLinkage;
import io.vertx.tp.workflow.atom.ConfigRecord;
import io.vertx.tp.workflow.atom.ConfigTodo;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Behaviour {
    /*
     * Component Configuration Binding
     */
    Behaviour bind(JsonObject config);

    /*
     * Ticket, Todo, Linkage Configuration
     */
    Behaviour bind(ConfigTodo todo, ConfigLinkage linkage);

    /*
     * Record / Entity Configuration
     */
    Behaviour bind(ConfigRecord record);
}
