/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.fmd.domain.tables;


import cn.vertxup.fmd.domain.Db;
import cn.vertxup.fmd.domain.Indexes;
import cn.vertxup.fmd.domain.Keys;
import cn.vertxup.fmd.domain.tables.records.FBookRecord;
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
public class FBook extends TableImpl<FBookRecord> {

    /**
     * The reference instance of <code>DB_ETERNAL.F_BOOK</code>
     */
    public static final FBook F_BOOK = new FBook();
    private static final long serialVersionUID = -1681386497;
    /**
     * The column <code>DB_ETERNAL.F_BOOK.KEY</code>. 「key」- 账本主键ID
     */
    public final TableField<FBookRecord, String> KEY = createField("KEY", org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "「key」- 账本主键ID");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.NAME</code>. 「name」 - 账本名称
     */
    public final TableField<FBookRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR(255), this, "「name」 - 账本名称");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.CODE</code>. 「code」 - 账本的系统编号
     */
    public final TableField<FBookRecord, String> CODE = createField("CODE", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "「code」 - 账本的系统编号");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.SERIAL</code>. 「serial」 - 财务系统账本编号
     */
    public final TableField<FBookRecord, String> SERIAL = createField("SERIAL", org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "「serial」 - 财务系统账本编号");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.TYPE</code>. 「type」 - 账本类型
     */
    public final TableField<FBookRecord, String> TYPE = createField("TYPE", org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "「type」 - 账本类型");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.MAJOR</code>. 「major」- 主账本标识
     */
    public final TableField<FBookRecord, Boolean> MAJOR = createField("MAJOR", org.jooq.impl.SQLDataType.BIT, this, "「major」- 主账本标识");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.AMOUNT</code>. 「amount」- 交易金额，正数：应收，负数：应退，最终计算总金额
     */
    public final TableField<FBookRecord, BigDecimal> AMOUNT = createField("AMOUNT", org.jooq.impl.SQLDataType.DECIMAL(18, 2).nullable(false), this, "「amount」- 交易金额，正数：应收，负数：应退，最终计算总金额");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.COMMENT</code>. 「comment」 - 账本备注
     */
    public final TableField<FBookRecord, String> COMMENT = createField("COMMENT", org.jooq.impl.SQLDataType.CLOB, this, "「comment」 - 账本备注");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.CHECKED</code>. 「checked」- 是否检查
     */
    public final TableField<FBookRecord, Boolean> CHECKED = createField("CHECKED", org.jooq.impl.SQLDataType.BIT, this, "「checked」- 是否检查");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.CHECKED_DESC</code>. 「checkedDesc」 - 账本检查描述信息
     */
    public final TableField<FBookRecord, String> CHECKED_DESC = createField("CHECKED_DESC", org.jooq.impl.SQLDataType.CLOB, this, "「checkedDesc」 - 账本检查描述信息");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.EXCEED</code>. 「exceed」- 是否加收
     */
    public final TableField<FBookRecord, Boolean> EXCEED = createField("EXCEED", org.jooq.impl.SQLDataType.BIT, this, "「exceed」- 是否加收");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.EXCEED_DESC</code>. 「exceedDesc」 - 账本加收描述信息
     */
    public final TableField<FBookRecord, String> EXCEED_DESC = createField("EXCEED_DESC", org.jooq.impl.SQLDataType.CLOB, this, "「exceedDesc」 - 账本加收描述信息");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.PRE_AUTHORIZE_ID</code>. 「preAuthorizeId」- 关联预授权
     */
    public final TableField<FBookRecord, String> PRE_AUTHORIZE_ID = createField("PRE_AUTHORIZE_ID", org.jooq.impl.SQLDataType.VARCHAR(36), this, "「preAuthorizeId」- 关联预授权");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.PRE_AUTHORIZE</code>. 「preAuthorize」- 是否预授权
     */
    public final TableField<FBookRecord, Boolean> PRE_AUTHORIZE = createField("PRE_AUTHORIZE", org.jooq.impl.SQLDataType.BIT, this, "「preAuthorize」- 是否预授权");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.PRE_AUTHORIZE_DESC</code>. 「preAuthorizeDesc」 - 预授权描述信息
     */
    public final TableField<FBookRecord, String> PRE_AUTHORIZE_DESC = createField("PRE_AUTHORIZE_DESC", org.jooq.impl.SQLDataType.CLOB, this, "「preAuthorizeDesc」 - 预授权描述信息");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.MODEL_ID</code>. 「modelId」- 关联的模型identifier，用于描述
     */
    public final TableField<FBookRecord, String> MODEL_ID = createField("MODEL_ID", org.jooq.impl.SQLDataType.VARCHAR(255), this, "「modelId」- 关联的模型identifier，用于描述");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.MODEL_KEY</code>. 「modelKey」- 关联的模型记录ID，用于描述哪一个Model中的记录
     */
    public final TableField<FBookRecord, String> MODEL_KEY = createField("MODEL_KEY", org.jooq.impl.SQLDataType.VARCHAR(36), this, "「modelKey」- 关联的模型记录ID，用于描述哪一个Model中的记录");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.PARENT_ID</code>. 「parentId」- 子账本专用，引用父账本ID
     */
    public final TableField<FBookRecord, String> PARENT_ID = createField("PARENT_ID", org.jooq.impl.SQLDataType.VARCHAR(36), this, "「parentId」- 子账本专用，引用父账本ID");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.ORDER_ID</code>. 「orderId」- 订单对应的订单ID
     */
    public final TableField<FBookRecord, String> ORDER_ID = createField("ORDER_ID", org.jooq.impl.SQLDataType.VARCHAR(36), this, "「orderId」- 订单对应的订单ID");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.SIGMA</code>. 「sigma」- 统一标识
     */
    public final TableField<FBookRecord, String> SIGMA = createField("SIGMA", org.jooq.impl.SQLDataType.VARCHAR(32), this, "「sigma」- 统一标识");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.LANGUAGE</code>. 「language」- 使用的语言
     */
    public final TableField<FBookRecord, String> LANGUAGE = createField("LANGUAGE", org.jooq.impl.SQLDataType.VARCHAR(10), this, "「language」- 使用的语言");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.ACTIVE</code>. 「active」- 是否启用
     */
    public final TableField<FBookRecord, Boolean> ACTIVE = createField("ACTIVE", org.jooq.impl.SQLDataType.BIT, this, "「active」- 是否启用");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.METADATA</code>. 「metadata」- 附加配置数据
     */
    public final TableField<FBookRecord, String> METADATA = createField("METADATA", org.jooq.impl.SQLDataType.CLOB, this, "「metadata」- 附加配置数据");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public final TableField<FBookRecord, LocalDateTime> CREATED_AT = createField("CREATED_AT", org.jooq.impl.SQLDataType.LOCALDATETIME, this, "「createdAt」- 创建时间");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public final TableField<FBookRecord, String> CREATED_BY = createField("CREATED_BY", org.jooq.impl.SQLDataType.VARCHAR(36), this, "「createdBy」- 创建人");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public final TableField<FBookRecord, LocalDateTime> UPDATED_AT = createField("UPDATED_AT", org.jooq.impl.SQLDataType.LOCALDATETIME, this, "「updatedAt」- 更新时间");
    /**
     * The column <code>DB_ETERNAL.F_BOOK.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public final TableField<FBookRecord, String> UPDATED_BY = createField("UPDATED_BY", org.jooq.impl.SQLDataType.VARCHAR(36), this, "「updatedBy」- 更新人");

    /**
     * Create a <code>DB_ETERNAL.F_BOOK</code> table reference
     */
    public FBook() {
        this(DSL.name("F_BOOK"), null);
    }

    /**
     * Create an aliased <code>DB_ETERNAL.F_BOOK</code> table reference
     */
    public FBook(String alias) {
        this(DSL.name(alias), F_BOOK);
    }

    /**
     * Create an aliased <code>DB_ETERNAL.F_BOOK</code> table reference
     */
    public FBook(Name alias) {
        this(alias, F_BOOK);
    }

    private FBook(Name alias, Table<FBookRecord> aliased) {
        this(alias, aliased, null);
    }

    private FBook(Name alias, Table<FBookRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<FBookRecord> getRecordType() {
        return FBookRecord.class;
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
        return Arrays.<Index>asList(Indexes.F_BOOK_CODE, Indexes.F_BOOK_PRIMARY, Indexes.F_BOOK_SERIAL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<FBookRecord> getPrimaryKey() {
        return Keys.KEY_F_BOOK_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<FBookRecord>> getKeys() {
        return Arrays.<UniqueKey<FBookRecord>>asList(Keys.KEY_F_BOOK_PRIMARY, Keys.KEY_F_BOOK_CODE, Keys.KEY_F_BOOK_SERIAL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FBook as(String alias) {
        return new FBook(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FBook as(Name alias) {
        return new FBook(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public FBook rename(String name) {
        return new FBook(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public FBook rename(Name name) {
        return new FBook(name, null);
    }
}
