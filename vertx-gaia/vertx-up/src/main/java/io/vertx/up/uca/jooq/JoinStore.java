package io.vertx.up.uca.jooq;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.jooq.JooqDsl;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.up.atom.Kv;
import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.unity.Ux;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
class JoinStore {
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
    private transient Class<?> firstDao;
    private JoinEngine talbe;

    JoinStore() {
    }

    <T> void join(final Class<?> daoCls, final String field) {
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
    }

    <T> void add(final Class<?> daoCls, final String field) {
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
        this.firstDao = daoCls;
        this.firstAnalyzer = this.ANALYZERS.get(daoCls);
    }

    <T> void alias(final Class<?> daoCls, final String field, final String alias) {
        final String tableName = this.CLASS_MAP.getOrDefault(daoCls, null);
        final JqAnalyzer analyzer = this.ANALYZERS.get(daoCls);
        if (Objects.nonNull(tableName)) {
            final Field column = analyzer.column(field);
            this.putColumn(tableName, column, alias);
        }
    }

    /*
     * Bind pojo file to dao class
     * This step could be done one by one here, this operation could not
     * be done in banch.
     */
    <T> void pojo(final Class<?> daoCls, final String pojo) {
        final JqAnalyzer analyzer = this.ANALYZERS.get(daoCls);
        if (Objects.nonNull(analyzer)) {
            analyzer.on(pojo, daoCls);
        }
    }

    // -------------------- Meta data processing -----------

    Set<String> metaFirstField() {
        return this.firstAnalyzer.fieldSet();
    }

    String metaTableName(final String field) {
        return this.FIELD_TABLE_MAP.get(field);
    }

    Field metaColumn(final String field) {
        final Field found = this.FIELD_MAP.get(field);
        if (Objects.isNull(found)) {
            return null;
        } else {
            return found;
        }
    }


    JooqDsl metaDsl() {
        return this.firstAnalyzer.dsl();
    }

    String metaPrefix(final String table) {
        Objects.requireNonNull(table);
        return PREFIX_MAP.get(table);
    }

    // -------------------- Spec Meta Operation -----------


    boolean noPrefix() {
        return this.PREFIX_MAP.isEmpty();
    }

    boolean hasTable() {
        return this.TABLES.isEmpty();
    }

    JqAnalyzer analyzer(final String name) {
        final Class<?> daoCls = this.NAME_MAP.get(name);
        return this.ANALYZERS.get(daoCls);
    }

    UxJooq jooq() {
        return Ux.Jooq.on(this.firstDao);
    }

    UxJooq childJooq() {
        final Set<Class<?>> analyzers = this.ANALYZERS.keySet()
            .stream().filter(item -> !item.equals(this.firstDao))
            .collect(Collectors.toSet());
        if (1 == analyzers.size()) {
            final Class<?> daoCls = analyzers.iterator().next();
            return Ux.Jooq.on(daoCls);
        } else {
            throw new _501NotSupportException(getClass());
        }
    }

    Set<String> childKeySet() {
        final Collection<JqAnalyzer> analyzers = this.ANALYZERS.values()
            .stream().filter(item -> !item.equals(this.firstAnalyzer))
            .collect(Collectors.toSet());
        if (1 == analyzers.size()) {
            final JqAnalyzer analyzer = analyzers.iterator().next();
            return analyzer.primarySet();
        } else {
            throw new _501NotSupportException(getClass());
        }
    }

    ConcurrentMap<String, String> mapColumn() {
        return this.COLUMN_MAP;
    }

    Field fieldFull(final String field) {
        final Field original = this.FIELD_MAP.get(field);
        return DSL.field(this.PREFIX_MAP.get(this.first.getKey()) + "." + original.getName());
    }

    Field fieldFull() {
        return this.fieldFull(this.first.getValue());
    }

    JqEdge edge(final int index) {
        return this.EDGES.get(index);
    }

    List<String> tables() {
        return this.TABLES;
    }

    Table<Record> tableRecord() {
        return this.tableRecord(this.first.getKey());
    }

    Table<Record> tableRecord(final int index) {
        final String table = this.TABLES.get(index);
        return this.tableRecord(table);
    }

    Table<Record> tableRecord(final String tableName) {
        final String alias = this.PREFIX_MAP.get(tableName);
        final Table<Record> tableRecord = DSL.table(DSL.name(tableName)).as(DSL.name(alias));
        return tableRecord;
    }


    // -------------------- Data Operation -----------

    /*
     * The record is `firstAnalyzer` data record
     * 1) Extract primary key value ( Joined )
     */
    JsonObject dataJoin(final JsonObject record) {
        Objects.requireNonNull(record);
        final JsonObject joined = new JsonObject();
        final String firstField = this.first.getValue();
        final Object value = record.getValue(firstField);
        if (Objects.nonNull(value)) {
            this.EDGES.forEach(edge -> {
                final String toField = edge.getToField();
                if (Objects.nonNull(toField)) {
                    joined.put(toField, value);
                }
            });
        }
        return joined;
    }
    // -------------------- Private Operation -----------

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
        for (final String fieldName : fields.keySet()) {
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
                this.putColumn(tableName, field, fieldName);
            }
        }
    }

    private void putColumn(final String tableName, final Field column, final String alias) {
        final String key = '"' + tableName + "\".\"" + column.getName() + '"';
        this.COLUMN_MAP.put(key.toUpperCase(), alias);
    }
}
