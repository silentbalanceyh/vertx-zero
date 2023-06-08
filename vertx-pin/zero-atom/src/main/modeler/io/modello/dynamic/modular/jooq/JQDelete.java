package io.modello.dynamic.modular.jooq;

import io.modello.dynamic.modular.jooq.internal.Jq;
import io.vertx.mod.atom.modeling.data.DataEvent;
import io.vertx.mod.atom.modeling.element.DataMatrix;
import org.jooq.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Jooq中的Delete处理
 */
@SuppressWarnings("all")
class JQDelete extends AbstractJQCrud {

    JQDelete(final DSLContext context) {
        super(context);
    }

    DataEvent delete(final DataEvent event) {
        return this.write(event, (table, matrix) -> {
            /* 执行单表删除功能 */
            final DeleteWhereStep query = this.stepDelete(table, matrix);
            return query.execute();
        }, null);
    }

    DataEvent deleteBatch(final DataEvent event) {
        return this.<Integer>writeBatch(event, (table, matrix) -> {
            final Batch batch = this.prepareBatch(table, matrix);
            final List<Integer> result = new ArrayList<>();
            Arrays.stream(batch.execute()).forEach(result::add);
            return result.toArray(new Integer[]{});
        }, null);
    }

    private Batch prepareBatch(final String table, final List<DataMatrix> matrices) {
        final List<Query> batchOps = new ArrayList<>();
        matrices.stream().map(matrix -> this.stepDelete(table, matrix)).forEach(batchOps::add);
        return this.context.batch(batchOps);
    }

    private DeleteWhereStep stepDelete(final String table, final DataMatrix matrix) {
        final DeleteWhereStep query = this.context.deleteFrom(Jq.toTable(table));
        final Condition condition = Jq.inWhere(matrix);
        query.where(condition);
        /* Batch */
        return query;
    }
}
