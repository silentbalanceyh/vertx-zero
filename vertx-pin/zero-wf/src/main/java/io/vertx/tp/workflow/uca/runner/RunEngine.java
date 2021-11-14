package io.vertx.tp.workflow.uca.runner;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.camunda.bpm.engine.task.Task;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class RunEngine implements RunOn {
    @Override
    public Future<ProcessInstance> moveAsync(final ProcessInstance instance, final JsonObject params) {
        Objects.requireNonNull(instance);
        final TaskService service = WfPin.camundaTask();
        final Task task = service.createTaskQuery().active()
            .processInstanceId(instance.getId()).singleResult();
        Objects.requireNonNull(task);
        service.complete(task.getId(), params.getMap());
        Wf.Log.infoMove(this.getClass(), "（Move） `{0}（id = {1}）` has been started!!!",
            instance.getId(), instance.getProcessDefinitionId());
        return Ux.future(instance);
    }

    @Override
    public Future<ProcessInstance> startAsync(final String definitionKey, final JsonObject params) {
        final RuntimeService service = WfPin.camundaRuntime();
        final ProcessInstantiationBuilder builder = service.createProcessInstanceByKey(definitionKey);
        if (Ut.notNil(params)) {
            // Parameters Filling
            builder.setVariables(params.getMap());
        }
        final ProcessInstance instance = builder.execute();
        Wf.Log.infoMove(this.getClass(), "（Start） `{0}（id = {1}）` has been started!!!",
            instance.getId(), instance.getProcessDefinitionId());
        return Ux.future(instance);
    }
}
