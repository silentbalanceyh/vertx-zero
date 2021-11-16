package io.vertx.tp.workflow.refine;

import cn.zeroup.macrocosm.cv.WfCv;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._404ProcessMissingException;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class WfFlow {

    // --------------- Process Fetching ------------------
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

    // --------------- Form Fetching ------------------
    static JsonObject formInput(final JsonObject form, final String sigma) {
        final String definition = form.getString(KName.Flow.DEFINITION_KEY);
        final JsonObject parameters = new JsonObject();
        final String code = form.getString(KName.CODE);
        final String configFile = WfCv.ROOT_FOLDER + "/" + definition + "/" + code + ".json";
        // Dynamic Processing
        if (Ut.ioExist(configFile)) {
            parameters.put(KName.DYNAMIC, Boolean.FALSE);
            parameters.put(KName.CODE, configFile);
        } else {
            parameters.put(KName.DYNAMIC, Boolean.TRUE);
            parameters.put(KName.CODE, code);
            parameters.put(KName.SIGMA, sigma);
        }
        return parameters;
    }
}
