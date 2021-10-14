package cn.originx.uca.modello;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.modular.plugin.OComponent;
import io.vertx.up.atom.Kv;
import io.vertx.up.commune.Record;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class OutMulti implements OComponent {
    public static final String DEFAULT_DELIMITER = ",";
    private static final Annal LOGGER = Annal.get(OutMulti.class);
    private static final String DELIMITER = "delimiter";

    @Override
    @SuppressWarnings("unchecked")
    public Object after(final Kv<String, Object> kv, final Record record, final JsonObject combineData) {
        final JsonObject sourceNorm = Ut.sureJObject(combineData.getJsonObject(KName.SOURCE_NORM));
        Ut.ifJValue(sourceNorm, DELIMITER, DEFAULT_DELIMITER);
        Object value = kv.getValue();
        Object rt = value;
        if (Ut.notNil(sourceNorm)) {
            /*
             * Purpose: changing such as [] to "", ["01-xxx","02-yyy"] to "01,02"
             */
            /* Step 1. changing such as ["01-xxx","02-yyy"] to ["01","02"] */
            JsonArray suffixCutOff = new JsonArray();
            JsonArray src = (JsonArray) value;
            src.forEach(str -> {
                String option = String.valueOf(str);
                if (option.contains("-")) {
                    option = option.replaceAll("-[\\s\\S]+", "");
                }
                suffixCutOff.add(option);
            });
            /* Step 2. changing such as ["01","02"] to "01,02" */
            rt = String.join(sourceNorm.getString(DELIMITER), suffixCutOff.getList());
        }
        return rt;
    }
}
