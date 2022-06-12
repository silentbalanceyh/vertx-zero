package io.vertx.tp.workflow.uca.camunda;

import io.vertx.core.Future;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IoTask extends AbstractIo<Task> {
    @Override
    public Future<Task> run(final String taskId) {
        final TaskService service = WfPin.camundaTask();
        final Task task = service.createTaskQuery()
            .taskId(taskId)
            .active().singleResult();
        return Ux.future(task);
    }

    @Override
    public Future<Task> child(final String instanceId) {
        final TaskService service = WfPin.camundaTask();
        final Task task = service.createTaskQuery()
            .initializeFormKeys()
            .processInstanceId(instanceId)
            .active().singleResult();
        return Ux.future(task);
    }
}
