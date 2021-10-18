package cn.originx.uca.plugin.semi;

import cn.originx.scaffold.plugin.AbstractAfter;
import cn.originx.uca.graphic.Pixel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/**
 * 图引擎专用处理器
 */
public class AfterEdge extends AbstractAfter {
    @Override
    public Future<JsonObject> afterAsync(final JsonObject record, final JsonObject options) {
        return Pixel.edge(this.operation(options), this.atom.identifier()).drawAsync(record)
            .compose(processed -> Ux.future(record));
    }

    @Override
    public Future<JsonArray> afterAsync(final JsonArray records, final JsonObject options) {
        return Pixel.edge(this.operation(options), this.atom.identifier()).drawAsync(records)
            .compose(processed -> Ux.future(records));
    }
}
