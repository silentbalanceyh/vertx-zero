package io.vertx.tp.modular.jooq.internal;

import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.modeling.element.DataMatrix;
import org.jooq.Record;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

class Reader {
    /**
     * 单条记录读取操作
     */
    static DataEvent doRead(
            /* 执行该方法的类 */
            final Class<?> clazz,

            /* 入参，专用DTO */
            final DataEvent event,

            /* 核心执行函数，到单张表 */
            final BiFunction<String, DataMatrix, Record> actor
    ) {
        /* 读取所有数据行 */
        Data.doExecute(clazz, event, (rows) -> rows.forEach(row -> row.matrixData().forEach((table, matrix) -> {

            Data.doInput(table, matrix);
            /* 记录处理 */
            final Record record = actor.apply(table, matrix);
            /* 设置反向同步记录 */
            row.success(table, record, new HashSet<>());
        })));

        /* 执行最终返回 */
        return Data.doFinal(event);
    }

    /**
     * 批量记录读取操作
     */
    static DataEvent doReads(
            /* 执行该方法的类 */
            final Class<?> clazz,
            /* 入参，专用DTO */
            final DataEvent event,

            /* 核心执行函数，到多张表 */
            final BiFunction<String, List<DataMatrix>, Record[]> actor
    ) {
        /* 读取所有数据行，有几张表查几次 */
        Data.doExecute(clazz, event, (rows) -> {
            /* 按表转换，转换成 Table -> List<DataMatrix> 的结构 */
            final ConcurrentMap<String, List<DataMatrix>> batch = Argument.batch(rows);
            /* 按表批量处理 */
            batch.keySet().forEach(table -> {
                /* 执行单表记录 */
                final List<DataMatrix> values = batch.get(table);

                Data.doInput(table, values);
                final Record[] records = actor.apply(table, values);
                /* 合并两个结果集 */
                Data.doJoin(rows, records).accept(table);
            });
        });
        /* 执行最终返回 */
        return Data.doFinal(event);
    }
}
