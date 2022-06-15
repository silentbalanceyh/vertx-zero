package io.vertx.tp.workflow.uca.conformity;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import cn.zeroup.macrocosm.cv.em.PassWay;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.runtime.WTask;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class GearForkJoin extends AbstractGear {

    private final JsonObject configuration = new JsonObject();

    public GearForkJoin() {
        super(PassWay.Fork);
    }

    @Override
    public Gear configuration(final JsonObject config) {
        if (Ut.notNil(config)) {
            this.configuration.mergeIn(config, true);
        }
        return this;
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
        taskMap.keySet().forEach(taskKey -> {
            // 0. Calculate the acceptedBy / toUser
            final JsonObject eachData = parameters.copy();
            final JsonObject assignData = assignMap.getOrDefault(taskKey, new JsonObject());
            eachData.mergeIn(assignData, true);
            // 1. Deserialize new WTodo
            final WTodo todo = Ux.fromJson(eachData, WTodo.class);

            // 2. Set relation
            todo.setTraceId(ticket.getKey());

            // 3. traceOrder = 1 and generate serial/code
            todo.setTraceOrder(1);
            this.todoSerial(todo, ticket, seed.getAndIncrement());
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
        taskMap.keySet().forEach(taskKey -> {
            // 0. Calculate the acceptedBy / toUser
            final JsonObject eachData = parameters.copy();
            final JsonObject assignData = assignMap.getOrDefault(taskKey, new JsonObject());
            eachData.mergeIn(assignData, true);
            // 1. Deserialize new WTodo
            final WTodo generated = Ux.fromJson(eachData, WTodo.class);

            // 2. Set relation
            generated.setTraceId(ticket.getKey());

            // 3. traceOrder = 1 and generate serial/code
            generated.setTraceOrder(todo.getTraceOrder() + 1);
            this.todoSerial(todo, ticket, seed.getAndIncrement());

            // 4. Set todo auditor information
            this.todoAuditor(generated, todo);
            todos.add(generated);
        });
        return Ux.future(todos);
    }

    /*
     *  The format: <Ticket Serial>-<traceOrder><sequence>
     *  Here the `sequence` means current generated all size from `01 ~ XX`.
     */
    private void todoSerial(final WTodo todo, final WTicket ticket, final int sequence) {
        final String serial = ticket.getCode() + "-" +
            Ut.fromAdjust(todo.getTraceOrder(), 2) +
            Ut.fromAdjust(sequence, 2);
        todo.setCode(serial);
        todo.setSerial(serial);
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
     *         "taskKey": "...",
     *         "taskId": "...",
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
            final String toUser = Ut.visitT(parameters, path);
            // 1.2. Data Building
            final JsonObject value = new JsonObject();
            value.put(KName.Flow.TASK_ID, task.getId());
            value.put(KName.Flow.TASK_KEY, task.getTaskDefinitionKey());
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
