package io.vertx.tp.modular.ray;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.reference.DataQRule;
import io.vertx.up.commune.Record;
import io.vertx.up.commune.element.AmbJson;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class RayCombine {

    static Record combine(final Record record, final ConcurrentMap<String, AmbJson> ambMap) {
        ambMap.forEach((field, each) -> combine(record, field, each));
        return record;
    }

    static Record[] combine(final Record[] records, final ConcurrentMap<String, AmbJson> ambMap,
                            final ConcurrentMap<String, DataQRule> rule) {

        return records;
    }

    private static void combine(final Record record, final String field, final AmbJson amb) {
        // 读取 single
        final Boolean single = amb.isSingle();
        if (Objects.nonNull(single)) {
            if (single) {
                final JsonObject data = amb.dataT();
                record.add(field, data);
            } else {
                final JsonArray data = amb.dataT();
                record.add(field, data);
            }
        }
    }
}
