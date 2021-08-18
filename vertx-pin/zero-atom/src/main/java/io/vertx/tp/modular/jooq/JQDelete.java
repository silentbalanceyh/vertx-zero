package io.vertx.tp.modular.jooq;

import io.vertx.core.Future;
import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.modular.jooq.internal.Jq;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DeleteWhereStep;

/**
 * Jooq中的Delete处理
 */
@SuppressWarnings("all")
class JQDelete {
    private final transient DSLContext context;

    JQDelete(final DSLContext context) {
        this.context = context;
    }

    DataEvent delete(final DataEvent event) {
        return this.context.transactionResult(configuration -> Jq.doWrite(this.getClass(), event, (table, matrix) -> {
            /* 执行单表删除功能 */
            final DeleteWhereStep query = this.context.deleteFrom(Jq.toTable(table));
            final Condition condition = Jq.inWhere(matrix);
            query.where(condition);
            /* 执行结果 */
            return query.execute();
        }));
    }

    Future<DataEvent> deleteAsync(final DataEvent event) {
        return null;
    }

    DataEvent deleteBatch(final DataEvent event) {
        return this.context.transactionResult(configuration -> Jq.doWrites(this.getClass(), event, (table, matrixList) -> {
            /* 执行单表删除功能 */
            final DeleteWhereStep query = this.context.deleteFrom(Jq.toTable(table));
            final Condition condition = Jq.inWhere(matrixList);
            query.where(condition);
            /* 执行结果 */
            final int ret = query.execute();
            return new int[]{ret};
        }));
    }

    Future<DataEvent> deleteBatchAsync(final DataEvent event) {
        return null;
    }
}
