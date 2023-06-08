package io.vertx.up.uca.jooq;

import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.pojo.Mirror;
import io.vertx.up.commune.pojo.Mojo;
import io.vertx.up.exception.booting.JooqFieldMissingException;
import io.vertx.up.exception.booting.JooqMergeException;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.jooq.JooqDsl;
import io.vertx.up.plugin.jooq.condition.JooqCond;
import io.vertx.up.util.Ut;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.jooq.impl.DSL.row;

@SuppressWarnings("all")
public class JqAnalyzer {

    private static final Annal LOGGER = Annal.get(JqAnalyzer.class);
    private static final ConcurrentMap<Integer, JooqDsl> DAO_POOL =
        new ConcurrentHashMap<>();

    private static final Cc<Integer, JooqDsl> CC_DAO = Cc.open();

    private transient final JooqDsl dsl;
    /* Field to Column */
    private transient final ConcurrentMap<String, String> mapping =
        new ConcurrentHashMap<>();
    /* Column to Field */
    private transient final ConcurrentMap<String, String> revert =
        new ConcurrentHashMap<>();

    private transient Mojo pojo;
    private transient Table<?> table;
    /*
     *  sigma -> zSigma -> Z_SIGMA
     *  fieldMap
     *     zSigma -> Field ( Jooq )
     *  typeMap
     *     sigma -> Type
     */
    private transient ConcurrentMap<String, Field> fieldMap = new ConcurrentHashMap<>();
    private transient ConcurrentMap<String, Class<?>> typeMap = new ConcurrentHashMap<>();
    private transient Class<?> entityCls;

    private JqAnalyzer(final JooqDsl dsl) {
        this.dsl = CC_DAO.pick(() -> dsl, dsl.hashCode());
        // Fn.po?l(DAO_POOL, dsl.hashCode(), () -> dsl);
        // Mapping initializing
        this.table = Ut.field(dsl.dao(), "table");

        final Class<?> typeCls = Ut.field(dsl.dao(), "type");
        this.entityCls = typeCls;

        final java.lang.reflect.Field[] fields = Ut.fields(typeCls);
        // Analyze Type and definition sequence, columns hitted.
        final Field[] columns = this.table.fields();

        // Mapping building
        for (int idx = VValue.IDX; idx < columns.length; idx++) {
            final Field column = columns[idx];
            final java.lang.reflect.Field field = fields[idx];
            /*
             * Help for join & jooq
             * 1) tableFields:
             *    pojo field = column ( Field )
             *
             * 2) mapping:
             *    pojo field = column name
             *
             * 3) revert:
             *    column name = pojo field
             *
             */
            this.fieldMap.put(field.getName(), column);
            this.mapping.put(field.getName(), column.getName());
            this.revert.put(column.getName(), field.getName());
        }
    }

    public static JqAnalyzer create(final JooqDsl dsl) {
        return new JqAnalyzer(dsl);
    }

    public JooqDsl dsl() {
        return this.dsl;
    }

/*    public Vertx vertx() {
        return Objects.isNull(this.vertxDAO) ? null : vertxDAO.vertx();
    }*/

    public Table<?> table() {
        return this.table;
    }

    public Class<?> type() {
        return this.entityCls;
    }

    public TreeSet<String> primarySet() {
        TreeSet<String> primary = this.keySet(this.table.getPrimaryKey());
        return primary.isEmpty() ? new TreeSet<>() : primary;
    }

    public String primary() {
        TreeSet<String> primary = this.keySet(this.table.getPrimaryKey());
        return primary.isEmpty() ? null : primary.iterator().next();
    }

    /*
     * Primary key value collect
     * 1) Object
     * 2) List<Object>
     */
    public <T> Object primaryValue(final T input) {
        final String primaryField = this.primary();
        if (Objects.isNull(primaryField)) {
            /*
             * null returned
             */
            return null;
        } else {
            /*
             * extract primary
             */
            return Ut.field(input, primaryField);
        }
    }

    public <T> List<Object> primaryValue(final List<T> list) {
        final List<Object> values = new ArrayList<>();
        list.stream().map(this::primaryValue).forEach(values::add);
        return values;
    }

    public List<TreeSet<String>> uniqueKey() {
        /*
         * keys include
         * - PrimaryKey
         * - UniqueKey
         */
        final List<TreeSet<String>> keys = new ArrayList<>();
        this.table.getKeys().forEach(ukey -> {
            /*
             *  UniqueKey
             */
            final TreeSet<String> keySet = this.keySet(ukey);
            if (!keySet.isEmpty()) {
                keys.add(keySet);
            }
        });
        return keys;
    }

