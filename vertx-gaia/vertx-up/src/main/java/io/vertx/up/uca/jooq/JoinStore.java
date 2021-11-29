package io.vertx.up.uca.jooq;

import io.vertx.tp.plugin.jooq.JooqDsl;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.up.atom.Kv;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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

    JqAnalyzer metaAnalyzer(final String name) {
        final Class<?> daoCls = this.NAME_MAP.get(name);
        return this.ANALYZERS.get(daoCls);
    }

    JqAnalyzer metaAnalyzer() {
        return this.firstAnalyzer;
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


    boolean isJoined() {
        return this.PREFIX_MAP.isEmpty();
    }

    boolean hasTable() {
        return this.TABLES.isEmpty();
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
