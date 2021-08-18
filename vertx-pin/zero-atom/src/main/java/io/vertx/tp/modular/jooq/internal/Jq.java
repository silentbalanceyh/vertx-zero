package io.vertx.tp.modular.jooq.internal;

import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.modeling.element.DataMatrix;
import io.vertx.tp.atom.modeling.element.DataRow;
import io.vertx.tp.modular.query.Ingest;
import io.vertx.up.exception.WebException;
import io.vertx.up.fn.Actuator;
import io.vertx.up.log.Annal;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
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
    public static DataEvent doExec(final Class<?> clazz, final DataEvent event, final Consumer<List<DataRow>> consumer) {
        OSync.doExecute(clazz, event, consumer);
        return OSync.doFinal(event);
    }

    public static <T> void output(final T expected, final Predicate<T> predicate, final Actuator actuator, final Supplier<WebException> supplier/* 使用函数为延迟调用 */) {
        OSync.doImpact(expected, predicate, actuator, supplier);
    }

    public static Consumer<String> output(final List<DataRow> rows, final Record[] records) {
        return OSync.doJoin(rows, records);
    }

    public static DataEvent doCount(final Class<?> clazz, final DataEvent event, final BiFunction<Set<String>, Ingest, Long> actor) {
        return DQuery.doCount(clazz, event, actor);
    }

    public static DataEvent doQuery(final Class<?> clazz, final DataEvent event, final BiFunction<Set<String>, Ingest, Record> actor) {
        return DQuery.doQuery(clazz, event, actor);
    }

    public static DataEvent doQuery(final Class<?> clazz, final DataEvent event, final BiFunction<Set<String>, Ingest, Record[]> actor, final BiFunction<Set<String>, Ingest, Long> counter) {
        return DQuery.doQuery(clazz, event, actor, counter);
    }

    public static DataEvent doAll(final Class<?> clazz, final DataEvent event, final BiFunction<Set<String>, Ingest, Record[]> actor) {
        return DQuery.doQuery(clazz, event, actor, null);
    }
}
