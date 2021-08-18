package io.vertx.tp.modular.jooq;

import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.modeling.element.DataMatrix;
import io.vertx.tp.atom.modeling.element.DataRow;
import io.vertx.tp.error._417ConditionEmptyException;
import io.vertx.tp.error._417DataUnexpectException;
import io.vertx.tp.modular.jooq.internal.Jq;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.WebException;
import io.vertx.up.fn.Actuator;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import org.jooq.DSLContext;
import org.jooq.Record;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
                    output(expected, testFn,
                            /* 成功 */ () -> row.success(table),
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
                    output(table, rows, records);
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
                    output(expected, testFn,
                            /* 成功 */ () -> rows.forEach(row -> row.success(table)),
                            /* 失败 */ () -> new _417DataUnexpectException(getClass(), table, expected.toString())
                    );
                }))
        );
    }

    private void ensure(final String table, final DataMatrix matrix) {
        Fn.outWeb(matrix.getAttributes().isEmpty(), logger(), _417ConditionEmptyException.class, getClass(), table);
    }

    private void ensure(final String table, final List<DataMatrix> matrixes) {
        matrixes.forEach(matrix -> ensure(table, matrix));
    }

    private <T> void output(final T expected,
                            final Predicate<T> predicate,
                            final Actuator actuator,
                            final Supplier<WebException> supplier/* 使用函数为延迟调用 */) {
        if (Objects.isNull(predicate)) {            /* 不关心执行结果影响多少行 */
            actuator.execute();
        } else {
            if (predicate.test(expected)) {         /* 关心结果，执行条件检查 */
                actuator.execute();
            } else {
                throw supplier.get();               /* 检查不通过抛出异常 */
            }
        }
    }

    private void output(final String table, final List<DataRow> rows, final Record[] records) {
        for (int idx = Values.IDX; idx < rows.size(); idx++) {              /* 两个数据集按索引合并 */
            final DataRow row = rows.get(idx);
            if (null != row) {
                if (idx < records.length) {
                    final Record record = records[idx];
                    row.success(table, record, new HashSet<>());            /* 直接调用内置方法 */
                } else {
                    row.success(table, null, new HashSet<>());       /* 空数据返回 */
                }
            }
        }
    }


    private Annal logger() {
        return Annal.get(this.getClass());
    }
}
