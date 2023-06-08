package io.modello.dynamic.modular.jooq.internal;

import io.horizon.eon.VString;
import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.error._417TableCounterException;
import io.vertx.mod.atom.modeling.element.DataMatrix;
import io.vertx.up.fn.Fn;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.vertx.mod.atom.refine.Ao.LOG;

/**
 * 读取元数据专用
 */
@SuppressWarnings("all")
class Meta {

    private static final Annal LOGGER = Annal.get(Meta.class);

    static Table<Record> table(final String name) {
        return DSL.table(DSL.name(name));
    }

    static Table<Record> table(final String name, final String alias) {
        return DSL.table(DSL.name(name)).as(DSL.name(alias));
    }

    /**
     * Map的处理
     * Table -> Alias格式，实现Join模式
     */
    static Table<Record> natureJoin(final ConcurrentMap<String, String> tableMap) {
        final Iterator<String> it = tableMap.keySet().iterator();
        Fn.outWeb(1 < tableMap.size() && !it.hasNext(),
            _417TableCounterException.class, Meta.class, "> 1");
        String table = it.next();
        Table<Record> result = table(table, tableMap.get(table));
        while (it.hasNext()) {
            table = it.next();
            final String alias = tableMap.get(table);
            result = result.naturalJoin(table(table, alias));
        }
        return result;
    }

    /**
     * @param leader   主表表名
     * @param joined   被连接的子表信息 table -> column
     * @param aliasMap 被重命名的结果
     */
    static Table<Record> leftJoin(final String leader,
                                  final ConcurrentMap<String, String> joined,
                                  final ConcurrentMap<String, String> aliasMap) {
        final Table<Record> first = table(leader, aliasMap.get(leader));
        Fn.outWeb(1 == aliasMap.size() && Objects.isNull(first),
            _417TableCounterException.class, Meta.class, "> 1");
        /*
         * 读取被 join 的第一张表
         */
        if (joined.isEmpty()) {
            return first;
        } else {
            /*
             * Table Condition
             */
            final Iterator<String> itJoin = joined.keySet()
                .stream().filter(table -> !leader.equals(table))
                .iterator();
            /*
             * 计算条件专用
             */
            final ConcurrentMap<String, String> normalized = new ConcurrentHashMap<>();
            aliasMap.keySet().forEach(table -> {
                final String tableKey = aliasMap.get(table);
                final String tableField = joined.get(table);
                /*
                 * 转换成 Column
                 */
                normalized.put(tableKey, tableField);
            });
            /*
             * T1 join T2
             * first = T1, joinTable = T2
             */
            final String firstJoin = itJoin.next();
            final Table<Record> joinTable = table(firstJoin, aliasMap.get(firstJoin));

            final String fromTable = first.getName();
            final String toTable = joinTable.getName();
            /*
             * T1 Join T2
             */
            TableOnConditionStep<Record> conditionStep = first.leftJoin(joinTable)
                .on(joinCondition(fromTable, normalized.get(fromTable), toTable, normalized.get(toTable)));
            while (itJoin.hasNext()) {
                final String join = itJoin.next();
                final Table<Record> tableJoin = table(join, aliasMap.get(join));
                final String nextName = tableJoin.getName();
                /*
                 * Joined Join T3, but on use
                 * T1.FIELD = T3.FIELD always
                 */
                conditionStep = conditionStep.join(tableJoin)
                    .on(joinCondition(fromTable, normalized.get(fromTable), nextName, normalized.get(nextName)));
            }
            return conditionStep;
        }
    }

/*  Fix issue of `join.null`, here could not do re-assignment of
    variable, instead you should do as new code.
private static TableOnConditionStep<Record> joinCondition(
        final Table<Record> from, final Table<Record> to,
        final ConcurrentMap<String, String> joined) {
        final String fromTable = from.getName();
        final String toTable = to.getName();
        return from.leftJoin(to).on(joinCondition(
            fromTable, joined.get(fromTable),
            toTable, joined.get(toTable)));
    }*/

    @SuppressWarnings("unchecked")
    private static Condition joinCondition(final String fromTable, final String fromField,
                                           final String toTable, final String toField) {
        /*
         * T1 Field构造
         */
        final Name fromName = DSL.name(fromField);
        final Field from = DSL.field(fromTable + VString.DOT + fromField);
        /*
         * T2 Field构造
         */
        final Field to = DSL.field(toTable + VString.DOT + toField);
        return from.eq(to);
    }

    static Field field(final String field, final DataMatrix matrix) {
        final String column = matrix.getColumn(field);
        Class<?> type = matrix.getType(field);
        /*
         * JsonArray / JsonObject 执行转换，转成 String 类型，兼容性处理
         */
        if (JsonArray.class == type || JsonObject.class == type) {
            /*
             * Bug: org.jooq.exception.SQLDialectNotSupportedException: Type class io.vertx.core.json.JsonArray is not supported in dialect DEFAULT
             */
            type = String.class;
        }
        return DSL.field(column, type);
    }

    /**
     * 根据DataMatrix中的数据构造字段
     */
    static Field field(final String field, final Set<DataMatrix> matrixSet,
                       final ConcurrentMap<String, String> fieldMap) {
        final DataMatrix matrix = matrixSet.stream()
            .filter(item -> item.getAttributes().contains(field))
            .findFirst().orElse(null);
        /*
         * 如果不包含字段则不抛异常，仅返回 null
         * Old
         * Fn.outWeb(null == matrix, _500ConditionFieldException.class, Meta.class, field);
         */
        if (null == matrix) {
            LOG.SQL.info(LOGGER, "模型中无法找到条件字段（INTERNAL）：`{0}`，省略……", field);
            return null;
        } else {
            final String column = matrix.getColumn(field);
            final Class<?> type = matrix.getType(field);
            if (null == fieldMap || fieldMap.isEmpty()) {
                // 不带表前缀
                return DSL.field(column, type);
            } else {
                // 带表前缀
                final String prefix = fieldMap.get(column);
                return DSL.field(prefix + "." + column, type);
            }
        }
    }
}
