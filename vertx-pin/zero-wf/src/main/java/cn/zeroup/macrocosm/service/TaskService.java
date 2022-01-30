package cn.zeroup.macrocosm.service;

import cn.vertxup.workflow.domain.tables.daos.WTicketDao;
import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigRecord;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.tp.workflow.atom.EngineOn;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.modeling.ActionOn;
import io.vertx.tp.workflow.uca.runner.StoreOn;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

import javax.inject.Inject;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TaskService implements TaskStub {
    @Inject
    private transient AclStub aclStub;

    @Override
    public Future<JsonObject> fetchQueue(final JsonObject condition) {
        final JsonObject combine = Ux.whereQrA(condition, "flowEnd", Boolean.FALSE);
        return Ux.Join.on()

            // Join WTodo Here
            .add(WTodoDao.class, KName.Flow.TRACE_ID)
            .join(WTicketDao.class)

            // Alias must be called after `add/join`
            .alias(WTicketDao.class, new JsonObject()
                .put(KName.KEY, KName.Flow.TRACE_KEY)
                .put(KName.STATUS, KName.Flow.TRACE_STATUS)
                .put(KName.SERIAL, KName.Flow.TRACE_SERIAL)
                .put(KName.CODE, KName.Flow.TRACE_CODE)
            )
            .searchAsync(combine);
    }

    @Override
    public Future<JsonObject> fetchTodo(final String key, final String userId) {
        return Ux.Jooq.on(WTodoDao.class).<WTodo>fetchByIdAsync(key).compose(todo -> {
            if (Objects.isNull(todo)) {
                return Ux.futureJ();
            }
            final String traceId = todo.getTraceId();
            return Wf.instance(traceId).compose(process -> this.runningFlow(todo, process, userId));
        });
    }

    @Override
    public Future<JsonObject> fetchFinished(final String key) {
        return Ux.Jooq.on(WTodoDao.class).<WTodo>fetchByIdAsync(key).compose(todo -> {
            // Fetch ProcessDefinition
            // Workflow Processing
            final String traceId = todo.getTraceId();
            return Wf.instance(traceId).compose(process -> this.historyFlow(todo, process));
/*            if (todo.getInstance()) {
                final String traceId = todo.getTraceId();
                return Wf.instance(traceId).compose(process -> this.historyFlow(todo, process));
            } else {
                return this.history(todo);
            }*/
        });
    }

    private Future<JsonObject> running(final WTodo todo) {
        // TODO: Pending for future development
        return Ux.futureJ();
    }

    private Future<JsonObject> history(final WTodo todo) {
        // TODO: Pending for future development
        return Ux.futureJ();
    }

    private Future<JsonObject> historyFlow(final WTodo todo, final WProcess process) {
        final StoreOn storeOn = StoreOn.get();
        return storeOn.workflowGet(process.definition(), process.instanceFinished())
            // Fetch History Only
            .compose(workflow -> this.fetchWorkflow(todo, workflow, false))
            // edition = false
            .compose(response -> Ux.future(response.put(KName.Flow.ACL,
                new JsonObject().put(KName.EDITION, Boolean.FALSE))));
    }

    private Future<JsonObject> runningFlow(final WTodo todo, final WProcess process, final String userId) {
        final StoreOn storeOn = StoreOn.get();
        return storeOn.workflowGet(process.definition(), process.instance())
            .compose(workflow -> this.fetchWorkflow(todo, workflow, true))
            // Fetch Running with Acl
            .compose(response -> this.aclStub.authorize(process, todo, userId).compose(Ux.attachJ(KName.Flow.ACL, response)));
    }

    private Future<JsonObject> fetchWorkflow(final WTodo todo, final JsonObject workflow, final boolean excludeMy) {
        // Record based on start
        final EngineOn engine = EngineOn.connect(workflow.getString(KName.Flow.DEFINITION_KEY));
        // Record Action processing
        final ConfigRecord record = engine.configRecord();
        final ActionOn action = ActionOn.action(record.getMode());
        // Record of Todo processing
        final ConfigTodo configTodo = new ConfigTodo(todo);
        return action.fetchAsync(todo.getModelKey(), configTodo)
            .compose(json -> {
                final JsonObject response = Ux.toJson(todo);
                // record processing
                response.put(KName.RECORD, json);
                return Ux.future(response);
            })
            .compose(response -> this.fetchHistory(todo, excludeMy)
                .compose(Ux.attachJ(KName.HISTORY, response))
            );
    }

    private Future<JsonArray> fetchHistory(final WTodo todo, final boolean excludeMy) {
        final String traceId = todo.getTraceId();
        final String key = todo.getKey();
        /*
         * Condition:
         * 1. traceId is the same as all todo
         * 2. key is not equal todo key
         * 3. status should be FINISHED/REJECTED
         */
        final JsonObject criteria = Ux.whereAnd();
        criteria.put("traceId", traceId);
        if (excludeMy) {
            criteria.put(KName.KEY + ",<>", key);
        }
        criteria.put(KName.STATUS, new JsonArray()
            .add(TodoStatus.FINISHED.name())
            .add(TodoStatus.REJECTED.name())
            .add(TodoStatus.CANCELED.name())
        );
        return Ux.Jooq.on(WTodoDao.class).fetchAsync(criteria).compose(Ux::futureA);
    }
}
