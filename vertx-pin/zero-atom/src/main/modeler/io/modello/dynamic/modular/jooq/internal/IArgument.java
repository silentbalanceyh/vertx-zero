package io.modello.dynamic.modular.jooq.internal;

import io.vertx.mod.atom.modeling.element.DataMatrix;
import io.vertx.mod.atom.modeling.element.DataRow;
import io.vertx.up.util.Ut;
import org.jooq.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

/*
 * 条件工具类
 */
@SuppressWarnings("all")
class IArgument {

    /**
     * 根据 DataMatrix中的信息设置参数
     */
    static <T> void inSet(final DataMatrix matrix,
                          final BiFunction<Field, Object, T> function) {
        matrix.getAttributes().forEach(field -> {
            final Field column = Meta.field(field, matrix);
            final Object value = matrix.getValue(field);
            /*
             * 为空的时候不调用函数
             * 如果是需要清除值，则直接传入空字符串，而不是空，前端
             * 需要对 undefined 和 "" 进行判断
             * 1）undefined -> null（忽略）
             * 2）"" -> 空值（设置，清空）
             */
            if (Objects.nonNull(value)) {
                /*
                 * 类型对比
                 */
                final Class<?> typeDef = column.getType();
                final Class<?> typeIn = value.getClass();
                if (typeDef == typeIn) {
                    /*
                     * 类型相同，直接设置
                     */
                    function.apply(column, value);
                } else {
                    /*
                     * 类型不同，需要执行类型转换，否则无法设值
                     */
                    final Object normalized = Ut.aiValue(value, typeDef);
                    if (Objects.nonNull(normalized)) {
                        function.apply(column, normalized);
                    }
                }
            } else {
                /*
                 * 如果为空则直接为空
                 */
                function.apply(column, null);
            }
        });
    }

    /**
     * 一转多个
     */
    static ConcurrentMap<String, List<DataMatrix>> inBatch(
        final List<DataRow> rows
    ) {
        /* 按表转换，批量专用 */
        final ConcurrentMap<String, List<DataMatrix>> resultMap
            = new ConcurrentHashMap<>();
        rows.forEach(row -> {
            /* 处理数据行 */
            final ConcurrentMap<String, DataMatrix> rowData = row.matrixData();
            rowData.forEach((table, matrix) -> {
                if (!resultMap.containsKey(table)) {
                    resultMap.put(table, new ArrayList<>());
                }
                resultMap.get(table).add(matrix);
            });
        });
        return resultMap;
    }
}
