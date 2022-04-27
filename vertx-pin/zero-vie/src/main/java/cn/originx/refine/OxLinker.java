package cn.originx.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.up.atom.query.Criteria;
import io.vertx.up.commune.Record;
import io.vertx.up.unity.Ux;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class OxLinker {

    static Future<Record> viGet(
        final DataAtom atom, final String identifier,
        final String field, final Object value) {
        final JsonObject criteria = new JsonObject();
        criteria.put(field, value);
        return OxTo.toDao(atom.sigma(), identifier).fetchOneAsync(Criteria.create(criteria));
    }

    static Future<Record[]> viGet(
        final DataAtom atom, final String identifier,
        final String field, final JsonArray values) {
        final JsonObject criteria = new JsonObject();
        criteria.put(field + ",i", values);
        return OxTo.toDao(atom.sigma(), identifier).fetchAsync(criteria);
    }

    static Future<ConcurrentMap<String, Record>> viGetMap(
        final DataAtom atom, final String identifier,
        final String field, final JsonArray values,
        final String fieldGroup) {
        return viGet(atom, identifier, field, values).compose(records -> {
            final ConcurrentMap<String, Record> map = new ConcurrentHashMap<>();
            Arrays.stream(records).forEach(record -> {
                final Object groupValue = record.get(fieldGroup);
                if (Objects.nonNull(groupValue)) {
                    map.put(groupValue.toString(), record);
                }
            });
            return Ux.future(map);
        });
    }
}
