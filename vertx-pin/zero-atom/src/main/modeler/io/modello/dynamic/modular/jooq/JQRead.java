package io.modello.dynamic.modular.jooq;

import io.modello.dynamic.modular.jooq.internal.Jq;
import io.vertx.mod.atom.modeling.data.DataEvent;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectWhereStep;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
class JQRead extends AbstractJQCrud {

    JQRead(final DSLContext context) {
        super(context);
    }

    /*
     * 单条件处理，这里读取的最终数据只有一条，所以直接操作即可
     * 数据结构如：
     * Record -> Table1 -> DataMatrix1
     *        -> Table2 -> DataMatrix2
     */
    DataEvent fetchById(final DataEvent event) {
        /* 1. 读取当前DataMatrix 中的数据 */
        return this.read(event, (table, matrix) -> {
            /* 2. 执行条件处理 */
            final SelectWhereStep query = this.context.selectFrom(Jq.toTable(table));

            final Condition condition = Jq.inWhere(matrix);
            query.where(condition);

            /* 3. 执行结果 */
            return query.fetchOne();
        });
    }

    DataEvent fetchByIds(final DataEvent events) {
        /* 1. 读取当前 DataMatrix 中的数据 */
        return this.readBatch(events, (table, matrix) -> {
            /* 2. 执行条件处理 */
            final SelectWhereStep query = this.context.selectFrom(Jq.toTable(table));
            final Condition condition = Jq.inWhere(matrix);
            query.where(condition);

            /* 3. 执行结果 */
            return query.fetchArray();
        });
    }
}
