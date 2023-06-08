package io.modello.dynamic.modular.jooq.internal;

import io.horizon.eon.VValue;
import io.horizon.uca.log.Annal;
import io.vertx.mod.atom.modeling.element.DataMatrix;
import io.vertx.mod.atom.modeling.element.DataRow;
import org.jooq.Record;
import org.jooq.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
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
        return io.modello.dynamic.modular.jooq.internal.Meta.field(field, matrix, fieldMap);
    }


    /* 构造表 */
    public static Table<Record> toTable(final String name) {
        return io.modello.dynamic.modular.jooq.internal.Meta.table(name);
    }

    public static Record toRecord(final Result<Record> result) {
        if (Objects.isNull(result) || result.isEmpty()) {
            return null;
        } else {
            return result.get(VValue.IDX);
        }
    }

    public static Record[] toRecords(final Result<Record> result) {
        final List<Record> records = new ArrayList<>();
        result.forEach(records::add);
        return records.toArray(new Record[]{});
    }

    public static Long toCount(final Result<Record> result) {
        if (Objects.isNull(result) || result.isEmpty()) {
            return 0L;
        } else {
            final Record record = result.get(VValue.IDX);
            if (Objects.isNull(record)) {
                return 0L;
            } else {
                return record.get(0, Long.class);
            }
        }
    }

    /*
     * 自然连接
     * SELECT * FROM T1,T2
     *  */
    public static Table<Record> joinNature(final ConcurrentMap<String, String> aliasMap) {
        return io.modello.dynamic.modular.jooq.internal.Meta.natureJoin(aliasMap);
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
}
