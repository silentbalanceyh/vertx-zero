package io.vertx.tp.modular.jooq.internal;

import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.modeling.element.DataMatrix;
import io.vertx.tp.atom.modeling.element.DataRow;
import io.vertx.up.log.Annal;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Jooq统一工具类，和Ux,Ut模式一样，内部类全部使用包域
 */
@SuppressWarnings("all")
public class Jq {
    private static final Annal LOGGER = Annal.get(Jq.class);

    // 数据库元数据：to前缀
    /* 构造字段 */
    public static Field toField(final String field, final Set<DataMatrix> matrix,
                                final ConcurrentMap<String, String> fieldMap) {
        return Meta.field(field, matrix, fieldMap);
    }


    /* 构造表 */
    public static Table<Record> toTable(final String name) {
        return Meta.table(name);
    }

    /*
     * 自然连接
     * SELECT * FROM T1,T2
     *  */
    public static Table<Record> joinNature(final ConcurrentMap<String, String> aliasMap) {
        return Meta.natureJoin(aliasMap);
    }

    /*
     * 左连接
     * SELECT * FROM T1 LEFT JOIN T2 ON T1.x = T2.y
     */
    public static Table<Record> joinLeft(final String leader, final ConcurrentMap<String, String> vectors,
                                         final ConcurrentMap<String, String> aliasMap) {
        return Meta.leftJoin(leader, vectors, aliasMap);
    }

    // ----------------------- Input --------------------
    // 参数设置：in前缀
    public static <T> void argSet(final DataMatrix matrix, final BiFunction<Field, Object, T> function) {
        IArgument.inSet(matrix, function);
    }

    public static ConcurrentMap<String, List<DataMatrix>> argBatch(final List<DataRow> rows) {
        return IArgument.inBatch(rows);
    }

    public static Condition inWhere(final DataMatrix matrix) {
        return inMonitor(() -> IWhere.key(matrix), "onKey");
    }

    public static Condition inWhere(final List<DataMatrix> matrixList) {
        return inMonitor(() -> IWhere.keys(matrixList), "onKeys");
    }

    private static Condition inMonitor(final Supplier<Condition> supplier,
                                       final String method) {
        final Condition condition = supplier.get();
        LOGGER.debug("[ Ox ] 方法：{0}，查询条件：{1}", method, condition);
        return condition;
    }

    // ----------------------- Output --------------------
    public static DataEvent run(final Class<?> clazz, final DataEvent event, final Consumer<List<DataRow>> consumer) {
        return OFlow.doSync(clazz, event, consumer);
    }

    public static <R> CompletionStage<R> runAsync(final Class<?> clazz, final DataEvent event, final Function<List<DataRow>, CompletionStage<R>> executor) {
        return OFlow.doAsync(clazz, event, executor);
    }
}
