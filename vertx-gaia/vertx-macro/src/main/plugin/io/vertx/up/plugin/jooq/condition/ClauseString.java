package io.vertx.up.plugin.jooq.condition;

import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonArray;
import org.jooq.Condition;
import org.jooq.Field;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

@SuppressWarnings("all")
public class ClauseString implements Clause {
    @Override
    public Condition where(final Field columnName, final String fieldName, final String op, final Object value) {
        final Term term = termInternal(Pool.TERM_OBJECT_MAP, op);
        return term.where(columnName, fieldName, value);
    }

    protected Annal logger() {
        return Annal.get(getClass());
    }

    /*
     * Date / DateTime / Time spec
     */
    protected Term termDate(final String op) {
        return termInternal(Pool.TERM_DATE_MAP, op);
    }

    protected Object normalized(final Object value, final Function<Object, Object> convert) {
        if (value instanceof JsonArray) {
            final JsonArray result = new JsonArray();
            ((JsonArray) value).stream().map(convert)
                .filter(Objects::nonNull).forEach(result::add);
            return result;
        } else {
            return convert.apply(value);
        }
    }

    private Term termInternal(final ConcurrentMap<String, Term> map, final String op) {
        final Term term = map.get(op);
        if (Objects.isNull(term)) {
            logger().warn(Info.JOOQ_TERM_ERR, op);
        } else {
            logger().debug(Info.JOOQ_TERM, term, op);
        }
        return term;
    }
}
