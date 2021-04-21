package io.vertx.tp.atom.modeling.element;

import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.commune.Record;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

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

    private final transient Record record;
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

    public DataRow setData(final Record record) {
        // 同步当前行的 id 信息
        this.id = record.key();
        /*
         * 不改引用，只填充数据，将 record 输入的数据
         * 填充到当前行的 DataRow 中
         */
        record.fields().forEach(attribute ->
                this.record.set(attribute, record.get(attribute)));
        // 设置 matrix 中 主键信息
        Ao.connect(this.record, this.matrix);

        // this.record.matrix(this.matrix);
        return this;
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

    public Record getRecord() {
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

    public Record getRowRecord() {
        return this.record;
    }

    /*
     * 对于 DataRow 而言，写入结果时需要单表
     */
    public DataRow success(final String table) {
        this.rowResult.put(table, Boolean.TRUE);
        return this;
    }

    /*
     * 需要反向同步的情况调用该方法
     * 反向同步：
     * Matrix, Keys 中的数据反向写入到 Record 中
     * 证明外层调用需要依赖响应结果
     */
    public DataRow success(final String table, final org.jooq.Record record, final Set<String> projection) {
        /* 先同步DataMatrix */
        final DataMatrix matrix = this.matrix.get(table);
        Sync.doData(matrix, this.record, record, projection);
        /* 再同步主键 */
        return this.success(table);
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
