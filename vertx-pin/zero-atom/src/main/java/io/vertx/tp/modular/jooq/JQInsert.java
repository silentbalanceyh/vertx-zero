package io.vertx.tp.modular.jooq;

import io.vertx.core.Future;
import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.modeling.element.DataMatrix;
import io.vertx.tp.modular.jooq.internal.Jq;
import io.vertx.up.util.Ut;
import org.jooq.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Jooq中的Insert处理
 */
@SuppressWarnings("all")
class JQInsert {

    private final transient DSLContext context;

    JQInsert(final DSLContext context) {
        this.context = context;
    }

    /*
     * DataRow -> 单表插入 / 多表插入
     * 会让数据库执行过程中的方法抛出的异常进行转换，并且生成 WebException抛出
     */
    DataEvent insert(final DataEvent event) {
        /*
         * 写操作开启事务处理
         */
        return this.context.transactionResult(configuration -> Jq.doWrite(this.getClass(), event, (table, matrix) -> {
            /* 执行单表插入功能 */
            final InsertReturningStep step = this.stepInsert(table, matrix);
            return step.execute();
        }, Ut::isPositive));
    }

    Future<DataEvent> insertAsync(final DataEvent event) {
        return null;
    }

    DataEvent insertBatch(final DataEvent event) {
        return this.context.transactionResult(configuration -> Jq.doWrites(this.getClass(), event, (table, matrixList) -> {
            /* 执行批量插入 */
            final Batch batch = this.prepareBatch(table, matrixList);
            /* 执行批量 */
            return batch.execute();
        }, Ut::isPositive));
    }

    Future<DataEvent> insertBatchAsync(final DataEvent event) {
        return null;
    }

    private Batch prepareBatch(final String table, final List<DataMatrix> matrices) {
        final List<Query> batchOps = new ArrayList<>();
        matrices.stream().map(matrix -> this.stepInsert(table, matrix)).forEach(batchOps::add);
        return this.context.batch(batchOps);
    }

    private InsertReturningStep stepInsert(final String table, final DataMatrix matrix) {
        /* 创建 insert 语句 */
        final InsertSetMoreStep steps = (InsertSetMoreStep) this.context.insertInto(Jq.toTable(table));
        /* 设置参数 */
        Jq.inArgument(matrix, steps::set);
        /* 执行 insert，若有重复Key则忽略 */
        return steps;
    }
}
