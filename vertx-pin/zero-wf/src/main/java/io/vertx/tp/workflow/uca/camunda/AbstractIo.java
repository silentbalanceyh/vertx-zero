package io.vertx.tp.workflow.uca.camunda;

import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.tp.error._404ProcessMissingException;
import io.vertx.tp.workflow.init.WfPin;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractIo<I, O> implements Io<I, O> {
    @Override
    public ProcessDefinition pDefinition(final String idOrKey) {
        Objects.requireNonNull(idOrKey);
        final ProcessDefinition result = WfPool.CC_DEFINITION.pick(() -> {
            final RepositoryService service = WfPin.camundaRepository();
            // By id first
            final ProcessDefinition definition = service.getProcessDefinition(idOrKey);
            if (Objects.nonNull(definition)) {
                return definition;
            }
            // By key then
            return service.createProcessDefinitionQuery()
                // New Version for each
                .latestVersion().processDefinitionKey(idOrKey).singleResult();
        }, idOrKey);
        if (Objects.isNull(result)) {
            throw new _404ProcessMissingException(this.getClass(), idOrKey);
        }
        return result;
    }

    @Override
    public ProcessInstance pInstance(final String instanceId) {
        final RuntimeService service = WfPin.camundaRuntime();
        return service.createProcessInstanceQuery()
            .processInstanceId(instanceId).singleResult();
    }

    @Override
    public HistoricProcessInstance pHistory(final String instanceId) {
        final HistoryService service = WfPin.camundaHistory();
        return service.createHistoricProcessInstanceQuery()
            .processInstanceId(instanceId).singleResult();
    }
}
