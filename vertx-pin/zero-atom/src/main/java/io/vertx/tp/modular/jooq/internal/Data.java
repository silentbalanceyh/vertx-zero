package io.vertx.tp.modular.jooq.internal;

import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.modeling.element.DataMatrix;
import io.vertx.tp.atom.modeling.element.DataRow;
import io.vertx.tp.atom.modeling.element.DataTpl;
import io.vertx.tp.error._417ConditionEmptyException;
import io.vertx.tp.error._417DataTransactionException;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.WebException;
import io.vertx.up.fn.Actuator;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

class Data {

    private static final Annal LOGGER = Annal.get(Data.class);

    /* 执行结果处理 */
    static DataEvent doFinal(final DataEvent event) {
        if (!event.succeed()) {
            final WebException error = event.getError();
            if (null != error) {
                throw error;
            } else {
                LOGGER.error("[ Ox ] 异常为空，但响应也非法。success = {0}", event.succeed());
            }
        }
        return event;
    }

    /* 执行过程报错 */
    static void doExecute(final Class<?> clazz,
                          final DataEvent event,
                          final Consumer<List<DataRow>> consumer) {
        final Annal logger = Annal.get(clazz);
        try {
            final List<DataRow> rows = event.getRows();
            if (null == rows || rows.isEmpty()) {
                /* 读取不了DataRow，第一层处理 */
                LOGGER.error("[ Ox ] 行引用为空，DataRow = null。");
            } else {
                consumer.accept(rows);
            }
        } catch (final DataAccessException ex) {
            logger.jvm(ex);
            final WebException error = new _417DataTransactionException(clazz, ex);
            event.failure(error);
        } catch (final Throwable ex) {
            ex.printStackTrace();
        }
    }

    static <T> void doImpact(final T expected,
                             final Predicate<T> predicate,
                             final Actuator actuator,
                             final Supplier<WebException> supplier/* 使用函数为延迟调用 */) {
        if (null == predicate) {
            /* 不关心执行结果影响多少行 */
            actuator.execute();
        } else {
            /* 关心结果，执行条件检查 */
            if (predicate.test(expected)) {
                actuator.execute();
            } else {
                /* 检查不通过抛出异常 */
                throw supplier.get();
            }
        }
    }

    static Consumer<String> doJoin(
            final List<DataRow> rows,
            final Record[] records) {
        return table -> {
            /* 两个数据集按索引合并 */
            for (int idx = Values.IDX; idx < rows.size(); idx++) {
                final DataRow row = rows.get(idx);
                if (null != row) {
                    if (idx < records.length) {
                        final Record record = records[idx];
                        /* 直接调用内置方法 */
                        row.success(table, record, new HashSet<>());
                    } else {
                        /* 空数据返回 */
                        row.success(table, null, new HashSet<>());
                    }
                }
            }
        };
    }

    static List<DataRow> doJoin(
            final Set<String> tableSet,
            final Record[] records,
            final DataTpl tpl,
            final Set<String> projection
    ) {
        final List<DataRow> rows = new ArrayList<>();
        for (final Record record : records) {
            final DataRow row = new DataRow(tpl);
            tableSet.forEach(table -> row.success(table, record, projection));
            rows.add(row);
        }
        return rows;
    }

    static void doInput(final String table, final DataMatrix matrix) {
        Fn.outWeb(matrix.getAttributes().isEmpty(), LOGGER,
                _417ConditionEmptyException.class, Data.class, table);
    }

    static void doInput(final String table, final List<DataMatrix> matrixList) {
        matrixList.forEach(matrix -> doInput(table, matrix));
    }
}
