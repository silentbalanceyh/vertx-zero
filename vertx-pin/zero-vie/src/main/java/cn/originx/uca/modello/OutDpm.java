package cn.originx.uca.modello;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.modular.plugin.OComponent;
import io.vertx.tp.optic.business.ExAttributeComponent;
import io.vertx.up.atom.Kv;
import io.vertx.up.commune.Record;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class OutDpm extends ExAttributeComponent implements OComponent {
    @Override
    public Object after(final Kv<String, Object> kv, final Record record, final JsonObject combineData) {
        return this.translateTo(kv.getValue(), combineData);
    }
}
