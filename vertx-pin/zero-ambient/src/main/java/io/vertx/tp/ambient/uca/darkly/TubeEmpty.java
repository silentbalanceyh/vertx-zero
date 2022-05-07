package io.vertx.tp.ambient.uca.darkly;

import cn.vertxup.ambient.domain.tables.pojos.XActivityRule;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

import java.util.Queue;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class TubeEmpty implements Tube {
    @Override
    public Future<JsonObject> traceAsync(final JsonObject data, final XActivityRule rule, final Queue<String> serialQ) {
        return Ux.future(data);
    }
}
