package io.vertx.tp.optic;

import io.vertx.aeon.specification.query.HQBE;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Envelop;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class UiViewHQBE implements HQBE {
    @Override
    public Future<Envelop> before(final JsonObject qbeJ, final Envelop envelop) {

        return Future.succeededFuture(envelop);
    }
}
