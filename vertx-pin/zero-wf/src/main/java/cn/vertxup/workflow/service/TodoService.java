package cn.vertxup.workflow.service;

import cn.vertxup.workflow.cv.WfMsg;
import cn.vertxup.workflow.cv.em.TodoStatus;
import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import com.fasterxml.jackson.core.type.TypeReference;
import io.horizon.spi.feature.Todo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.init.WfPin;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static io.vertx.mod.workflow.refine.Wf.LOG;

public class TodoService implements TodoStub {

    @Override
    public Future<JsonObject> createTodo(final String type, final JsonObject data) {
        /*
         * Get by type
         */
        final JsonObject defaultTodo = WfPin.getTodo(type);
        final JsonObject inputData = data.copy();
        if (Objects.nonNull(defaultTodo)) {
            /*
             * Expression for defaultTodo
             */
            final JsonObject params = data.copy();
            final String name = Ut.fromExpression(defaultTodo.getString(KName.NAME), params);
            final String code = Ut.fromExpression(defaultTodo.getString(KName.CODE), params);
            final String url = Ut.fromExpression(defaultTodo.getString("todoUrl"), params);
            inputData.mergeIn(defaultTodo);
            inputData.put(KName.NAME, name);
            inputData.put(KName.CODE, code);
            inputData.put("todoUrl", url);
        }
        final WTodo todo = Ut.deserialize(inputData, WTodo.class);
        return Ux.Jooq.on(WTodoDao.class)
            .insertAsync(todo)
            .compose(Ux::futureJ);
    }

    @Override
    public Future<JsonArray> fetchTodos(final String sigma, final String type, final JsonArray statues) {
        final JsonObject filters = new JsonObject();
        filters.put("sigma", sigma);
        if (Objects.nonNull(type)) {
            filters.put("type", type);
        }
        filters.put("status,i", statues);
        return Ux.Jooq.on(WTodoDao.class).fetchAndAsync(filters).compose(Ux::futureA);
    }

    @Override
    public Future<JsonArray> fetchTodos(final String sigma, final JsonArray types, final JsonArray statues) {
        final JsonObject filters = this.toFilters(sigma, types, statues);
        return Ux.Jooq.on(WTodoDao.class).fetchAndAsync(filters).compose(Ux::futureA);
    }

    @Override
    public Future<JsonArray> fetchTodos(final String sigma, final JsonArray types, final JsonArray statues, final JsonArray codes) {
        final JsonObject filters = this.toFilters(sigma, types, statues);
        filters.put("code,i", codes);
        return Ux.Jooq.on(WTodoDao.class).fetchAndAsync(filters).compose(Ux::futureA);
    }

    private JsonObject toFilters(final String sigma, final JsonArray types, final JsonArray statues) {
        final JsonObject filters = new JsonObject();
        filters.put("sigma", sigma);
        if (Objects.nonNull(types)) {
            filters.put("type,i", types);
        }
        filters.put("status,i", statues);
        return filters;
    }

    @Override
    public Future<JsonArray> updateStatus(final Set<String> keys, final JsonObject params) {
        return Ux.Jooq.on(WTodoDao.class)
            .<WTodo>fetchInAsync(KName.KEY, Ut.toJArray(keys))
            .compose(Ux::futureA)
            .compose(Fn.ofJArray((todoArray) -> {
                /*
                 * Update status of WTodo
                 */
                List<WTodo> todoList = Ut.deserialize(todoArray, new TypeReference<List<WTodo>>() {
                });
                {
                    /*
                     * WTodo Auditor setting
                     */
                    todoList = todoList.stream().map(todo -> this.combineTodo(todo, params))
                        .collect(Collectors.toList());
                }
                return Ux.Jooq.on(WTodoDao.class)
                    .updateAsync(todoList)
                    .compose(Ux::futureA);
            }));
    }

    @Override
    public Future<JsonObject> updateStatus(final String key, final JsonObject params) {
        return Ux.Jooq.on(WTodoDao.class)
            .<WTodo>fetchByIdAsync(key)
            .compose(Ux::futureJ)
            .compose(Fn.ofJObject((todoJson) -> {
                /*
                 * Update status of WTodo
                 */
                WTodo todo = Ut.deserialize(todoJson, WTodo.class);
                {
                    todo = this.combineTodo(todo, params);
                }
                return Ux.Jooq.on(WTodoDao.class)
                    .updateAsync(todo)
                    .compose(Ux::futureJ);
            }));
    }

    private WTodo combineTodo(final WTodo todo, final JsonObject params) {
        if (Objects.isNull(todo)) {
            return null;
        } else {
            /*
             * WTodo Auditor setting
             */
            final String userId = params.getString(KName.USER_ID);
            if (Ut.isNotNil(userId)) {
                todo.setUpdatedBy(userId);
                todo.setUpdatedAt(LocalDateTime.now());
                /*
                 * WTodo once for `createdBy`
                 */
                if (Objects.isNull(todo.getCreatedBy())) {
                    todo.setCreatedBy(userId);
                }
            }
            /*
             * Status
             */
            if (params.containsKey(KName.STATUS)) {
                final String status = params.getString(KName.STATUS);
                todo.setStatus(status);
                if (TodoStatus.FINISHED.name().equals(status)) {
                    /*
                     * It means that WTodo has been updated by user
                     */
                    todo.setFinishedBy(todo.getUpdatedBy());
                }
            }
            return todo;
        }
    }

    @Override
    public Future<JsonObject> fetchTodo(final String key) {
        return Ux.Jooq.on(WTodoDao.class)
            .<WTodo>fetchByIdAsync(key)
            .compose(Ux::futureJ)
            .compose(Fn.ofJObject((todo) -> Ux.channel(Todo.class, () -> todo, channel -> {
                LOG.Init.info(this.getClass(), WfMsg.CHANNEL_TODO, channel.getClass().getName());
                /*
                 * X_TODO channel and data merged.
                 */
                final JsonObject params = Ut.elementSubset(todo,
                    KName.MODEL_ID, KName.MODEL_CATEGORY, KName.MODEL_KEY, KName.SIGMA);
                return channel.fetchAsync(key, params).compose(Fn.ofMerge(todo));
            })));
    }
}
