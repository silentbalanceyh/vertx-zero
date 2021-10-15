package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.jooq.JooqDsl;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.tp.plugin.jooq.condition.JooqCond;
import io.vertx.up.atom.Kv;
import io.vertx.up.atom.pojo.Mojo;
import io.vertx.up.atom.query.Pager;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.uca.jooq.util.JqOut;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Join Operation Complex JqTool Component
 */
@SuppressWarnings("all")
class JqJoinder {
    /*
     * Class -> Analyzer
     */
    private transient final ConcurrentMap<Class<?>, JqAnalyzer> ANALYZERS
        = new ConcurrentHashMap<>();
    /*
     * Table prefix: Name -> Alias
     */
    private transient final ConcurrentMap<String, String> PREFIX_MAP
        = new ConcurrentHashMap<>();
    /*
     * Mapping assist for calculation
     */
    private transient final ConcurrentMap<Class<?>, String> CLASS_MAP
        = new ConcurrentHashMap<>();
    private transient final ConcurrentMap<String, Class<?>> NAME_MAP
        = new ConcurrentHashMap<>();
    /*
     * Field Map
     */
    private transient final ConcurrentMap<String, String> FIELD_TABLE_MAP
        = new ConcurrentHashMap<>();
    private transient final ConcurrentMap<String, Field> FIELD_MAP
        = new ConcurrentHashMap<>();
    /*
     * AS Map
     */
    private transient final ConcurrentMap<String, String> COLUMN_MAP
        = new ConcurrentHashMap<>();
    /*
     * Next table ( exclude the first added table here )
     */
    private transient final List<String> TABLES = new ArrayList<>();

    private transient final List<JqEdge> EDGES = new ArrayList<>();

    private transient Kv<String, String> first;
    private transient JqAnalyzer firstAnalyzer;
    private JqJoinder talbe;

    JqJoinder() {
    }

    <T> JqJoinder add(final Class<T> daoCls, final String field) {
        /*
         * Stored analyzer by daoCls
         */
        this.putDao(daoCls);
        /*
         * The first
         */
        final String firstTable = this.CLASS_MAP.get(daoCls);
        this.first = Kv.create(firstTable, field);
        /*
         * Assist for joining here
         */
        this.firstAnalyzer = this.ANALYZERS.get(daoCls);
        return this;
    }

    <T> JqJoinder alias(final Class<?> daoCls, final String field, final String alias) {
        final String tableName = this.CLASS_MAP.getOrDefault(daoCls, null);
        final JqAnalyzer analyzer = this.ANALYZERS.get(daoCls);
        if (Objects.nonNull(tableName)) {
            final Field column = analyzer.column(field);
            this.putColumn(tableName, column, alias);
        }
        return this;
    }

    private void putColumn(final String tableName, final Field column, final String alias) {
        final String key = '"' + tableName + "\".\"" + column.getName() + '"';
        this.COLUMN_MAP.put(key.toUpperCase(), alias);
    }

    Set<String> firstFields() {
        return this.firstAnalyzer.fieldSet();
    }

    <T> JqJoinder join(final Class<?> daoCls, final String field) {
        /*
         * Support three tables only as max table here
         * It means that if there exist more than 3 tables, we recommend
         * to re-design database instead of using `JOIN`
         * Because multi tables join may caused performance issue.
         */
        if (2 < this.ANALYZERS.size()) {
            throw new RuntimeException("Join table counter limitation! ");
        }
        /*
         * Stored analyzer by daoCls
         */
        putDao(daoCls);
        /*
         * Build Relation
         */
        final String toTable = this.CLASS_MAP.get(daoCls);
        this.TABLES.add(toTable);
        /*
         * JqEdge
         */
        {
            final JqEdge edge = new JqEdge();
            edge.setFrom(this.first.getKey(), this.first.getValue());
            edge.setTo(toTable, field);
            this.EDGES.add(edge);
        }
        return this;
    }

    private <T> void putDao(final Class<T> daoCls) {
        /*
         * Analyzer building
         */
        final JooqDsl dsl = JooqInfix.getDao(daoCls);
        final JqAnalyzer analyzer = JqAnalyzer.create(dsl);
        final String tableName = analyzer.table().getName();
        this.ANALYZERS.put(daoCls, analyzer);
        {
            this.CLASS_MAP.put(daoCls, tableName);
            this.NAME_MAP.put(tableName, daoCls);
        }
        /*
         * Table Name -> Table Alias mapping
         */
        final Integer size = this.ANALYZERS.size();
        final String tableAlias = "T" + size;
        this.PREFIX_MAP.put(tableName, DSL.table(tableAlias).getName());
        /*
         * Field -> Table
         */
        final ConcurrentMap<String, Field> fields = analyzer.columns();
        for (String fieldName : fields.keySet()) {
            final Field field = fields.get(fieldName);
            this.FIELD_MAP.put(fieldName, field);
            /*
             * Column Field here
             */
            if (!this.FIELD_TABLE_MAP.containsKey(field.getName())) {
                this.FIELD_TABLE_MAP.put(field.getName(), tableAlias);
                /*
                 * Prefix.Column -> Field
                 */
                putColumn(tableName, field, fieldName);
            }
        }
    }

