package io.vertx.up.uca.jooq;

import io.github.jklingsporn.vertx.jooq.classic.VertxDAO;
import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonObject;
import io.vertx.up.plugin.jooq.JooqDsl;
import io.vertx.up.plugin.jooq.condition.JooqCond;
import io.vertx.up.uca.log.DevEnv;
import io.vertx.up.util.Ut;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.util.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * Two mode:
 *
 * 1) Dim1: Sync / Async
 * 2) Dim2: Pojo / Bind-Pojo / Non-Pojo
 * 3) Dim3: T, List<T>, JsonObject, JsonArray
 *
 * This class is for basic operation abstraction such as:
 *
 * INSERT, UPDATE, DELETE, SELECT etc.
 *
 * The scope is default ( Package Only )
 */
@SuppressWarnings("all")
abstract class AbstractAction {
    protected transient final JooqDsl dsl;
    protected transient final JqAnalyzer analyzer;

    protected AbstractAction(final JqAnalyzer analyzer) {
        this.analyzer = analyzer;
        this.dsl = analyzer.dsl();
    }

    protected VertxDAO dao() {
        return this.dsl.dao();
    }

    protected DSLContext context() {
        return this.dsl.context();
    }

    protected void logging(final String pattern, final Object... args) {
        final Annal logger = Annal.get(getClass());
        if (DevEnv.devJooqCond()) {
            logger.info(pattern, args);
        }
    }

    protected void warning(final String pattern, final Object... args) {
        final Annal logger = Annal.get(getClass());
        logger.warn(pattern, args);
    }

    // -------------------------------- Input Method
    /*
     * Parameter processing here
     * Here are two situations:
     * 1): No collection -> List<Object> -> [Element]
     * 2): List type -> Direct for list -> [Element, ...]
     */
    protected Collection<Object> parameters(final Object value) {
        if (value instanceof Collection) {
            return (Collection<Object>) value;
        } else {
            /*
             * List as the first collection type selected
             */
            return Arrays.asList(value);
        }
    }

    protected Condition condition(final JsonObject criteria) {
        return Ut.isNil(criteria) ? null : JooqCond.transform(criteria, this.analyzer::column);
    }

    protected Condition conditionAnd(final JsonObject criteria) {
        return JooqCond.transform(criteria, Operator.AND, this.analyzer::column);
    }

    // ---------------------------------- Sync Operation
    protected <T> org.jooq.Record newRecord(T pojo) {
        Objects.requireNonNull(pojo);
        final org.jooq.Record record = this.context().newRecord(this.analyzer.table(), pojo);
        int size = record.size();
        for (int i = 0; i < size; i++)
            if (record.get(i) == null) {
                @SuppressWarnings("unchecked")
                Field<Object> field = (Field<Object>) record.field(i);
                if (!field.getDataType().nullable() && !field.getDataType().identity())
                    record.set(field, DSL.defaultValue());
            }
        return record;
    }

    protected <T> UpdateConditionStep editRecord(T pojo) {
        Objects.requireNonNull(pojo);
        org.jooq.Record record = this.context().newRecord(this.analyzer.table(), pojo);
        // Condition where = DSL.trueCondition();
        UniqueKey<?> pk = this.analyzer.table().getPrimaryKey();
        final Set<Condition> conditions = new HashSet<>();
        for (TableField<?, ?> tableField : pk.getFields()) {
            //exclude primary keys from update
            record.changed(tableField, false);
            final Condition condition = ((TableField<org.jooq.Record, Object>) tableField).eq(record.get(tableField));
            conditions.add(condition);
            // where = where.?nd(((TableField<org.jooq.Record, Object>) tableField).eq(record.get(tableField)));
        }
        final Condition where = DSL.and(conditions);
        Map<String, Object> valuesToUpdate =
            Arrays.stream(record.fields())
                .collect(HashMap::new, (m, f) -> m.put(f.getName(), f.getValue(record)), HashMap::putAll);
        return this.context().update(this.analyzer.table()).set(valuesToUpdate).where(where);
    }
    // ---------------------------------- Output Method
}
