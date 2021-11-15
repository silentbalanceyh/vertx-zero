package io.vertx.up.unity;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.util.Ut;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

class Where {
    /*
     * 1 - instant is "YYYY-MM-DD HH:mm:ss"
     * 2 - Because here should convert to "YYYY-MM-DD" instead of equal here
     * 3 - generate between `>` and `<` into filters
     */
    static JsonObject whereDay(final JsonObject filters, final String field,
                               final Instant instant) {
        /*
         * field / instant
         */
        if (Ut.notNil(field) && Objects.nonNull(instant)) {
            final LocalDateTime current = Ut.toDateTime(instant);
            /* Get today date */
            final LocalDateTime begin = LocalDateTime.of(current.toLocalDate(), LocalTime.MIN);
            final LocalDateTime end = LocalDateTime.of(current.toLocalDate(), LocalTime.MAX);
            final JsonObject condition = new JsonObject();
            condition.put(field + ",<", Ut.parse(end).toInstant());
            condition.put(field + ",>", Ut.parse(begin).toInstant());
            condition.put(Strings.EMPTY, Boolean.TRUE);
            filters.put("$" + field, condition);
        }
        return filters;
    }

    static JsonObject whereAnd() {
        return new JsonObject().put(Strings.EMPTY, Boolean.TRUE);
    }

    static JsonObject whereOr() {
        return new JsonObject().put(Strings.EMPTY, Boolean.FALSE);
    }

    static JsonObject whereKeys(final JsonArray keys) {
        final JsonObject criteria = whereAnd();
        criteria.put(KName.KEY + ",i", keys);
        return criteria;
    }

    static JsonObject whereQrA(final JsonObject qr, final String field, final Object value) {
        Objects.requireNonNull(qr);
        // If value instance of JsonObject, skip
        if (value instanceof JsonObject) {
            final JsonObject query = qr.copy();
            final JsonObject original = query.getJsonObject(Qr.KEY_CRITERIA);
            if (Ut.notNil(original)) {
                // Combine two conditions
                final JsonObject criteria = new JsonObject();
                criteria.put("$Or$", original.copy());
                criteria.put(field, value);
                criteria.put(Strings.EMPTY, Boolean.TRUE);
                query.put(Qr.KEY_CRITERIA, criteria);
            } else {
                // Replace old conditions.
                query.put(Qr.KEY_CRITERIA, value);
            }
            return qr;
        } else {
            final JsonObject query = qr.copy();
            if (!query.containsKey(Qr.KEY_CRITERIA)) {
                query.put(Qr.KEY_CRITERIA, new JsonObject());
            }
            // Reference extract from query
            // Because you have added new condition, the connector must be AND
            final JsonObject criteria = query.getJsonObject(Qr.KEY_CRITERIA);
            criteria.put(field, value);
            criteria.put(Strings.EMPTY, Boolean.TRUE);
            query.put(Qr.KEY_CRITERIA, criteria);   // Double sure reference of `criteria`
            return query;
        }
    }
}
