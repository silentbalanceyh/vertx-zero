package io.vertx.tp.battery.uca;

import cn.vertxup.battery.domain.tables.pojos.BBag;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CombinerBag implements Combiner<BBag, ConcurrentMap<String, BBag>> {
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
            if (Ut.notNil(itemUi)) {
                mapJ.put(code, itemUi);
            }
        });

        // ui replace
        final JsonArray uiTo = new JsonArray();
        ui.forEach(key -> uiTo.addAll(mapJ.getOrDefault(key.toString(), new JsonArray())));
        final JsonObject configRef = uiConfig.getJsonObject(KName.CONFIG);
        final JsonObject formRef = configRef.getJsonObject("_form");
        formRef.put("ui", uiTo);
        return Ux.future(uiConfig);
    }
}
