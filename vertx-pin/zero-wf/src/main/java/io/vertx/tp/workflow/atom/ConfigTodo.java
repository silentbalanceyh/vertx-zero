package io.vertx.tp.workflow.atom;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ConfigTodo implements Serializable {
    private final transient String identifier;
    private final transient String key;
    private final transient String indent;
    private final transient JsonObject todoData = new JsonObject();
    // Todo Number definition
    private transient Class<?> daoCls;

    /*
     * This constructor will be called when the data have been populated
     * from database ( storage ) and build `ConfigTodo` based on system record
     * - WTicket
     * - WTodo
     */
    public ConfigTodo(final WRecord record) {
        Objects.requireNonNull(record);
        this.identifier = record.identifier();
        this.key = record.key();
        /*
         * Dao Component Processing
         */
        final WTicket ticket = record.ticket();
        if (Objects.nonNull(ticket) && Objects.nonNull(ticket.getModelComponent())) {
            this.daoCls = Ut.clazz(ticket.getModelComponent(), null);
        }
        this.indent = null;
    }


    /*
     * This constructor will be called when todo started, it means that the
     * instance has been created by
     * {
     *      "todo": {
     *          "comment": "Todo ConfigRunner"
     *      }
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
        this.todoData.mergeIn(todo, true);
    }

    public String identifier() {
        return this.identifier;
    }

    public String key() {
        return this.key;
    }

    public Class<?> dao() {
        return this.daoCls;
    }

    public String indent() {
        return this.indent;
    }

    public Future<JsonObject> generate(final JsonObject data) {
        return Ke.umIndent(data, this.indent).compose(processed -> {
            final JsonObject ticketData = processed.copy();
            Ut.ifJCopy(ticketData, KName.INDENT, KName.SERIAL);
            /*
             * Todo Generate `key` for `todoUrl`
             * Previous
             * ticketData.put(KName.KEY, UUID.randomUUID().toString());
             */
            Ut.ifJCopy(ticketData, KName.INDENT, KName.CODE);
            {
                /*
                 * modelKey processing here.
                 * Extract from
                 * {
                 *      "record": {
                 *          "key": "modelKey here"
                 *      }
                 * }
                 */
                final JsonObject record = data.getJsonObject(KName.RECORD);
                ticketData.put(KName.MODEL_KEY, record.getValue(KName.KEY));
            }
            final JsonObject todo = this.todoData.copy();
            final JsonObject combine = new JsonObject();
            Ut.<String>itJObject(todo, (expression, field) -> {
                if (expression.contains("`")) {
                    combine.put(field, Ut.fromExpression(expression, ticketData));
                } else {
                    combine.put(field, expression);
                }
            });
            ticketData.mergeIn(combine);
            // Camunda Definition
            final JsonObject workflow = ticketData.getJsonObject(KName.Flow.WORKFLOW, new JsonObject());
            {
                ticketData.put(KName.Flow.FLOW_DEFINITION_KEY, workflow.getString(KName.Flow.DEFINITION_KEY));
                ticketData.put(KName.Flow.FLOW_DEFINITION_ID, workflow.getString(KName.Flow.DEFINITION_ID));
            }
            return Ux.future(ticketData);
        });
    }
}
