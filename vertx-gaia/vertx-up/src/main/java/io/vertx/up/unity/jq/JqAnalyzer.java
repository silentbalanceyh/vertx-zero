package io.vertx.up.unity.jq;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.pojo.Mirror;
import io.vertx.up.atom.pojo.Mojo;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.zero.JooqFieldMissingException;
import io.vertx.up.exception.zero.JooqMergeException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("all")
class JqAnalyzer {

    private static final Annal LOGGER = Annal.get(JqAnalyzer.class);
    private static final ConcurrentMap<Integer, VertxDAO> DAO_POOL =
            new ConcurrentHashMap<>();

    private transient final VertxDAO vertxDAO;
    /* Field to Column */
    private transient final ConcurrentMap<String, String> mapping =
            new ConcurrentHashMap<>();
    /* Column to Field */
    private transient final ConcurrentMap<String, String> revert =
            new ConcurrentHashMap<>();

    private transient Mojo pojo;
    private transient String tableName;

    private transient ConcurrentMap<String, Field> fieldMap = new ConcurrentHashMap<>();

    private JqAnalyzer(final VertxDAO vertxDAO) {
        this.vertxDAO = Fn.pool(DAO_POOL, vertxDAO.hashCode(), () -> vertxDAO);
        // Mapping initializing
        final Table<?> tableField = Ut.field(this.vertxDAO, "table");

        final Class<?> typeCls = Ut.field(this.vertxDAO, "type");
        final java.lang.reflect.Field[] fields = Ut.fields(typeCls);
        // Analyze Type and definition sequence, columns hitted.
        final Field[] columns = tableField.fields();
        /*
         * Help for join
         */
        this.tableName = tableField.getName();

        // Mapping building
        for (int idx = Values.IDX; idx < columns.length; idx++) {
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

    static JqAnalyzer create(final VertxDAO vertxDAO) {
        return new JqAnalyzer(vertxDAO);
    }

    String table() {
        return this.tableName;
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

    ConcurrentMap<String, Field> columns() {
        return this.fieldMap;
    }

    Field column(final String field) {
        String columnField = columnName(field);
        Fn.outUp(null == columnField, LOGGER,
                JooqFieldMissingException.class, UxJooq.class, field, Ut.field(this.vertxDAO, "type"));
        LOGGER.debug(Info.JOOQ_FIELD, field, columnField);
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

    void on(final String pojo, final Class<?> clazz) {
        if (Ut.isNil(pojo)) {
            this.pojo = null;
        } else {
            LOGGER.debug(Info.JOOQ_BIND, pojo, clazz);
            this.pojo = Mirror.create(UxJooq.class).mount(pojo)
                    .mojo().bindColumn(this.mapping);
            // When bind pojo, the system will analyze columns
            LOGGER.debug(Info.JOOQ_MOJO, this.pojo.getIn(), this.pojo.getInColumn());
        }
    }

    <T> T copyEntity(final T target, final T updated) {
        Fn.outUp(null == updated, LOGGER, JooqMergeException.class,
                UxJooq.class, null == target ? null : target.getClass(), Ut.serialize(target));
        return Fn.getSemi(null == target && null == updated, LOGGER, () -> null, () -> {
            final JsonObject targetJson = null == target ? new JsonObject() : Ut.serializeJson(target);
            /*
             * Skip Primary Key
             */
            final Table<?> tableField = Ut.field(this.vertxDAO, "table");
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

    String pojoFile() {
        if (Objects.isNull(this.pojo)) {
            return Strings.EMPTY;
        } else {
            return this.pojo.getPojoFile();
        }
    }

    Mojo pojo() {
        return this.pojo;
    }
}
