package io.vertx.mod.battery.uca.configure;

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
        data.put(KName.KEY_P, bag.getKey());
        // __metadata for definition
        data.put(KName.__.METADATA, metadata);
        // major data
        final Combiner<JsonObject, BBag> combiner = Combiner.outDao();
        return combiner.configure(data, bag).compose(recordData -> {
            if (Ut.isNotNil(recordData)) {
                data.mergeIn(recordData);
            }
            return Ux.future(data);
        });
    }
}
