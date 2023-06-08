package io.vertx.mod.workflow.atom.runtime;

import cn.vertxup.workflow.cv.WfCv;
import cn.vertxup.workflow.cv.em.PassWay;
import cn.vertxup.workflow.cv.em.TodoStatus;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.horizon.eon.VValue;
import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.spi.business.ExActivity;
import io.horizon.spi.component.Dictionary;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.EngineOn;
import io.vertx.mod.workflow.atom.configuration.MetaInstance;
import io.vertx.mod.workflow.uca.camunda.Io;
import io.vertx.mod.workflow.uca.modeling.ActionOn;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
    private final transient JsonObject dataAfter = new JsonObject();
    // Change Flag for activity
    private final transient ChangeFlag tabb;
    private transient PassWay type;
    /*
     * This variable stored prev record when do different operation
     * 1) ADD:          prev = null
     * 2) UPDATE:       prev = value
     * 3) DELETE:       prev = value
     *
     * When current record data() called, the original record stored in
     * __data node and write __flag node for normalization
     */
    private WRecord prev;
    // Before WRecord
    private WTicket ticket;
    private List<WTodo> todo = new ArrayList<>();

    private WRecord(final ChangeFlag tabb) {
        this.tabb = tabb;
    }

    private WRecord() {
        this(null);
    }

    public static WRecord create() {
        return create(false, null);
    }

    public static WRecord create(final boolean write, final ChangeFlag flag) {
        if (write) {
            Objects.requireNonNull(flag);
            /*
             * Write mode for WRecord, it stored prev record reference
             * for update data in comparing code logical
             * Here are two constructors:
             *
             * 1) with flag: identify the record operation
             * 2) without flag: the flag = null
             */
            return new WRecord(flag).prev(new WRecord());
        } else {
            /*
             * Read mode only, it will ignore prev record reference
             */
            return new WRecord();
        }
    }

    // ------------- Reference for original record

    public WRecord prev() {
        return this.prev;
    }

    public WRecord prev(final WRecord prev) {
        this.prev = prev;
        return this;
    }

    // ------------- Response Build
    /*
     *  Empty Response Build
     *  - WTodo = null
     *  - WTicket = null
     */
    public WRecord bind() {
        this.todo = new ArrayList<>();
        this.ticket = null;
        return this;
    }

    public WRecord bind(final PassWay type) {
        this.type = type;
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
    public WRecord ticket(final WTicket ticket) {
        this.ticket = Ux.cloneT(ticket);
        return this;
    }

    public WRecord task(final WTodo todo) {
        this.todo.clear();
        this.todo.add(Ux.cloneT(todo));
        return this;
    }

    public WRecord task(final List<WTodo> todoList) {
        if (Objects.nonNull(todoList)) {
            this.todo.clear();
            this.todo.addAll(todoList);
        }
        return this;
    }

    /*
     * Child Bind
     */
    public WRecord ticket(final JsonObject child) {
        if (Ut.isNotNil(child)) {
            this.child.mergeIn(child.copy());
        }
        return this;
    }

    /*
     * Linkage Bind
     */
    public WRecord linkage(final String field, final JsonArray linkage) {
        final JsonArray data = Ut.valueJArray(linkage);
        this.linkage.put(field, data);
        return this;
    }


    // ------------- Field Get
    /*
     * Different Field for usage here
     * - task()                         WTodo instance
     * - ticket()                       WTicket instance
     * - identifier()                   WTicket ( modelId field ) for uniform model identifier
     * - key()                          WTicket ( modelKey field ) for model record primary key
     * - status()                       Todo Status ( Original Status stored in database )
     */
    public WTodo task() {
        if (this.todo.isEmpty() || VValue.ONE < this.todo.size()) {
            return null;
        } else {
            return this.todo.get(VValue.IDX);
        }
    }

    public PassWay way() {
        return this.type;
    }

    public WTicket ticket() {
        return this.ticket;
    }

    public JsonObject child() {
        return this.child;
    }

    public String identifier() {
        return this.ticket.getModelId();
    }

    public String key() {
        return this.ticket.getModelKey();
    }

    /*
     * These two methods are only called when UPDATING todo
     * Here the `status` field stored the original status of W_TODO
     * for future usage:
     * - Update the actual records by ActionOn
     * - Update the related records by ActionOn
     */
    public TodoStatus status() {
        if (Objects.isNull(this.prev) || Objects.isNull(this.prev.todo)) {
            return null;
        }
        final WTodo todo = this.prev.task();
        return Ut.toEnum(todo::getStatus, TodoStatus.class, null);
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
        final JsonObject response = this.data();
        // WProcessDefinition put into response data
        return this.dataProcess(history)
            // WTicket / WTodo data part of current code logical
            .compose(workflow -> this.dataTicket(response, workflow))
            // `history` field mount
            .compose(this::dataHistory)
            .compose(Fn.ofJObject(KName.HISTORY, response));
    }

    public Future<WRecord> futureAfter(final JsonObject dataAfter) {
        if (Ut.isNotNil(dataAfter)) {
            this.dataAfter.clear();
            this.dataAfter.mergeIn(dataAfter, true);
        }
        return Ux.future(this);
    }

    // ------------- Response Building -------------------------
    private Future<JsonObject> dataTicket(final JsonObject response, final JsonObject workflow) {
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
    }

    private Future<JsonObject> dataProcess(final boolean history) {
        /*
         * history to switch
         * 1. instance(), history = false
         * 2. instanceFinished(), history = true
         */
        final Io<JsonObject> ioFlow = Io.ioFlow();
        if (!history) {
            // Task must not be null
            final WTodo todo = this.task();
            Objects.requireNonNull(todo);
            final Io<Task> ioTask = Io.ioTask();
            final String taskId = todo.getTaskId();
            return ioTask.run(taskId).compose(ioFlow::run);
        } else {
            // Instance must not be null
            Objects.requireNonNull(this.ticket);
            final String instanceId = this.ticket.getFlowInstanceId();
            final HistoricProcessInstance instance = ioFlow.inHistoric(instanceId);
            return ioFlow.end(instance);
        }
    }

    private Future<JsonArray> dataHistory(final JsonObject processed) {
        /*
         * Condition:
         * 1. traceId is the same as all todo
         * 2. key is not equal todo key
         * 3. status should be FINISHED/REJECTED
         */

            /*
            OLD Code ( Get X_TODO )
            final JsonObject criteria = Ux.whereAnd();
            criteria.put(KName.Flow.TRACE_ID, this.ticket.getKey());
            if (!history && Objects.nonNull(this.todo)) {
                criteria.put(KName.KEY + ",<>", this.todo.getKey());
            }
            criteria.put(KName.STATUS, new JsonArray()
                .add(TodoStatus.FINISHED.name())
                .add(TodoStatus.REJECTED.name())
                .add(TodoStatus.CANCELED.name())
            );
            return Ux.Jooq.on(WTodoDao.class).fetchAsync(criteria)
                .compose(Ux::futureA);*/
        /*
         * 1) Fetch catalog first
         * 2) Convert Catalog to extract identifier
         * 3) Build `modelId` and `modelKey`
         */
        return Ux.channel(Dictionary.class, JsonArray::new,
                dict -> dict.fetchTree(this.ticket.getSigma(), WfCv.CODE_CATALOG))
            .compose(dict -> {
                final ConcurrentMap<String, String> mapId = new ConcurrentHashMap<>();
                Ut.itJArray(dict).forEach(record -> {
                    final String code = record.getString(KName.CODE);
                    final String identifier = record.getString(KName.IDENTIFIER);
                    if (Objects.nonNull(code) && Objects.nonNull(identifier)) {
                        mapId.put(code, identifier);
                    }
                });
                return Ux.future(mapId);
            })
            .compose(map -> Ux.channel(ExActivity.class, JsonArray::new, activity -> {
                // java.lang.NullPointerException: Avoid null for catalog
                if (Objects.isNull(this.ticket.getCatalog())) {
                    return Ux.futureA();
                }
                final String modelId = map.getOrDefault(this.ticket.getCatalog(), null);
                if (Objects.isNull(modelId)) {
                    return Ux.futureA();
                }
                return activity.activities(modelId, this.ticket.getKey());
            }));
    }

    // ------------- Data Building -------------------------
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
        // prev may be empty, it means that could not call old data
        // Objects.requireNonNull(this.ticket);
        final JsonObject response = new JsonObject();
        if (Objects.nonNull(this.ticket)) {
            /*
             * 1. Mount Data to WTicket
             *  - modelChild -> []
             *  - traceKey   -> WTicket -> key
             */
            this.onTicket(response);

            /*
             * 2. Mount Child Ticket
             *  - Set `traceKey` to `key` and removed key
             *  -> Mount childJ     -> Removed ( key )
             */
            this.onChild(response);

            /*
             * 3. Mount Todo to WTicket
             *  - taskCode   -> WTodo -> code
             *  - taskSerial -> WTodo -> serial
             *  - key        -> WTodo -> key
             */
            final WTodo todo = this.task();
            if (Objects.nonNull(todo)) {
                this.onTodo(response, todo);
            }


            /*
             * 4. Mount Compare
             *  - __data     -> Previous Data
             *  - __flag     -> Previous Flag
             *  - Mount dataAfter   -> Removed ( key, serial, code )
             */
            this.onCompare(response);
        }
        return response;
    }

    public JsonArray dataAop() {
        final JsonArray response = new JsonArray();
        if (Objects.nonNull(this.ticket)) {
            final JsonObject ticketJ = new JsonObject();
            /*
             * 1. Mount Data to WTicket
             *  - modelChild -> []
             *  - traceKey   -> WTicket -> key
             */
            this.onTicket(ticketJ);

            /*
             * 2. Mount Child Ticket
             *  - Set `traceKey` to `key` and removed key
             *  -> Mount childJ     -> Removed ( key )
             */
            this.onChild(ticketJ);

            this.todo.forEach(todoItem -> {
                final JsonObject ticketData = ticketJ.copy();
                /*
                 * 3. Mount Todo to WTicket
                 *  - taskCode   -> WTodo -> code
                 *  - taskSerial -> WTodo -> serial
                 *  - key        -> WTodo -> key
                 */
                this.onTodo(ticketData, todoItem);

                /*
                 * 4. Mount Compare
                 *  - __data     -> Previous Data
                 *  - __flag     -> Previous Flag
                 *  - Mount dataAfter   -> Removed ( key, serial, code )
                 */
                this.onCompare(ticketData);
                response.add(ticketData);
            });
        }
        return response;
    }

    private void onCompare(final JsonObject response) {
        if (Objects.nonNull(this.prev)) {
            // Data Original, UPDATE Only
            final JsonObject dataPrev = this.prev.data();
            if (Ut.isNotNil(dataPrev)) {
                /*
                 * Here are some calculation, the __data with `this.prev` must exist in
                 * Todo task generation here, in this kind of situation, here are the matrix
                 *
                 * WTodo        OLD            NEW
                 *  Prev         o              x ( Will be Closed )
                 *  Generated    x              o
                 *
                 * The data structure will populate the `o` marked instead of
                 * other situation here, it means that:
                 * 1) The $zo = this.prev.__data ( OLD DATA )
                 * 2) The $zn = current ( NEW DATA )
                 */
                response.put(KName.__.DATA, dataPrev);
            }
        }
        // Operation on Record
        if (Objects.nonNull(this.tabb)) {
            response.put(KName.__.FLAG, this.tabb);
        }
        // AOP Data After
        final JsonObject dataAfter = this.dataAfter.copy();
        dataAfter.remove(KName.KEY);
        dataAfter.remove(KName.CODE);
        dataAfter.remove(KName.SERIAL);
        response.mergeIn(dataAfter, true);
    }

    private void onTicket(final JsonObject response) {
        final JsonObject ticketJ = Ux.toJson(this.ticket);
        Ut.valueToJObject(ticketJ, KName.MODEL_CHILD);
        // WTicket
        // traceKey <- key
        ticketJ.put(KName.Flow.TRACE_KEY, this.ticket.getKey());
        response.mergeIn(ticketJ, true);
    }

    private void onTodo(final JsonObject response, final WTodo todo) {
        final JsonObject todoJson = Ux.toJson(todo);
        todoJson.remove(KName.SERIAL);
        todoJson.remove(KName.CODE);
        response.mergeIn(todoJson, true);
        // WTodo
        // taskCode <- code
        // taskSerial <- serial
        response.put(KName.Flow.TASK_CODE, todo.getCode());
        response.put(KName.Flow.TASK_SERIAL, todo.getSerial());
        // WTodo ( Approval Workflow )
    }

    private void onChild(final JsonObject result) {
        // Linkage Data Put
        this.linkage.forEach(result::put);
        // Child
        if (Ut.isNotNil(this.child)) {
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
    }
}
