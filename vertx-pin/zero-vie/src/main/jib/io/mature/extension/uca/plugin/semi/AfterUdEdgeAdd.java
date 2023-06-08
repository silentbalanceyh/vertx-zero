package io.mature.extension.uca.plugin.semi;

import io.horizon.eon.em.typed.ChangeFlag;
import io.mature.extension.refine.Ox;
import io.mature.extension.scaffold.plugin.AbstractAfter;
import io.mature.extension.uca.graphic.Pixel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AfterUdEdgeAdd extends AbstractAfter {
    @Override
    public Future<JsonObject> afterAsync(final JsonObject record, final JsonObject options) {
        final JsonArray normalized = Ox.toLinker(record);
        /*
         * 先删除，再添加
         */
        return Pixel.edge(ChangeFlag.ADD, this.atom.identifier()).drawAsync(normalized)
            .compose(processed -> Ux.future(record));
    }

    @Override
    public Future<JsonArray> afterAsync(final JsonArray records, final JsonObject options) {
        final JsonArray normalized = Ox.toLinker(records);
        return Pixel.edge(ChangeFlag.ADD, this.atom.identifier()).drawAsync(normalized)
            .compose(processed -> Ux.future(records));
    }
}
