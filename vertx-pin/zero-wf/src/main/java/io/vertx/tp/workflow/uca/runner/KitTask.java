package io.vertx.tp.workflow.uca.runner;

import io.vertx.tp.workflow.init.WfPin;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class KitTask {

    Task byInstanceId(final String instanceId) {
        final TaskService service = WfPin.camundaTask();
        return service.createTaskQuery()
            .initializeFormKeys()
            .processInstanceId(instanceId)
            .active().singleResult();
    }

    Task byTaskId(final String taskId) {
        final TaskService service = WfPin.camundaTask();
        return service.createTaskQuery()
            .taskId(taskId).singleResult();
    }
}
