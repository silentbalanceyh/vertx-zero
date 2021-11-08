package io.vertx.tp.workflow.uca.runner;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.camunda.bpm.engine.task.Task;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ProcEngine implements ProcOn {
    @Override
    public Future<Boolean> completeAsync(final String instanceId, final JsonObject params) {
        final TaskService service = WfPin.camundaTask();
        final List<Task> tasks = service.createTaskQuery()
            .processInstanceId(instanceId).list();
        tasks.forEach(task -> service.complete(task.getId(), params.getMap()));
        return Ux.futureT();
    }

    @Override
    public Future<Boolean> startAsync(final String process) {
        final RuntimeService service = WfPin.camundaRuntime();
        final ProcessInstantiationBuilder builder = service.createProcessInstanceByKey(process);
        final ProcessInstance instance = builder.execute();
        Wf.Log.infoMove(this.getClass(), "Process `{0}（id = {1}）` has been started!!!",
            process, instance.getProcessDefinitionId());
        return Ux.futureT();
    }
}
