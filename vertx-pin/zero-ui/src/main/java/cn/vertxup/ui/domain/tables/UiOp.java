/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.ui.domain.tables;


import cn.vertxup.ui.domain.Db;
import cn.vertxup.ui.domain.Indexes;
import cn.vertxup.ui.domain.Keys;
import cn.vertxup.ui.domain.tables.records.UiOpRecord;
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
public class UiOp extends TableImpl<UiOpRecord> {

    /**
     * The reference instance of <code>DB_ETERNAL.UI_OP</code>
     */
    public static final UiOp UI_OP = new UiOp();
    private static final long serialVersionUID = 1L;
    /**
     * The column <code>DB_ETERNAL.UI_OP.KEY</code>. 「key」- 操作主键
     */
    public final TableField<UiOpRecord, String> KEY = createField(DSL.name("KEY"), SQLDataType.VARCHAR(36).nullable(false), this, "「key」- 操作主键");
    /**
     * The column <code>DB_ETERNAL.UI_OP.ACTION</code>. 「action」-
     * S_ACTION中的code（权限检查专用）
     */
    public final TableField<UiOpRecord, String> ACTION = createField(DSL.name("ACTION"), SQLDataType.VARCHAR(255), this, "「action」- S_ACTION中的code（权限检查专用）");
    /**
     * The column <code>DB_ETERNAL.UI_OP.TEXT</code>. 「text」- 该操作上的文字信息
     */
    public final TableField<UiOpRecord, String> TEXT = createField(DSL.name("TEXT"), SQLDataType.VARCHAR(255), this, "「text」- 该操作上的文字信息");
    /**
     * The column <code>DB_ETERNAL.UI_OP.EVENT</code>. 「event」- 操作中的 event 事件名称
     */
    public final TableField<UiOpRecord, String> EVENT = createField(DSL.name("EVENT"), SQLDataType.VARCHAR(32), this, "「event」- 操作中的 event 事件名称");
    /**
     * The column <code>DB_ETERNAL.UI_OP.CLIENT_KEY</code>. 「clientKey」-
     * 一般是Html中对应的key信息，如 $opSave
     */
    public final TableField<UiOpRecord, String> CLIENT_KEY = createField(DSL.name("CLIENT_KEY"), SQLDataType.VARCHAR(32), this, "「clientKey」- 一般是Html中对应的key信息，如 $opSave");
    /**
     * The column <code>DB_ETERNAL.UI_OP.CLIENT_ID</code>. 「clientId」-
     * 没有特殊情况，clientId = clientKey
     */
    public final TableField<UiOpRecord, String> CLIENT_ID = createField(DSL.name("CLIENT_ID"), SQLDataType.VARCHAR(32), this, "「clientId」- 没有特殊情况，clientId = clientKey");
    /**
     * The column <code>DB_ETERNAL.UI_OP.CONFIG</code>. 「config」-
     * 该按钮操作对应的配置数据信息, icon, type
     */
    public final TableField<UiOpRecord, String> CONFIG = createField(DSL.name("CONFIG"), SQLDataType.CLOB, this, "「config」- 该按钮操作对应的配置数据信息, icon, type");
    /**
     * The column <code>DB_ETERNAL.UI_OP.PLUGIN</code>. 「plugin」- 该按钮中的插件，如
     * tooltip，component等
     */
    public final TableField<UiOpRecord, String> PLUGIN = createField(DSL.name("PLUGIN"), SQLDataType.CLOB, this, "「plugin」- 该按钮中的插件，如 tooltip，component等");
    /**
     * The column <code>DB_ETERNAL.UI_OP.CONTROL_ID</code>. 「controlId」- 挂载专用的ID
     */
    public final TableField<UiOpRecord, String> CONTROL_ID = createField(DSL.name("CONTROL_ID"), SQLDataType.VARCHAR(128), this, "「controlId」- 挂载专用的ID");
    /**
     * The column <code>DB_ETERNAL.UI_OP.CONTROL_TYPE</code>. 「controlType」-
     * 操作关联的控件类型
     */
    public final TableField<UiOpRecord, String> CONTROL_TYPE = createField(DSL.name("CONTROL_TYPE"), SQLDataType.VARCHAR(255), this, "「controlType」- 操作关联的控件类型");
    /**
     * The column <code>DB_ETERNAL.UI_OP.ACTIVE</code>. 「active」- 是否启用
     */
    public final TableField<UiOpRecord, Boolean> ACTIVE = createField(DSL.name("ACTIVE"), SQLDataType.BIT, this, "「active」- 是否启用");
    /**
     * The column <code>DB_ETERNAL.UI_OP.SIGMA</code>. 「sigma」- 统一标识
     */
    public final TableField<UiOpRecord, String> SIGMA = createField(DSL.name("SIGMA"), SQLDataType.VARCHAR(32), this, "「sigma」- 统一标识");
    /**
     * The column <code>DB_ETERNAL.UI_OP.METADATA</code>. 「metadata」- 附加配置
     */
    public final TableField<UiOpRecord, String> METADATA = createField(DSL.name("METADATA"), SQLDataType.CLOB, this, "「metadata」- 附加配置");
    /**
     * The column <code>DB_ETERNAL.UI_OP.LANGUAGE</code>. 「language」- 使用的语言
     */
    public final TableField<UiOpRecord, String> LANGUAGE = createField(DSL.name("LANGUAGE"), SQLDataType.VARCHAR(8), this, "「language」- 使用的语言");
    /**
     * The column <code>DB_ETERNAL.UI_OP.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public final TableField<UiOpRecord, LocalDateTime> CREATED_AT = createField(DSL.name("CREATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「createdAt」- 创建时间");
    /**
     * The column <code>DB_ETERNAL.UI_OP.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public final TableField<UiOpRecord, String> CREATED_BY = createField(DSL.name("CREATED_BY"), SQLDataType.VARCHAR(36), this, "「createdBy」- 创建人");
    /**
     * The column <code>DB_ETERNAL.UI_OP.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public final TableField<UiOpRecord, LocalDateTime> UPDATED_AT = createField(DSL.name("UPDATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「updatedAt」- 更新时间");
    /**
     * The column <code>DB_ETERNAL.UI_OP.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public final TableField<UiOpRecord, String> UPDATED_BY = createField(DSL.name("UPDATED_BY"), SQLDataType.VARCHAR(36), this, "「updatedBy」- 更新人");

    private UiOp(Name alias, Table<UiOpRecord> aliased) {
        this(alias, aliased, null);
    }

    private UiOp(Name alias, Table<UiOpRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>DB_ETERNAL.UI_OP</code> table reference
     */
    public UiOp(String alias) {
        this(DSL.name(alias), UI_OP);
    }