    public TreeSet<String> fieldSet() {
        return new TreeSet<>(this.mapping.keySet());
    }

    private TreeSet<String> keySet(final UniqueKey<?> uk) {
        final TreeSet<String> keySet = new TreeSet<>();
        uk.getFields().forEach(column -> {
            /*
             * Column to Field converting
             */
            final String field = this.revert.getOrDefault(column.getName(), null);
            if (Objects.nonNull(field)) {
                keySet.add(field);
            }
        });
        return keySet;
    }

    private String columnName(final String field) {
        String targetField;
        if (null == this.pojo) {
            /*
             * The mapping is
             * field = column here
             */
            if (this.mapping.values().contains(field)) {
                /*
                 * it means that you could get `Column` information here
                 *
                 * Situation 1:
                 * Input `field` is column field name directly, it means that the
                 * mapping contains `field` in value. hit target column directly here, in this kind of
                 * situation, the `field` is COLUMN
                 *
                 * COLUMN is `Jooq` needed
                 */
                targetField = field;
            } else {
                /*
                 * The field is not in `COLUMN` value set
                 *
                 * it means that `field` is field, not column here,
                 * Situation 2:
                 * Input `field` is not column field, and we must convert field to column
                 * instead of `field` here.
                 */
                targetField = this.mapping.get(field);
            }
        } else {
            /*
             * The argument `field` is input field.
             *
             * key = input field
             * This key is before `pojo file` here, it could let you convert field
             *
             * value = pojo actual field here
             * This value is after `pojo file` here, it could let you get actula pojo field
             */
            final ConcurrentMap<String, String> inColumnMapping = this.pojo.getInColumn();
            final ConcurrentMap<String, String> inMapping = this.pojo.getIn();
            if (inMapping.containsKey(field)) {
                /*
                 * field = actualField = column
                 */
                final String actualField = inMapping.get(field);
                /*
                 * The system must get column by outMapping here
                 */
                final String columnName = inColumnMapping.get(actualField);
                if (Objects.isNull(columnName)) {
                    /*
                     * Not column
                     */
                    targetField = field;
                } else {
                    /*
                     * Found correct column here.
                     */
                    targetField = columnName;
                }
            } else {
                /*
                 * field ( actualField ) = column
                 */
                /*
                 * The system must get column by outMapping here
                 */
                final String columnName = inColumnMapping.get(field);
                if (Objects.isNull(columnName)) {
                    /*
                     * Found correct column here.
                     */
                    targetField = columnName;
                } else {
                    /*
                     * Lookup continue here.
                     */
                    targetField = field;
                }
            }
        }
        return targetField;
    }

    public ConcurrentMap<String, Field> columns() {
        return this.fieldMap;
    }

    public ConcurrentMap<String, Class<?>> types() {
        if (this.typeMap.isEmpty()) {
            // Here are no pojo defined
            if (Objects.isNull(this.pojo)) {
                this.fieldMap.forEach((name, field) -> this.typeMap.put(name, field.getType()));
            } else {
                this.fieldMap.forEach((name, field) -> {
                    final String fieldName = this.pojo.getOut(name);
                    if (Ut.isNotNil(fieldName)) {
                        this.typeMap.put(fieldName, field.getType());
                    }
                });
            }
        }
        return this.typeMap;
    }

    public Field column(final String field) {
        String columnField = columnName(field);
        Fn.outBoot(null == columnField, LOGGER,
            JooqFieldMissingException.class, UxJooq.class, field, this.entityCls);
        LOGGER.debug(INFO.JOOQ_FIELD, field, columnField);
        /*
         * Old code for field construct, following code will caurse Type/DataType missing
         * DSL.field(DSL.name(targetField));
         * 1) Extract from tableFields first
         * 2) Extract by construct ( Type / DataType ) will missing
         */
        Field found;
        if (field.equals(columnField)) {
            found = this.fieldMap.get(field);
        } else {
            final String actualField = this.revert.get(columnField);
            found = this.fieldMap.get(actualField);
        }
        if (Objects.isNull(found)) {
            found = DSL.field(DSL.name(columnField));
        }
        return found;
    }

