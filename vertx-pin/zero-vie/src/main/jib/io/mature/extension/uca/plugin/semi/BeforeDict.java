package io.mature.extension.uca.plugin.semi;

import io.mature.extension.scaffold.plugin.AbstractBefore;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BeforeDict extends AbstractBefore {
    @Override
    public Future<JsonObject> beforeAsync(final JsonObject record, final JsonObject options) {
        return this.fabric.inFrom(record);
    }

    @Override
    public Future<JsonArray> beforeAsync(final JsonArray records, final JsonObject options) {
        return this.fabric.inFrom(records);
    }
}
