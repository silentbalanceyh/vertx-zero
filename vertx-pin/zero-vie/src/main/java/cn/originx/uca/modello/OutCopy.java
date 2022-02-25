package cn.originx.uca.modello;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.modular.plugin.OComponent;
import io.vertx.tp.modular.plugin.OExpression;
import io.vertx.up.atom.Kv;
import io.vertx.up.commune.Record;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class OutCopy implements OComponent {
    private static final Annal LOGGER = Annal.get(OutCopy.class);

    @Override
    public Object after(final Kv<String, Object> kv, final Record record, final JsonObject combineData) {
        final JsonObject sourceNorm = Ut.valueJObject(combineData.getJsonObject(KName.SOURCE_NORM));
        if (Ut.notNil(sourceNorm)) {
            /*
             * Record Processing
             */
            final ConcurrentMap<String, OExpression> exprMap = IoHelper.afterExpression(combineData);
            Ut.<JsonArray>itJObject(sourceNorm, (array, field) -> {
                final Object value = record.get(field);
                if (Objects.nonNull(value)) {
                    final Set<String> copyFields = Ut.toSet(array);
                    copyFields.forEach(targetField -> {
                        if (record.isValue(targetField)) {
                            LOGGER.warn(Info.COPY_SKIP, targetField, record.identifier(), record.toJson());
                        } else {
                            /*
                             * Attach the value directly
                             */
                            if (exprMap.containsKey(targetField)) {
                                final OExpression expression = exprMap.get(targetField);
                                final Object normalized = expression.after(Kv.create(targetField, value));
                                record.attach(targetField, normalized);
                            } else {
                                record.attach(targetField, value);
                            }
                        }
                    });
                }
            });
        }
        return kv.getValue();
    }
}
