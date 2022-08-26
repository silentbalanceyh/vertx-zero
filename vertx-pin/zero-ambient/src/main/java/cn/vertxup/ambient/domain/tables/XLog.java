/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.ambient.domain.tables;


import cn.vertxup.ambient.domain.Db;
import cn.vertxup.ambient.domain.Indexes;
import cn.vertxup.ambient.domain.Keys;
import cn.vertxup.ambient.domain.tables.records.XLogRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class XLog extends TableImpl<XLogRecord> {

    /**
     * The reference instance of <code>DB_ETERNAL.X_LOG</code>
     */
    public static final XLog X_LOG = new XLog();
    private static final long serialVersionUID = 1L;
    /**
     * The column <code>DB_ETERNAL.X_LOG.KEY</code>. 「key」- 日志的主键
     */
    public final TableField<XLogRecord, String> KEY = createField(DSL.name("KEY"), SQLDataType.VARCHAR(36).nullable(false), this, "「key」- 日志的主键");
    /**
     * The column <code>DB_ETERNAL.X_LOG.TYPE</code>. 「type」- 日志的分类
     */
    public final TableField<XLogRecord, String> TYPE = createField(DSL.name("TYPE"), SQLDataType.VARCHAR(64), this, "「type」- 日志的分类");
    /**
     * The column <code>DB_ETERNAL.X_LOG.LEVEL</code>. 「level」- 日志级别：ERROR /
     * WARN / INFO
     */
    public final TableField<XLogRecord, String> LEVEL = createField(DSL.name("LEVEL"), SQLDataType.VARCHAR(10), this, "「level」- 日志级别：ERROR / WARN / INFO");
    /**
     * The column <code>DB_ETERNAL.X_LOG.INFO_STACK</code>. 「infoStack」- 堆栈信息
     */
    public final TableField<XLogRecord, String> INFO_STACK = createField(DSL.name("INFO_STACK"), SQLDataType.CLOB, this, "「infoStack」- 堆栈信息");
    /**
     * The column <code>DB_ETERNAL.X_LOG.INFO_SYSTEM</code>. 「infoSystem」- 日志内容
     */
    public final TableField<XLogRecord, String> INFO_SYSTEM = createField(DSL.name("INFO_SYSTEM"), SQLDataType.CLOB, this, "「infoSystem」- 日志内容");
    /**
     * The column <code>DB_ETERNAL.X_LOG.INFO_READABLE</code>. 「infoReadable」-
     * 日志的可读信息
     */
    public final TableField<XLogRecord, String> INFO_READABLE = createField(DSL.name("INFO_READABLE"), SQLDataType.CLOB, this, "「infoReadable」- 日志的可读信息");
    /**
     * The column <code>DB_ETERNAL.X_LOG.INFO_AT</code>. 「infoAt」- 日志记录时间
     */
    public final TableField<XLogRecord, LocalDateTime> INFO_AT = createField(DSL.name("INFO_AT"), SQLDataType.LOCALDATETIME(0), this, "「infoAt」- 日志记录时间");
    /**
     * The column <code>DB_ETERNAL.X_LOG.LOG_AGENT</code>. 「logAgent」- 记录日志的
     * agent 信息
     */
    public final TableField<XLogRecord, String> LOG_AGENT = createField(DSL.name("LOG_AGENT"), SQLDataType.VARCHAR(255), this, "「logAgent」- 记录日志的 agent 信息");
    /**
     * The column <code>DB_ETERNAL.X_LOG.LOG_IP</code>. 「logIp」- 日志扩展组件
     */
    public final TableField<XLogRecord, String> LOG_IP = createField(DSL.name("LOG_IP"), SQLDataType.VARCHAR(255), this, "「logIp」- 日志扩展组件");
    /**
     * The column <code>DB_ETERNAL.X_LOG.LOG_USER</code>. 「logUser」- 日志记录人
     */
    public final TableField<XLogRecord, String> LOG_USER = createField(DSL.name("LOG_USER"), SQLDataType.VARCHAR(36), this, "「logUser」- 日志记录人");
    /**
     * The column <code>DB_ETERNAL.X_LOG.ACTIVE</code>. 「active」- 是否启用
     */
    public final TableField<XLogRecord, Boolean> ACTIVE = createField(DSL.name("ACTIVE"), SQLDataType.BIT, this, "「active」- 是否启用");
    /**
     * The column <code>DB_ETERNAL.X_LOG.SIGMA</code>. 「sigma」- 统一标识
     */
    public final TableField<XLogRecord, String> SIGMA = createField(DSL.name("SIGMA"), SQLDataType.VARCHAR(32), this, "「sigma」- 统一标识");
    /**
     * The column <code>DB_ETERNAL.X_LOG.METADATA</code>. 「metadata」- 附加配置
     */
    public final TableField<XLogRecord, String> METADATA = createField(DSL.name("METADATA"), SQLDataType.CLOB, this, "「metadata」- 附加配置");
    /**
     * The column <code>DB_ETERNAL.X_LOG.LANGUAGE</code>. 「language」- 使用的语言
     */
    public final TableField<XLogRecord, String> LANGUAGE = createField(DSL.name("LANGUAGE"), SQLDataType.VARCHAR(8), this, "「language」- 使用的语言");
    /**
     * The column <code>DB_ETERNAL.X_LOG.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public final TableField<XLogRecord, LocalDateTime> CREATED_AT = createField(DSL.name("CREATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「createdAt」- 创建时间");
    /**
     * The column <code>DB_ETERNAL.X_LOG.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public final TableField<XLogRecord, String> CREATED_BY = createField(DSL.name("CREATED_BY"), SQLDataType.VARCHAR(36), this, "「createdBy」- 创建人");
    /**
     * The column <code>DB_ETERNAL.X_LOG.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public final TableField<XLogRecord, LocalDateTime> UPDATED_AT = createField(DSL.name("UPDATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「updatedAt」- 更新时间");
    /**
     * The column <code>DB_ETERNAL.X_LOG.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public final TableField<XLogRecord, String> UPDATED_BY = createField(DSL.name("UPDATED_BY"), SQLDataType.VARCHAR(36), this, "「updatedBy」- 更新人");

    private XLog(Name alias, Table<XLogRecord> aliased) {
        this(alias, aliased, null);
    }

    private XLog(Name alias, Table<XLogRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>DB_ETERNAL.X_LOG</code> table reference
     */
    public XLog(String alias) {
        this(DSL.name(alias), X_LOG);
    }

    /**
     * Create an aliased <code>DB_ETERNAL.X_LOG</code> table reference
     */
    public XLog(Name alias) {
        this(alias, X_LOG);
    }

    /**
     * Create a <code>DB_ETERNAL.X_LOG</code> table reference
     */
    public XLog() {
        this(DSL.name("X_LOG"), null);
    }

    public <O extends Record> XLog(Table<O> child, ForeignKey<O, XLogRecord> key) {
        super(child, key, X_LOG);
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<XLogRecord> getRecordType() {
        return XLogRecord.class;
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Db.DB_ETERNAL;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.X_LOG_IDXM_X_LOG_SIGMA_TYPE);
    }

    @Override
    public UniqueKey<XLogRecord> getPrimaryKey() {
        return Keys.KEY_X_LOG_PRIMARY;
    }

    @Override
    public XLog as(String alias) {
        return new XLog(DSL.name(alias), this);
    }

    @Override
    public XLog as(Name alias) {
        return new XLog(alias, this);
    }

    @Override
    public XLog as(Table<?> alias) {
        return new XLog(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public XLog rename(String name) {
        return new XLog(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public XLog rename(Name name) {
        return new XLog(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public XLog rename(Table<?> name) {
        return new XLog(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row18 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row18<String, String, String, String, String, String, LocalDateTime, String, String, String, Boolean, String, String, String, LocalDateTime, String, LocalDateTime, String> fieldsRow() {
        return (Row18) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function18<? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super LocalDateTime, ? super String, ? super String, ? super String, ? super Boolean, ? super String, ? super String, ? super String, ? super LocalDateTime, ? super String, ? super LocalDateTime, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function18<? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super LocalDateTime, ? super String, ? super String, ? super String, ? super Boolean, ? super String, ? super String, ? super String, ? super LocalDateTime, ? super String, ? super LocalDateTime, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
