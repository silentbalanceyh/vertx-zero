/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.fm.domain.tables;


import cn.vertxup.fm.domain.Db;
import cn.vertxup.fm.domain.Indexes;
import cn.vertxup.fm.domain.Keys;
import cn.vertxup.fm.domain.tables.records.FPreAuthorizeRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.8"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class FPreAuthorize extends TableImpl<FPreAuthorizeRecord> {

    /**
     * The reference instance of <code>DB_ETERNAL.F_PRE_AUTHORIZE</code>
     */
    public static final FPreAuthorize F_PRE_AUTHORIZE = new FPreAuthorize();
    private static final long serialVersionUID = -1515347919;
    /**
     * The column <code>DB_ETERNAL.F_PRE_AUTHORIZE.KEY</code>. 「key」- 预授权ID
     */
    public final TableField<FPreAuthorizeRecord, String> KEY = createField("KEY", org.jooq.impl.SQLDataType.VARCHAR(36), this, "「key」- 预授权ID");
    /**
     * The column <code>DB_ETERNAL.F_PRE_AUTHORIZE.CODE</code>. 「code」 - 预授权系统编号
     */
    public final TableField<FPreAuthorizeRecord, String> CODE = createField("CODE", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "「code」 - 预授权系统编号");
    /**
     * The column <code>DB_ETERNAL.F_PRE_AUTHORIZE.SERIAL</code>. 「serial」 - 预授权单据号
     */
    public final TableField<FPreAuthorizeRecord, String> SERIAL = createField("SERIAL", org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "「serial」 - 预授权单据号");
    /**
     * The column <code>DB_ETERNAL.F_PRE_AUTHORIZE.AMOUNT</code>. 「amount」- 当前预授权刷单金额
     */
    public final TableField<FPreAuthorizeRecord, BigDecimal> AMOUNT = createField("AMOUNT", org.jooq.impl.SQLDataType.DECIMAL(18, 2).nullable(false), this, "「amount」- 当前预授权刷单金额");
    /**
     * The column <code>DB_ETERNAL.F_PRE_AUTHORIZE.COMMENT</code>. 「comment」 - 预授权备注
     */
    public final TableField<FPreAuthorizeRecord, String> COMMENT = createField("COMMENT", org.jooq.impl.SQLDataType.CLOB, this, "「comment」 - 预授权备注");
    /**
     * The column <code>DB_ETERNAL.F_PRE_AUTHORIZE.EXPIRED_AT</code>. 「expiredAt」——预授权有效期
     */
    public final TableField<FPreAuthorizeRecord, LocalDateTime> EXPIRED_AT = createField("EXPIRED_AT", org.jooq.impl.SQLDataType.LOCALDATETIME, this, "「expiredAt」——预授权有效期");
    /**
     * The column <code>DB_ETERNAL.F_PRE_AUTHORIZE.BANK_NAME</code>. 「bankName」- 预授权银行名称
     */
    public final TableField<FPreAuthorizeRecord, String> BANK_NAME = createField("BANK_NAME", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "「bankName」- 预授权银行名称");
    /**
     * The column <code>DB_ETERNAL.F_PRE_AUTHORIZE.BANK_CARD</code>. 「bankCard」- 刷预授权的银行卡号
     */
    public final TableField<FPreAuthorizeRecord, String> BANK_CARD = createField("BANK_CARD", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "「bankCard」- 刷预授权的银行卡号");
    /**
     * The column <code>DB_ETERNAL.F_PRE_AUTHORIZE.ORDER_ID</code>. 「orderId」- 预授权所属订单ID
     */
    public final TableField<FPreAuthorizeRecord, String> ORDER_ID = createField("ORDER_ID", org.jooq.impl.SQLDataType.VARCHAR(36), this, "「orderId」- 预授权所属订单ID");
    /**
     * The column <code>DB_ETERNAL.F_PRE_AUTHORIZE.BILL_ID</code>. 「billId」- 预授权所属账单ID
     */
    public final TableField<FPreAuthorizeRecord, String> BILL_ID = createField("BILL_ID", org.jooq.impl.SQLDataType.VARCHAR(36), this, "「billId」- 预授权所属账单ID");
    /**
     * The column <code>DB_ETERNAL.F_PRE_AUTHORIZE.SIGMA</code>. 「sigma」- 统一标识
     */
    public final TableField<FPreAuthorizeRecord, String> SIGMA = createField("SIGMA", org.jooq.impl.SQLDataType.VARCHAR(32), this, "「sigma」- 统一标识");
    /**
     * The column <code>DB_ETERNAL.F_PRE_AUTHORIZE.LANGUAGE</code>. 「language」- 使用的语言
     */
    public final TableField<FPreAuthorizeRecord, String> LANGUAGE = createField("LANGUAGE", org.jooq.impl.SQLDataType.VARCHAR(10), this, "「language」- 使用的语言");
    /**
     * The column <code>DB_ETERNAL.F_PRE_AUTHORIZE.ACTIVE</code>. 「active」- 是否启用
     */
    public final TableField<FPreAuthorizeRecord, Boolean> ACTIVE = createField("ACTIVE", org.jooq.impl.SQLDataType.BIT, this, "「active」- 是否启用");
    /**
     * The column <code>DB_ETERNAL.F_PRE_AUTHORIZE.METADATA</code>. 「metadata」- 附加配置数据
     */
    public final TableField<FPreAuthorizeRecord, String> METADATA = createField("METADATA", org.jooq.impl.SQLDataType.CLOB, this, "「metadata」- 附加配置数据");
    /**
     * The column <code>DB_ETERNAL.F_PRE_AUTHORIZE.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public final TableField<FPreAuthorizeRecord, LocalDateTime> CREATED_AT = createField("CREATED_AT", org.jooq.impl.SQLDataType.LOCALDATETIME, this, "「createdAt」- 创建时间");
    /**
     * The column <code>DB_ETERNAL.F_PRE_AUTHORIZE.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public final TableField<FPreAuthorizeRecord, String> CREATED_BY = createField("CREATED_BY", org.jooq.impl.SQLDataType.VARCHAR(36), this, "「createdBy」- 创建人");
    /**
     * The column <code>DB_ETERNAL.F_PRE_AUTHORIZE.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public final TableField<FPreAuthorizeRecord, LocalDateTime> UPDATED_AT = createField("UPDATED_AT", org.jooq.impl.SQLDataType.LOCALDATETIME, this, "「updatedAt」- 更新时间");
    /**
     * The column <code>DB_ETERNAL.F_PRE_AUTHORIZE.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public final TableField<FPreAuthorizeRecord, String> UPDATED_BY = createField("UPDATED_BY", org.jooq.impl.SQLDataType.VARCHAR(36), this, "「updatedBy」- 更新人");

    /**
     * Create a <code>DB_ETERNAL.F_PRE_AUTHORIZE</code> table reference
     */
    public FPreAuthorize() {
        this(DSL.name("F_PRE_AUTHORIZE"), null);
    }

    /**
     * Create an aliased <code>DB_ETERNAL.F_PRE_AUTHORIZE</code> table reference
     */
    public FPreAuthorize(String alias) {
        this(DSL.name(alias), F_PRE_AUTHORIZE);
    }

    /**
     * Create an aliased <code>DB_ETERNAL.F_PRE_AUTHORIZE</code> table reference
     */
    public FPreAuthorize(Name alias) {
        this(alias, F_PRE_AUTHORIZE);
    }

    private FPreAuthorize(Name alias, Table<FPreAuthorizeRecord> aliased) {
        this(alias, aliased, null);
    }

    private FPreAuthorize(Name alias, Table<FPreAuthorizeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<FPreAuthorizeRecord> getRecordType() {
        return FPreAuthorizeRecord.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Db.DB_ETERNAL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.F_PRE_AUTHORIZE_CODE, Indexes.F_PRE_AUTHORIZE_SERIAL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<FPreAuthorizeRecord>> getKeys() {
        return Arrays.<UniqueKey<FPreAuthorizeRecord>>asList(Keys.KEY_F_PRE_AUTHORIZE_CODE, Keys.KEY_F_PRE_AUTHORIZE_SERIAL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FPreAuthorize as(String alias) {
        return new FPreAuthorize(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FPreAuthorize as(Name alias) {
        return new FPreAuthorize(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public FPreAuthorize rename(String name) {
        return new FPreAuthorize(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public FPreAuthorize rename(Name name) {
        return new FPreAuthorize(name, null);
    }
}