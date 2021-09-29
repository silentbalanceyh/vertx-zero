package io.vertx.up.uca.jooq;

import io.github.jklingsporn.vertx.jooq.classic.VertxDAO;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.jooq.JooqDsl;
import io.vertx.tp.plugin.jooq.condition.JooqCond;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.util.Arrays;
import java.util.Collection;

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

    protected Annal logger() {
        return Annal.get(getClass());
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

    protected Condition condition(final String field, final Object value) {
        final Field column = this.analyzer.column(field);
        return column.eq(value);
    }

    // ---------------------------------- Sync Operation
    protected <T> Record newRecord(T pojo) {
        final Record record = this.context().newRecord(this.dsl.getTable(), pojo);
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

    // ---------------------------------- Output Method
}
