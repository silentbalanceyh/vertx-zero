package io.vertx.mod.battery.uca.configure;

import cn.vertxup.battery.domain.tables.pojos.BBag;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class CombinerBag implements Combiner<BBag, ConcurrentMap<String, BBag>> {
    @Override
    public Future<JsonObject> configure(final BBag bag, final ConcurrentMap<String, BBag> map) {
        final JsonObject uiConfig = Ut.toJObject(bag.getUiConfig());
        final JsonObject formConfig = Ut.visitJObject(uiConfig, KName.CONFIG, "_form");
        // ui capture
        final JsonArray ui = Ut.valueJArray(formConfig, "ui");
        if (Ut.isNil(ui)) {
            return Ux.future(uiConfig);
        }

        // ui calculation on children
        final ConcurrentMap<String, JsonArray> mapJ = new ConcurrentHashMap<>();
        map.forEach((code, itemBag) -> {
            final JsonObject itemJ = Ut.toJObject(itemBag.getUiConfig());
            final JsonArray itemUi = Ut.visitJArray(itemJ, KName.CONFIG, "_form", "ui");
            if (Ut.isNotNil(itemUi)) {
                mapJ.put(code, itemUi);
            }
        });

        // ui replace, here the `Ref` means reference and will be replaced directly
        final JsonArray uiTo = new JsonArray();
        ui.forEach(item -> {
            if (item instanceof JsonArray) {
                // JsonArray
                uiTo.add(item);
            } else {
                // String Literal
                uiTo.addAll(mapJ.getOrDefault(item.toString(), new JsonArray()));
            }
        });
        final JsonObject configRef = uiConfig.getJsonObject(KName.CONFIG);
        final JsonObject formRef = configRef.getJsonObject("_form");
        formRef.put("ui", uiTo);

        // Double Check for Ensure
        {
            configRef.put("_form", formRef);
            uiConfig.put(KName.CONFIG, configRef);
        }
        // combiner
        final Combiner<JsonObject, BBag> bagCombiner = Combiner.outBag();
        final Combiner<JsonObject, Collection<BBag>> childrenCombiner = Combiner.outChildren();
        return Ux.future(uiConfig)
            // bag
            .compose(response -> bagCombiner.configure(response, bag))
            // bag -> children
            .compose(response -> childrenCombiner.configure(response, map.values()));
    }
}
