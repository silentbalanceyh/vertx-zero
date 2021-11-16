package io.vertx.tp.workflow.atom;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

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

    public ConfigTodo(final WTodo todo) {
        Objects.requireNonNull(todo);
        this.daoCls = Ut.clazz(todo.getModelComponent(), null);
        this.identifier = todo.getModelId();
        this.key = todo.getModelKey();
        this.data.mergeIn(Ux.toJson(todo), true);
        this.indent = null;
    }

    /*
     * {
     *      "todo": "config
     * }
     */
    public ConfigTodo(final JsonObject data) {
        final JsonObject todo = data.getJsonObject(KName.Flow.TODO);
        Objects.requireNonNull(todo);
        this.indent = todo.getString(KName.INDENT, null);
        if (todo.containsKey(KName.MODEL_COMPONENT)) {
            this.daoCls = Ut.clazz(todo.getString(KName.MODEL_COMPONENT), null);
        }
        this.identifier = todo.getString(KName.MODEL_ID, null);
        this.key = todo.getString(KName.MODEL_KEY, null);
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

    public Future<WTodo> generate(final String modelKey) {
        return Ke.umIndent(this.data, this.indent).compose(processed -> {
            final JsonObject todoData = processed.copy();
            Ut.ifJCopy(todoData, KName.INDENT, KName.SERIAL);
            // Todo Generate `key` for `todoUrl`
            {
                todoData.put(KName.KEY, UUID.randomUUID().toString());
                todoData.put(KName.MODEL_KEY, modelKey);
            }
            final JsonObject todo = this.data.getJsonObject(KName.Flow.TODO);
            final JsonObject combine = new JsonObject();
            Ut.<String>itJObject(todo, (expression, field) -> {
                if (expression.contains("`")) {
                    combine.put(field, Ut.fromExpression(expression, todoData));
                } else {
                    combine.put(field, expression);
                }
            });
            todoData.mergeIn(combine);
            return Ux.future(Ux.fromJson(todoData, WTodo.class));
        });
    }
}
