/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.ambient.domain.tables;


import cn.vertxup.ambient.domain.Db;
import cn.vertxup.ambient.domain.Keys;
import cn.vertxup.ambient.domain.tables.records.XModuleRecord;
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
public class XModule extends TableImpl<XModuleRecord> {

    /**
     * The reference instance of <code>DB_ETERNAL.X_MODULE</code>
     */
    public static final XModule X_MODULE = new XModule();
    private static final long serialVersionUID = 1L;
    /**
     * The column <code>DB_ETERNAL.X_MODULE.KEY</code>. 「key」- 模块唯一主键
     */
    public final TableField<XModuleRecord, String> KEY = createField(DSL.name("KEY"), SQLDataType.VARCHAR(36).nullable(false), this, "「key」- 模块唯一主键");
    /**
     * The column <code>DB_ETERNAL.X_MODULE.NAME</code>. 「name」- 模块名称
     */
    public final TableField<XModuleRecord, String> NAME = createField(DSL.name("NAME"), SQLDataType.VARCHAR(255), this, "「name」- 模块名称");
    /**
     * The column <code>DB_ETERNAL.X_MODULE.CODE</code>. 「code」- 模块编码
     */
    public final TableField<XModuleRecord, String> CODE = createField(DSL.name("CODE"), SQLDataType.VARCHAR(36), this, "「code」- 模块编码");
    /**
     * The column <code>DB_ETERNAL.X_MODULE.ENTRY</code>. 「entry」— 模块入口地址
     */
    public final TableField<XModuleRecord, String> ENTRY = createField(DSL.name("ENTRY"), SQLDataType.VARCHAR(255), this, "「entry」— 模块入口地址");
    /**
     * The column <code>DB_ETERNAL.X_MODULE.BLOCK_CODE</code>. 「blockCode」—
     * 所属模块系统编码
     */
    public final TableField<XModuleRecord, String> BLOCK_CODE = createField(DSL.name("BLOCK_CODE"), SQLDataType.VARCHAR(255), this, "「blockCode」— 所属模块系统编码");
    /**
     * The column <code>DB_ETERNAL.X_MODULE.APP_ID</code>. 「appId」- 关联的应用程序ID
     */
    public final TableField<XModuleRecord, String> APP_ID = createField(DSL.name("APP_ID"), SQLDataType.VARCHAR(255), this, "「appId」- 关联的应用程序ID");
    /**
     * The column <code>DB_ETERNAL.X_MODULE.MODEL_ID</code>. 「modelId」-
     * 当前模块关联的主模型ID
     */
    public final TableField<XModuleRecord, String> MODEL_ID = createField(DSL.name("MODEL_ID"), SQLDataType.VARCHAR(36), this, "「modelId」- 当前模块关联的主模型ID");
    /**
     * The column <code>DB_ETERNAL.X_MODULE.ACTIVE</code>. 「active」- 是否启用
     */
    public final TableField<XModuleRecord, Boolean> ACTIVE = createField(DSL.name("ACTIVE"), SQLDataType.BIT, this, "「active」- 是否启用");
    /**
     * The column <code>DB_ETERNAL.X_MODULE.SIGMA</code>. 「sigma」- 统一标识
     */
    public final TableField<XModuleRecord, String> SIGMA = createField(DSL.name("SIGMA"), SQLDataType.VARCHAR(32), this, "「sigma」- 统一标识");
    /**
     * The column <code>DB_ETERNAL.X_MODULE.METADATA</code>. 「metadata」- 附加配置
     */
    public final TableField<XModuleRecord, String> METADATA = createField(DSL.name("METADATA"), SQLDataType.CLOB, this, "「metadata」- 附加配置");
    /**
     * The column <code>DB_ETERNAL.X_MODULE.LANGUAGE</code>. 「language」- 使用的语言
     */
    public final TableField<XModuleRecord, String> LANGUAGE = createField(DSL.name("LANGUAGE"), SQLDataType.VARCHAR(8), this, "「language」- 使用的语言");
    /**
     * The column <code>DB_ETERNAL.X_MODULE.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public final TableField<XModuleRecord, LocalDateTime> CREATED_AT = createField(DSL.name("CREATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「createdAt」- 创建时间");
    /**
     * The column <code>DB_ETERNAL.X_MODULE.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public final TableField<XModuleRecord, String> CREATED_BY = createField(DSL.name("CREATED_BY"), SQLDataType.VARCHAR(36), this, "「createdBy」- 创建人");
    /**
     * The column <code>DB_ETERNAL.X_MODULE.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public final TableField<XModuleRecord, LocalDateTime> UPDATED_AT = createField(DSL.name("UPDATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「updatedAt」- 更新时间");
    /**
     * The column <code>DB_ETERNAL.X_MODULE.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public final TableField<XModuleRecord, String> UPDATED_BY = createField(DSL.name("UPDATED_BY"), SQLDataType.VARCHAR(36), this, "「updatedBy」- 更新人");

    private XModule(Name alias, Table<XModuleRecord> aliased) {
        this(alias, aliased, null);
    }

    private XModule(Name alias, Table<XModuleRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>DB_ETERNAL.X_MODULE</code> table reference
     */
    public XModule(String alias) {
        this(DSL.name(alias), X_MODULE);
    }

    /**
     * Create an aliased <code>DB_ETERNAL.X_MODULE</code> table reference
     */
    public XModule(Name alias) {
        this(alias, X_MODULE);
    }

    /**
     * Create a <code>DB_ETERNAL.X_MODULE</code> table reference
     */
    public XModule() {
        this(DSL.name("X_MODULE"), null);
    }

    public <O extends Record> XModule(Table<O> child, ForeignKey<O, XModuleRecord> key) {
        super(child, key, X_MODULE);
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<XModuleRecord> getRecordType() {
        return XModuleRecord.class;
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Db.DB_ETERNAL;
    }

    @Override
    public UniqueKey<XModuleRecord> getPrimaryKey() {
        return Keys.KEY_X_MODULE_PRIMARY;
    }

    @Override
    public List<UniqueKey<XModuleRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_X_MODULE_ENTRY);
    }

    @Override
    public XModule as(String alias) {
        return new XModule(DSL.name(alias), this);
    }

    @Override
    public XModule as(Name alias) {
        return new XModule(alias, this);
    }

    @Override
    public XModule as(Table<?> alias) {
        return new XModule(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public XModule rename(String name) {
        return new XModule(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public XModule rename(Name name) {
        return new XModule(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public XModule rename(Table<?> name) {
        return new XModule(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row15 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row15<String, String, String, String, String, String, String, Boolean, String, String, String, LocalDateTime, String, LocalDateTime, String> fieldsRow() {
        return (Row15) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function15<? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super Boolean, ? super String, ? super String, ? super String, ? super LocalDateTime, ? super String, ? super LocalDateTime, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function15<? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super Boolean, ? super String, ? super String, ? super String, ? super LocalDateTime, ? super String, ? super LocalDateTime, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
