package io.vertx.tp.workflow.atom;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ConfigTodo implements Serializable {
    private final transient String identifier;
    private final transient String key;
    private final transient JsonObject data = new JsonObject();
    private final transient String indent;
    // Todo Number definition
    private transient Class<?> daoCls;

    public ConfigTodo(final JsonObject data) {
        this.indent = data.getString(KName.INDENT, null);
        if (data.containsKey(KName.MODEL_COMPONENT)) {
            this.daoCls = Ut.clazz(data.getString(KName.MODEL_COMPONENT), null);
        }
        this.identifier = data.getString(KName.MODEL_ID, null);
        this.key = data.getString(KName.MODEL_KEY, null);
        this.data.mergeIn(data, true);
    }

    public String identifier() {
        return this.identifier;
    }

    public String key() {
        return this.key;
    }

    public JsonObject data() {
        return this.data;
    }

    public Class<?> dao() {
        return this.daoCls;
    }

    public String indent() {
        return this.indent;
    }
}
