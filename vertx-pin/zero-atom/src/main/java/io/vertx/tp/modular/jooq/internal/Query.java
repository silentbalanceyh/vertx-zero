package io.vertx.tp.modular.jooq.internal;

import io.vertx.tp.atom.cv.em.ModelType;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.modeling.element.DataMatrix;
import io.vertx.tp.atom.modeling.element.DataTpl;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.error._417TableCounterException;
import io.vertx.tp.modular.query.Ingest;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;
import org.jooq.Record;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

/*
 * 查询专用，解析查询树
 */
class Query {

    static DataEvent doCount(
            /* 当前操作类 */
            final Class<?> clazz,
            /* 发送的事件 */
            final DataEvent event,
            /* Bi Consumer */
            final BiFunction<Set<String>, Ingest, Long> consumer
    ) {
        final Ingest ingest = getIngest(event);

        final ConcurrentMap<String, DataMatrix> matrix = getMatrix(clazz, event);

        /* 5. 执行结果 */
        final Long counter = consumer.apply(matrix.keySet(), ingest);

        event.stored(counter);
        return event;
    }

    /**
     * 查询和其他操作执行分流处理
     */
    static DataEvent doQuery(
            /* 当前操作类 */
            final Class<?> clazz,
            /* 发送的事件 */
            final DataEvent event,
            /* Bi Consumer */
            final BiFunction<Set<String>, Ingest, Record> consumer
    ) {

        final Ingest ingest = getIngest(event);

        final ConcurrentMap<String, DataMatrix> matrix = getMatrix(clazz, event);

        final DataTpl tpl = event.getTpl();

        /* 5. 执行结果 */
        final Record record = consumer.apply(matrix.keySet(), ingest);

        /* 6. 处理行数据 */
        event.stored(Data.doJoin(matrix.keySet(), new Record[]{record}, tpl));
        return event;
    }

    static DataEvent doQuery(
            /* 当前操作类 */
            final Class<?> clazz,
            /* 发送的事件 */
            final DataEvent event,
            /* Bi Consumer */
            final BiFunction<Set<String>, Ingest, Record[]> consumer,
            /* Bi Counter */
            final BiFunction<Set<String>, Ingest, Long> counter
    ) {

        final Ingest ingest = getIngest(event);

        final ConcurrentMap<String, DataMatrix> matrix = getMatrix(clazz, event);

        final DataTpl tpl = event.getTpl();

        final Record[] records = consumer.apply(matrix.keySet(), ingest);
        event.stored(Data.doJoin(matrix.keySet(), records, tpl));
        if (Objects.nonNull(counter)) {
            final Long count = counter.apply(matrix.keySet(), ingest);
            event.stored(count);
        }
        return event;
    }

    private static Ingest getIngest(final DataEvent event) {
        /* 1. 读取模型类型 */
        final ModelType type = event.getType();
        /* 2. 专用转换 */
        final Ingest ingest = Ingest.create(type);
        final DataAtom atomRef = event.getTpl().atom();
        /* 3. 生成 Condition */
        Ao.infoUca(Query.class, "查询解析器：{0}，操作模型：{1}",
                null == ingest ? null : ingest.getClass().getName(), atomRef.identifier());
        return ingest;
    }

    private static ConcurrentMap<String, DataMatrix> getMatrix(final Class<?> clazz, final DataEvent event) {
        /* 1. 读取模型类型 */
        final ModelType type = event.getType();
        final DataTpl tpl = event.getTpl();
        final ConcurrentMap<String, DataMatrix> matrix = tpl.matrixData();

        if (ModelType.DIRECT == type) {
            /* 4.单表单映射 */
            Fn.outWeb(Values.ONE != matrix.size(), _417TableCounterException.class, clazz, matrix.size());
        } else if (ModelType.JOINED == type) {
            /* 6.单表多映射 */
            Fn.outWeb(Values.ONE >= matrix.size(), _417TableCounterException.class, clazz, matrix.size());
        }
        return matrix;
    }
}