    /*
     * Bind pojo file to dao class
     * This step could be done one by one here, this operation could not
     * be done in banch.
     */
    <T> JqJoinder pojo(final Class<?> daoCls, final String pojo) {
        final JqAnalyzer analyzer = this.ANALYZERS.get(daoCls);
        if (Objects.nonNull(analyzer)) {
            analyzer.on(pojo, daoCls);
        }
        return this;
    }

    /*
     * Pagination Searching
     */
    Future<JsonObject> searchPaginationAsync(final Qr qr, final Mojo mojo) {
        final JsonObject response = new JsonObject();
        final JsonArray data = this.searchArray(qr, mojo);

        response.put("list", data);
        final Long counter = this.searchCount(qr);
        response.put("count", counter);
        return Ux.future(response);
    }

    Future<Long> countPaginationAsync(final Qr qr) {
        return Future.succeededFuture(this.searchCount(qr));
    }

    private Long searchCount(final Qr qr) {
        /*
         * DSLContext
         */
        final JooqDsl dsl = this.firstAnalyzer.dsl();
        final DSLContext context = dsl.context();
        final Table table = getTable();
        if (Objects.isNull(table)) {
            throw new RuntimeException("Table null issue! ");
        }
        /*
         * Started step
         */
        final Field original = FIELD_MAP.get(this.first.getValue());
        final Field field = DSL.field(this.PREFIX_MAP.get(this.first.getKey()) + "." + original.getName());
        final SelectWhereStep started = context.select(field).from(table);
        /*
         * Condition for "criteria"
         */
        if (null != qr.getCriteria()) {
            final Condition condition = JooqCond.transform(qr.getCriteria().toJson(),
                this::getColumn, this::getTable);
            started.where(condition);
        }
        /*
         * Old version
         * started.fetchCount()
         */
        return Long.valueOf(context.fetchCount(started));
    }

    JsonArray searchArray(final Qr qr, final Mojo mojo) {
        /*
         * DSLContext
         */
        final JooqDsl dsl = this.firstAnalyzer.dsl();
        final DSLContext context = dsl.context();
        final Table table = getTable();
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
                this::getColumn, this::getTable);
            started.where(condition);
        }
        /*
         * Sort
         */
        if (null != qr.getSorter()) {
            final List<OrderField> orders = JooqCond.orderBy(qr.getSorter(), this::getColumn, this::getTable);
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
        return JqOut.toJoin(records, projection, this.COLUMN_MAP, mojo);
    }

    private Field getColumn(final String field) {
        final Field found = this.FIELD_MAP.get(field);
        if (Objects.isNull(found)) {
            return null;
        } else {
            return found;
        }
    }

    private String getTable(final String field) {
        return this.FIELD_TABLE_MAP.get(field);
    }

    private Table<Record> getTable() {
        /*
         * Two tables or one table
         */
        if (!this.PREFIX_MAP.isEmpty()) {
            /*
             * The first table
             */
            Table<Record> first = getTableRecord(this.first.getKey());
            if (this.TABLES.isEmpty()) {
                return first;
            } else {
                /*
                 * First and Second
                 */
                final int size = this.TABLES.size();
                TableOnConditionStep<Record> conditionStep;
                final String tableName = this.TABLES.get(0);
                final Table<Record> record = getTableRecord(tableName);
                conditionStep = buildCondition(first, record, this.EDGES.get(0));
                for (int idx = 1; idx < size; idx++) {
                    final String middleName = this.TABLES.get(idx);
                    final Table<Record> next = getTableRecord(middleName);
                    conditionStep = buildCondition(conditionStep, next, this.EDGES.get(idx));
                }
                return conditionStep;
            }
        } else return null;
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
        final JqAnalyzer major = findByName(edge.getFromTable());
        final Field hitted = major.column(majorField);
        final String fromPrefix = PREFIX_MAP.get(edge.getFromTable());
        final Field hittedField = DSL.field(fromPrefix + "." + hitted.getName());
        /*
         * T2
         */
        final String toField = edge.getToField();
        final JqAnalyzer toTable = findByName(edge.getToTable());
        final Field joined = toTable.column(toField);
        final String toPrefix = PREFIX_MAP.get(edge.getToTable());
        final Field joinedField = DSL.field(toPrefix + "." + joined.getName());
        /*
         * Left Join here
         */
        return from.leftJoin(to).on(hittedField.eq(joinedField));
    }

    private Table<Record> getTableRecord(final String table) {
        final String alias = this.PREFIX_MAP.get(table);
        final Table<Record> tableRecord = DSL.table(DSL.name(table)).as(DSL.name(alias));
        return tableRecord;
    }

    private JqAnalyzer findByName(final String name) {
        final Class<?> daoCls = this.NAME_MAP.get(name);
        return this.ANALYZERS.get(daoCls);
    }
}
