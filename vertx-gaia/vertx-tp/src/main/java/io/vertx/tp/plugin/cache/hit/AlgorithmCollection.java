package io.vertx.tp.plugin.cache.hit;

import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class AlgorithmCollection extends AbstractL1Algorithm {

    @Override
    public String dataType() {
        return CNODE_LIST;
    }

    @Override
    public void dataProcess(final ConcurrentMap<String, Object> resultMap, final JsonObject jsonBody, final boolean isRefer) {
    }

    @Override
    public void dataRefer(final ConcurrentMap<String, Object> resultMap, final JsonObject jsonBody) {
    }

    @Override
    public void dataTree(final ConcurrentMap<String, Object> resultMap, final JsonObject jsonBody) {
    }
}
