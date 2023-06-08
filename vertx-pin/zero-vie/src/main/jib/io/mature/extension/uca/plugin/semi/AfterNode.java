package io.mature.extension.uca.plugin.semi;

import io.mature.extension.scaffold.plugin.AbstractAfter;
import io.mature.extension.uca.graphic.Pixel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/**
 * 图引擎专用处理器
 * 节点后置添加
 */
public class AfterNode extends AbstractAfter {
    @Override
    public Future<JsonObject> afterAsync(final JsonObject record, final JsonObject options) {
        return Pixel.node(this.operation(options), this.atom.identifier()).drawAsync(record)
            .compose(processed -> Ux.future(record));
    }

    @Override
    public Future<JsonArray> afterAsync(final JsonArray records, final JsonObject options) {
        return Pixel.node(this.operation(options), this.atom.identifier()).drawAsync(records)
            .compose(processed -> Ux.future(records));
    }
}
