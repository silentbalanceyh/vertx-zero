package cn.originx.uca.modello;

import io.horizon.specification.modeler.Record;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.modular.plugin.OComponent;
import io.vertx.up.atom.Kv;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class OutCompress implements OComponent {
    @Override
    public Object after(final Kv<String, Object> kv, final Record record, final JsonObject combineData) {
        /*
         * {
         *     "name": "当前属性名称",
         *     "format": "当前属性数据类型",
         *     "rule": "PREFIX,...",
         *     "result": "ONE"
         * }
         */
        final JsonObject config = IoHelper.configCompress(combineData);
        final JsonObject data = IoHelper.compressFn(config.getString(KName.RULE)).apply(record, config);
        if (Ut.isNil(data)) {
            /*
             * 无数据
             */
            return null;
        } else {
            return IoHelper.end(data, config);
        }
    }
}
