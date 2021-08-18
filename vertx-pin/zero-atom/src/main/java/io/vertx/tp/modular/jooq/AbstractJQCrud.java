package io.vertx.tp.modular.jooq;

import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.modeling.element.DataMatrix;
import io.vertx.tp.error._417ConditionEmptyException;
import io.vertx.tp.error._417DataUnexpectException;
import io.vertx.tp.modular.jooq.internal.Jq;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import org.jooq.DSLContext;
import org.jooq.Record;

import java.util.HashSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

@SuppressWarnings("all")
abstract class AbstractJQCrud {
    protected final transient DSLContext context;

    AbstractJQCrud(final DSLContext context) {
        this.context = context;
    }

    <R> DataEvent write(final DataEvent event, final BiFunction<String, DataMatrix, R> actorFn, final Predicate<R> testFn) {
        /* 读取所有的数据行（单表也按照多表处理） */
        return this.context.transactionResult(configuration -> Jq.doExec(this.getClass(), event,
                (rows) -> rows.forEach(row -> row.matrixData().forEach((table, matrix) -> {
                    // 输入检查
                    this.ensure(table, matrix);
                    // 执行结果（单表）
                    final R expected = actorFn.apply(table, matrix);
                    // 执行结果（检查）
                    Jq.output(expected, testFn, /* 成功 */ () -> row.success(table),
                            /* 失败 */ () -> new _417DataUnexpectException(getClass(), table, String.valueOf(expected)));
                })))
        );
    }

    <R> DataEvent read(final DataEvent event, final BiFunction<String, DataMatrix, Record> actorFn) {
        return this.context.transactionResult(configuration -> Jq.doExec(this.getClass(), event,
                (rows) -> rows.forEach(row -> row.matrixData().forEach((table, matrix) -> {
                    // 输入检查
                    this.ensure(table, matrix);
                    // 执行结果
                    final Record record = actorFn.apply(table, matrix);
                    // 反向同步记录
                    row.success(table, record, new HashSet<>());
                })))
        );
    }

    <R> DataEvent readBatch(final DataEvent event, final BiFunction<String, List<DataMatrix>, Record[]> actorFn) {
        return this.context.transactionResult(configuration -> Jq.doExec(this.getClass(), event,
                /* 按表转换，转换成 Table -> List<DataMatrix> 的结构 */
                (rows) -> Jq.argBatch(rows).forEach((table, values) -> {
                    /* 执行单表记录 */
                    this.ensure(table, values);
                    /* 执行结果 */
                    final Record[] records = actorFn.apply(table, values);
                    // 合并结果集
                    Jq.output(rows, records).accept(table);
                }))
        );
    }

    <R> DataEvent writeBatch(final DataEvent event, final BiFunction<String, List<DataMatrix>, R[]> actorFn, final Predicate<R[]> testFn) {
        return this.context.transactionResult(configuration -> Jq.doExec(this.getClass(), event,
                /* 按表转换，转换成 Table -> List<DataMatrix> 的结构 */
                (rows) -> Jq.argBatch(rows).forEach((table, values) -> {
                    /* 执行单表记录 */
                    this.ensure(table, values);
                    /* 执行结果（单表）*/
                    final R[] expected = actorFn.apply(table, values);
                    /* 单张表检查结果 */
                    Jq.output(expected, testFn, /*  成功 */ () -> rows.forEach(row -> row.success(table)),
                            /* 失败 */ () -> new _417DataUnexpectException(getClass(), table, expected.toString()));
                }))
        );
    }

    private void ensure(final String table, final DataMatrix matrix) {
        Fn.outWeb(matrix.getAttributes().isEmpty(), logger(), _417ConditionEmptyException.class, getClass(), table);
    }

    private void ensure(final String table, final List<DataMatrix> matrixes) {
        matrixes.forEach(matrix -> ensure(table, matrix));
    }

    private Annal logger() {
        return Annal.get(this.getClass());
    }
}
