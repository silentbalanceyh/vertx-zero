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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WRecord implements Serializable {
    private final transient ConcurrentMap<String, JsonArray> linkage = new ConcurrentHashMap<>();
    private final transient JsonObject child = new JsonObject();
    private transient WTicket ticket;
    private transient WTodo todo;
    private transient TodoStatus status;
    private transient WProcessDefinition process;

    // ------------- Response Build
    /*
     *  Empty Response Build
     *  - WTodo = null
     *  - WTicket = null
     */
    public WRecord bind() {
        this.todo = null;
        this.ticket = null;
        return this;
    }

    /*
     *  Success Response Build
     *  - WTicket = {} with JsonObject ( Child )
     *  - WTodo = {}
     *  - history = []
     *  - linkageXX = []
     *
     *  1. Ticket Full Data = WTicket + <Extension> = {}
     *     Extension Ticket primary key is the same as Ticket
     *  2. History should be JsonArray ( Captured WTodo / Camunda Task )
     *  3. Linkage should be related to current WTodo record
     */
    public WRecord bind(final WTicket ticket) {
        this.ticket = ticket;
        return this;
    }

    public WRecord bind(final WTodo todo) {
        this.todo = todo;
        return this;
    }

    /*
     * Linkage Bind
     */
    public WRecord linkage(final String field, final JsonArray linkage) {
        final JsonArray data = Ut.sureJArray(linkage);
        this.linkage.put(field, data);
        return this;
    }

    /*
     * Child Bind
     */
    public WRecord child(final JsonObject child) {
        if (Ut.notNil(child)) {
            this.child.mergeIn(child);
        }
        return this;
    }

    // ------------- Field Get
    /*
     * Different Field for usage here
     * - todo()                         WTodo instance
     * - ticket()                       WTicket instance
     * - identifier()                   WTicket ( modelId field ) for uniform model identifier
     * - key()                          WTicket ( modelKey field ) for model record primary key
     * - status()                       Todo Status ( Original Status stored in database )
     */
    public WTodo todo() {
        return this.todo;
    }

    public WTicket ticket() {
        return this.ticket;
    }

    public String identifier() {
        return this.ticket.getModelId();
    }

    public String key() {
        return this.ticket.getModelKey();
    }

    public TodoStatus status() {
        return this.status;
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

    // ------------- Async Code Logical
    /*
     * Async code logical for Future usage here
     * - futureJ()                      WTicket + WTodo only
     * - futureJ(Boolean)               WTicket + WTodo
     *                                  Linkage Fields
     *                                  history ( Tracking )
     */
    public Future<JsonObject> futureJ() {
        return Ux.future(this.data());
    }

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
            final MetaInstance metadataInput = engine.metadata();
            // Record Action processing
            final ActionOn action = ActionOn.action(metadataInput.recordMode());
            // Record of Todo processing
            final MetaInstance metadataOutput = MetaInstance.output(this, metadataInput);
            if (metadataOutput.recordSkip()) {
                return Ux.future(response);
            }
            // Record for different fetch
            final String modelKey = this.ticket.getModelKey();
            if (Objects.isNull(modelKey)) {
                final Set<String> keys = Ut.toSet(Ut.toJArray(this.ticket.getModelChild()));
                return action.fetchAsync(keys, this.ticket.getModelId(),
                    metadataOutput).compose(array -> {
                    // record processing
                    response.put(KName.RECORD, array);
                    return Ux.future(response);
                });
            } else {
                return action.fetchAsync(this.ticket.getModelKey(), this.ticket.getModelId(),
                    metadataOutput).compose(json -> {
                    // record processing
                    response.put(KName.RECORD, json);
                    return Ux.future(response);
                });
            }
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
            // Child
            if (Ut.notNil(this.child)) {
                /*
                 * Switch `key` and `traceKey` instead of all records
                 * - `key` field is W_TODO record primary key
                 * - `traceKey` field is W_TICKET record primary key
                 *
                 * Please be careful about following.
                 */
                final JsonObject childJ = this.child.copy();
                if (!result.containsKey(KName.Flow.TRACE_KEY)) {
                    result.put(KName.Flow.TRACE_KEY, childJ.getValue(KName.KEY));
                }
                childJ.remove(KName.KEY);
                result.mergeIn(childJ);
            }
            return Ux.future(result);
        });
    }


    /*
     * Data Specification of current record:
     * {
     *      "ticket.field1": "value1",
     *      "ticket.field2": "value2",
     *      "...": "..."
     *      "modelChild": [
     *          "when multi entities"
     *      ],
     *
     *      "serial": "ticket serial",
     *      "code": "ticket code"
     *      "traceKey": "ticket field",
     *
     *      "key": "todo field",
     *      "taskCode": "todo code",
     *      "taskSerial": "todo serial"
     * }
     *
     *
     *
     * Json Data for following critical in data structure
     * 1. Camunda Workflow
     *  - flowDefinitionKey                 - Process Definition Key
     *  - flowDefinitionId                  - Process Definition Key/Id
     *                                        ( Different workflow instance contain different id )
     *  - flowEnd                           - Whether workflow is ended
     *  - flowInstanceId                    - Process Instance Key
     *
     *  - taskKey                           - Task Definition Key
     *  - taskCode                          - Todo Serial ( -01, -02, -03 )
     *  - taskId                            - Task Id ( Camunda Workflow Id )
     *
     *
     * 2. Critical Key
     *  - key                               - W_TODO primary key
     *  - traceKey                          - W_TICKET primary key ( ticket extension key )
     *
     *
     * 3. Entity Part
     *  - modelId                           - Entity identifier
     *  - modelKey                          - 「Single Record」Primary Key of JsonObject entity record
     *  - modelChild ( With quantity )      - 「Multi Record」Primary Keys of JsonArray entity records
     */
    public JsonObject data() {
        Objects.requireNonNull(this.ticket);
        final JsonObject response = new JsonObject();
        final JsonObject ticketJ = Ux.toJson(this.ticket);
        Ut.ifJObject(ticketJ, KName.MODEL_CHILD);
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
}
