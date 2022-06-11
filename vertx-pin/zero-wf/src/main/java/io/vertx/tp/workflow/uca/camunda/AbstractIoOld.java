package io.vertx.tp.workflow.uca.camunda;

import cn.zeroup.macrocosm.cv.WfPool;
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
public abstract class AbstractIoOld<P, T> implements IoOld<P, T> {
    /*
     * Fetch ProcessDefinition instance by
     * 1) By id first
     * 2) By key then
     */
    @Override
    public Future<ProcessDefinition> definition(final String key) {
        Objects.requireNonNull(key);
        final ProcessDefinition result = WfPool.CC_DEFINITION.pick(() -> {
            final RepositoryService service = WfPin.camundaRepository();
            // By id first
            final ProcessDefinition definition = service.getProcessDefinition(key);
            if (Objects.nonNull(definition)) {
                return definition;
            }
            // By key then
            return service.createProcessDefinitionQuery()
                // New Version for each
                .latestVersion().processDefinitionKey(key).singleResult();
        }, key);
        if (Objects.isNull(result)) {
            return Ux.thenError(_404ProcessMissingException.class, this.getClass(), key);
        }
        return Ux.future(result);
    }
}
