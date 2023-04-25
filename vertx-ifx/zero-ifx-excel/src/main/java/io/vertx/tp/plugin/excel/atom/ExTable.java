package io.vertx.tp.plugin.excel.atom;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._404ConnectMissingException;
import io.vertx.tp.plugin.booting.KConnect;
import io.vertx.up.eon.bridge.Strings;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ExTable implements Serializable {
    /* Field */
    private final transient List<String> fields = new ArrayList<>();
    private final transient List<ExRecord> values = new ArrayList<>();

    /* Metadata Row */
    private transient final String sheet;
    /* Complex Structure */
    private final transient ConcurrentMap<Integer, String> indexMap = new ConcurrentHashMap<>();
    private transient String name;
    private transient String description;
    private transient KConnect connect;

    public ExTable(final String sheet) {
        this.sheet = sheet;
    }

    public String getName() {
        return this.name;
    }

    /*
     * ( Bean )
     */
    public void setName(final String name) {
        this.name = name;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    /*
     * Class<?>
     * Dao / Pojo
     */
    @SuppressWarnings("all")
    public <T> Class<T> classPojo() {
        return (Class<T>) this.getConnect().getPojo();
    }

    public Class<?> classDao() {
        return this.getConnect().getDao();
    }

    public String filePojo() {
        return this.getConnect().getPojoFile();
    }

    public Set<String> ukIn() {
        return this.getConnect().ukIn();
    }

    public String pkIn() {
        return this.getConnect().pkIn();
    }

    /*
     * Business Unique
     */
    public JsonObject whereUnique(final JsonObject data) {
        final JsonArray unique = this.getConnect().getUnique();
        final JsonObject filters = new JsonObject();
        Ut.itJArray(unique, String.class, (field, index) -> {
            final Object value = data.getValue(field);
            if (Objects.nonNull(value)) {
                filters.put(field, value);
            }
        });
        return filters;
    }

    /*
     * 批量条件需执行新算法，防止
     * java.lang.StackOverflowError: null
     * 	at org.jooq.impl.AbstractQueryPart.equals(AbstractQueryPart.java:158)
     * 先根据 unique 统计数据，按统计结果排序，然后构造一个组
     * 如：code / sigma / stateId
     * 计算三个字段的总的数据量，得到最终的查询条件：
     * 1) 如果只有一个则直接 = 符号，若多个再使用 in 操作符，防止 in 操作符越界
     * 2) 得到的每个维度之间的全部使用 OR 连接符，叶节点则使用 AND 连接符
     * 转换公式如：
     * A & B1 & C1
     * A & B1 & C2
     * A & B2 & C3
     * A & B2 & C4
     * -->
     * A & IN (B1, B2) & IN (C1, C2, C3, C4)
     * 1）提供第二参数选择是否要截断C条件，因为导入过程只牵涉到保存，所以最低维度条件已经被涵盖，
     *    不会存在 A & IN (B1, B2) 条件不满足时候，却满足IN (C1, C2, C3, C4)的情况，由于第三条件
     *    是根据输入数据计算的，而第三条件满足，前边条件不满足的情况不存在，这种时候可直接省略第三条件（简化查询）
     * 2）简化第三条件可能引起查询数据会更多，但由于导入不牵涉删除，只是保存和更新，最终计算结构并不会受到影响，且
     *    在 Zero 中第三条件一般会是单条数据的 unique 判断。
     * 此处提取数据时如果前条件不满足，那么直接忽略这种数据，这种数据没有前置条件，可以不纳入考虑
     */
    public JsonObject whereAncient(final JsonArray data) {
        final JsonArray unique = this.getConnect().getUnique();
        final ConcurrentMap<String, Integer> counter = Ut.elementCount(data, unique);
        /*
         * 查找最大节点，最大节点的比对必须超过阈值，阈值设置原理
         * 1）前端下拉处理，稍稍复杂一点，阈值设置为32
         * 2）LBS中城市超过省份格式，目前只有城市、二级市会遇到：org.jooq.impl.AbstractQueryPart.equals(AbstractQueryPart.java:158)
         */
        final Integer max = Collections.max(counter.values());
        final JsonObject filters;
        if (32 < max) {
            filters = Ux.whereAnd();
            final Set<String> fields = new HashSet<>();
            counter.forEach((field, count) -> {
                if (!Objects.equals(max, count)) {
                    fields.add(field);
                }
            });
            fields.forEach(field -> {
                final JsonArray value = Ut.valueJArray(data, field);
                filters.put(field + ",i", value);
            });
        } else {
            // 底层处理，数据量比较少地情况
            filters = Ux.whereOr();
            Ut.itJArray(data, JsonObject.class, (item, index) -> {
                final String indexKey = "$" + index;
                filters.put(indexKey, this.whereUnique(item).put(Strings.EMPTY, Boolean.TRUE));
            });
        }
        return filters;
    }

    /*
     * System Unique
     */
    @SuppressWarnings("unchecked")
    public <ID> ID whereKey(final JsonObject data) {
        final String keyField = this.pkIn();
        if (Objects.nonNull(keyField)) {
            final Object id = data.getValue(keyField);
            return null == id ? null : (ID) id;
        } else {
            return null;
        }
    }

    /*
     * ( No Bean ) Iterator row of Add operation
     */
    public void add(final String field) {
        if (Ut.notNil(field)) {
            final int index = this.indexMap.size();
            this.fields.add(field);
            // index map
            this.indexMap.put(index, field);
        }
    }

    public void add(final String field, final String child) {
        if (Ut.notNil(field) && Ut.notNil(child)) {
            final String combine = field + Strings.DOT + child;
            if (!this.fields.contains(combine)) {
                this.fields.add(combine);
            }
            // index map
            final int index = this.indexMap.size();
            this.indexMap.put(index, combine);
        }
    }

    public void add(final ExRecord record) {
        if (!record.isEmpty()) {
            this.values.add(record);
        }
    }

    /*
     * Spec method to calculate row distinguish
     */
    public Set<Integer> indexDiff() {
        final Set<Integer> excludes = new HashSet<>();
        this.indexMap.forEach((index, field) -> {
            /*
             * Do not contain . means pure fields
             */
            if (!field.contains(".")) {
                excludes.add(index);
            }
        });
        return excludes;
    }

    /*
     * Get row values of List, ExRecord row data.
     */
    public List<ExRecord> get() {
        return this.values;
    }

    /*
     * Extract field by index
     */
    public String field(final int index) {
        return this.indexMap.getOrDefault(index, null);
    }

    public int size() {
        return this.fields.size();
    }

    private KConnect getConnect() {
        Fn.outWeb(null == this.connect, _404ConnectMissingException.class, this.getClass(), this.name);
        return this.connect;
    }

    public void setConnect(final KConnect connect) {
        this.connect = connect;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExTable)) {
            return false;
        }
        final ExTable table = (ExTable) o;
        return this.name.equals(table.name) &&
            this.sheet.equals(table.sheet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.sheet);
    }

    @Override
    public String toString() {
        final StringBuilder content = new StringBuilder();
        content.append("sheet = ").append(this.sheet).append(",");
        content.append("name = ").append(this.name).append(",");
        content.append("description = ").append(this.description).append("\n");
        content.append("daoCls = ").append(this.connect).append(",\n");
        this.fields.forEach(field -> content.append(field).append(","));
        content.append("\n");
        this.values.forEach(row -> content.append(row.toString()).append("\n"));
        return content.toString();
    }
}
