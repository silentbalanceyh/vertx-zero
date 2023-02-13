/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.erp.domain.tables;


import cn.vertxup.erp.domain.Db;
import cn.vertxup.erp.domain.Keys;
import cn.vertxup.erp.domain.tables.records.ECustomerRecord;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
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
public class ECustomer extends TableImpl<ECustomerRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DB_HOTEL.E_CUSTOMER</code>
     */
    public static final ECustomer E_CUSTOMER = new ECustomer();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ECustomerRecord> getRecordType() {
        return ECustomerRecord.class;
    }

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.KEY</code>. 「key」- 客户ID
     */
    public final TableField<ECustomerRecord, String> KEY = createField(DSL.name("KEY"), SQLDataType.VARCHAR(36).nullable(false), this, "「key」- 客户ID");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.NAME</code>. 「name」- 客户名称
     */
    public final TableField<ECustomerRecord, String> NAME = createField(DSL.name("NAME"), SQLDataType.VARCHAR(255), this, "「name」- 客户名称");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.CODE</code>. 「code」- 客户编号
     */
    public final TableField<ECustomerRecord, String> CODE = createField(DSL.name("CODE"), SQLDataType.VARCHAR(255), this, "「code」- 客户编号");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.TYPE</code>. 「type」-
     * 客户分类（不同类型代表不同客户）
     */
    public final TableField<ECustomerRecord, String> TYPE = createField(DSL.name("TYPE"), SQLDataType.VARCHAR(36), this, "「type」- 客户分类（不同类型代表不同客户）");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.STATUS</code>. 「status」- 客户状态
     */
    public final TableField<ECustomerRecord, String> STATUS = createField(DSL.name("STATUS"), SQLDataType.VARCHAR(36), this, "「status」- 客户状态");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.TAX_CODE</code>. 「taxCode」- 税号
     */
    public final TableField<ECustomerRecord, String> TAX_CODE = createField(DSL.name("TAX_CODE"), SQLDataType.VARCHAR(255), this, "「taxCode」- 税号");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.TAX_TITLE</code>. 「taxTitle」- 开票抬头
     */
    public final TableField<ECustomerRecord, String> TAX_TITLE = createField(DSL.name("TAX_TITLE"), SQLDataType.VARCHAR(255), this, "「taxTitle」- 开票抬头");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.CONTACT_NAME</code>. 「contactName」-
     * 联系人姓名
     */
    public final TableField<ECustomerRecord, String> CONTACT_NAME = createField(DSL.name("CONTACT_NAME"), SQLDataType.VARCHAR(255), this, "「contactName」- 联系人姓名");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.CONTACT_PHONE</code>.
     * 「contactPhone」- 联系人电话
     */
    public final TableField<ECustomerRecord, String> CONTACT_PHONE = createField(DSL.name("CONTACT_PHONE"), SQLDataType.VARCHAR(20), this, "「contactPhone」- 联系人电话");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.CONTACT_EMAIL</code>.
     * 「contactEmail」- 联系人Email
     */
    public final TableField<ECustomerRecord, String> CONTACT_EMAIL = createField(DSL.name("CONTACT_EMAIL"), SQLDataType.VARCHAR(255), this, "「contactEmail」- 联系人Email");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.CONTACT_ONLINE</code>.
     * 「contactOnline」- 在线联系方式
     */
    public final TableField<ECustomerRecord, String> CONTACT_ONLINE = createField(DSL.name("CONTACT_ONLINE"), SQLDataType.VARCHAR(255), this, "「contactOnline」- 在线联系方式");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.TITLE</code>. 「title」- 客户显示标题
     */
    public final TableField<ECustomerRecord, String> TITLE = createField(DSL.name("TITLE"), SQLDataType.VARCHAR(255), this, "「title」- 客户显示标题");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.COMMENT</code>. 「comment」- 客户备注
     */
    public final TableField<ECustomerRecord, String> COMMENT = createField(DSL.name("COMMENT"), SQLDataType.CLOB, this, "「comment」- 客户备注");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.EMAIL</code>. 「email」- 企业邮箱
     */
    public final TableField<ECustomerRecord, String> EMAIL = createField(DSL.name("EMAIL"), SQLDataType.VARCHAR(255), this, "「email」- 企业邮箱");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.FAX</code>. 「fax」- 传真号
     */
    public final TableField<ECustomerRecord, String> FAX = createField(DSL.name("FAX"), SQLDataType.VARCHAR(255), this, "「fax」- 传真号");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.HOMEPAGE</code>. 「homepage」- 客户主页
     */
    public final TableField<ECustomerRecord, String> HOMEPAGE = createField(DSL.name("HOMEPAGE"), SQLDataType.VARCHAR(128), this, "「homepage」- 客户主页");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.LOGO</code>. 「logo」- 附件对应的
     * attachment Key
     */
    public final TableField<ECustomerRecord, String> LOGO = createField(DSL.name("LOGO"), SQLDataType.VARCHAR(36), this, "「logo」- 附件对应的 attachment Key");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.PHONE</code>. 「phone」- 客户座机
     */
    public final TableField<ECustomerRecord, String> PHONE = createField(DSL.name("PHONE"), SQLDataType.VARCHAR(20), this, "「phone」- 客户座机");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.ADDRESS</code>. 「address」- 客户地址
     */
    public final TableField<ECustomerRecord, String> ADDRESS = createField(DSL.name("ADDRESS"), SQLDataType.CLOB, this, "「address」- 客户地址");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.SIGN_NAME</code>. 「signName」- 签单人姓名
     */
    public final TableField<ECustomerRecord, String> SIGN_NAME = createField(DSL.name("SIGN_NAME"), SQLDataType.VARCHAR(255), this, "「signName」- 签单人姓名");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.SIGN_PHONE</code>. 「signPhone」-
     * 签单人电话
     */
    public final TableField<ECustomerRecord, String> SIGN_PHONE = createField(DSL.name("SIGN_PHONE"), SQLDataType.VARCHAR(20), this, "「signPhone」- 签单人电话");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.RUN_UP</code>. 「runUp」- 挂账属性
     */
    public final TableField<ECustomerRecord, Boolean> RUN_UP = createField(DSL.name("RUN_UP"), SQLDataType.BIT, this, "「runUp」- 挂账属性");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.RUN_UP_AMOUNT</code>. 「runUpAmount」-
     * 挂账限额
     */
    public final TableField<ECustomerRecord, BigDecimal> RUN_UP_AMOUNT = createField(DSL.name("RUN_UP_AMOUNT"), SQLDataType.DECIMAL(18, 2), this, "「runUpAmount」- 挂账限额");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.RLT_PRICECAT</code>. 「rltPricecat」-
     * 挂账限额
     */
    public final TableField<ECustomerRecord, String> RLT_PRICECAT = createField(DSL.name("RLT_PRICECAT"), SQLDataType.VARCHAR(255), this, "「rltPricecat」- 挂账限额");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.BANK_ID</code>. 「bankId」- 开户行
     */
    public final TableField<ECustomerRecord, String> BANK_ID = createField(DSL.name("BANK_ID"), SQLDataType.VARCHAR(36), this, "「bankId」- 开户行");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.BANK_CARD</code>. 「bankCard」- 开户行账号
     */
    public final TableField<ECustomerRecord, String> BANK_CARD = createField(DSL.name("BANK_CARD"), SQLDataType.VARCHAR(255), this, "「bankCard」- 开户行账号");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.METADATA</code>. 「metadata」- 附加配置
     */
    public final TableField<ECustomerRecord, String> METADATA = createField(DSL.name("METADATA"), SQLDataType.CLOB, this, "「metadata」- 附加配置");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.ACTIVE</code>. 「active」- 是否启用
     */
    public final TableField<ECustomerRecord, Boolean> ACTIVE = createField(DSL.name("ACTIVE"), SQLDataType.BIT, this, "「active」- 是否启用");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.SIGMA</code>. 「sigma」- 统一标识（客户所属应用）
     */
    public final TableField<ECustomerRecord, String> SIGMA = createField(DSL.name("SIGMA"), SQLDataType.VARCHAR(32), this, "「sigma」- 统一标识（客户所属应用）");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.LANGUAGE</code>. 「language」- 使用的语言
     */
    public final TableField<ECustomerRecord, String> LANGUAGE = createField(DSL.name("LANGUAGE"), SQLDataType.VARCHAR(8), this, "「language」- 使用的语言");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public final TableField<ECustomerRecord, LocalDateTime> CREATED_AT = createField(DSL.name("CREATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「createdAt」- 创建时间");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public final TableField<ECustomerRecord, String> CREATED_BY = createField(DSL.name("CREATED_BY"), SQLDataType.VARCHAR(36), this, "「createdBy」- 创建人");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public final TableField<ECustomerRecord, LocalDateTime> UPDATED_AT = createField(DSL.name("UPDATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「updatedAt」- 更新时间");

    /**
     * The column <code>DB_HOTEL.E_CUSTOMER.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public final TableField<ECustomerRecord, String> UPDATED_BY = createField(DSL.name("UPDATED_BY"), SQLDataType.VARCHAR(36), this, "「updatedBy」- 更新人");

    private ECustomer(Name alias, Table<ECustomerRecord> aliased) {
        this(alias, aliased, null);
    }

    private ECustomer(Name alias, Table<ECustomerRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>DB_HOTEL.E_CUSTOMER</code> table reference
     */
    public ECustomer(String alias) {
        this(DSL.name(alias), E_CUSTOMER);
    }

    /**
     * Create an aliased <code>DB_HOTEL.E_CUSTOMER</code> table reference
     */
    public ECustomer(Name alias) {
        this(alias, E_CUSTOMER);
    }

    /**
     * Create a <code>DB_HOTEL.E_CUSTOMER</code> table reference
     */
    public ECustomer() {
        this(DSL.name("E_CUSTOMER"), null);
    }

    public <O extends Record> ECustomer(Table<O> child, ForeignKey<O, ECustomerRecord> key) {
        super(child, key, E_CUSTOMER);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Db.DB_HOTEL;
    }

    @Override
    public UniqueKey<ECustomerRecord> getPrimaryKey() {
        return Keys.KEY_E_CUSTOMER_PRIMARY;
    }

    @Override
    public List<UniqueKey<ECustomerRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_E_CUSTOMER_TAX_CODE);
    }

    @Override
    public ECustomer as(String alias) {
        return new ECustomer(DSL.name(alias), this);
    }

    @Override
    public ECustomer as(Name alias) {
        return new ECustomer(alias, this);
    }

    @Override
    public ECustomer as(Table<?> alias) {
        return new ECustomer(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public ECustomer rename(String name) {
        return new ECustomer(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ECustomer rename(Name name) {
        return new ECustomer(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public ECustomer rename(Table<?> name) {
        return new ECustomer(name.getQualifiedName(), null);
    }
}
