package io.vertx.tp.modular.jooq.internal;

import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.modeling.element.DataRow;
import io.vertx.tp.atom.modeling.element.DataTpl;
import io.vertx.tp.error._417DataTransactionException;
import io.vertx.up.exception.WebException;
import io.vertx.up.log.Annal;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

class OSync {

    private static final Annal LOGGER = Annal.get(OSync.class);

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
            final List<DataRow> rows = event.dataRows();
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
}
