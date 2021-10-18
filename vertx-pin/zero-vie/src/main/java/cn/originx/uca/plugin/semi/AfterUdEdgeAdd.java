package cn.originx.uca.plugin.semi;

import cn.originx.refine.Ox;
import cn.originx.scaffold.plugin.AbstractAfter;
import cn.originx.uca.graphic.Pixel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.ChangeFlag;
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
