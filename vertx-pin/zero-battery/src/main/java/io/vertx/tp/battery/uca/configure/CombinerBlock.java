package io.vertx.tp.battery.uca.configure;

import cn.vertxup.battery.domain.tables.pojos.BBag;
import cn.vertxup.battery.domain.tables.pojos.BBlock;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class CombinerBlock implements Combiner<BBag, List<BBlock>> {
    @Override
    public Future<JsonObject> configure(final BBag bag, final List<BBlock> map) {
        final JsonObject data = new JsonObject();
        final JsonObject metadata = new JsonObject();
        map.forEach(block -> {
            final JsonObject uiContent = Ut.toJObject(block.getUiContent());
            final JsonObject uiConfig = Ut.toJObject(block.getUiConfig());
            final JsonObject defined = uiConfig.getJsonObject(KName.FIELD, new JsonObject());
            metadata.mergeIn(defined, true);
            data.mergeIn(uiContent, true);
        });
        // key for bag
        data.put(KName.KEY, bag.getKey());
        data.put("__" + KName.METADATA, metadata);
        return Ux.future(data);
    }
}
