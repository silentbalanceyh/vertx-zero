/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.psi.domain.tables;


import cn.vertxup.psi.domain.Db;
import cn.vertxup.psi.domain.Keys;
import cn.vertxup.psi.domain.tables.records.PPosRecord;
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
public class PPos extends TableImpl<PPosRecord> {

    /**
     * The reference instance of <code>DB_ETERNAL.P_POS</code>
     */
    public static final PPos P_POS = new PPos();
    private static final long serialVersionUID = 1L;
    /**
     * The column <code>DB_ETERNAL.P_POS.KEY</code>. 「key」- 仓位主键
     */
    public final TableField<PPosRecord, String> KEY = createField(DSL.name("KEY"), SQLDataType.VARCHAR(36).nullable(false), this, "「key」- 仓位主键");
    /**
     * The column <code>DB_ETERNAL.P_POS.NAME</code>. 「name」- 仓位名称
     */
    public final TableField<PPosRecord, String> NAME = createField(DSL.name("NAME"), SQLDataType.VARCHAR(255), this, "「name」- 仓位名称");
    /**
     * The column <code>DB_ETERNAL.P_POS.CODE</code>. 「code」- 仓位编号（系统可用）
     */
    public final TableField<PPosRecord, String> CODE = createField(DSL.name("CODE"), SQLDataType.VARCHAR(255), this, "「code」- 仓位编号（系统可用）");
    /**
     * The column <code>DB_ETERNAL.P_POS.TYPE</code>. 「type」- 仓位类型
     */
    public final TableField<PPosRecord, String> TYPE = createField(DSL.name("TYPE"), SQLDataType.VARCHAR(36), this, "「type」- 仓位类型");
    /**
     * The column <code>DB_ETERNAL.P_POS.STATUS</code>. 「status」- 仓位状态
     */
    public final TableField<PPosRecord, String> STATUS = createField(DSL.name("STATUS"), SQLDataType.VARCHAR(36), this, "「status」- 仓位状态");
    /**
     * The column <code>DB_ETERNAL.P_POS.WH_ID</code>. 「whId」- 所属仓库信息
     */
    public final TableField<PPosRecord, String> WH_ID = createField(DSL.name("WH_ID"), SQLDataType.VARCHAR(36), this, "「whId」- 所属仓库信息");
    /**
     * The column <code>DB_ETERNAL.P_POS.DIRECT</code>. 「direct」- 直接仓位，1对1
     */
    public final TableField<PPosRecord, Boolean> DIRECT = createField(DSL.name("DIRECT"), SQLDataType.BIT, this, "「direct」- 直接仓位，1对1");
    /**
     * The column <code>DB_ETERNAL.P_POS.CAPACITY</code>. 「capacity」- 仓位容量
     */
    public final TableField<PPosRecord, Long> CAPACITY = createField(DSL.name("CAPACITY"), SQLDataType.BIGINT.defaultValue(DSL.inline("0", SQLDataType.BIGINT)), this, "「capacity」- 仓位容量");
    /**
     * The column <code>DB_ETERNAL.P_POS.CAPACITY_EXCEED</code>.
     * 「capacityExceed」- 仓位超容部分
     */
    public final TableField<PPosRecord, Long> CAPACITY_EXCEED = createField(DSL.name("CAPACITY_EXCEED"), SQLDataType.BIGINT.defaultValue(DSL.inline("0", SQLDataType.BIGINT)), this, "「capacityExceed」- 仓位超容部分");
    /**
     * The column <code>DB_ETERNAL.P_POS.LIMIT_TYPE</code>. 「limitType」- 仓位类型限制
     */
    public final TableField<PPosRecord, String> LIMIT_TYPE = createField(DSL.name("LIMIT_TYPE"), SQLDataType.CLOB, this, "「limitType」- 仓位类型限制");
    /**
     * The column <code>DB_ETERNAL.P_POS.LIMIT_RULE</code>. 「limitRule」- 仓位限制规则
     */
    public final TableField<PPosRecord, String> LIMIT_RULE = createField(DSL.name("LIMIT_RULE"), SQLDataType.CLOB, this, "「limitRule」- 仓位限制规则");
    /**
     * The column <code>DB_ETERNAL.P_POS.POS_ROW</code>. 「posRow」- 行维度
     */
    public final TableField<PPosRecord, Integer> POS_ROW = createField(DSL.name("POS_ROW"), SQLDataType.INTEGER, this, "「posRow」- 行维度");
    /**
     * The column <code>DB_ETERNAL.P_POS.POS_COLUMN</code>. 「posColumn」- 列维度
     */
    public final TableField<PPosRecord, Integer> POS_COLUMN = createField(DSL.name("POS_COLUMN"), SQLDataType.INTEGER, this, "「posColumn」- 列维度");
    /**
     * The column <code>DB_ETERNAL.P_POS.POS_HEIGHT</code>. 「posHeight」- 高维度
     */
    public final TableField<PPosRecord, Integer> POS_HEIGHT = createField(DSL.name("POS_HEIGHT"), SQLDataType.INTEGER, this, "「posHeight」- 高维度");
    /**
     * The column <code>DB_ETERNAL.P_POS.POS_TAGS</code>. 「posTags」- 标签，横切维度位置管理
     */
    public final TableField<PPosRecord, String> POS_TAGS = createField(DSL.name("POS_TAGS"), SQLDataType.CLOB, this, "「posTags」- 标签，横切维度位置管理");
    /**
     * The column <code>DB_ETERNAL.P_POS.POS_TRACE</code>. 「posTrace」- 空间位置描述
     */
    public final TableField<PPosRecord, String> POS_TRACE = createField(DSL.name("POS_TRACE"), SQLDataType.CLOB, this, "「posTrace」- 空间位置描述");
    /**
     * The column <code>DB_ETERNAL.P_POS.COMMENT</code>. 「comment」- 仓库备注
     */
    public final TableField<PPosRecord, String> COMMENT = createField(DSL.name("COMMENT"), SQLDataType.CLOB, this, "「comment」- 仓库备注");
    /**
     * The column <code>DB_ETERNAL.P_POS.ACTIVE</code>. 「active」- 是否启用
     */
    public final TableField<PPosRecord, Boolean> ACTIVE = createField(DSL.name("ACTIVE"), SQLDataType.BIT, this, "「active」- 是否启用");
    /**
     * The column <code>DB_ETERNAL.P_POS.SIGMA</code>. 「sigma」- 统一标识
     */
    public final TableField<PPosRecord, String> SIGMA = createField(DSL.name("SIGMA"), SQLDataType.VARCHAR(32), this, "「sigma」- 统一标识");
    /**
     * The column <code>DB_ETERNAL.P_POS.METADATA</code>. 「metadata」- 附加配置
     */
    public final TableField<PPosRecord, String> METADATA = createField(DSL.name("METADATA"), SQLDataType.CLOB, this, "「metadata」- 附加配置");
    /**
     * The column <code>DB_ETERNAL.P_POS.LANGUAGE</code>. 「language」- 使用的语言
     */
    public final TableField<PPosRecord, String> LANGUAGE = createField(DSL.name("LANGUAGE"), SQLDataType.VARCHAR(8), this, "「language」- 使用的语言");
    /**
     * The column <code>DB_ETERNAL.P_POS.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public final TableField<PPosRecord, LocalDateTime> CREATED_AT = createField(DSL.name("CREATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「createdAt」- 创建时间");
    /**
     * The column <code>DB_ETERNAL.P_POS.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public final TableField<PPosRecord, String> CREATED_BY = createField(DSL.name("CREATED_BY"), SQLDataType.VARCHAR(36), this, "「createdBy」- 创建人");
    /**
     * The column <code>DB_ETERNAL.P_POS.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public final TableField<PPosRecord, LocalDateTime> UPDATED_AT = createField(DSL.name("UPDATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「updatedAt」- 更新时间");
    /**
     * The column <code>DB_ETERNAL.P_POS.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public final TableField<PPosRecord, String> UPDATED_BY = createField(DSL.name("UPDATED_BY"), SQLDataType.VARCHAR(36), this, "「updatedBy」- 更新人");

    private PPos(Name alias, Table<PPosRecord> aliased) {
        this(alias, aliased, null);
    }

    private PPos(Name alias, Table<PPosRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>DB_ETERNAL.P_POS</code> table reference
     */
    public PPos(String alias) {
        this(DSL.name(alias), P_POS);
    }

    /**
     * Create an aliased <code>DB_ETERNAL.P_POS</code> table reference
     */
    public PPos(Name alias) {
        this(alias, P_POS);
    }

    /**
     * Create a <code>DB_ETERNAL.P_POS</code> table reference
     */
    public PPos() {
        this(DSL.name("P_POS"), null);
    }

    public <O extends Record> PPos(Table<O> child, ForeignKey<O, PPosRecord> key) {
        super(child, key, P_POS);
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PPosRecord> getRecordType() {
        return PPosRecord.class;
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Db.DB_ETERNAL;
    }

    @Override
    public UniqueKey<PPosRecord> getPrimaryKey() {
        return Keys.KEY_P_POS_PRIMARY;
    }

    @Override
    public List<UniqueKey<PPosRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_P_POS_CODE);
    }

    @Override
    public PPos as(String alias) {
        return new PPos(DSL.name(alias), this);
    }

    @Override
    public PPos as(Name alias) {
        return new PPos(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PPos rename(String name) {
        return new PPos(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PPos rename(Name name) {
        return new PPos(name, null);
    }
}
