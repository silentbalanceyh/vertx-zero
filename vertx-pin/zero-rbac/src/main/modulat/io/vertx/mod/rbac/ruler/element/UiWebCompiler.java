package io.vertx.mod.rbac.ruler.element;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class UiWebCompiler implements HAdmitCompiler {
    @Override
    public Future<JsonArray> ingest(final JsonObject qr, final JsonObject config) {
        return Ux.futureA();
    }
}
