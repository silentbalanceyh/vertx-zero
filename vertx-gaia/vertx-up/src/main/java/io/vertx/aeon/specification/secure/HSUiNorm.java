package io.vertx.aeon.specification.secure;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HSUiNorm implements HSUi {
    @Override
    public Future<JsonObject> configure(final HPermit input) {

        return Future.succeededFuture(new JsonObject());
    }
}
