package io.vertx.tp.workflow.atom;

import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.modeling.ActionOn;
import io.vertx.tp.workflow.uca.runner.StoreOn;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WRecord implements Serializable {
    private transient WTicket ticket;
    private transient WTodo todo;
    private transient WProcess process;

    public static Future<WRecord> future(final WTicket ticket, final WTodo todo) {
        return Ux.future(new WRecord().bind(ticket).bind(todo));
    }

    public WRecord bind(final WTicket ticket) {
        this.ticket = ticket;
        return this;
    }

    public WRecord bind(final WTodo todo) {
        this.todo = todo;
        return this;
    }

    public WRecord bind() {
        this.todo = null;
        this.ticket = null;
        return this;
    }

    public WTodo todo() {
        return this.todo;
    }

    public WTicket ticket() {
        return this.ticket;
    }

    public boolean isEmpty() {
        return Objects.isNull(this.todo) ||
            Objects.isNull(this.ticket);
    }

    public JsonObject data() {
        if (this.isEmpty()) {
            return new JsonObject();
        } else {
            final JsonObject response = Ux.toJson(this.ticket);
            response.put(KName.Flow.TRACE_KEY, this.ticket.getKey());
            response.put(KName.Flow.TRACE_SERIAL, this.ticket.getSerial());
            response.put(KName.Flow.TRACE_CODE, this.ticket.getCode());
            final JsonObject todo = Ux.toJson(this.todo);
            response.mergeIn(todo, true);
            return response;
        }
    }

    public Future<JsonObject> futureJ() {
        return Ux.future(this.data());
    }

    // ------------- Field Get

    public String identifier() {
        return this.todo.getModelId();
    }

    public String key() {
        return this.todo.getModelKey();
    }

    // ------------- Code Logical for
    public Future<JsonObject> futureJ(final boolean history) {
        // Here read `flowProcessId`
        final String instanceId = this.ticket.getFlowInstanceId();
        final JsonObject response = this.data();
        return Wf.instance(instanceId).compose(process -> {
            /*
             * history to switch
             * 1. instance(), history = false
             * 2. instanceFinished(), history = true
             */
            this.process = process;
            final StoreOn storeOn = StoreOn.get();
            if (history) {
                // Fetch History Only
                return storeOn.workflowGet(process.definition(),
                    process.instanceFinished());
            } else {
                // Fetch Workflow
                return storeOn.workflowGet(process.definition(),
                    process.instance());
            }
        }).compose(workflow -> {
            // Record based on start
            final EngineOn engine = EngineOn.connect(workflow.getString(KName.Flow.DEFINITION_KEY));
            // Record Action processing
            final ConfigRecord record = engine.configRecord();
            final ActionOn action = ActionOn.action(record.getMode());
            // Record of Todo processing
            final ConfigTodo configTodo = new ConfigTodo(this);
            return action.fetchAsync(this.todo.getModelKey(), configTodo).compose(json -> {
                // record processing
                response.put(KName.RECORD, json);
                return Ux.future(response);
            });
        }).compose(processed -> {
            /*
             * Condition:
             * 1. traceId is the same as all todo
             * 2. key is not equal todo key
             * 3. status should be FINISHED/REJECTED
             */
            final JsonObject criteria = Ux.whereAnd();
            criteria.put(KName.Flow.TRACE_ID, this.ticket.getKey());
            if (!history) {
                // Exclude current todo
                criteria.put(KName.KEY + ",<>", this.todo.getKey());
            }
            criteria.put(KName.STATUS, new JsonArray()
                .add(TodoStatus.FINISHED.name())
                .add(TodoStatus.REJECTED.name())
                .add(TodoStatus.CANCELED.name())
            );
            return Ux.Jooq.on(WTodoDao.class).fetchAsync(criteria)
                .compose(Ux::futureA);
        }).compose(Ux.attachJ(KName.HISTORY, response));
    }
}
