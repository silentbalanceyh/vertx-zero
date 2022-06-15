package io.vertx.tp.workflow.uca.conformity;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.PassWay;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.runtime.WTask;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class GearForkJoin extends AbstractGear {

    public GearForkJoin() {
        super(PassWay.Fork);
    }

    @Override
    public Future<List<WTodo>> todoAsync(final JsonObject parameters, final WTask wTask, final WTicket ticket) {
        final ConcurrentMap<String, Task> taskMap = wTask.fork();
        if (taskMap.isEmpty()) {
            return Ux.futureL();
        }
        final ConcurrentMap<String, JsonObject> assignMap = this.todoAssign(parameters, taskMap, false);
        final List<WTodo> todos = new ArrayList<>();
        final AtomicInteger seed = new AtomicInteger(1);
        taskMap.forEach((taskKey, task) -> {
            // 0. Calculate the acceptedBy / toUser
            final JsonObject eachData = parameters.copy();
            final JsonObject assignData = assignMap.getOrDefault(taskKey, new JsonObject());
            eachData.mergeIn(assignData, true);

            // 1. Deserialize new WTodo
            final WTodo todo = this.todoStart(parameters, ticket, task);
            // Duplicate entry 'da89a198-a9fb-40f8-a3cc-6a77df8cea22' for key 'PRIMARY'
            todo.setKey(UUID.randomUUID().toString());

            // 2. Serial Generation
            todo.setSerialFork(String.valueOf(seed.getAndIncrement()));
            this.todoSerial(todo, ticket, null);

            todos.add(todo);
        });
        return Ux.future(todos);
    }

    @Override
    public Future<List<WTodo>> todoAsync(final JsonObject parameters, final WTask wTask, final WTicket ticket, final WTodo todo) {
        final ConcurrentMap<String, Task> taskMap = wTask.fork();
        if (taskMap.isEmpty()) {
            return Ux.futureL();
        }
        final ConcurrentMap<String, JsonObject> assignMap = this.todoAssign(parameters, taskMap, true);
        final List<WTodo> todos = new ArrayList<>();
        final AtomicInteger seed = new AtomicInteger(1);
        taskMap.forEach((taskKey, task) -> {
            // 0. Calculate the acceptedBy / toUser
            final JsonObject eachData = parameters.copy();
            final JsonObject assignData = assignMap.getOrDefault(taskKey, new JsonObject());
            eachData.mergeIn(assignData, true);

            // 1. Deserialize new WTodo
            final WTodo generated = this.todoGenerate(eachData, ticket, task, todo);

            // 2. Serial Generation
            final String serialFork = todo.getSerialFork();
            if (Ut.isNil(serialFork)) {
                generated.setSerialFork(String.valueOf(seed.getAndIncrement()));
            } else {
                generated.setSerialFork(serialFork);
            }
            this.todoSerial(generated, ticket, null);

            // 2. Select Method to set serial
            todos.add(generated);
        });
        return Ux.future(todos);
    }

    /*
     * TaskKey = toUser ( String ), Here the data structure is as following:
     *
     * Configuration: {
     *     "taskKey1": "path1",
     *     "taskKey2": "path2",
     *     "...": "..."
     * }
     *
     * parameters: {
     *     "toUser": {
     *         "path1": "value1",
     *         "path2": "value2",
     *         "...": "..."
     *     }
     * }
     *
     * Also sync the `accepted` and `toUser`, the result data structure:
     * {
     *     "taskKey": {
     *         "toUser": "...",
     *         "accepted": "..."
     *     }
     * }
     * Also this method should remove `accepted` and `toUser` from parameters
     */
    private ConcurrentMap<String, JsonObject> todoAssign(final JsonObject parameters, final ConcurrentMap<String, Task> taskMap, final boolean generation) {
        // 1. Iterator to build the response
        final ConcurrentMap<String, JsonObject> response = new ConcurrentHashMap<>();
        taskMap.forEach((taskKey, task) -> {
            // 1.1. Path extract from configuration
            final String path = this.configuration.getString(taskKey);
            Wf.Log.infoTransition(this.getClass(), "Task key = {0} will parse {1}", taskKey, path);
            // 1.2. Data Building
            final JsonObject value = new JsonObject();
            final String toUser = Ut.visitTSmart(parameters, path);
            if (generation) {
                value.putNull(KName.Flow.Auditor.TO_USER);
            } else {
                value.put(KName.Flow.Auditor.TO_USER, toUser);
            }
            value.put(KName.Flow.Auditor.ACCEPTED_BY, toUser);
            response.put(taskKey, value);
        });

        parameters.remove(KName.Flow.Auditor.TO_USER);
        parameters.remove(KName.Flow.Auditor.ACCEPTED_BY);
        return response;
    }
}
