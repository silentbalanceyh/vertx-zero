/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.fm.domain.tables.records;


import cn.vertxup.fm.domain.tables.FPreAuthorize;
import cn.vertxup.fm.domain.tables.interfaces.IFPreAuthorize;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record20;
import org.jooq.Row20;
import org.jooq.impl.UpdatableRecordImpl;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FPreAuthorizeRecord extends UpdatableRecordImpl<FPreAuthorizeRecord> implements VertxPojo, Record20<String, String, String, String, BigDecimal, String, LocalDateTime, String, String, String, String, String, String, String, Boolean, String, LocalDateTime, String, LocalDateTime, String>, IFPreAuthorize {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.KEY</code>. 「key」- 预授权ID
     */
    @Override
    public FPreAuthorizeRecord setKey(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.KEY</code>. 「key」- 预授权ID
     */
    @Override
    public String getKey() {
        return (String) get(0);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.CODE</code>. 「code」 - 预授权系统编号
     */
    @Override
    public FPreAuthorizeRecord setCode(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.CODE</code>. 「code」 - 预授权系统编号
     */
    @Override
    public String getCode() {
        return (String) get(1);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.SERIAL</code>. 「serial」 -
     * 预授权单据号
     */
    @Override
    public FPreAuthorizeRecord setSerial(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.SERIAL</code>. 「serial」 -
     * 预授权单据号
     */
    @Override
    public String getSerial() {
        return (String) get(2);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.STATUS</code>. 「status」 -
     * 预授权状态，Lock/Unlock
     */
    @Override
    public FPreAuthorizeRecord setStatus(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.STATUS</code>. 「status」 -
     * 预授权状态，Lock/Unlock
     */
    @Override
    public String getStatus() {
        return (String) get(3);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.AMOUNT</code>. 「amount」-
     * 当前预授权刷单金额
     */
    @Override
    public FPreAuthorizeRecord setAmount(BigDecimal value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.AMOUNT</code>. 「amount」-
     * 当前预授权刷单金额
     */
    @Override
    public BigDecimal getAmount() {
        return (BigDecimal) get(4);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.COMMENT</code>. 「comment」 -
     * 预授权备注
     */
    @Override
    public FPreAuthorizeRecord setComment(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.COMMENT</code>. 「comment」 -
     * 预授权备注
     */
    @Override
    public String getComment() {
        return (String) get(5);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.EXPIRED_AT</code>.
     * 「expiredAt」——预授权有效期
     */
    @Override
    public FPreAuthorizeRecord setExpiredAt(LocalDateTime value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.EXPIRED_AT</code>.
     * 「expiredAt」——预授权有效期
     */
    @Override
    public LocalDateTime getExpiredAt() {
        return (LocalDateTime) get(6);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.BANK_NAME</code>. 「bankName」-
     * 预授权银行名称
     */
    @Override
    public FPreAuthorizeRecord setBankName(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.BANK_NAME</code>. 「bankName」-
     * 预授权银行名称
     */
    @Override
    public String getBankName() {
        return (String) get(7);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.BANK_CARD</code>. 「bankCard」-
     * 刷预授权的银行卡号
     */
    @Override
    public FPreAuthorizeRecord setBankCard(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.BANK_CARD</code>. 「bankCard」-
     * 刷预授权的银行卡号
     */
    @Override
    public String getBankCard() {
        return (String) get(8);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.ORDER_ID</code>. 「orderId」-
     * 预授权所属订单ID
     */
    @Override
    public FPreAuthorizeRecord setOrderId(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.ORDER_ID</code>. 「orderId」-
     * 预授权所属订单ID
     */
    @Override
    public String getOrderId() {
        return (String) get(9);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.BILL_ID</code>. 「billId」-
     * 预授权所属账单ID
     */
    @Override
    public FPreAuthorizeRecord setBillId(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.BILL_ID</code>. 「billId」-
     * 预授权所属账单ID
     */
    @Override
    public String getBillId() {
        return (String) get(10);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.BOOK_ID</code>. 「bookId」-
     * 所属账本ID
     */
    @Override
    public FPreAuthorizeRecord setBookId(String value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.BOOK_ID</code>. 「bookId」-
     * 所属账本ID
     */
    @Override
    public String getBookId() {
        return (String) get(11);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public FPreAuthorizeRecord setSigma(String value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return (String) get(12);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public FPreAuthorizeRecord setLanguage(String value) {
        set(13, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public String getLanguage() {
        return (String) get(13);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public FPreAuthorizeRecord setActive(Boolean value) {
        set(14, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return (Boolean) get(14);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public FPreAuthorizeRecord setMetadata(String value) {
        set(15, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public String getMetadata() {
        return (String) get(15);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.CREATED_AT</code>.
     * 「createdAt」- 创建时间
     */
    @Override
    public FPreAuthorizeRecord setCreatedAt(LocalDateTime value) {
        set(16, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.CREATED_AT</code>.
     * 「createdAt」- 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(16);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.CREATED_BY</code>.
     * 「createdBy」- 创建人
     */
    @Override
    public FPreAuthorizeRecord setCreatedBy(String value) {
        set(17, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.CREATED_BY</code>.
     * 「createdBy」- 创建人
     */
    @Override
    public String getCreatedBy() {
        return (String) get(17);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.UPDATED_AT</code>.
     * 「updatedAt」- 更新时间
     */
    @Override
    public FPreAuthorizeRecord setUpdatedAt(LocalDateTime value) {
        set(18, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.UPDATED_AT</code>.
     * 「updatedAt」- 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(18);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.UPDATED_BY</code>.
     * 「updatedBy」- 更新人
     */
    @Override
    public FPreAuthorizeRecord setUpdatedBy(String value) {
        set(19, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_PRE_AUTHORIZE.UPDATED_BY</code>.
     * 「updatedBy」- 更新人
     */
    @Override
    public String getUpdatedBy() {
        return (String) get(19);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record20 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row20<String, String, String, String, BigDecimal, String, LocalDateTime, String, String, String, String, String, String, String, Boolean, String, LocalDateTime, String, LocalDateTime, String> fieldsRow() {
        return (Row20) super.fieldsRow();
    }

    @Override
    public Row20<String, String, String, String, BigDecimal, String, LocalDateTime, String, String, String, String, String, String, String, Boolean, String, LocalDateTime, String, LocalDateTime, String> valuesRow() {
        return (Row20) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return FPreAuthorize.F_PRE_AUTHORIZE.KEY;
    }

    @Override
    public Field<String> field2() {
        return FPreAuthorize.F_PRE_AUTHORIZE.CODE;
    }

    @Override
    public Field<String> field3() {
        return FPreAuthorize.F_PRE_AUTHORIZE.SERIAL;
    }

    @Override
    public Field<String> field4() {
        return FPreAuthorize.F_PRE_AUTHORIZE.STATUS;
    }

    @Override
    public Field<BigDecimal> field5() {
        return FPreAuthorize.F_PRE_AUTHORIZE.AMOUNT;
    }

    @Override
    public Field<String> field6() {
        return FPreAuthorize.F_PRE_AUTHORIZE.COMMENT;
    }

    @Override
    public Field<LocalDateTime> field7() {
        return FPreAuthorize.F_PRE_AUTHORIZE.EXPIRED_AT;
    }

    @Override
    public Field<String> field8() {
        return FPreAuthorize.F_PRE_AUTHORIZE.BANK_NAME;
    }

    @Override
    public Field<String> field9() {
        return FPreAuthorize.F_PRE_AUTHORIZE.BANK_CARD;
    }

    @Override
    public Field<String> field10() {
        return FPreAuthorize.F_PRE_AUTHORIZE.ORDER_ID;
    }

    @Override
    public Field<String> field11() {
        return FPreAuthorize.F_PRE_AUTHORIZE.BILL_ID;
    }

    @Override
    public Field<String> field12() {
        return FPreAuthorize.F_PRE_AUTHORIZE.BOOK_ID;
    }

    @Override
    public Field<String> field13() {
        return FPreAuthorize.F_PRE_AUTHORIZE.SIGMA;
    }

    @Override
    public Field<String> field14() {
        return FPreAuthorize.F_PRE_AUTHORIZE.LANGUAGE;
    }

    @Override
    public Field<Boolean> field15() {
        return FPreAuthorize.F_PRE_AUTHORIZE.ACTIVE;
    }

    @Override
    public Field<String> field16() {
        return FPreAuthorize.F_PRE_AUTHORIZE.METADATA;
    }

    @Override
    public Field<LocalDateTime> field17() {
        return FPreAuthorize.F_PRE_AUTHORIZE.CREATED_AT;
    }

    @Override
    public Field<String> field18() {
        return FPreAuthorize.F_PRE_AUTHORIZE.CREATED_BY;
    }

    @Override
    public Field<LocalDateTime> field19() {
        return FPreAuthorize.F_PRE_AUTHORIZE.UPDATED_AT;
    }

    @Override
    public Field<String> field20() {
        return FPreAuthorize.F_PRE_AUTHORIZE.UPDATED_BY;
    }

    @Override
    public String component1() {
        return getKey();
    }

    @Override
    public String component2() {
        return getCode();
    }

    @Override
    public String component3() {
        return getSerial();
    }

    @Override
    public String component4() {
        return getStatus();
    }

    @Override
    public BigDecimal component5() {
        return getAmount();
    }

    @Override
    public String component6() {
        return getComment();
    }

    @Override
    public LocalDateTime component7() {
        return getExpiredAt();
    }

    @Override
    public String component8() {
        return getBankName();
    }

    @Override
    public String component9() {
        return getBankCard();
    }

    @Override
    public String component10() {
        return getOrderId();
    }

    @Override
    public String component11() {
        return getBillId();
    }

    @Override
    public String component12() {
        return getBookId();
    }

    @Override
    public String component13() {
        return getSigma();
    }

    @Override
    public String component14() {
        return getLanguage();
    }

    @Override
    public Boolean component15() {
        return getActive();
    }

    @Override
    public String component16() {
        return getMetadata();
    }

    @Override
    public LocalDateTime component17() {
        return getCreatedAt();
    }

    @Override
    public String component18() {
        return getCreatedBy();
    }

    @Override
    public LocalDateTime component19() {
        return getUpdatedAt();
    }

    @Override
    public String component20() {
        return getUpdatedBy();
    }

    @Override
    public String value1() {
        return getKey();
    }

    @Override
    public String value2() {
        return getCode();
    }

    @Override
    public String value3() {
        return getSerial();
    }

    @Override
    public String value4() {
        return getStatus();
    }

    @Override
    public BigDecimal value5() {
        return getAmount();
    }

    @Override
    public String value6() {
        return getComment();
    }

    @Override
    public LocalDateTime value7() {
        return getExpiredAt();
    }

    @Override
    public String value8() {
        return getBankName();
    }

    @Override
    public String value9() {
        return getBankCard();
    }

    @Override
    public String value10() {
        return getOrderId();
    }

    @Override
    public String value11() {
        return getBillId();
    }

    @Override
    public String value12() {
        return getBookId();
    }

    @Override
    public String value13() {
        return getSigma();
    }

    @Override
    public String value14() {
        return getLanguage();
    }

    @Override
    public Boolean value15() {
        return getActive();
    }

    @Override
    public String value16() {
        return getMetadata();
    }

    @Override
    public LocalDateTime value17() {
        return getCreatedAt();
    }

    @Override
    public String value18() {
        return getCreatedBy();
    }

    @Override
    public LocalDateTime value19() {
        return getUpdatedAt();
    }

    @Override
    public String value20() {
        return getUpdatedBy();
    }

    @Override
    public FPreAuthorizeRecord value1(String value) {
        setKey(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value2(String value) {
        setCode(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value3(String value) {
        setSerial(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value4(String value) {
        setStatus(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value5(BigDecimal value) {
        setAmount(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value6(String value) {
        setComment(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value7(LocalDateTime value) {
        setExpiredAt(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value8(String value) {
        setBankName(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value9(String value) {
        setBankCard(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value10(String value) {
        setOrderId(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value11(String value) {
        setBillId(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value12(String value) {
        setBookId(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value13(String value) {
        setSigma(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value14(String value) {
        setLanguage(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value15(Boolean value) {
        setActive(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value16(String value) {
        setMetadata(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value17(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value18(String value) {
        setCreatedBy(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value19(LocalDateTime value) {
        setUpdatedAt(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord value20(String value) {
        setUpdatedBy(value);
        return this;
    }

    @Override
    public FPreAuthorizeRecord values(String value1, String value2, String value3, String value4, BigDecimal value5, String value6, LocalDateTime value7, String value8, String value9, String value10, String value11, String value12, String value13, String value14, Boolean value15, String value16, LocalDateTime value17, String value18, LocalDateTime value19, String value20) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        value14(value14);
        value15(value15);
        value16(value16);
        value17(value17);
        value18(value18);
        value19(value19);
        value20(value20);
        return this;
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    @Override
    public void from(IFPreAuthorize from) {
        setKey(from.getKey());
        setCode(from.getCode());
        setSerial(from.getSerial());
        setStatus(from.getStatus());
        setAmount(from.getAmount());
        setComment(from.getComment());
        setExpiredAt(from.getExpiredAt());
        setBankName(from.getBankName());
        setBankCard(from.getBankCard());
        setOrderId(from.getOrderId());
        setBillId(from.getBillId());
        setBookId(from.getBookId());
        setSigma(from.getSigma());
        setLanguage(from.getLanguage());
        setActive(from.getActive());
        setMetadata(from.getMetadata());
        setCreatedAt(from.getCreatedAt());
        setCreatedBy(from.getCreatedBy());
        setUpdatedAt(from.getUpdatedAt());
        setUpdatedBy(from.getUpdatedBy());
    }

    @Override
    public <E extends IFPreAuthorize> E into(E into) {
        into.from(this);
        return into;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached FPreAuthorizeRecord
     */
    public FPreAuthorizeRecord() {
        super(FPreAuthorize.F_PRE_AUTHORIZE);
    }

    /**
     * Create a detached, initialised FPreAuthorizeRecord
     */
    public FPreAuthorizeRecord(String key, String code, String serial, String status, BigDecimal amount, String comment, LocalDateTime expiredAt, String bankName, String bankCard, String orderId, String billId, String bookId, String sigma, String language, Boolean active, String metadata, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy) {
        super(FPreAuthorize.F_PRE_AUTHORIZE);

        setKey(key);
        setCode(code);
        setSerial(serial);
        setStatus(status);
        setAmount(amount);
        setComment(comment);
        setExpiredAt(expiredAt);
        setBankName(bankName);
        setBankCard(bankCard);
        setOrderId(orderId);
        setBillId(billId);
        setBookId(bookId);
        setSigma(sigma);
        setLanguage(language);
        setActive(active);
        setMetadata(metadata);
        setCreatedAt(createdAt);
        setCreatedBy(createdBy);
        setUpdatedAt(updatedAt);
        setUpdatedBy(updatedBy);
    }

    /**
     * Create a detached, initialised FPreAuthorizeRecord
     */
    public FPreAuthorizeRecord(cn.vertxup.fm.domain.tables.pojos.FPreAuthorize value) {
        super(FPreAuthorize.F_PRE_AUTHORIZE);

        if (value != null) {
            setKey(value.getKey());
            setCode(value.getCode());
            setSerial(value.getSerial());
            setStatus(value.getStatus());
            setAmount(value.getAmount());
            setComment(value.getComment());
            setExpiredAt(value.getExpiredAt());
            setBankName(value.getBankName());
            setBankCard(value.getBankCard());
            setOrderId(value.getOrderId());
            setBillId(value.getBillId());
            setBookId(value.getBookId());
            setSigma(value.getSigma());
            setLanguage(value.getLanguage());
            setActive(value.getActive());
            setMetadata(value.getMetadata());
            setCreatedAt(value.getCreatedAt());
            setCreatedBy(value.getCreatedBy());
            setUpdatedAt(value.getUpdatedAt());
            setUpdatedBy(value.getUpdatedBy());
        }
    }

        public FPreAuthorizeRecord(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }
}
