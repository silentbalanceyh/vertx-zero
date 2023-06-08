package io.vertx.mod.workflow.uca.conformity;

import cn.vertxup.workflow.cv.em.PassWay;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.runtime.WTask;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
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
public class GearMulti extends AbstractGear {
    public GearMulti() {
        super(PassWay.Multi);
    }

    @Override
    public Future<List<WTodo>> todoAsync(final JsonObject parameters, final WTask wTask, final WTicket ticket) {
        final List<Task> taskList = wTask.multi();
        if (taskList.isEmpty()) {
            return Ux.futureL();
        }

        // Assign Map
        final ConcurrentMap<String, JsonObject> assignMap = this.buildAssign(parameters, taskList, false);
        final List<Future<WTodo>> queue = new ArrayList<>();

        // Gain
        final Gain starter = Gain.starter(ticket);
        taskList.forEach(task -> {
            // 0. Calculate the acceptedBy / toUser
            final JsonObject eachData = parameters.copy();
            final JsonObject assignData = assignMap.getOrDefault(task.getTaskDefinitionKey(), new JsonObject());
            eachData.mergeIn(assignData, true);

            // 1. Deserialize new WTodo
            queue.add(starter.buildAsync(eachData, task, null));
        });

        return Fn.combineT(queue).compose(generatedQ -> {

            final AtomicInteger seed = new AtomicInteger(1);
            generatedQ.forEach(generated -> {
                // 2. Select Method to Set Serial
                generated.setSerialFork(null);
                this.buildSerial(generated, ticket, seed.getAndIncrement());
            });
            return Ux.future(generatedQ);
        });
    }

    @Override
    public Future<List<WTodo>> todoAsync(final JsonObject parameters, final WTask wTask, final WTicket ticket, final WTodo todo) {
        final List<Task> taskList = wTask.multi();
        if (taskList.isEmpty()) {
            return Ux.futureL();
        }
        final ConcurrentMap<String, JsonObject> assignMap = this.buildAssign(parameters, taskList, true);
        final List<Future<WTodo>> queue = new ArrayList<>();

        // Gain
        final Gain generator = Gain.generator(ticket);
        taskList.forEach(task -> {
            // 0. Calculate the acceptedBy / toUser
            final JsonObject eachData = parameters.copy();
            final JsonObject assignData = assignMap.getOrDefault(task.getTaskDefinitionKey(), new JsonObject());
            eachData.mergeIn(assignData, true);

            // 1. Deserialize new WTodo
            queue.add(generator.buildAsync(eachData, task, todo));
        });
        return Fn.combineT(queue).compose(generatedQ -> {

            final AtomicInteger seed = new AtomicInteger(1);
            generatedQ.forEach(generated -> {
                // 2. Select Method to Set Serial
                generated.setSerialFork(todo.getSerialFork());
                this.buildSerial(generated, ticket, seed.getAndIncrement());
            });
            return Ux.future(generatedQ);
        });
    }

    /*
     * TaskKey = toUser ( String ), Here the data structure is as following:
     *
     * parameters: {
     *     "toUser": [
     *         "value1",
     *         "value2",
     *         "..."
     *     ]
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
    private ConcurrentMap<String, JsonObject> buildAssign(final JsonObject parameters, final List<Task> tasks, final boolean generation) {
        // 1. Iterator to build the response
        final ConcurrentMap<String, JsonObject> response = new ConcurrentHashMap<>();
        final JsonArray toUser = Ut.valueJArray(parameters, KName.Auditor.TO_USER);
        // 2. Iterate the data structure by List / Index
        final int size = tasks.size();
        Ut.itJArray(toUser, String.class, (userKey, index) -> {
            if (index < (size - 1)) {
                // Ok for generate
                final Task task = tasks.get(index);
                final JsonObject value = new JsonObject();
                value.put(KName.Flow.TASK_ID, task.getId());
                value.put(KName.Flow.TASK_KEY, task.getTaskDefinitionKey());
                if (generation) {
                    value.putNull(KName.Auditor.TO_USER);
                } else {
                    value.put(KName.Auditor.TO_USER, toUser);
                }
                value.put(KName.Auditor.ACCEPTED_BY, toUser);
                response.put(task.getTaskDefinitionKey(), value);
            }
        });

        parameters.remove(KName.Auditor.TO_USER);
        parameters.remove(KName.Auditor.ACCEPTED_BY);
        return response;
    }
}
