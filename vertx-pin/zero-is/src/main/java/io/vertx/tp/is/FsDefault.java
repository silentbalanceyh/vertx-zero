package io.vertx.tp.is;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FsDefault implements Fs {
    @Override
    public Future<JsonArray> mkdir(final JsonArray data, final JsonObject config) {
        return Ux.future(data);
    }
}
