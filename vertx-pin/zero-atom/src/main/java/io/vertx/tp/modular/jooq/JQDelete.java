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
class JQDelete extends AbstractJQCrud {

    JQDelete(final DSLContext context) {
        super(context);
    }

    DataEvent delete(final DataEvent event) {
        return this.write(event, (table, matrix) -> {
            /* 执行单表删除功能 */
            final DeleteWhereStep query = this.context.deleteFrom(Jq.toTable(table));
            final Condition condition = Jq.inWhere(matrix);
            query.where(condition);
            return query.execute();
        }, null);
    }

    Future<DataEvent> deleteAsync(final DataEvent event) {
        return this.writeAsync(event, (table, matrix) -> {
            final DeleteWhereStep query = this.context.deleteFrom(Jq.toTable(table));
            final Condition condition = Jq.inWhere(matrix);
            query.where(condition);
            return query.executeAsync();
        }, null);
    }

    DataEvent deleteBatch(final DataEvent event) {
        return this.<Integer>writeBatch(event, (table, matrix) -> {
            /* 执行单表删除功能 */
            final DeleteWhereStep query = this.context.deleteFrom(Jq.toTable(table));
            final Condition condition = Jq.inWhere(matrix);
            query.where(condition);
            /* 执行结果 */
            final int ret = query.execute();
            return new Integer[]{ret};
        }, null);
    }

    Future<DataEvent> deleteBatchAsync(final DataEvent event) {
        return this.<Integer>writeAsync(event, (table, matrix) -> {
            final DeleteWhereStep query = this.context.deleteFrom(Jq.toTable(table));
            final Condition condition = Jq.inWhere(matrix);
            query.where(condition);
            return query.executeAsync();
        }, null);
    }
}
