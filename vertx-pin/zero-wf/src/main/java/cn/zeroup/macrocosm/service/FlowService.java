package cn.zeroup.macrocosm.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FlowService implements FlowStub {
    @Override
    public Future<JsonObject> fetchFlow(final String code, final String sigma) {
        return null;
    }
}
