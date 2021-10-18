/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.lbs.domain.tables;


import cn.vertxup.lbs.domain.Db;
import cn.vertxup.lbs.domain.Keys;
import cn.vertxup.lbs.domain.tables.records.LStateRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class LState extends TableImpl<LStateRecord> {

    /**
     * The reference instance of <code>DB_ETERNAL.L_STATE</code>
     */
    public static final LState L_STATE = new LState();
    private static final long serialVersionUID = 1L;
    /**
     * The column <code>DB_ETERNAL.L_STATE.KEY</code>. 「key」- 省会主键
     */
    public final TableField<LStateRecord, String> KEY = createField(DSL.name("KEY"), SQLDataType.VARCHAR(36).nullable(false), this, "「key」- 省会主键");
    /**
     * The column <code>DB_ETERNAL.L_STATE.NAME</code>. 「name」- 省会名称
     */
    public final TableField<LStateRecord, String> NAME = createField(DSL.name("NAME"), SQLDataType.VARCHAR(32).nullable(false), this, "「name」- 省会名称");
    /**
     * The column <code>DB_ETERNAL.L_STATE.CODE</code>. 「code」- 省会编码
     */
    public final TableField<LStateRecord, String> CODE = createField(DSL.name("CODE"), SQLDataType.VARCHAR(36), this, "「code」- 省会编码");
    /**
     * The column <code>DB_ETERNAL.L_STATE.ALIAS</code>. 「alias」- 别名（缩写）
     */
    public final TableField<LStateRecord, String> ALIAS = createField(DSL.name("ALIAS"), SQLDataType.VARCHAR(32).nullable(false), this, "「alias」- 别名（缩写）");
    /**
     * The column <code>DB_ETERNAL.L_STATE.METADATA</code>. 「metadata」- 附加配置
     */
    public final TableField<LStateRecord, String> METADATA = createField(DSL.name("METADATA"), SQLDataType.CLOB, this, "「metadata」- 附加配置");
    /**
     * The column <code>DB_ETERNAL.L_STATE.ORDER</code>. 「order」- 排序
     */
    public final TableField<LStateRecord, Integer> ORDER = createField(DSL.name("ORDER"), SQLDataType.INTEGER, this, "「order」- 排序");
    /**
     * The column <code>DB_ETERNAL.L_STATE.COUNTRY_ID</code>. 「countryId」- 国家ID
     */
    public final TableField<LStateRecord, String> COUNTRY_ID = createField(DSL.name("COUNTRY_ID"), SQLDataType.VARCHAR(36).nullable(false), this, "「countryId」- 国家ID");
    /**
     * The column <code>DB_ETERNAL.L_STATE.ACTIVE</code>. 「active」- 是否启用
     */
    public final TableField<LStateRecord, Boolean> ACTIVE = createField(DSL.name("ACTIVE"), SQLDataType.BIT, this, "「active」- 是否启用");
    /**
     * The column <code>DB_ETERNAL.L_STATE.SIGMA</code>. 「sigma」- 统一标识
     */
    public final TableField<LStateRecord, String> SIGMA = createField(DSL.name("SIGMA"), SQLDataType.VARCHAR(32), this, "「sigma」- 统一标识");
    /**
     * The column <code>DB_ETERNAL.L_STATE.LANGUAGE</code>. 「language」- 使用的语言
     */
    public final TableField<LStateRecord, String> LANGUAGE = createField(DSL.name("LANGUAGE"), SQLDataType.VARCHAR(8), this, "「language」- 使用的语言");
    /**
     * The column <code>DB_ETERNAL.L_STATE.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public final TableField<LStateRecord, LocalDateTime> CREATED_AT = createField(DSL.name("CREATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「createdAt」- 创建时间");
    /**
     * The column <code>DB_ETERNAL.L_STATE.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public final TableField<LStateRecord, String> CREATED_BY = createField(DSL.name("CREATED_BY"), SQLDataType.VARCHAR(36), this, "「createdBy」- 创建人");
    /**
     * The column <code>DB_ETERNAL.L_STATE.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public final TableField<LStateRecord, LocalDateTime> UPDATED_AT = createField(DSL.name("UPDATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「updatedAt」- 更新时间");
    /**
     * The column <code>DB_ETERNAL.L_STATE.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public final TableField<LStateRecord, String> UPDATED_BY = createField(DSL.name("UPDATED_BY"), SQLDataType.VARCHAR(36), this, "「updatedBy」- 更新人");

    private LState(Name alias, Table<LStateRecord> aliased) {
        this(alias, aliased, null);
    }

    private LState(Name alias, Table<LStateRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>DB_ETERNAL.L_STATE</code> table reference
     */
    public LState(String alias) {
        this(DSL.name(alias), L_STATE);
    }

    /**
     * Create an aliased <code>DB_ETERNAL.L_STATE</code> table reference
     */
    public LState(Name alias) {
        this(alias, L_STATE);
    }

    /**
     * Create a <code>DB_ETERNAL.L_STATE</code> table reference
     */
    public LState() {
        this(DSL.name("L_STATE"), null);
    }

    public <O extends Record> LState(Table<O> child, ForeignKey<O, LStateRecord> key) {
        super(child, key, L_STATE);
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<LStateRecord> getRecordType() {
        return LStateRecord.class;
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Db.DB_ETERNAL;
    }

    @Override
    public UniqueKey<LStateRecord> getPrimaryKey() {
        return Keys.KEY_L_STATE_PRIMARY;
    }

    @Override
    public List<UniqueKey<LStateRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_L_STATE_CODE);
    }

    @Override
    public LState as(String alias) {
        return new LState(DSL.name(alias), this);
    }

    @Override
    public LState as(Name alias) {
        return new LState(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public LState rename(String name) {
        return new LState(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public LState rename(Name name) {
        return new LState(name, null);
    }

    // -------------------------------------------------------------------------
    // Row14 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row14<String, String, String, String, String, Integer, String, Boolean, String, String, LocalDateTime, String, LocalDateTime, String> fieldsRow() {
        return (Row14) super.fieldsRow();
    }
}
