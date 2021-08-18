package io.vertx.tp.modular.jooq;

import io.vertx.core.Future;
import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.modeling.element.DataMatrix;
import io.vertx.tp.modular.jooq.internal.Jq;
import io.vertx.up.util.Ut;
import org.jooq.*;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
class JQUpdate {
    private final transient DSLContext context;
    /* 查询数据专用 */
    private final transient JQQuery query;

    JQUpdate(final DSLContext context) {
        this.context = context;
        this.query = new JQQuery(context);
    }

    DataEvent update(final DataEvent event) {
        return this.context.transactionResult(configuration -> Jq.doWrite(this.getClass(), event, (table, matrix) -> {
            /* 执行表单更新功能 */
            final UpdateSetMoreStep step = this.stepUpdate(table, matrix);

            return step.execute();
        }, Ut::isPositive));
    }

    Future<DataEvent> updateAsync(final DataEvent event) {
        return null;
    }

    DataEvent updateBatch(final DataEvent event) {
        return this.context.transactionResult(configuration -> Jq.doWrites(this.getClass(), event, (table, matrixList) -> {
            /* 批量更新 */
            final Batch batch = this.prepareBatch(table, matrixList);
            return batch.execute();
        }));
    }

    Future<DataEvent> updateBatchAsync(final DataEvent event) {
        return null;
    }

    private Batch prepareBatch(final String table, final List<DataMatrix> matrices) {
        final List<Query> batchOps = new ArrayList<>();
        matrices.stream().map(matrix -> this.stepUpdate(table, matrix)).forEach(batchOps::add);
        return this.context.batch(batchOps);
    }

    private UpdateSetMoreStep stepUpdate(final String table, final DataMatrix matrix) {
        final UpdateSetMoreStep steps = (UpdateSetMoreStep) this.context.update(Jq.toTable(table));

        Jq.inArgument(matrix, steps::set);

        final Condition condition = Jq.inWhere(matrix);
        steps.where(condition);

        return steps;
    }
}
