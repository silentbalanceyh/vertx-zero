package io.mature.extension.uca.plugin.semi;

import io.horizon.spi.plugin.AfterPlugin;
import io.horizon.spi.plugin.BeforePlugin;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AfterPk implements AfterPlugin {
    private transient BeforePlugin beforePlugin;

    @Override
    public AfterPlugin bind(final DataAtom atom) {
        this.beforePlugin = new BeforePk().bind(atom);
        return this;
    }

    @Override
    public Future<JsonObject> afterAsync(final JsonObject record, final JsonObject options) {
        if (Objects.isNull(this.beforePlugin)) {
            return Ux.future(record);
        }
        return this.beforePlugin.beforeAsync(record, options);
    }

    @Override
    public Future<JsonArray> afterAsync(final JsonArray records, final JsonObject options) {
        if (Objects.isNull(this.beforePlugin)) {
            return Ux.future(records);
        }
        return this.beforePlugin.beforeAsync(records, options);
    }
}
