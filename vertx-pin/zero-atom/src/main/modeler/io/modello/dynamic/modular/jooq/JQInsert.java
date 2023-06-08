package io.modello.dynamic.modular.jooq;

import io.modello.dynamic.modular.jooq.internal.Jq;
import io.vertx.mod.atom.modeling.data.DataEvent;
import io.vertx.mod.atom.modeling.element.DataMatrix;
import io.vertx.up.util.Ut;
import org.jooq.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Jooq中的Insert处理
 */
@SuppressWarnings("all")
class JQInsert extends AbstractJQCrud {

    JQInsert(final DSLContext context) {
        super(context);
    }

    /*
     * DataRow -> 单表插入 / 多表插入
     * 会让数据库执行过程中的方法抛出的异常进行转换，并且生成 WebException抛出
     */
    DataEvent insert(final DataEvent event) {
        return this.write(event, (table, matrix) -> {
            /* 执行单表插入 */
            final InsertReturningStep step = this.stepInsert(table, matrix);
            return step.execute();
        }, Ut::isPositive);
    }

    DataEvent insertBatch(final DataEvent event) {
        return this.<Integer>writeBatch(event, (table, matrix) -> {
            /* 执行批量插入 */
            final Batch batch = this.prepareBatch(table, matrix);
            /* 执行批量 */
            final List<Integer> result = new ArrayList<>();
            Arrays.stream(batch.execute()).forEach(result::add);
            return result.toArray(new Integer[]{});
        }, Ut::isPositive);
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
        Jq.argSet(matrix, steps::set);
        /* 执行 insert，若有重复Key则忽略 */
        return steps;
    }
}
