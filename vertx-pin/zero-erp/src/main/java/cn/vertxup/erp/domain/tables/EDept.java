/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.erp.domain.tables;


import cn.vertxup.erp.domain.Db;
import cn.vertxup.erp.domain.Indexes;
import cn.vertxup.erp.domain.Keys;
import cn.vertxup.erp.domain.tables.records.EDeptRecord;
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
public class EDept extends TableImpl<EDeptRecord> {

    /**
     * The reference instance of <code>DB_ETERNAL.E_DEPT</code>
     */
    public static final EDept E_DEPT = new EDept();
    private static final long serialVersionUID = 1L;
    /**
     * The column <code>DB_ETERNAL.E_DEPT.KEY</code>. 「key」- 部门主键
     */
    public final TableField<EDeptRecord, String> KEY = createField(DSL.name("KEY"), SQLDataType.VARCHAR(36).nullable(false), this, "「key」- 部门主键");
    /**
     * The column <code>DB_ETERNAL.E_DEPT.NAME</code>. 「name」- 部门名称
     */
    public final TableField<EDeptRecord, String> NAME = createField(DSL.name("NAME"), SQLDataType.VARCHAR(255), this, "「name」- 部门名称");
    /**
     * The column <code>DB_ETERNAL.E_DEPT.CODE</code>. 「code」- 部门编号
     */
    public final TableField<EDeptRecord, String> CODE = createField(DSL.name("CODE"), SQLDataType.VARCHAR(255), this, "「code」- 部门编号");
    /**
     * The column <code>DB_ETERNAL.E_DEPT.MANAGER_ID</code>. 「managerId」- 部门经理
     */
    public final TableField<EDeptRecord, String> MANAGER_ID = createField(DSL.name("MANAGER_ID"), SQLDataType.VARCHAR(36), this, "「managerId」- 部门经理");
    /**
     * The column <code>DB_ETERNAL.E_DEPT.MANAGER_NAME</code>. 「managerName」-
     * 部门名称
     */
    public final TableField<EDeptRecord, String> MANAGER_NAME = createField(DSL.name("MANAGER_NAME"), SQLDataType.VARCHAR(255), this, "「managerName」- 部门名称");
    /**
     * The column <code>DB_ETERNAL.E_DEPT.COMPANY_ID</code>. 「companyId」- 所属公司
     */
    public final TableField<EDeptRecord, String> COMPANY_ID = createField(DSL.name("COMPANY_ID"), SQLDataType.VARCHAR(36), this, "「companyId」- 所属公司");
    /**
     * The column <code>DB_ETERNAL.E_DEPT.DEPT_ID</code>. 「deptId」- 父部门
     */
    public final TableField<EDeptRecord, String> DEPT_ID = createField(DSL.name("DEPT_ID"), SQLDataType.VARCHAR(36), this, "「deptId」- 父部门");
    /**
     * The column <code>DB_ETERNAL.E_DEPT.COMMENT</code>. 「comment」- 部门备注
     */
    public final TableField<EDeptRecord, String> COMMENT = createField(DSL.name("COMMENT"), SQLDataType.CLOB, this, "「comment」- 部门备注");
    /**
     * The column <code>DB_ETERNAL.E_DEPT.METADATA</code>. 「metadata」- 附加配置
     */
    public final TableField<EDeptRecord, String> METADATA = createField(DSL.name("METADATA"), SQLDataType.CLOB, this, "「metadata」- 附加配置");
    /**
     * The column <code>DB_ETERNAL.E_DEPT.ACTIVE</code>. 「active」- 是否启用
     */
    public final TableField<EDeptRecord, Boolean> ACTIVE = createField(DSL.name("ACTIVE"), SQLDataType.BIT, this, "「active」- 是否启用");
    /**
     * The column <code>DB_ETERNAL.E_DEPT.SIGMA</code>. 「sigma」- 统一标识
     */
    public final TableField<EDeptRecord, String> SIGMA = createField(DSL.name("SIGMA"), SQLDataType.VARCHAR(32), this, "「sigma」- 统一标识");
    /**
     * The column <code>DB_ETERNAL.E_DEPT.LANGUAGE</code>. 「language」- 使用的语言
     */
    public final TableField<EDeptRecord, String> LANGUAGE = createField(DSL.name("LANGUAGE"), SQLDataType.VARCHAR(8), this, "「language」- 使用的语言");
    /**
     * The column <code>DB_ETERNAL.E_DEPT.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public final TableField<EDeptRecord, LocalDateTime> CREATED_AT = createField(DSL.name("CREATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「createdAt」- 创建时间");
    /**
     * The column <code>DB_ETERNAL.E_DEPT.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public final TableField<EDeptRecord, String> CREATED_BY = createField(DSL.name("CREATED_BY"), SQLDataType.VARCHAR(36), this, "「createdBy」- 创建人");
    /**
     * The column <code>DB_ETERNAL.E_DEPT.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public final TableField<EDeptRecord, LocalDateTime> UPDATED_AT = createField(DSL.name("UPDATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「updatedAt」- 更新时间");
    /**
     * The column <code>DB_ETERNAL.E_DEPT.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public final TableField<EDeptRecord, String> UPDATED_BY = createField(DSL.name("UPDATED_BY"), SQLDataType.VARCHAR(36), this, "「updatedBy」- 更新人");

    private EDept(Name alias, Table<EDeptRecord> aliased) {
        this(alias, aliased, null);
    }

    private EDept(Name alias, Table<EDeptRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>DB_ETERNAL.E_DEPT</code> table reference
     */
    public EDept(String alias) {
        this(DSL.name(alias), E_DEPT);
    }

    /**
     * Create an aliased <code>DB_ETERNAL.E_DEPT</code> table reference
     */
    public EDept(Name alias) {
        this(alias, E_DEPT);
    }

    /**
     * Create a <code>DB_ETERNAL.E_DEPT</code> table reference
     */
    public EDept() {
        this(DSL.name("E_DEPT"), null);
    }

    public <O extends Record> EDept(Table<O> child, ForeignKey<O, EDeptRecord> key) {
        super(child, key, E_DEPT);
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EDeptRecord> getRecordType() {
        return EDeptRecord.class;
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Db.DB_ETERNAL;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.E_DEPT_IDX_E_DEPT_SIGMA, Indexes.E_DEPT_IDX_E_DEPT_SIGMA_ACTIVE);
    }

    @Override
    public UniqueKey<EDeptRecord> getPrimaryKey() {
        return Keys.KEY_E_DEPT_PRIMARY;
    }

    @Override
    public List<UniqueKey<EDeptRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_E_DEPT_NAME, Keys.KEY_E_DEPT_CODE);
    }

    @Override
    public EDept as(String alias) {
        return new EDept(DSL.name(alias), this);
    }

    @Override
    public EDept as(Name alias) {
        return new EDept(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EDept rename(String name) {
        return new EDept(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public EDept rename(Name name) {
        return new EDept(name, null);
    }

    // -------------------------------------------------------------------------
    // Row16 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row16<String, String, String, String, String, String, String, String, String, Boolean, String, String, LocalDateTime, String, LocalDateTime, String> fieldsRow() {
        return (Row16) super.fieldsRow();
    }
}
