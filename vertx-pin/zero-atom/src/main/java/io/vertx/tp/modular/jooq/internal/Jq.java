package io.vertx.tp.modular.jooq.internal;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.modeling.element.DataMatrix;
import io.vertx.tp.modular.query.Ingest;
import io.vertx.up.log.Annal;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;
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
    public static Table<Record> natureJoin(final ConcurrentMap<String, String> aliasMap) {
        return Meta.natureJoin(aliasMap);
    }

    /*
     * 左连接
     * SELECT * FROM T1 LEFT JOIN T2 ON T1.x = T2.y
     */
    public static Table<Record> leftJoin(final String leader, final ConcurrentMap<String, String> vectors,
                                         final ConcurrentMap<String, String> aliasMap) {
        return Meta.leftJoin(leader, vectors, aliasMap);
    }

    // ----------------------- Input --------------------
    // 参数设置：in前缀
    public static <T> void inArgument(final DataMatrix matrix, final BiFunction<Field, Object, T> function) {
        IArgument.inSet(matrix, function);
    }

    public static Condition onKey(final DataMatrix matrix) {
        return onMonitor(() -> IWhere.key(matrix), "onKey");
    }

    public static Condition onKeys(final List<DataMatrix> matrixList) {
        return onMonitor(() -> IWhere.keys(matrixList), "onKeys");
    }

    private static Condition onMonitor(final Supplier<Condition> supplier,
                                       final String method) {
        final Condition condition = supplier.get();
        LOGGER.debug("[ Ox ] 方法：{0}，查询条件：{1}", method, condition);
        return condition;
    }

    // ----------------------- Do --------------------
    // 数据库批量操作
    public static DataEvent doWrite(final Class<?> clazz, final DataEvent event, final BiFunction<String, DataMatrix, Integer> actor) {
        return DWriter.doWrite(clazz, event, actor, null);
    }

    public static DataEvent doWrite(final Class<?> clazz, final DataEvent event, final BiFunction<String, DataMatrix, Integer> actor, final Predicate<Integer> predicate) {
        return DWriter.doWrite(clazz, event, actor, predicate);
    }

    public static DataEvent doWrites(final Class<?> clazz, final DataEvent event, final BiFunction<String, List<DataMatrix>, int[]> actor) {
        return DWriter.doWrites(clazz, event, actor, null);
    }

    public static DataEvent doWrites(final Class<?> clazz, final DataEvent event, final BiFunction<String, List<DataMatrix>, int[]> actor, final Predicate<int[]> predicate) {
        return DWriter.doWrites(clazz, event, actor, predicate);
    }

    // 数据库读操作
    public static DataEvent doRead(final Class<?> clazz, final DataEvent event, final BiFunction<String, DataMatrix, Record> actor) {
        return DReader.doRead(clazz, event, actor);
    }

    public static DataEvent doReads(final Class<?> clazz, final DataEvent event, final BiFunction<String, List<DataMatrix>, Record[]> actor) {
        return DReader.doReads(clazz, event, actor);
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

    // ----------------------- Output --------------------
    public static Boolean outB(final DataEvent event, final Function<DataEvent, DataEvent> executor) {
        return OEvent.bool(event, executor);
    }

    public static Long outC(final DataEvent event, final Function<DataEvent, DataEvent> executor) {
        return OEvent.count(event, executor);
    }

    public static io.vertx.up.commune.Record outR(final DataEvent event, final Function<DataEvent, DataEvent> executor) {
        return OEvent.record(event, executor, false);
    }

    public static io.vertx.up.commune.Record[] outRs(final DataEvent event, final Function<DataEvent, DataEvent> executor) {
        return OEvent.record(event, executor, true);
    }

    public static JsonObject outP(final DataEvent event, final Function<DataEvent, DataEvent> executor) {
        return OEvent.pagination(event, executor);
    }

    public static Future<io.vertx.up.commune.Record> outRAsync(final DataEvent event, final Function<DataEvent, DataEvent> executor) {
        return OEvent.recordAsync(event, executor, false);
    }

    public static Future<io.vertx.up.commune.Record[]> outRsAsync(final DataEvent event, final Function<DataEvent, DataEvent> executor) {
        return OEvent.recordAsync(event, executor, true);
    }

    public static Future<JsonObject> outPAsync(final DataEvent event, final Function<DataEvent, DataEvent> executor) {
        return OEvent.paginationAsync(event, executor);
    }
}
