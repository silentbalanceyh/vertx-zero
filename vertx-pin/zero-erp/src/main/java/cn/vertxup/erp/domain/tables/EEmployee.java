/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.erp.domain.tables;


import cn.vertxup.erp.domain.Db;
import cn.vertxup.erp.domain.Indexes;
import cn.vertxup.erp.domain.Keys;
import cn.vertxup.erp.domain.tables.records.EEmployeeRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EEmployee extends TableImpl<EEmployeeRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DB_HOTEL.E_EMPLOYEE</code>
     */
    public static final EEmployee E_EMPLOYEE = new EEmployee();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EEmployeeRecord> getRecordType() {
        return EEmployeeRecord.class;
    }

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.KEY</code>. 「key」- 员工主键
     */
    public final TableField<EEmployeeRecord, String> KEY = createField(DSL.name("KEY"), SQLDataType.VARCHAR(36).nullable(false), this, "「key」- 员工主键");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.COMPANY_ID</code>. 「companyId」- 所属公司
     */
    public final TableField<EEmployeeRecord, String> COMPANY_ID = createField(DSL.name("COMPANY_ID"), SQLDataType.VARCHAR(36), this, "「companyId」- 所属公司");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.DEPT_ID</code>. 「deptId」- 所属部门
     */
    public final TableField<EEmployeeRecord, String> DEPT_ID = createField(DSL.name("DEPT_ID"), SQLDataType.VARCHAR(36), this, "「deptId」- 所属部门");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.TEAM_ID</code>. 「teamId」- 所属组
     */
    public final TableField<EEmployeeRecord, String> TEAM_ID = createField(DSL.name("TEAM_ID"), SQLDataType.VARCHAR(36), this, "「teamId」- 所属组");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.IDENTITY_ID</code>. 「identityId」-
     * 关联档案
     */
    public final TableField<EEmployeeRecord, String> IDENTITY_ID = createField(DSL.name("IDENTITY_ID"), SQLDataType.VARCHAR(36), this, "「identityId」- 关联档案");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.VICE_NAME</code>. 「viceName」- 员工姓名
     */
    public final TableField<EEmployeeRecord, String> VICE_NAME = createField(DSL.name("VICE_NAME"), SQLDataType.VARCHAR(255), this, "「viceName」- 员工姓名");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.VICE_EMAIL</code>. 「viceEmail」- 员工邮箱
     */
    public final TableField<EEmployeeRecord, String> VICE_EMAIL = createField(DSL.name("VICE_EMAIL"), SQLDataType.VARCHAR(255), this, "「viceEmail」- 员工邮箱");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.VICE_MOBILE</code>. 「viceMobile」-
     * 员工手机
     */
    public final TableField<EEmployeeRecord, String> VICE_MOBILE = createField(DSL.name("VICE_MOBILE"), SQLDataType.VARCHAR(255), this, "「viceMobile」- 员工手机");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.WORK_NUMBER</code>. 「workNumber」- 工号
     */
    public final TableField<EEmployeeRecord, String> WORK_NUMBER = createField(DSL.name("WORK_NUMBER"), SQLDataType.VARCHAR(255), this, "「workNumber」- 工号");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.WORK_TITLE</code>. 「workTitle」- 头衔
     */
    public final TableField<EEmployeeRecord, String> WORK_TITLE = createField(DSL.name("WORK_TITLE"), SQLDataType.VARCHAR(255), this, "「workTitle」- 头衔");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.WORK_LOCATION</code>.
     * 「workLocation」- 办公地点
     */
    public final TableField<EEmployeeRecord, String> WORK_LOCATION = createField(DSL.name("WORK_LOCATION"), SQLDataType.CLOB, this, "「workLocation」- 办公地点");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.WORK_PHONE</code>. 「workPhone」- 办公电话
     */
    public final TableField<EEmployeeRecord, String> WORK_PHONE = createField(DSL.name("WORK_PHONE"), SQLDataType.VARCHAR(20), this, "「workPhone」- 办公电话");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.WORK_EXTENSION</code>.
     * 「workExtension」- 分机号
     */
    public final TableField<EEmployeeRecord, String> WORK_EXTENSION = createField(DSL.name("WORK_EXTENSION"), SQLDataType.VARCHAR(20), this, "「workExtension」- 分机号");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.WORK_HIRE_AT</code>. 「workHireAt」-
     * 入职时间
     */
    public final TableField<EEmployeeRecord, LocalDateTime> WORK_HIRE_AT = createField(DSL.name("WORK_HIRE_AT"), SQLDataType.LOCALDATETIME(0), this, "「workHireAt」- 入职时间");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.BANK_ID</code>. 「bankId」- 开户行
     */
    public final TableField<EEmployeeRecord, String> BANK_ID = createField(DSL.name("BANK_ID"), SQLDataType.VARCHAR(36), this, "「bankId」- 开户行");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.BANK_CARD</code>. 「bankCard」- 开户行账号
     */
    public final TableField<EEmployeeRecord, String> BANK_CARD = createField(DSL.name("BANK_CARD"), SQLDataType.VARCHAR(255), this, "「bankCard」- 开户行账号");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.TYPE</code>. 「type」- 员工分类
     */
    public final TableField<EEmployeeRecord, String> TYPE = createField(DSL.name("TYPE"), SQLDataType.VARCHAR(36), this, "「type」- 员工分类");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.STATUS</code>. 「status」- 员工状态
     */
    public final TableField<EEmployeeRecord, String> STATUS = createField(DSL.name("STATUS"), SQLDataType.VARCHAR(36), this, "「status」- 员工状态");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.METADATA</code>. 「metadata」- 附加配置
     */
    public final TableField<EEmployeeRecord, String> METADATA = createField(DSL.name("METADATA"), SQLDataType.CLOB, this, "「metadata」- 附加配置");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.ACTIVE</code>. 「active」- 是否启用
     */
    public final TableField<EEmployeeRecord, Boolean> ACTIVE = createField(DSL.name("ACTIVE"), SQLDataType.BIT, this, "「active」- 是否启用");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.SIGMA</code>. 「sigma」- 统一标识（公司所属应用）
     */
    public final TableField<EEmployeeRecord, String> SIGMA = createField(DSL.name("SIGMA"), SQLDataType.VARCHAR(32), this, "「sigma」- 统一标识（公司所属应用）");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.LANGUAGE</code>. 「language」- 使用的语言
     */
    public final TableField<EEmployeeRecord, String> LANGUAGE = createField(DSL.name("LANGUAGE"), SQLDataType.VARCHAR(8), this, "「language」- 使用的语言");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public final TableField<EEmployeeRecord, LocalDateTime> CREATED_AT = createField(DSL.name("CREATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「createdAt」- 创建时间");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public final TableField<EEmployeeRecord, String> CREATED_BY = createField(DSL.name("CREATED_BY"), SQLDataType.VARCHAR(36), this, "「createdBy」- 创建人");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public final TableField<EEmployeeRecord, LocalDateTime> UPDATED_AT = createField(DSL.name("UPDATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「updatedAt」- 更新时间");

    /**
     * The column <code>DB_HOTEL.E_EMPLOYEE.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public final TableField<EEmployeeRecord, String> UPDATED_BY = createField(DSL.name("UPDATED_BY"), SQLDataType.VARCHAR(36), this, "「updatedBy」- 更新人");

    private EEmployee(Name alias, Table<EEmployeeRecord> aliased) {
        this(alias, aliased, null);
    }

    private EEmployee(Name alias, Table<EEmployeeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>DB_HOTEL.E_EMPLOYEE</code> table reference
     */
    public EEmployee(String alias) {
        this(DSL.name(alias), E_EMPLOYEE);
    }

    /**
     * Create an aliased <code>DB_HOTEL.E_EMPLOYEE</code> table reference
     */
    public EEmployee(Name alias) {
        this(alias, E_EMPLOYEE);
    }

    /**
     * Create a <code>DB_HOTEL.E_EMPLOYEE</code> table reference
     */
    public EEmployee() {
        this(DSL.name("E_EMPLOYEE"), null);
    }

    public <O extends Record> EEmployee(Table<O> child, ForeignKey<O, EEmployeeRecord> key) {
        super(child, key, E_EMPLOYEE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Db.DB_HOTEL;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.E_EMPLOYEE_IDX_E_EMPLOYEE_SIGMA, Indexes.E_EMPLOYEE_IDX_E_EMPLOYEE_SIGMA_ACTIVE, Indexes.E_EMPLOYEE_IDX_E_EMPLOYEE_WORK_NUMBER);
    }

    @Override
    public UniqueKey<EEmployeeRecord> getPrimaryKey() {
        return Keys.KEY_E_EMPLOYEE_PRIMARY;
    }

    @Override
    public List<UniqueKey<EEmployeeRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_E_EMPLOYEE_WORK_NUMBER);
    }

    @Override
    public EEmployee as(String alias) {
        return new EEmployee(DSL.name(alias), this);
    }

    @Override
    public EEmployee as(Name alias) {
        return new EEmployee(alias, this);
    }

    @Override
    public EEmployee as(Table<?> alias) {
        return new EEmployee(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public EEmployee rename(String name) {
        return new EEmployee(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public EEmployee rename(Name name) {
        return new EEmployee(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public EEmployee rename(Table<?> name) {
        return new EEmployee(name.getQualifiedName(), null);
    }
}
