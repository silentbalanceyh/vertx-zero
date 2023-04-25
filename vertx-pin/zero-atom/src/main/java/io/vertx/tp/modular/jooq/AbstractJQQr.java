package io.vertx.tp.modular.jooq;

import io.horizon.eon.em.modeler.ModelType;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.modeling.element.DataMatrix;
import io.vertx.tp.atom.modeling.element.DataRow;
import io.vertx.tp.atom.modeling.element.DataTpl;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.error._417TableCounterException;
import io.vertx.tp.modular.query.Ingest;
import io.vertx.up.eon.bridge.Values;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import org.jooq.DSLContext;
import org.jooq.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
abstract class AbstractJQQr {
    protected final transient DSLContext context;

    AbstractJQQr(final DSLContext context) {
        this.context = context;
    }

    protected DataEvent aggr(
        final DataEvent event,
        final BiFunction<Set<String>, Ingest, Long> queryFn
    ) {
        return this.context.transactionResult(configuration -> {
            final Ingest ingest = this.ingest(event);
            final ConcurrentMap<String, DataMatrix> matrix = this.matrix(this.getClass(), event);
            final Long counter = queryFn.apply(matrix.keySet(), ingest);
            event.stored(counter);
            return event;
        });
    }

    protected DataEvent qr(
        final DataEvent event,
        final BiFunction<Set<String>, Ingest, Record> queryFn) {
        return this.context.transactionResult(configuration -> {
            final Ingest ingest = this.ingest(event);
            final ConcurrentMap<String, DataMatrix> matrix = this.matrix(this.getClass(), event);
            // Query部分
            final DataTpl tpl = event.getTpl();
            final Set<String> projection = event.getProjection();

            final Record record = queryFn.apply(matrix.keySet(), ingest);
            event.stored(this.output(matrix.keySet(), new Record[]{record}, tpl, projection));
            return event;
        });
    }

    protected DataEvent qrBatch(
        final DataEvent event,
        final BiFunction<Set<String>, Ingest, Record[]> queryFn,
        final BiFunction<Set<String>, Ingest, Long> countFn
    ) {
        return this.context.transactionResult(configuration -> {
            final Ingest ingest = this.ingest(event);
            final ConcurrentMap<String, DataMatrix> matrix = this.matrix(this.getClass(), event);
            // Query部分
            final DataTpl tpl = event.getTpl();
            final Set<String> projection = event.getProjection();

            final Record[] records = queryFn.apply(matrix.keySet(), ingest);
            event.stored(this.output(matrix.keySet(), records, tpl, projection));
            if (Objects.nonNull(countFn)) {
                final Long counter = countFn.apply(matrix.keySet(), ingest);
                event.stored(counter);
            }
            return event;
        });
    }

    //  ---------------- Private ------------------

    private Ingest ingest(final DataEvent event) {
        /* 1. 读取模型类型 */
        final ModelType type = event.getType();
        /* 2. 专用转换 */
        final Ingest ingest = Ingest.create(type);
        final DataAtom atomRef = event.getTpl().atom();
        /* 3. 生成 Condition */
        Ao.infoUca(this.getClass(), "查询解析器：{0}，操作模型：{1}", null == ingest ? null : ingest.getClass().getName(), atomRef.identifier());
        return ingest;
    }

    private ConcurrentMap<String, DataMatrix> matrix(final Class<?> clazz, final DataEvent event) {
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

    private List<DataRow> output(final Set<String> tableSet, final Record[] records, final DataTpl tpl, final Set<String> projection) {
        final List<DataRow> rows = new ArrayList<>();
        for (final Record record : records) {
            final DataRow row = new DataRow(tpl);
            tableSet.forEach(table -> row.success(table, record, projection));
            rows.add(row);
        }
        return rows;
    }

    private Annal logger() {
        return Annal.get(this.getClass());
    }
}