    /**
     * Create an aliased <code>DB_ETERNAL.UI_OP</code> table reference
     */
    public UiOp(Name alias) {
        this(alias, UI_OP);
    }

    /**
     * Create a <code>DB_ETERNAL.UI_OP</code> table reference
     */
    public UiOp() {
        this(DSL.name("UI_OP"), null);
    }

    public <O extends Record> UiOp(Table<O> child, ForeignKey<O, UiOpRecord> key) {
        super(child, key, UI_OP);
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UiOpRecord> getRecordType() {
        return UiOpRecord.class;
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Db.DB_ETERNAL;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.UI_OP_IDXM_UI_OP_SIGMA_CONTROL_ID);
    }

    @Override
    public UniqueKey<UiOpRecord> getPrimaryKey() {
        return Keys.KEY_UI_OP_PRIMARY;
    }

    @Override
    public List<UniqueKey<UiOpRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_UI_OP_CONTROL_ID_2, Keys.KEY_UI_OP_CONTROL_ID);
    }

    @Override
    public UiOp as(String alias) {
        return new UiOp(DSL.name(alias), this);
    }

    @Override
    public UiOp as(Name alias) {
        return new UiOp(alias, this);
    }

    @Override
    public UiOp as(Table<?> alias) {
        return new UiOp(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public UiOp rename(String name) {
        return new UiOp(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public UiOp rename(Name name) {
        return new UiOp(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public UiOp rename(Table<?> name) {
        return new UiOp(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row18 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row18<String, String, String, String, String, String, String, String, String, String, Boolean, String, String, String, LocalDateTime, String, LocalDateTime, String> fieldsRow() {
        return (Row18) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function18<? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super Boolean, ? super String, ? super String, ? super String, ? super LocalDateTime, ? super String, ? super LocalDateTime, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function18<? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super Boolean, ? super String, ? super String, ? super String, ? super LocalDateTime, ? super String, ? super LocalDateTime, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
