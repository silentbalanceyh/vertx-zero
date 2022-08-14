package io.vertx.tp.rbac.acl.rule.internal;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class UiDaoCompiler implements HAdmitCompiler {
    @Override
    public Future<JsonArray> ingest(final JsonObject qr, final JsonObject config) {
        return null;
    }
}