    /*
     * Pick all columns that match input String[] field
     * This operation could be used in different aggr
     */
    public Field[] column(final String... fields) {
        /*
         * List<Field> building
         */
        final List<Field> columnList = new ArrayList<>();
        Arrays.asList(fields).forEach(field -> {
            /*
             * Column field
             */
            final Field columnField = this.column(field);
            if (Objects.nonNull(columnField)) {
                columnList.add(columnField);
            }
        });
        // The style is for Jooq
        return columnList.toArray(new Field[]{});
    }

    public void on(final String pojo, final Class<?> clazz) {
        if (Ut.isNil(pojo)) {
            this.pojo = null;
        } else {
            LOGGER.debug(INFO.JOOQ_BIND, pojo, clazz);
            this.pojo = Mirror.create(UxJooq.class).mount(pojo)
                .mojo().bindColumn(this.mapping);
            // When bind pojo, the system will analyze columns
            LOGGER.debug(INFO.JOOQ_MOJO, this.pojo.getIn(), this.pojo.getInColumn());
        }
    }

    public <T> T copyEntity(final T target, final T updated) {
        Fn.outBoot(null == updated, LOGGER, JooqMergeException.class,
            UxJooq.class, null == target ? null : target.getClass(), Ut.serialize(target));
        return Fn.runOr(null == target && null == updated, LOGGER, () -> null, () -> {
            final JsonObject targetJson = null == target ? new JsonObject() : Ut.serializeJson(target);
            /*
             * Skip Primary Key
             */
            final Table<?> tableField = this.table();
            final UniqueKey key = tableField.getPrimaryKey();
            key.getFields().stream().map(item -> ((TableField) item).getName())
                .filter(this.revert::containsKey)
                .map(this.revert::get)
                .forEach(item -> Ut.field(updated, item.toString(), null));
            /*
             * Deserialization
             */
            final JsonObject sourceJson = Ut.serializeJson(updated);
            targetJson.mergeIn(sourceJson, true);
            final Class<?> type = null == target ? updated.getClass() : target.getClass();
            return (T) Ut.deserialize(targetJson, type);
        });
    }

    public String pojoFile() {
        return this.pojoFile(null);
    }

    public String pojoFile(final String pojoExternal) {
        if (Objects.isNull(pojoExternal)) {
            if (Objects.isNull(this.pojo)) {
                /*
                 * If current analyzer is null pojo
                 * return "" instead of other pojo file
                 */
                return VString.EMPTY;
            } else {
                return this.pojo.getPojoFile();
            }
        } else {
            /*
             * External is high priority
             * -- !!!! Do not replace the pojo file that bind to analyzer
             */
            return pojoExternal;
        }
    }

    public Mojo pojo() {
        return this.pojo;
    }

    // -------------------------------- Condition Building
    public Condition condition(final JsonObject criteria) {
        return Ut.isNil(criteria) ? DSL.trueCondition() : JooqCond.transform(criteria, this::column);
    }

    public <ID> Condition conditionId(ID id) {
        UniqueKey<?> uk = this.table.getPrimaryKey();
        Objects.requireNonNull(uk, () -> "[ Jq ] No primary key");
        /**
         * Copied from jOOQs DAOImpl#equal-method
         */
        TableField<? extends org.jooq.Record, ?>[] pk = uk.getFieldsArray();
        Condition condition;
        if (pk.length == 1) {
            condition = ((Field<Object>) pk[0]).equal(pk[0].getDataType().convert(id));
        } else {
            condition = row(pk).equal((org.jooq.Record) id);
        }
        return condition;
    }

    public <T> Condition conditionUk(T pojo) {
        Objects.requireNonNull(pojo);
        org.jooq.Record record = this.dsl.context().newRecord(this.table, pojo);
        // Condition where = DSL.trueCondition();
        final Set<Condition> conditions = new HashSet<>();
        UniqueKey<?> pk = this.table.getPrimaryKey();
        for (TableField<?, ?> tableField : pk.getFields()) {
            //exclude primary keys from update
            final Condition condition = ((TableField<org.jooq.Record, Object>) tableField).eq(record.get(tableField));
            conditions.add(condition);
            // where = where.?nd(((TableField<org.jooq.Record, Object>) tableField).eq(record.get(tableField)));
        }
        return DSL.and(conditions);
    }

    public Condition conditionField(final String field, final Object value) {
        final Field column = this.column(field);
        if (value instanceof Collection) {
            // IN
            return column.in(value);
        } else {
            // =
            return column.eq(value);
        }
    }
}
