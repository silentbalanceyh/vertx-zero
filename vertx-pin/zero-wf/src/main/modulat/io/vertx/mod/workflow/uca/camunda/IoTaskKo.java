package io.vertx.mod.workflow.uca.camunda;

import io.vertx.core.Future;
import io.vertx.mod.workflow.init.WfPin;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IoTaskKo extends AbstractIo<Task> {

    public static String DELEGATE_DELETE = "zero.delegate.deleted";

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
            .taskAssigneeNotIn(DELEGATE_DELETE)
            .processInstanceId(instanceId)
            .active().list());
    }
}
