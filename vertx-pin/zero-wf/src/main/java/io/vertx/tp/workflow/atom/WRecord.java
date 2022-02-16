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
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WRecord implements Serializable {
    private final transient ConcurrentMap<String, JsonArray> linkage = new ConcurrentHashMap<>();
    private transient WTicket ticket;
    private transient WTodo todo;
    private transient TodoStatus status;
    private transient WProcess process;

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

    public WRecord linkage(final String field, final JsonArray linkage) {
        final JsonArray data = Ut.sureJArray(linkage);
        this.linkage.put(field, data);
        return this;
    }

    public WTodo todo() {
        return this.todo;
    }

    public WTicket ticket() {
        return this.ticket;
    }

    public JsonObject data() {
        Objects.requireNonNull(this.ticket);
        final JsonObject response = new JsonObject();
        final JsonObject ticketJ = Ux.toJson(this.ticket);
        // WTicket
        // traceKey <- key
        ticketJ.put(KName.Flow.TRACE_KEY, this.ticket.getKey());
        response.mergeIn(ticketJ, true);
        if (Objects.nonNull(this.todo)) {
            final JsonObject todoJson = Ux.toJson(this.todo);
            response.mergeIn(todoJson, true);
            // WTodo
            // taskCode <- code
            // taskSerial <- serial
            response.put(KName.Flow.TASK_CODE, this.todo.getCode());
            response.put(KName.Flow.TASK_SERIAL, this.todo.getSerial());
        }
        // WTicket
        // serial
        // code
        response.put(KName.SERIAL, this.ticket.getSerial());
        response.put(KName.CODE, this.ticket.getCode());
        return response;
    }

    public Future<JsonObject> futureJ() {
        return Ux.future(this.data());
    }

    // ------------- Field Get

    public String identifier() {
        return this.ticket.getModelId();
    }

    public String key() {
        return this.ticket.getModelKey();
    }


    // ------------- Workflow Moving
    /*
     * These two methods are only called when UPDATING todo
     * Here the `status` field stored the original status of W_TODO
     * for future usage:
     * - Update the actual records by ActionOn
     * - Update the related records by ActionOn
     */
    public WRecord status(final String literal) {
        this.status = Ut.toEnum(() -> literal, TodoStatus.class, null);
        return this;
    }

    public TodoStatus status() {
        return this.status;
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
            final ActionOn action = ActionOn.action(engine.mode());
            // Record of Todo processing
            final ConfigTodo configTodo = new ConfigTodo(this);
            return action.fetchAsync(this.ticket.getModelKey(), configTodo).compose(json -> {
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
            if (!history && Objects.nonNull(this.todo)) {
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
        }).compose(Ux.attachJ(KName.HISTORY, response)).compose(result -> {
            // Linkage Data Put
            this.linkage.forEach(result::put);
            return Ux.future(result);
        });
    }
}
