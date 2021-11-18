package cn.zeroup.macrocosm.service;

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
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TaskService implements TaskStub {
    @Inject
    private transient AclStub aclStub;

    @Override
    public Future<JsonObject> fetchQueue(final JsonObject condition) {
        final JsonObject normalized = Ux.whereQrA(condition, KName.INSTANCE, Boolean.TRUE);
        // Condition / Connect to Task of Camunda Platform
        return Ux.Jooq.on(WTodoDao.class).searchAsync(normalized);
    }

    @Override
    public Future<JsonObject> fetchTodo(final String key, final String userId) {
        return Ux.Jooq.on(WTodoDao.class).<WTodo>fetchByIdAsync(key).compose(todo -> {
            if (Objects.isNull(todo)) {
                return Ux.futureJ();
            }
            // Fetch ProcessDefinition
            if (todo.getInstance()) {
                // Workflow Processing
                final String traceId = todo.getTraceId();
                return Wf.instance(traceId)
                    .compose(process -> this.fetchWithFlow(todo, process, userId));
            } else {
                return this.fetchWithoutFlow(todo);
            }
        });
    }

    @Override
    public Future<List<WTodo>> fetchHistory(final WTodo todo) {
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
        criteria.put(KName.KEY + ",<>", key);
        criteria.put(KName.STATUS, new JsonArray()
            .add(TodoStatus.FINISHED)
            .add(TodoStatus.REJECTED)
        );
        return Ux.Jooq.on(WTodoDao.class).fetchAsync(criteria);
    }

    private Future<JsonObject> fetchWithoutFlow(final WTodo todo) {
        // TODO: Pending for future development
        return Ux.futureJ();
    }

    private Future<JsonObject> fetchWithFlow(final WTodo todo, final WProcess process, final String userId) {
        final StoreOn storeOn = StoreOn.get();
        return storeOn.workflowByInstance(process.definition(), process.instance())
            .compose(workflow -> {
                // Record based on start
                final EngineOn engine = EngineOn.connect(workflow.getString(KName.Flow.DEFINITION_KEY));
                // Record Action processing
                final ConfigRecord record = engine.configRecord();
                final ActionOn action = ActionOn.action(record.getMode());
                // Record of Todo processing
                final ConfigTodo configTodo = new ConfigTodo(todo);
                return action.fetchAsync(todo.getModelKey(), configTodo);
            })
            .compose(json -> {
                final JsonObject response = Ux.toJson(todo);
                // record processing
                response.put(KName.RECORD, json);
                return Ux.future(response);
            })
            .compose(response ->
                this.fetchHistory(todo).compose(Ux::futureA)
                    .compose(Ux.attachJ(KName.HISTORY, response))
            )
            .compose(response ->
                this.aclStub.authorize(process, todo, userId)
                    .compose(Ux.attachJ(KName.Flow.ACL, response))
            );
    }
}
