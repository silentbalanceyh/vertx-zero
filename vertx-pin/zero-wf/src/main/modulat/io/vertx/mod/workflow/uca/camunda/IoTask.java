package io.vertx.mod.workflow.uca.camunda;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.init.WfPin;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;

import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IoTask extends AbstractIo<Task> {
    @Override
    public Future<Task> run(final String taskId) {
        final TaskService service = WfPin.camundaTask();
        return Ux.future(service.createTaskQuery()
            /*
             * Fix Issue:
             * The form key / form reference is not initialized. You must call initializeFormKeys()
             * on the task query before you can retrieve the form key or the form reference.
             */
            .initializeFormKeys()
            .taskId(taskId)
            .active().singleResult());
    }

    @Override
    public Future<Task> child(final String instanceId) {
        final TaskService service = WfPin.camundaTask();
        return Ux.future(service.createTaskQuery()
            /*
             * Fix Issue:
             * The form key / form reference is not initialized. You must call initializeFormKeys()
             * on the task query before you can retrieve the form key or the form reference.
             */
            .initializeFormKeys()
            .processInstanceId(instanceId)
            .active().singleResult());
    }

    @Override
    public Future<List<Task>> children(final String instanceId) {
        final TaskService service = WfPin.camundaTask();
        return Ux.future(service.createTaskQuery()
            /*
             * Fix Issue:
             * The form key / form reference is not initialized. You must call initializeFormKeys()
             * on the task query before you can retrieve the form key or the form reference.
             */
            .initializeFormKeys()
            .processInstanceId(instanceId)
            .active().list());
    }

    @Override
    public Future<JsonObject> out(final JsonObject workflow, final Task task) {
        Objects.requireNonNull(task);
        workflow.put(KName.MULTIPLE, Boolean.FALSE);
        workflow.put(KName.Flow.TASK, task.getTaskDefinitionKey());
        workflow.put(KName.Flow.TASK_NAME, task.getName());
        return Ux.future(workflow);
    }
}
