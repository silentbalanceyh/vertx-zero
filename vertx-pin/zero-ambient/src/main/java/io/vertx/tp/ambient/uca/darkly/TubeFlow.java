package io.vertx.tp.ambient.uca.darkly;

import cn.vertxup.ambient.domain.tables.pojos.XActivityRule;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class TubeFlow implements Tube {
    @Override
    public Future<JsonObject> traceAsync(final JsonObject data, final XActivityRule rule) {
        /*
         * For Activity Generation
         * 1) Extract `HAtom` for model
         * 2) Extract data to ( NEW / OLD ) data twins ( Refactor )
         * 3) Build default Activity JsonObject and ActivityChange
         */
        return Ux.future(data);
    }
}
