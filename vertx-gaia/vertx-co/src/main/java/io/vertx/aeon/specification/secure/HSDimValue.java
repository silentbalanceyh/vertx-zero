package io.vertx.aeon.specification.secure;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

public class HSDimValue extends AbstractSDim {

    protected Future<JsonArray> compress(final HPermit input, final JsonArray source){
        // 默认压缩，什么都不做
        return Future.succeededFuture(source);
    }
}
