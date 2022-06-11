package io.vertx.tp.workflow.uca.camunda;

import io.vertx.core.Future;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IoOldInstance extends AbstractIoOld<HistoricProcessInstance, ProcessInstance> {

    // ProcessInstance by instanceId
    @Override
    public Future<ProcessInstance> instance(final String instanceId) {
        if (Objects.isNull(instanceId)) {
            return Ux.future();
        }
        final RuntimeService service = WfPin.camundaRuntime();
        final ProcessInstance instance = service.createProcessInstanceQuery()
            .processInstanceId(instanceId).singleResult();
        return Ux.future(instance);
    }

    // HistoricProcessInstance by instanceId
    @Override
    public Future<HistoricProcessInstance> inverse(final String instanceId) {
        if (Objects.isNull(instanceId)) {
            return Ux.future();
        }
        final HistoryService service = WfPin.camundaHistory();
        final HistoricProcessInstance instance = service.createHistoricProcessInstanceQuery()
            .processInstanceId(instanceId).singleResult();
        return Ux.future(instance);
    }
}
