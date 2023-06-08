package io.mature.extension.uca.plugin.semi;

import io.mature.extension.scaffold.plugin.AbstractAfter;
import io.mature.extension.uca.elasticsearch.EsIndex;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/*
 * Es 专用处理，无关返回值
 */
public class AfterEs extends AbstractAfter {

    @Override
    public Future<JsonObject> afterAsync(final JsonObject record, final JsonObject options) {
        return this.fabric.inTo(record)
            .compose(EsIndex.create(this.operation(options), this.atom.identifier())::indexAsync)
            .compose(item -> Ux.future(record));
    }

    @Override
    public Future<JsonArray> afterAsync(final JsonArray records, final JsonObject options) {
        return this.fabric.inTo(records)
            .compose(EsIndex.create(this.operation(options), this.atom.identifier())::indexAsync)
            .compose(items -> Ux.future(records));
    }
}
