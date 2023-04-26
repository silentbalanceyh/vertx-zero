package cn.originx.uca.modello;

import io.horizon.specification.modeler.HRecord;
import io.horizon.spi.component.ExAttributeComponent;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.modular.plugin.OComponent;
import io.vertx.up.atom.Kv;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class OutBrand extends ExAttributeComponent implements OComponent {
    @Override
    public Object after(final Kv<String, Object> kv, final HRecord record, final JsonObject combineData) {
        return this.translateTo(kv.getValue(), combineData);
    }
}
