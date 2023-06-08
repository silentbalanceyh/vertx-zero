package io.aeon.experiment.mixture.fn;

import io.aeon.experiment.mixture.HOne;
import io.aeon.experiment.specification.KModule;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.shape.KJoin;
import io.vertx.up.atom.shape.KPoint;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HOneHybrid implements HOne<BiFunction<JsonObject, JsonObject, JsonObject>> {
    @Override
    public BiFunction<JsonObject, JsonObject, JsonObject> combine(final KModule module, final KModule connect, final MultiMap headers) {
        return (connectJ, moduleJ) -> {
            JsonObject connectV = Ut.valueJObject(connectJ).copy();

            final JsonObject result = new JsonObject();
            if (Ut.isNotNil(connectV)) {
                connectV = this.loadAttribute(connectV, connect);
                // Point Target Process for synonym
                final KPoint target = module.getConnect(connect.identifier());
                if (Objects.nonNull(target)) {
                    connectV = this.synonym(connectV, target);
                }
                result.mergeIn(connectV, true);
            }

            JsonObject moduleV = Ut.valueJObject(moduleJ).copy();
            moduleV = this.loadAttribute(moduleV, module);
            result.mergeIn(moduleV, true);
            return moduleV.copy();
        };
    }

    private JsonObject synonym(final JsonObject data, final KPoint point) {
        if (Objects.isNull(point) || Ut.isNil(point.getSynonym())) {
            // point is null ( Or ) synonym is null
            return data;
        } else {
            final JsonObject result = new JsonObject();
            final JsonObject synonym = point.getSynonym();
            data.fieldNames().forEach((field) -> {
                final Object value = data.getValue(field);
                if (synonym.containsKey(field)) {
                    // Renaming
                    final String targetField = synonym.getString(field);
                    result.put(targetField, value);
                } else {
                    // Keep the original
                    result.put(field, value);
                }
            });
            return result;
        }
    }

    private JsonObject loadAttribute(final JsonObject data, final KModule module) {
        final KJoin join = module.getConnect();
        if (Objects.isNull(join)) {
            return data.copy();
        } else {
            return this.synonym(data, join.getSource());
        }
    }
}
