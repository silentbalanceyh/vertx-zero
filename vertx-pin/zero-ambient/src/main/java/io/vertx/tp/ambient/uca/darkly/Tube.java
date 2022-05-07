package io.vertx.tp.ambient.uca.darkly;

import cn.vertxup.ambient.domain.tables.pojos.XActivityRule;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.em.TubeType;
import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.uca.cache.Cc;

import java.util.Objects;
import java.util.Queue;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Tube {

    Cc<String, Tube> CC_TUBE = Cc.openThread();

    static Tube instance(final TubeType type) {
        if (Objects.isNull(type)) {
            // Empty Tube
            // Nothing to do here
            return CC_TUBE.pick(TubeEmpty::new, TubeEmpty.class.getName());
        }
        if (TubeType.ATOM == type) {
            // type = ATOM
            // 1. Compare field definition
            // 2. TubeExpr
            return CC_TUBE.pick(TubeVs::new, TubeVs.class.getName());
        } else if (TubeType.WORKFLOW == type) {
            // type = WORKFLOW
            // 1. TubeExpr Directly
            return CC_TUBE.pick(TubeExpr::new, TubeExpr.class.getName());
        } else {
            throw new _501NotSupportException(Tube.class);
        }
    }

    Future<JsonObject> traceAsync(JsonObject data, XActivityRule rule, Queue<String> serialQ);
}
