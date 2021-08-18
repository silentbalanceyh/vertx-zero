package io.vertx.tp.modular.jooq.internal;

import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.modeling.element.DataRow;
import io.vertx.tp.error._417DataTransactionException;
import io.vertx.up.exception.WebException;
import io.vertx.up.log.Annal;
import org.jooq.exception.DataAccessException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Function;

class OFlow {

    /* 执行过程报错 */
    static DataEvent doSync(final Class<?> clazz,
                            final DataEvent event,
                            final Consumer<List<DataRow>> consumer) {
        final Annal logger = Annal.get(clazz);
        try {
            final List<DataRow> rows = event.dataRows();
            if (null == rows || rows.isEmpty()) {
                /* 读取不了DataRow，第一层处理 */
                logger.error("[ Ox ] 行引用为空，DataRow = null。");
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
        // Result Here
        if (!event.succeed()) {
            final WebException error = event.getError();
            if (null != error) {
                throw error;
            } else {
                logger.error("[ Ox ] 异常为空，但响应也非法。success = {0}", event.succeed());
            }
        }
        return event;
    }

    static <R> CompletionStage<R> doAsync(final Class<?> clazz,
                                          final DataEvent event,
                                          final Function<List<DataRow>, CompletionStage<R>> executor) {
        final Annal logger = Annal.get(clazz);
        try {
            final List<DataRow> rows = event.dataRows();
            if (null == rows || rows.isEmpty()) {
                /* 读取不了DataRow，第一层处理 */
                logger.error("[ Ox ] (Async) 行引用为空，DataRow = null。");
                return CompletableFuture.completedFuture(null);
            } else {
                return executor.apply(rows);
            }
        } catch (final Throwable ex) {
            ex.printStackTrace();
            final WebException error = new _417DataTransactionException(clazz, ex);
            event.failure(error);
            return CompletableFuture.completedFuture(null);
        }
    }
}
