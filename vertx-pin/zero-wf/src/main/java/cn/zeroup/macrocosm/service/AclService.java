package cn.zeroup.macrocosm.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AclService implements AclStub {
    @Override
    public Future<JsonObject> authorize(final ProcessDefinition definition, final ProcessInstance instance, final String userId) {
        return Ux.futureJ();
    }
}
