package io.vertx.up.unity.jq;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.pojo.Mojo;
import io.vertx.up.util.Ut;
import org.jooq.Field;
import org.jooq.Record;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

/*
 * Jooq Result for ( DataRegion Needed )
 * 1) `projection` to do filter and remove un-used columns
 */
@SuppressWarnings("all")
class JqResult {

    static <T> List<T> toResult(final List<T> list, final JsonArray projection, final Mojo mojo) {
        /*
         * convert projection to field
         */
        final Set<String> filters = getFilters(projection, mojo);
        /* Clear field */
        filters.forEach(filtered -> list.forEach(entity -> Ut.field(entity, filtered, null)));
        return list;
    }

    private static Set<String> getFilters(final JsonArray projection, final Mojo mojo) {
        final Set<String> filters = new HashSet<>();
        if (Objects.nonNull(mojo)) {
            /* Pojo file bind */
            projection.stream()
                    .filter(Objects::nonNull)
                    .map(field -> (String) field)
                    .map(field -> mojo.getIn().get(field))
                    .forEach(filters::add);
        } else {
            /* No Pojo file */
            filters.addAll(projection.getList());
        }
        return filters;
    }

    static JsonArray toJoin(final List<Record> records, final JsonArray projection,
                            final ConcurrentMap<String, String> fields, final Mojo mojo) {
        final JsonArray joinResult = new JsonArray();
        records.forEach(record -> {
            final int size = record.size();
            final JsonObject data = new JsonObject();
            for (int idx = 0; idx < size; idx++) {
                final Field field = record.field(idx);
                final Object value = record.get(field);
                if (Objects.nonNull(value)) {
                    /*
                     * Un translated
                     */
                    String resultField = fields.get(field.getName());
                    if (Ut.notNil(resultField) && !data.containsKey(resultField)) {
                        if (Objects.nonNull(mojo)) {
                            final String hitField = mojo.getOut().get(resultField);
                            if (Ut.notNil(hitField)) {
                                resultField = hitField;
                            }
                        }
                        if (value instanceof java.sql.Timestamp) {
                            /*
                             * Resolve issue: java.lang.IllegalStateException: Illegal type in JsonObject: class java.sql.Timestamp
                             */
                            final Date dateTime = (Date) value;
                            data.put(resultField, dateTime.toInstant());
                        } else if (value instanceof BigDecimal) {
                            /*
                             * java.lang.IllegalStateException: Illegal type in JsonObject: class java.math.BigDecimal
                             */
                            final BigDecimal decimal = (BigDecimal) value;
                            data.put(resultField, decimal.doubleValue());
                        } else {
                            data.put(resultField, value);
                        }
                    }
                }
            }
            joinResult.add(data);
        });
        final Set<String> filters = getFilters(projection, mojo);
        Ut.itJArray(joinResult).forEach(each -> filters.forEach(removed -> each.remove(removed)));
        return joinResult;
    }
}
