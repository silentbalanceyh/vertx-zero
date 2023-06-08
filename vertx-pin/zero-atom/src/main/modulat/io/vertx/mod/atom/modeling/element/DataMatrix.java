package io.vertx.mod.atom.modeling.element;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MField;
import io.modello.dynamic.modular.metadata.AoSentence;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.mod.atom.cv.AoMsg;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.vertx.mod.atom.refine.Ao.LOG;

/**
 * 每个项的矩阵信息，里面包含了几部分内容
 * 表名：tableName
 * 列名：columnName
 * 属性名：attributeName
 * 输入值：value
 * 类型：type（用于后期设置特殊参数专用）
 * 这里为什么使用属性名，而不是使用字段名，原因：
 * 1. 属性名位于模型，输入的参数直接是以属性名为主，而不是字段名为主
 * 2. 字段名只能内部使用，主要是 sourceField 以及 底层 Entity在构造表和提取相关参数时使用
 */
public class DataMatrix implements Serializable {

    /* 从属性名: attribute name -> 列名：column name */
    private transient final ConcurrentMap<String, String> attributeMap = new ConcurrentHashMap<>();
    /* 从属性名：attribute name -> 字段类型：type */
    private transient final ConcurrentMap<String, Class<?>> typeMap = new ConcurrentHashMap<>();
    /* 从属性名：attribute name -> 值 */
    private transient final ConcurrentMap<String, Object> valueMap = new ConcurrentHashMap<>();
    /* 从列名称：column name -> 属性名：attribute name */
    private transient final ConcurrentMap<String, String> revertMap = new ConcurrentHashMap<>();
    /* 主键集 */
    private transient final Set<String> primaryKeys = new HashSet<>();
    /* 语句属性 */
    private transient AoSentence sentence;

    private DataMatrix() {
    }

    public static DataMatrix create() {
        return new DataMatrix();
    }

    public DataMatrix on(final AoSentence sentence) {
        this.sentence = sentence;
        return this;
    }

    @Fluent
    public DataMatrix add(final MField field,
                          final MAttribute attribute,
                          final Object value) {
        if (null == field || null == attribute) {
            LOG.Atom.debug(this.getClass(), AoMsg.DATA_ATOM);
        } else {
            final String columnName = this.wrapperColumn(field.getColumnName());
            final String name = attribute.getName();
            final Class<?> type = Ut.clazz(field.getType());

            this.attributeMap.put(name, columnName);
            this.revertMap.put(columnName, name);
            this.typeMap.put(name, type);

            if (Objects.nonNull(value)) {
                /*
                 * 写入的时候 record 中的 value 不为 null
                 * 但是读取数据的时候 record 中的 value 在解析之前是为 null的
                 */
                this.valueMap.put(name, value);
            }

            if (field.getIsPrimary()) {
                this.primaryKeys.add(attribute.getName());
            }
        }
        return this;
    }

    @Fluent
    public DataMatrix add(final MField field,
                          final MAttribute attribute) {
        return this.add(field, attribute, null);
    }

    @Fluent
    public DataMatrix set(final String attribute, final Object value) {
        LOG.Atom.debug(this.getClass(), AoMsg.DATA_SET, attribute, value);
        if (Objects.nonNull(value) && this.attributeMap.containsKey(attribute)) {
            this.valueMap.put(attribute, value);
        }
        return this;
    }

    public DataMatrix copy() {
        final DataMatrix that = new DataMatrix();
        that.sentence = this.sentence;
        this.attributeMap.forEach(that.attributeMap::put);
        this.typeMap.forEach(that.typeMap::put);
        this.revertMap.forEach(that.revertMap::put);
        that.primaryKeys.addAll(this.primaryKeys);
        return that;
    }

    private String wrapperColumn(final String column) {
        String columnName = column;
        if (null != this.sentence) {
            columnName = this.sentence.columnDdl(columnName);
        }
        return columnName;
    }

    public Set<String> getAttributes() {
        return this.attributeMap.keySet();
    }

    public String getColumn(final String field) {
        return this.attributeMap.get(field);
    }

    public Set<String> getKeys() {
        return this.primaryKeys;
    }

    public String getField(final String column) {
        final String targetCol = this.wrapperColumn(column);
        return this.revertMap.get(targetCol);
    }

    public Class<?> getType(final String field) {
        return this.typeMap.get(field);
    }

    public Object getValue(final String field) {
        return this.valueMap.get(field);
    }

    // DEBUG: 调试专用方法
    public void appendConsole(final StringBuilder buffer) {
        buffer.append("| - 主键属性：").append(Ut.fromJoin(this.primaryKeys)).append("\n");
        buffer.append(String.format("%-20s", "字段名")).append("\t");
        buffer.append(String.format("%-20s", "列名")).append("\t");
        buffer.append(String.format("%-40s", "类型")).append("\t");
        buffer.append(String.format("%-20s", "值")).append("\n");
        this.attributeMap.forEach((field, column) -> {
            buffer.append(String.format("%-20s", field)).append("\t");
            buffer.append(String.format("%-20s", column)).append("\t");
            buffer.append(String.format("%-40s", this.typeMap.get(field))).append("\t");
            buffer.append(String.format("%-20s", this.valueMap.get(field))).append("\n");
        });
    }

    public void appendData(final StringBuilder builder, final Integer keyWidth) {
        this.attributeMap.forEach((field, column) -> {
            final String item = "%-" + keyWidth + "s";
            final Object value = this.getValue(field);
            builder.append(String.format(item, field))
                .append(String.format(item, value)).append("\n");
        });
    }
}
