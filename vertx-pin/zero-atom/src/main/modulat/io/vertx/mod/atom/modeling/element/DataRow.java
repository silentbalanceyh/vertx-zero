package io.vertx.mod.atom.modeling.element;

import io.modello.specification.HRecord;
import io.vertx.mod.atom.refine.Ao;
import io.vertx.up.util.Ut;
import org.jooq.Converter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * ## DataRow
 *
 * ### 1. Intro
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DataRow implements Serializable {
    /*
     * Table -> Row
     */
    private final transient ConcurrentMap<String, DataMatrix> matrix =
        new ConcurrentHashMap<>();
    private final transient ConcurrentMap<String, DataMatrix> keys =
        new ConcurrentHashMap<>();
    private final transient ConcurrentMap<String, Boolean> rowResult =
        new ConcurrentHashMap<>();
    /*
     * 数据行的结果，两种处理模式
     * 1. 直接 Record -> DataRow
     * 2. 直接 ID, Record -> DataRow
     * 3. 直接 ID -> DataRow
     **/
    private final transient DataTpl tpl;
    private final transient DataKey key;

    private final transient HRecord record;
    private transient Object id;

    public DataRow(final DataTpl tpl) {
        this.tpl = tpl;
        this.record = tpl.createRecord();
        this.key = tpl.createKey();
        /* 引用绑定 */
        this.key.setMatrix(this.keys);
        /* 拷贝 Tpl 中的数据到当前 Data Row */
        this.tpl.matrixData().forEach((key, value) -> this.matrix.put(key, value.copy()));
        this.tpl.matrixKey().forEach((key, value) -> this.keys.put(key, value.copy()));
        /* 初始化行处理结果 */
        this.matrix.keySet().forEach(table -> this.rowResult.put(table, Boolean.FALSE));
    }

    public ConcurrentMap<String, DataMatrix> matrixKey() {
        return this.keys;
    }

    public ConcurrentMap<String, DataMatrix> matrixData() {
        return this.matrix;
    }

    public DataKey getKey() {
        return this.key;
    }

    /*
     * 行主键有几个任务需要处理：
     * 1. 当前行的id设置成传入的id
     * 2. 当前行中的 matrix 数据列需要填充
     * 3. 当前行中的 keys 数据列需要填充
     * 4. 当前 record 需要填充
     */
    public <ID> DataRow setKey(final ID id) {
        // 设置当前行的 id 信息
        this.id = id;
        // 填充 record 的信息
        this.record.key(id);
        // 设置 matrix 中 主键信息
        Ao.connect(this.record, this.keys, this.matrix, this.record.joins());
        // this.record.matrix(this.keys, this.matrix);
        return this;
    }

    public Object getId() {
        /*
         * 旧代码
         * this.record.key()
         */
        return this.id;
    }

    public HRecord getRecord() {
        /*
         * 响应结果，会返回 null
         */
        final Set<String> primaryKeys = this.matrix.keySet().stream()
            .map(this.matrix::get)
            .flatMap(matrixItem -> matrixItem.getKeys().stream())
            .collect(Collectors.toSet());
        if (primaryKeys.size() == this.record.size()) {
            return null;
        } else {
            return this.record;
        }
    }
    // ----------------------------- Input / Output

    /**
     * Request Data Processing
     *
     * @param record {@link HRecord} Input new record or existing data record here.
     *
     * @return {@link DataRow}
     */
    public DataRow request(final HRecord record) {
        // 同步当前行的 id 信息
        this.id = record.key();
        /*
         * 不改引用，只填充数据，将 record 输入的数据
         * 填充到当前行的 DataRow 中
         */
        record.fieldUse().forEach(attribute -> this.record.set(attribute, record.get(attribute)));
        // 设置 matrix 中 主键信息
        this.matrix.values().forEach(matrix -> matrix.getAttributes().forEach(attribute -> matrix.set(attribute, record.get(attribute))));

        return this;
    }

    /**
     * Response Data Processing
     *
     * @param table      {@link java.lang.String} Input table name to build record ( include Joined multi tables )
     * @param record     {@link HRecord} Output data record
     * @param projection {@link java.util.Set}
     *
     * @return
     */
    @SuppressWarnings("all")
    public DataRow success(final String table, final org.jooq.Record record, final Set<String> projection) {
        /* 先同步DataMatrix */
        final DataMatrix matrix = this.matrix.get(table);
        if (null != record) {
            /* 有数据 */
            Arrays.asList(record.fields()).forEach(field -> {
                // 抽取表格列
                final String column = field.getName();
                // 抽取字段名
                final String attribute = matrix.getField(column);
                // 抽取字段名为空
                if (Ut.isNotNil(attribute)) {
                    /*
                     * 这里的 projections 的语义和 Jooq 以及数据域中的语义是一致的
                     *
                     * 1. 如果不传入任何 projection，= []，这种情况下不做任何过滤，直接返回所有的 projection 中的数据
                     * 2. 如果传入了 projection，那么在最终结果中只返回 projection 中包含的字段信息
                     *
                     * 在安全中的视图保存的语义和这里的语义是一致的，S_VIEW 中的保存语义：
                     *
                     * 不仅仅如此，如果出现了下边的情况下，这里提供相关的运算：
                     *
                     * 1. S_VIEW 中存储了当前角色或者用户的列信息
                     * 2. 请求中出现了 projection 的参数信息
                     *
                     * 以上两种情况会使用合并的方式来处理列的计算，也就是说此时 两个 projection 中的数据会进行集合添加，添加的
                     * 最终结果是最终的返回列信息。
                     */
                    if (0 == projection.size() || projection.contains(attribute)) {
                        // 抽取字段类型
                        final Class<?> type = matrix.getType(attribute);
                        // 提取 Converter
                        final Converter converter = Ao.converter(type);
                        final Object value;
                        if (Objects.isNull(converter)) {
                            // 类型直接转换
                            value = record.getValue(field, type);
                        } else {
                            // Type直接转换
                            value = record.getValue(field, converter);
                        }
                        // 填充值处理
                        matrix.set(attribute, value);
                        // 填充 Record 数据，必须是 JsonObject 可适配类型
                        this.record.set(attribute, Ut.aiJValue(value, type));
                    }
                }
            });
        }
        /* 再同步主键 */
        return this.success(table);
    }

    /*
     * 对于 DataRow 而言，写入结果时需要单表
     */
    public DataRow success(final String table) {
        this.rowResult.put(table, Boolean.TRUE);
        return this;
    }

    /*
     * 判断当前行有没操作成功
     * 1. 如果一行数据跨表，那么只要每一张表成功了，那么就成功了
     * 2. 所以最终检查的是 表结果 = TRUE 的数量
     */
    public boolean succeed() {
        final long results = this.rowResult.values()
            .stream().filter(item -> item).count();
        return this.rowResult.keySet().size() == results;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataRow)) {
            return false;
        }
        final DataRow dataRow = (DataRow) o;
        return this.id.equals(dataRow.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public void appendConsole(final StringBuilder source) {
        /* 头部信息读取 */
        source.append("------------------------- 记录：")
            .append(this.getId())
            .append(" -----------------------\n");
        this.keys.forEach((table, matrix) -> {
            final DataMatrix data = this.matrix.get(table);
            source.append("--------------------- 来源：").append(table).append("\n");
            data.appendData(source, 30);
        });
    }
}
