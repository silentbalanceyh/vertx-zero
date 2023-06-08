package io.vertx.up.plugin.jooq.condition;

import io.horizon.uca.qr.syntax.Ir;
import io.vertx.up.util.Ut;
import org.jooq.Condition;
import org.jooq.impl.DSL;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

/*
 * For date time processing here
 * 1) field,op,day
 * 2) field,op,date
 * 3) field,op,time
 * 4) field,op,datetime
 */
class JooqAnder {
    /*
     * op is `=`
     */
    private static final ConcurrentMap<String, BiFunction<String, Instant, Condition>> EQ_OPS =
        new ConcurrentHashMap<String, BiFunction<String, Instant, Condition>>() {
            {
                this.put(Ir.Instant.DAY, (field, value) -> {
                    // Time for locale
                    final LocalDate date = Ut.toDate(value);
                    return DSL.field(field).between(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
                });
                this.put(Ir.Instant.DATE, (field, value) -> {
                    final LocalDate date = Ut.toDate(value);
                    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    return DSL.field(field).eq(date.format(formatter));
                });
            }
        };
    /*
     * Major executor map
     */
    private static final ConcurrentMap<String, ConcurrentMap<String, BiFunction<String, Instant, Condition>>> EXECUTOR =
        new ConcurrentHashMap<String, ConcurrentMap<String, BiFunction<String, Instant, Condition>>>() {
            {
                this.put(Ir.Op.EQ, EQ_OPS);
            }
        };

    /*
     * This method will be called by `JooqCond`
     */
    static BiFunction<String, Instant, Condition> getExecutor(final String op, final String flag) {
        final ConcurrentMap<String, BiFunction<String, Instant, Condition>>
            executors = EXECUTOR.get(op);
        if (Objects.nonNull(executors) && !executors.isEmpty()) {
            /*
             * 1) Not equal `null`
             * 2) The map is not empty
             */
            return executors.get(flag);
        } else {
            return null;
        }
    }
}
