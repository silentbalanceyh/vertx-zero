package io.vertx.tp.workflow.refine;

import io.vertx.core.Future;
import io.vertx.tp.error._404ProcessMissingException;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class WfFlow {

    static Future<ProcessDefinition> processByKey(final String definitionKey) {
        final RepositoryService service = WfPin.camundaRepository();
        final ProcessDefinition definition = service.createProcessDefinitionQuery()
            // New Version for each
            .latestVersion().processDefinitionKey(definitionKey).singleResult();
        return processInternal(definition, definitionKey);
    }

    static Future<ProcessDefinition> processById(final String definitionId) {
        final RepositoryService service = WfPin.camundaRepository();
        final ProcessDefinition definition = service.getProcessDefinition(definitionId);
        return processInternal(definition, definitionId);
    }

    private static Future<ProcessDefinition> processInternal(final ProcessDefinition definition, final String key) {
        if (Objects.isNull(definition)) {
            // Error
            return Ux.thenError(_404ProcessMissingException.class, WfFlow.class, key);
        } else {
            return Ux.future(definition);
        }
    }
}
