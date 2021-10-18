/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.erp.domain.tables;


import cn.vertxup.erp.domain.Db;
import cn.vertxup.erp.domain.Keys;
import cn.vertxup.erp.domain.tables.records.EIdentityRecord;
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
public class EIdentity extends TableImpl<EIdentityRecord> {

    /**
     * The reference instance of <code>DB_ETERNAL.E_IDENTITY</code>
     */
    public static final EIdentity E_IDENTITY = new EIdentity();
    private static final long serialVersionUID = 1L;
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.KEY</code>. 「key」- 身份主键
     */
    public final TableField<EIdentityRecord, String> KEY = createField(DSL.name("KEY"), SQLDataType.VARCHAR(36).nullable(false), this, "「key」- 身份主键");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.CODE</code>. 「code」- 系统编号
     */
    public final TableField<EIdentityRecord, String> CODE = createField(DSL.name("CODE"), SQLDataType.VARCHAR(255), this, "「code」- 系统编号");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.TYPE</code>. 「type」- 身份类型/档案类型
     */
    public final TableField<EIdentityRecord, String> TYPE = createField(DSL.name("TYPE"), SQLDataType.VARCHAR(36), this, "「type」- 身份类型/档案类型");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.STATUS</code>. 「status」- 档案状态
     */
    public final TableField<EIdentityRecord, String> STATUS = createField(DSL.name("STATUS"), SQLDataType.VARCHAR(255), this, "「status」- 档案状态");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.COUNTRY</code>. 「country」- 国籍
     */
    public final TableField<EIdentityRecord, String> COUNTRY = createField(DSL.name("COUNTRY"), SQLDataType.VARCHAR(20), this, "「country」- 国籍");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.NATIVE_PLACE</code>.
     * 「nativePlace」- 籍贯
     */
    public final TableField<EIdentityRecord, String> NATIVE_PLACE = createField(DSL.name("NATIVE_PLACE"), SQLDataType.VARCHAR(255), this, "「nativePlace」- 籍贯");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.NATION</code>. 「nation」- 民族
     */
    public final TableField<EIdentityRecord, String> NATION = createField(DSL.name("NATION"), SQLDataType.VARCHAR(32), this, "「nation」- 民族");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.ADDRESS</code>. 「address」- 居住地址
     */
    public final TableField<EIdentityRecord, String> ADDRESS = createField(DSL.name("ADDRESS"), SQLDataType.CLOB, this, "「address」- 居住地址");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.REALNAME</code>. 「realname」- 真实姓名
     */
    public final TableField<EIdentityRecord, String> REALNAME = createField(DSL.name("REALNAME"), SQLDataType.VARCHAR(255), this, "「realname」- 真实姓名");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.BIRTHDAY</code>. 「birthday」- 生日
     */
    public final TableField<EIdentityRecord, LocalDateTime> BIRTHDAY = createField(DSL.name("BIRTHDAY"), SQLDataType.LOCALDATETIME(0), this, "「birthday」- 生日");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.DRIVER_LICENSE</code>.
     * 「driverLicense」- 驾驶证
     */
    public final TableField<EIdentityRecord, String> DRIVER_LICENSE = createField(DSL.name("DRIVER_LICENSE"), SQLDataType.VARCHAR(255), this, "「driverLicense」- 驾驶证");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.CAR_PLATE</code>. 「carPlate」- 常用车牌
     */
    public final TableField<EIdentityRecord, String> CAR_PLATE = createField(DSL.name("CAR_PLATE"), SQLDataType.VARCHAR(255), this, "「carPlate」- 常用车牌");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.PASSPORT</code>. 「passport」- 护照
     */
    public final TableField<EIdentityRecord, String> PASSPORT = createField(DSL.name("PASSPORT"), SQLDataType.VARCHAR(255), this, "「passport」- 护照");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.GENDER</code>. 「gender」- 性别
     */
    public final TableField<EIdentityRecord, Boolean> GENDER = createField(DSL.name("GENDER"), SQLDataType.BIT, this, "「gender」- 性别");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.MARITAL</code>. 「marital」- 婚姻状况
     */
    public final TableField<EIdentityRecord, String> MARITAL = createField(DSL.name("MARITAL"), SQLDataType.VARCHAR(32), this, "「marital」- 婚姻状况");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.CONTACT_MOBILE</code>.
     * 「contactMobile」- 联系手机
     */
    public final TableField<EIdentityRecord, String> CONTACT_MOBILE = createField(DSL.name("CONTACT_MOBILE"), SQLDataType.VARCHAR(20), this, "「contactMobile」- 联系手机");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.CONTACT_PHONE</code>.
     * 「contactPhone」- 座机
     */
    public final TableField<EIdentityRecord, String> CONTACT_PHONE = createField(DSL.name("CONTACT_PHONE"), SQLDataType.VARCHAR(20), this, "「contactPhone」- 座机");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.CONTACT_ADDRESS</code>.
     * 「contactAddress」- 联系地址
     */
    public final TableField<EIdentityRecord, String> CONTACT_ADDRESS = createField(DSL.name("CONTACT_ADDRESS"), SQLDataType.CLOB, this, "「contactAddress」- 联系地址");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.CONTACT_EMAIL</code>.
     * 「contactEmail」- 联系Email
     */
    public final TableField<EIdentityRecord, String> CONTACT_EMAIL = createField(DSL.name("CONTACT_EMAIL"), SQLDataType.VARCHAR(255), this, "「contactEmail」- 联系Email");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.URGENT_NAME</code>. 「urgentName」-
     * 紧急联系人
     */
    public final TableField<EIdentityRecord, String> URGENT_NAME = createField(DSL.name("URGENT_NAME"), SQLDataType.VARCHAR(255), this, "「urgentName」- 紧急联系人");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.URGENT_PHONE</code>.
     * 「urgentPhone」- 紧急联系电话
     */
    public final TableField<EIdentityRecord, String> URGENT_PHONE = createField(DSL.name("URGENT_PHONE"), SQLDataType.VARCHAR(255), this, "「urgentPhone」- 紧急联系电话");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.EC_QQ</code>. 「ecQq」- QQ号码
     */
    public final TableField<EIdentityRecord, String> EC_QQ = createField(DSL.name("EC_QQ"), SQLDataType.VARCHAR(64), this, "「ecQq」- QQ号码");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.EC_ALIPAY</code>. 「ecAlipay」- 支付宝
     */
    public final TableField<EIdentityRecord, String> EC_ALIPAY = createField(DSL.name("EC_ALIPAY"), SQLDataType.VARCHAR(64), this, "「ecAlipay」- 支付宝");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.EC_WECHAT</code>. 「ecWechat」- 微信
     */
    public final TableField<EIdentityRecord, String> EC_WECHAT = createField(DSL.name("EC_WECHAT"), SQLDataType.VARCHAR(64), this, "「ecWechat」- 微信");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.IDC_TYPE</code>. 「idcType」- 证件类型
     */
    public final TableField<EIdentityRecord, String> IDC_TYPE = createField(DSL.name("IDC_TYPE"), SQLDataType.VARCHAR(36), this, "「idcType」- 证件类型");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.IDC_NUMBER</code>. 「idcNumber」-
     * 证件号
     */
    public final TableField<EIdentityRecord, String> IDC_NUMBER = createField(DSL.name("IDC_NUMBER"), SQLDataType.VARCHAR(255), this, "「idcNumber」- 证件号");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.IDC_ADDRESS</code>. 「idcAddress」-
     * 证件地址
     */
    public final TableField<EIdentityRecord, String> IDC_ADDRESS = createField(DSL.name("IDC_ADDRESS"), SQLDataType.CLOB, this, "「idcAddress」- 证件地址");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.IDC_EXPIRED_AT</code>.
     * 「idcExpiredAt」- 证件过期时间
     */
    public final TableField<EIdentityRecord, LocalDateTime> IDC_EXPIRED_AT = createField(DSL.name("IDC_EXPIRED_AT"), SQLDataType.LOCALDATETIME(0), this, "「idcExpiredAt」- 证件过期时间");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.IDC_FRONT</code>. 「idcFront」-
     * 证件正面附件
     */
    public final TableField<EIdentityRecord, String> IDC_FRONT = createField(DSL.name("IDC_FRONT"), SQLDataType.VARCHAR(36), this, "「idcFront」- 证件正面附件");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.IDC_BACK</code>. 「idcBack」- 证件背面附件
     */
    public final TableField<EIdentityRecord, String> IDC_BACK = createField(DSL.name("IDC_BACK"), SQLDataType.VARCHAR(36), this, "「idcBack」- 证件背面附件");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.IDC_ISSUER</code>. 「idcIssuer」-
     * 证件签发机构
     */
    public final TableField<EIdentityRecord, String> IDC_ISSUER = createField(DSL.name("IDC_ISSUER"), SQLDataType.VARCHAR(255), this, "「idcIssuer」- 证件签发机构");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.IDC_ISSUE_AT</code>. 「idcIssueAt」-
     * 证件签发时间
     */
    public final TableField<EIdentityRecord, LocalDateTime> IDC_ISSUE_AT = createField(DSL.name("IDC_ISSUE_AT"), SQLDataType.LOCALDATETIME(0), this, "「idcIssueAt」- 证件签发时间");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.VERIFIED</code>. 「verified」-
     * 是否验证、备案
     */
    public final TableField<EIdentityRecord, Boolean> VERIFIED = createField(DSL.name("VERIFIED"), SQLDataType.BIT, this, "「verified」- 是否验证、备案");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.METADATA</code>. 「metadata」- 附加配置
     */
    public final TableField<EIdentityRecord, String> METADATA = createField(DSL.name("METADATA"), SQLDataType.CLOB, this, "「metadata」- 附加配置");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.ACTIVE</code>. 「active」- 是否启用
     */
    public final TableField<EIdentityRecord, Boolean> ACTIVE = createField(DSL.name("ACTIVE"), SQLDataType.BIT, this, "「active」- 是否启用");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.SIGMA</code>. 「sigma」- 统一标识
     */
    public final TableField<EIdentityRecord, String> SIGMA = createField(DSL.name("SIGMA"), SQLDataType.VARCHAR(32), this, "「sigma」- 统一标识");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.LANGUAGE</code>. 「language」- 使用的语言
     */
    public final TableField<EIdentityRecord, String> LANGUAGE = createField(DSL.name("LANGUAGE"), SQLDataType.VARCHAR(8), this, "「language」- 使用的语言");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    public final TableField<EIdentityRecord, LocalDateTime> CREATED_AT = createField(DSL.name("CREATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「createdAt」- 创建时间");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    public final TableField<EIdentityRecord, String> CREATED_BY = createField(DSL.name("CREATED_BY"), SQLDataType.VARCHAR(36), this, "「createdBy」- 创建人");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    public final TableField<EIdentityRecord, LocalDateTime> UPDATED_AT = createField(DSL.name("UPDATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「updatedAt」- 更新时间");
    /**
     * The column <code>DB_ETERNAL.E_IDENTITY.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    public final TableField<EIdentityRecord, String> UPDATED_BY = createField(DSL.name("UPDATED_BY"), SQLDataType.VARCHAR(36), this, "「updatedBy」- 更新人");

    private EIdentity(Name alias, Table<EIdentityRecord> aliased) {
        this(alias, aliased, null);
    }

    private EIdentity(Name alias, Table<EIdentityRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>DB_ETERNAL.E_IDENTITY</code> table reference
     */
    public EIdentity(String alias) {
        this(DSL.name(alias), E_IDENTITY);
    }

    /**
     * Create an aliased <code>DB_ETERNAL.E_IDENTITY</code> table reference
     */
    public EIdentity(Name alias) {
        this(alias, E_IDENTITY);
    }

    /**
     * Create a <code>DB_ETERNAL.E_IDENTITY</code> table reference
     */
    public EIdentity() {
        this(DSL.name("E_IDENTITY"), null);
    }

    public <O extends Record> EIdentity(Table<O> child, ForeignKey<O, EIdentityRecord> key) {
        super(child, key, E_IDENTITY);
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EIdentityRecord> getRecordType() {
        return EIdentityRecord.class;
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Db.DB_ETERNAL;
    }

    @Override
    public UniqueKey<EIdentityRecord> getPrimaryKey() {
        return Keys.KEY_E_IDENTITY_PRIMARY;
    }

    @Override
    public List<UniqueKey<EIdentityRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_E_IDENTITY_TYPE);
    }

    @Override
    public EIdentity as(String alias) {
        return new EIdentity(DSL.name(alias), this);
    }

    @Override
    public EIdentity as(Name alias) {
        return new EIdentity(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EIdentity rename(String name) {
        return new EIdentity(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public EIdentity rename(Name name) {
        return new EIdentity(name, null);
    }
}
