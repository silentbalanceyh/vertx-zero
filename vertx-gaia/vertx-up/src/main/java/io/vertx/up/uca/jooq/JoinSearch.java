package io.vertx.up.uca.jooq;

import io.vertx.core.json.JsonArray;
import io.vertx.tp.plugin.jooq.JooqDsl;
import io.vertx.tp.plugin.jooq.condition.JooqCond;
import io.vertx.up.atom.pojo.Mojo;
import io.vertx.up.atom.query.Pager;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.uca.jooq.util.JqOut;
import io.vertx.up.util.Ut;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
class JoinSearch {
    private final transient JoinStore store;

    JoinSearch(final JoinStore store) {
        this.store = store;
    }

    Long count(final Qr qr) {
        /*
         * DSLContext
         */
        final JooqDsl dsl = this.store.metaDsl();
        final DSLContext context = dsl.context();
        final Table table = this.getTable();
        if (Objects.isNull(table)) {
            throw new RuntimeException("Table null issue! ");
        }
        /*
         * Started step
         */
        final Field field = this.store.fieldFull();
        final SelectWhereStep started = context.select(field).from(table);
        /*
         * Condition for "criteria"
         */
        if (null != qr.getCriteria()) {
            final Condition condition = JooqCond.transform(qr.getCriteria().toJson(),
                this.store::metaColumn, this.store::metaTableName);
            started.where(condition);
        }
        /*
         * Old version
         * started.fetchCount()
         */
        return Long.valueOf(context.fetchCount(started));
    }

    JsonArray searchA(final Qr qr, final Mojo mojo) {
        /*
         * DSLContext
         */
        final JooqDsl dsl = this.store.metaDsl();
        final DSLContext context = dsl.context();
        final Table table = this.getTable();
        if (Objects.isNull(table)) {
            throw new RuntimeException("Table null issue! ");
        }
        /*
         * Started step
         */
        final SelectWhereStep started = context.selectFrom(table);
        /*
         * Condition for "criteria"
         */
        if (null != qr.getCriteria()) {
            final Condition condition = JooqCond.transform(qr.getCriteria().toJson(),
                this.store::metaColumn, this.store::metaTableName);
            started.where(condition);
        }
        /*
         * Sort
         */
        if (null != qr.getSorter()) {
            final List<OrderField> orders = JooqCond.orderBy(qr.getSorter(),
                this.store::metaColumn, this.store::metaTableName);
            started.orderBy(orders);
        }
        /*
         * Pager
         */
        if (null != qr.getPager()) {
            final Pager pager = qr.getPager();
            started.offset(pager.getStart()).limit(pager.getSize());
        }
        final List<Record> records = started.fetch();
        final Result result = started.fetch();
        /*
         * Result Only
         */
        final Set<String> projectionSet = qr.getProjection();
        final JsonArray projection = Objects.isNull(projectionSet) ? new JsonArray() : Ut.toJArray(projectionSet);
        return JqOut.toJoin(records, projection, this.store.mapColumn(), mojo);
    }

    private Table<Record> getTable() {
        if (this.store.noPrefix()) {
            return null;
        }
        /*
         * The first table
         */
        final List<String> tables = this.store.tables();
        final Table<Record> first = this.store.tableRecord();
        if (this.store.hasTable()) {
            return first;
        } else {
            /*
             * First and Second
             */
            final int size = tables.size();
            TableOnConditionStep<Record> conditionStep;
            final Table<Record> record = this.store.tableRecord(0);
            conditionStep = this.buildCondition(first, record, this.store.edge(0));
            for (int idx = 1; idx < size; idx++) {
                final Table<Record> next = this.store.tableRecord(idx);
                conditionStep = this.buildCondition(conditionStep, next, this.store.edge(idx));
            }
            return conditionStep;
        }
    }

    private TableOnConditionStep<Record> buildCondition(
        final Table<Record> from,
        final Table<Record> to,
        final JqEdge edge) {
        /*
         * T1 join T2 on T1.Field1 = T2.Field2
         */
        /*
         * T1
         */
        final String majorField = edge.getFromField();
        final JqAnalyzer major = this.store.analyzer(edge.getFromTable());
        final Field hitted = major.column(majorField);
        final String fromPrefix = this.store.metaPrefix(edge.getFromTable());
        final Field hittedField = DSL.field(fromPrefix + "." + hitted.getName());
        /*
         * T2
         */
        final String toField = edge.getToField();
        final JqAnalyzer toTable = this.store.analyzer(edge.getToTable());
        final Field joined = toTable.column(toField);
        final String toPrefix = this.store.metaPrefix(edge.getToTable());
        final Field joinedField = DSL.field(toPrefix + "." + joined.getName());
        /*
         * Left Join here
         */
        return from.leftJoin(to).on(hittedField.eq(joinedField));
    }
}
