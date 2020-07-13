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
import java.util.stream.Collectors;

/*
 * Jooq Result for ( DataRegion Needed )
 * 1) `projection` to do filter and remove un-used columns
 */
@SuppressWarnings("all")
class JqResult {

    static <T> List<T> toResult(final List<T> list, final JsonArray projectionArray, final Mojo mojo) {
        /*
         * convert projection to field
         */
        final Set<String> projections = getProjections(projectionArray, mojo);
        return toResult(list, projections);
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
        final Set<String> projections = getProjections(projection, mojo);
        return toResult(joinResult, projections);
    }

    private static <T> List<T> toResult(final List<T> list, final Set<String> projections) {
        if (!projections.isEmpty()) {
            /*
             * Get all fields here
             */
            final Class<?> componentType = list.getClass().getComponentType();
            final String[] fields = Arrays.stream(Ut.fields(componentType))
                    .map(java.lang.reflect.Field::getName)
                    .filter(Objects::nonNull)
                    /*
                     * Calculated filtered fields by projections
                     */
                    .filter(item -> !projections.contains(item))
                    .collect(Collectors.toSet()).toArray(new String[]{});
            for (String filtered : fields) {
                list.forEach(entity -> Ut.field(entity, filtered, null));
            }
        }
        return list;
    }

    private static JsonArray toResult(final JsonArray data, final Set<String> projections) {
        if (!projections.isEmpty()) {
            Ut.itJArray(data).forEach(record -> {
                final Set<String> filters = new HashSet<>(record.fieldNames()).stream()
                        /*
                         * Calculated filtered fields by projections
                         */
                        .filter(item -> !projections.contains(item))
                        .collect(Collectors.toSet());
                filters.forEach(filtered -> record.remove(filtered));
            });
        }
        return data;
    }

    private static Set<String> getProjections(final JsonArray projection, final Mojo mojo) {
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
}
