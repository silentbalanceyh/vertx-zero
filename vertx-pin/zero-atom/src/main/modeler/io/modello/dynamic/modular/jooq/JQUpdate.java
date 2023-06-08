package io.modello.dynamic.modular.jooq;

import io.modello.dynamic.modular.jooq.internal.Jq;
import io.vertx.mod.atom.modeling.data.DataEvent;
import io.vertx.mod.atom.modeling.element.DataMatrix;
import io.vertx.up.util.Ut;
import org.jooq.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("all")
class JQUpdate extends AbstractJQCrud {
    /* 查询数据专用 */
    private final transient JQQuery query;

    JQUpdate(final DSLContext context) {
        super(context);
        this.query = new JQQuery(context);
    }

    DataEvent update(final DataEvent event) {
        return this.write(event, (table, matrix) -> {
            final UpdateSetMoreStep step = this.stepUpdate(table, matrix);
            return step.execute();
        }, Ut::isPositive);
    }

    DataEvent updateBatch(final DataEvent event) {
        return this.<Integer>writeBatch(event, (table, matrix) -> {
            /* 批量更新 */
            final Batch batch = this.prepareBatch(table, matrix);
            final List<Integer> result = new ArrayList<>();
            Arrays.stream(batch.execute()).forEach(result::add);
            return result.toArray(new Integer[]{});
        }, Ut::isPositive);
    }

    private Batch prepareBatch(final String table, final List<DataMatrix> matrices) {
        final List<Query> batchOps = new ArrayList<>();
        matrices.stream().map(matrix -> this.stepUpdate(table, matrix)).forEach(batchOps::add);
        return this.context.batch(batchOps);
    }

    private UpdateSetMoreStep stepUpdate(final String table, final DataMatrix matrix) {
        final UpdateSetMoreStep steps = (UpdateSetMoreStep) this.context.update(Jq.toTable(table));

        Jq.argSet(matrix, steps::set);

        final Condition condition = Jq.inWhere(matrix);
        steps.where(condition);

        return steps;
    }
}
