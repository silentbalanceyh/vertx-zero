package io.vertx.mod.workflow.uca.camunda;

import cn.vertxup.workflow.cv.WfPool;
import io.vertx.mod.workflow.error._404ProcessMissingException;
import io.vertx.mod.workflow.init.WfPin;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractIo<I> implements Io<I> {
    @Override
    public ProcessDefinition inProcess(final String idOrKey) {
        Objects.requireNonNull(idOrKey);
        final ProcessDefinition result = WfPool.CC_DEFINITION.pick(() -> {
            final RepositoryService service = WfPin.camundaRepository();
            /*
             * Fetch ProcessDefinition by two dim:
             * 1) By id first
             * 2) By key then
             *
             * In Camunda, the specification describe the following situations:
             * 1. Each id will be unique workflow ( ProcessDefinition instance )
             * 2. Each key will be more than one workflow because of different version, but
             *    in Zero workflow engine, the unique one should be here for common usage
             *
             */
            final ProcessDefinitionQuery query = service
                .createProcessDefinitionQuery()
                // .latestVersion()
                /*
                 * Fix issue:「Camunda Exception」
                 * org.camunda.bpm.engine.ProcessEngineException:
                 * Calling latest() can only be used in combination with key(String) and keyLike(String) or name(String) and nameLike(String)
                 */
                .processDefinitionId(idOrKey);
            /*
             * Old Version:
             * final ProcessDefinition definition = service.getProcessDefinition(idOrKey);
             *
             * Because of above code, the system will meet following exception:
             *
             * Fix issue:「Camunda Exception」
             * - org.camunda.bpm.engine.exception.NullValueException:
             *   no deployed process definition found with id 'process.oa.trip': processDefinition is null
             */
            final ProcessDefinition definition = query.singleResult();
            if (Objects.nonNull(definition)) {
                return definition;
            }
            return service
                .createProcessDefinitionQuery()
                .latestVersion()
                .processDefinitionKey(idOrKey).singleResult();
        }, idOrKey);
        if (Objects.isNull(result)) {
            throw new _404ProcessMissingException(this.getClass(), idOrKey);
        }
        return result;
    }

    @Override
    public ProcessInstance inInstance(final String instanceId) {
        final RuntimeService service = WfPin.camundaRuntime();
        return service.createProcessInstanceQuery()
            .processInstanceId(instanceId).singleResult();
    }

    @Override
    public HistoricProcessInstance inHistoric(final String instanceId) {
        final HistoryService service = WfPin.camundaHistory();
        return service.createHistoricProcessInstanceQuery()
            .processInstanceId(instanceId).singleResult();
    }
}
