package io.vertx.tp.modular.jooq.internal;

import io.vertx.core.json.JsonArray;
import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.modeling.element.DataMatrix;
import io.vertx.tp.error._417DataUnexpectException;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Predicate;

class DWriter {
    /**
     * 满足条件的时候就执行
     *
     * @param clazz     执行该方法的核心类
     * @param event     DataEvent 的 DTO
     * @param actor     执行函数，String, DataMatrix, Integer分别代表：表名、列属性矩阵、影响行
     * @param predicate 检查结果的函数
     */
    static DataEvent doWrite(
            /* 执行该方法的类 */
            final Class<?> clazz,

            /* 入参，专用DTO */
            final DataEvent event,

            /* 核心执行函数，到单张表 */
            final BiFunction<String, DataMatrix, Integer> actor,

            /* 如果为空则不检查结果，如果检查结果则提供 */
            final Predicate<Integer> predicate
    ) {
        /* 读取所有的数据行（单表也按照多表处理） */
        ONorm.doExecute(clazz, event, (rows) -> rows.forEach(row -> row.matrixData().forEach((table, matrix) -> {

            ONorm.doInput(table, matrix);
            /* 执行结果（单表） */
            final int impacts = actor.apply(table, matrix);

            /* 执行结果（检查）*/
            ONorm.doImpact(impacts, predicate,

                    /* 检查通过的结果 */
                    () -> row.success(table),

                    /* 抛出异常的结果 */
                    () -> new _417DataUnexpectException(clazz, table, String.valueOf(impacts)));
        })));
        /* 执行最终返回 */
        return ONorm.doFinal(event);
    }

    static DataEvent doWrites(
            /* 值行该方法的类 */
            final Class<?> clazz,

            /* 入参，专用DTO */
            final DataEvent event,

            /* 核心执行函数，单表多数据 */
            final BiFunction<String, List<DataMatrix>, int[]> actor,

            /* 如果为空则不检查结果，如果期待结果则需要四参 */
            final Predicate<int[]> predicate
    ) {
        ONorm.doExecute(clazz, event, (rows) -> {
            /* 按表转换，转换成 Table -> List<DataMatrix> 的结构 */
            final ConcurrentMap<String, List<DataMatrix>> batch = IArgument.inBatch(rows);

            /* 生成按表批量 */
            batch.keySet().forEach(table -> {

                /* 执行单表记录 */
                final List<DataMatrix> values = batch.get(table);

                ONorm.doInput(table, values);
                final int[] expected = actor.apply(table, values);

                /* 针对单张表的检查 */
                final JsonArray expectedArray = new JsonArray();
                Arrays.stream(expected).forEach(expectedArray::add);
                ONorm.doImpact(expected, predicate,

                        /* 设置每一个结果 */
                        () -> rows.forEach(row -> row.success(table)),

                        /* 抛出异常的结果 */
                        () -> new _417DataUnexpectException(clazz, table, expectedArray.encode()));
            });
        });
        return ONorm.doFinal(event);
    }
}
