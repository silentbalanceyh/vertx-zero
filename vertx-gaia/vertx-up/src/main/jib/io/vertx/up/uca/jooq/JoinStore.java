package io.vertx.up.uca.jooq;

import io.horizon.atom.common.Kv;
import io.horizon.exception.web._501NotSupportException;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.pojo.Mojo;
import io.vertx.up.plugin.jooq.JooqDsl;
import io.vertx.up.plugin.jooq.JooqInfix;
import io.vertx.up.unity.Ux;
import org.jooq.Field;
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
     * Here each Dao class contain only one JqAnalyzer for deep analyzing
     * and future usage.
     */
    private transient final ConcurrentMap<Class<?>, JqAnalyzer> daoAnalyzer
        = new ConcurrentHashMap<>();
    /*
     * Table prefix: Name -> Prefix.Name
     * Here the system append prefix to table name for duplicated column issue.
     */
    private transient final ConcurrentMap<String, String> tablePrefix
        = new ConcurrentHashMap<>();
    /*
     * Mapping assist for calculation
     * Class<?> -> Table Name
     * Table Name -> Class<?>
     */
    private transient final ConcurrentMap<Class<?>, String> daoTable
        = new ConcurrentHashMap<>();
    private transient final ConcurrentMap<String, Class<?>> tableDao
        = new ConcurrentHashMap<>();


    /*
     * Next table ( exclude the first added table here )
     */
    private transient final List<String> tableList = new ArrayList<>();

    private transient final List<JqEdge> edgeList = new ArrayList<>();
    private transient final JoinAlias alias;
    private transient Kv<String, String> first;
    private transient JqAnalyzer firstAnalyzer;
    private transient Class<?> firstDao;

    JoinStore() {
        this.alias = new JoinAlias();
    }

    <T> void join(final Class<?> daoCls, final String field) {
        /*
         * Support three tables only as max table here
         * It means that if there exist more than 3 tables, we recommend
         * to re-design database instead of using `JOIN`
         * Because multi tables join may caused performance issue.
         */
        if (2 < this.daoAnalyzer.size()) {
            throw new RuntimeException("Join table counter limitation! ");
        }
        /*
         * Stored analyzer by daoCls
         */
        this.addDao(daoCls);
        /*
         * Build Relation
         */
        final String toTable = this.daoTable.get(daoCls);
        this.tableList.add(toTable);
        /*
         * JqEdge
         */
        {
            final JqEdge edge = new JqEdge();
            edge.setFrom(this.first.key(), this.first.value());
            edge.setTo(toTable, field);
            this.edgeList.add(edge);
        }
    }

    <T> void add(final Class<?> daoCls, final String field) {
        /*
         * Stored analyzer by daoCls
         */
        this.addDao(daoCls);
        /*
         * The first
         */
        final String firstTable = this.daoTable.get(daoCls);
        this.first = Kv.create(firstTable, field);
        /*
         * Assist for joining here
         */
        this.firstDao = daoCls;
        this.firstAnalyzer = this.daoAnalyzer.get(daoCls);
    }

    <T> void alias(final Class<?> daoCls, final String field, final String alias) {
        final String tableName = this.daoTable.getOrDefault(daoCls, null);
        if (Objects.nonNull(tableName)) {
            final JqAnalyzer analyzer = this.daoAnalyzer.get(daoCls);
            String fieldFound;
            final Mojo mojo = analyzer.pojo();
            if (Objects.isNull(mojo)) {
                // Keep
                fieldFound = field;
            } else {
                fieldFound = mojo.getIn(field);
                if (Objects.isNull(fieldFound)) {
                    // Keep
                    fieldFound = field;
                }
            }
            this.alias.addAlias(analyzer, tableName, fieldFound, alias);
        }
    }

    /*
     * Bind pojo file to dao class
     * This step could be done one by one here, this operation could not
     * be done in banch.
     */
    <T> void pojo(final Class<?> daoCls, final String pojo) {
        final JqAnalyzer analyzer = this.daoAnalyzer.get(daoCls);
        if (Objects.nonNull(analyzer)) {
            analyzer.on(pojo, daoCls);
        }
    }

    // -------------------- Meta data processing -----------

    Set<String> metaFirstField() {
        return this.firstAnalyzer.fieldSet();
    }

    String metaTable(final String field) {
        return this.alias.table(field);
    }

    Field metaColumn(final String field) {
        return this.alias.field(field);
    }


    JooqDsl metaDsl() {
        return this.firstAnalyzer.dsl();
    }

    String metaPrefix(final String table) {
        Objects.requireNonNull(table);
        return this.tablePrefix.get(table);
    }

    // -------------------- Spec Meta Operation -----------


    boolean noPrefix() {
        return this.tablePrefix.isEmpty();
    }

    boolean hasTable() {
        return this.tableList.isEmpty();
    }

    JqAnalyzer analyzer(final String name) {
        final Class<?> daoCls = this.tableDao.get(name);
        return this.daoAnalyzer.get(daoCls);
    }

    UxJooq jooq() {
        return Ux.Jooq.on(this.firstDao);
    }

    UxJooq childJooq() {
        final Set<Class<?>> analyzers = this.daoAnalyzer.keySet()
            .stream().filter(item -> !item.equals(this.firstDao))
            .collect(Collectors.toSet());
        if (1 == analyzers.size()) {
            final Class<?> daoCls = analyzers.iterator().next();
            return Ux.Jooq.on(daoCls);
        } else {
            throw new _501NotSupportException(this.getClass());
        }
    }

    Set<String> childKeySet() {
        final Collection<JqAnalyzer> analyzers = this.daoAnalyzer.values()
            .stream().filter(item -> !item.equals(this.firstAnalyzer))
            .collect(Collectors.toSet());
        if (1 == analyzers.size()) {
            final JqAnalyzer analyzer = analyzers.iterator().next();
            return analyzer.primarySet();
        } else {
            throw new _501NotSupportException(this.getClass());
        }
    }

    ConcurrentMap<String, Set<String>> mapColumn() {
        return this.alias.mapColumn();
    }

    Field field(final String field) {
        final String prefix = this.tablePrefix.get(this.first.key());
        return this.alias.field(field, prefix);
    }

    Field field() {
        return this.field(this.first.value());
    }

    JqEdge edge(final int index) {
        return this.edgeList.get(index);
    }

    List<String> tables() {
        return this.tableList;
    }

    Table<org.jooq.Record> tableRecord() {
        return this.tableRecord(this.first.key());
    }

    Table<org.jooq.Record> tableRecord(final int index) {
        final String table = this.tableList.get(index);
        return this.tableRecord(table);
    }

    Table<org.jooq.Record> tableRecord(final String tableName) {
        final String alias = this.tablePrefix.get(tableName);
        return DSL.table(DSL.name(tableName)).as(DSL.name(alias));
    }


    // -------------------- Data Operation -----------

    /*
     * The record is `firstAnalyzer` data record
     * 1) Extract primary key value ( Joined )
     */
    JsonObject dataJoin(final JsonObject record) {
        Objects.requireNonNull(record);
        final JsonObject joined = new JsonObject();
        final String firstField = this.first.value();
        final Object value = record.getValue(firstField);
        if (Objects.nonNull(value)) {
            this.edgeList.forEach(edge -> {
                final String toField = edge.getToField();
                if (Objects.nonNull(toField)) {
                    joined.put(toField, value);
                }
            });
        }
        return joined;
    }
    // -------------------- Private Operation -----------

    private <T> void addDao(final Class<T> daoCls) {
        /*
         * Analyzer building
         */
        final JooqDsl dsl = JooqInfix.getDao(daoCls);
        final JqAnalyzer analyzer = JqAnalyzer.create(dsl);
        final String tableName = analyzer.table().getName();
        this.daoAnalyzer.put(daoCls, analyzer);
        {
            this.daoTable.put(daoCls, tableName);
            this.tableDao.put(tableName, daoCls);
        }
        /*
         * Table Name -> Table Alias mapping
         */
        final int size = this.daoAnalyzer.size();
        final String tableAlias = "T" + size;
        this.tablePrefix.put(tableName, DSL.table(tableAlias).getName());
        // Alias Reference
        this.alias.addDao(analyzer, tableName, tableAlias);
    }
}
