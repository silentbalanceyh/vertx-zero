package io.vertx.up.unity;

import io.horizon.eon.VString;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Set;

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
        if (Ut.isNotNil(field) && Objects.nonNull(instant)) {
            final LocalDateTime current = Ut.toDateTime(instant);
            /* Get today date */
            final LocalDateTime begin = LocalDateTime.of(current.toLocalDate(), LocalTime.MIN);
            final LocalDateTime end = LocalDateTime.of(current.toLocalDate(), LocalTime.MAX);
            final JsonObject condition = new JsonObject();
            condition.put(field + ",<", Ut.parse(end).toInstant());
            condition.put(field + ",>", Ut.parse(begin).toInstant());
            condition.put(VString.EMPTY, Boolean.TRUE);
            filters.put("$" + field, condition);
        }
        return filters;
    }

    static JsonObject whereAnd() {
        return new JsonObject().put(VString.EMPTY, Boolean.TRUE);
    }

    static JsonObject whereOr() {
        return new JsonObject().put(VString.EMPTY, Boolean.FALSE);
    }

    static JsonObject whereKeys(final JsonArray keys) {
        final JsonObject criteria = whereAnd();
        criteria.put(KName.KEY + ",i", keys);
        return criteria;
    }

    static JsonObject whereAmb(final ClusterSerializable json,
                               final String fieldFrom,
                               final String fieldTo,
                               final boolean sigma) {
        final JsonObject criteria = whereAnd();
        if (json instanceof final JsonObject data) {
            final String from = Ut.valueString(data, fieldFrom);
            criteria.put(fieldTo, from);
            if (sigma) {
                criteria.put(KName.SIGMA, Ut.valueString(data, KName.SIGMA));
            }
        } else if (json instanceof final JsonArray data) {
            final Set<String> froms = Ut.valueSetString(data, fieldFrom);
            criteria.put(fieldTo + ",i", Ut.toJArray(froms));
            if (sigma) {
                criteria.put(KName.SIGMA, Ut.valueString(data, KName.SIGMA));
            }
        }
        return criteria;
    }
}
