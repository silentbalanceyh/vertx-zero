/*
 * This file is generated by jOOQ.
*/
package cn.vertxup.fmd.domain.tables.records;


import cn.vertxup.fmd.domain.tables.FSettlementItem;
import cn.vertxup.fmd.domain.tables.interfaces.IFSettlementItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record19;
import org.jooq.Row19;
import org.jooq.impl.UpdatableRecordImpl;


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
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FSettlementItemRecord extends UpdatableRecordImpl<FSettlementItemRecord> implements Record19<String, String, String, String, BigDecimal, String, String, String, String, String, String, String, String, Boolean, String, LocalDateTime, String, LocalDateTime, String>, IFSettlementItem {

    private static final long serialVersionUID = 860610180;

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.KEY</code>. 「key」- 结算单明细主键
     */
    @Override
    public FSettlementItemRecord setKey(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.KEY</code>. 「key」- 结算单明细主键
     */
    @Override
    public String getKey() {
        return (String) get(0);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.NAME</code>. 「name」 -  明细名称
     */
    @Override
    public FSettlementItemRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.NAME</code>. 「name」 -  明细名称
     */
    @Override
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.CODE</code>. 「code」 - 明细系统代码
     */
    @Override
    public FSettlementItemRecord setCode(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.CODE</code>. 「code」 - 明细系统代码
     */
    @Override
    public String getCode() {
        return (String) get(2);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.SERIAL</code>. 「serial」 - 明细编号
     */
    @Override
    public FSettlementItemRecord setSerial(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.SERIAL</code>. 「serial」 - 明细编号
     */
    @Override
    public String getSerial() {
        return (String) get(3);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.AMOUNT</code>. 「amount」——价税合计，实际结算金额
     */
    @Override
    public FSettlementItemRecord setAmount(BigDecimal value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.AMOUNT</code>. 「amount」——价税合计，实际结算金额
     */
    @Override
    public BigDecimal getAmount() {
        return (BigDecimal) get(4);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.COMMENT</code>. 「comment」 - 明细备注
     */
    @Override
    public FSettlementItemRecord setComment(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.COMMENT</code>. 「comment」 - 明细备注
     */
    @Override
    public String getComment() {
        return (String) get(5);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.MANUAL_NO</code>. 「manualNo」 - 手工单号（线下单号专用）
     */
    @Override
    public FSettlementItemRecord setManualNo(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.MANUAL_NO</code>. 「manualNo」 - 手工单号（线下单号专用）
     */
    @Override
    public String getManualNo() {
        return (String) get(6);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.CUSTOMER_ID</code>. 「customerId」结算对象（单位ID）
     */
    @Override
    public FSettlementItemRecord setCustomerId(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.CUSTOMER_ID</code>. 「customerId」结算对象（单位ID）
     */
    @Override
    public String getCustomerId() {
        return (String) get(7);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.PAY_TERM_ID</code>. 「payTermId」- 账单项ID
     */
    @Override
    public FSettlementItemRecord setPayTermId(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.PAY_TERM_ID</code>. 「payTermId」- 账单项ID
     */
    @Override
    public String getPayTermId() {
        return (String) get(8);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.SETTLEMENT_ID</code>. 「settlementId」- 结算单ID，该字段有值标识已经结算
     */
    @Override
    public FSettlementItemRecord setSettlementId(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.SETTLEMENT_ID</code>. 「settlementId」- 结算单ID，该字段有值标识已经结算
     */
    @Override
    public String getSettlementId() {
        return (String) get(9);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.RECEIVABLE_ID</code>. 「receivableId」- 应收账单ID
     */
    @Override
    public FSettlementItemRecord setReceivableId(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.RECEIVABLE_ID</code>. 「receivableId」- 应收账单ID
     */
    @Override
    public String getReceivableId() {
        return (String) get(10);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public FSettlementItemRecord setSigma(String value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return (String) get(11);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public FSettlementItemRecord setLanguage(String value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public String getLanguage() {
        return (String) get(12);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public FSettlementItemRecord setActive(Boolean value) {
        set(13, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return (Boolean) get(13);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.METADATA</code>. 「metadata」- 附加配置数据
     */
    @Override
    public FSettlementItemRecord setMetadata(String value) {
        set(14, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.METADATA</code>. 「metadata」- 附加配置数据
     */
    @Override
    public String getMetadata() {
        return (String) get(14);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    @Override
    public FSettlementItemRecord setCreatedAt(LocalDateTime value) {
        set(15, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(15);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public FSettlementItemRecord setCreatedBy(String value) {
        set(16, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public String getCreatedBy() {
        return (String) get(16);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    @Override
    public FSettlementItemRecord setUpdatedAt(LocalDateTime value) {
        set(17, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(17);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public FSettlementItemRecord setUpdatedBy(String value) {
        set(18, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT_ITEM.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public String getUpdatedBy() {
        return (String) get(18);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record19 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row19<String, String, String, String, BigDecimal, String, String, String, String, String, String, String, String, Boolean, String, LocalDateTime, String, LocalDateTime, String> fieldsRow() {
        return (Row19) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row19<String, String, String, String, BigDecimal, String, String, String, String, String, String, String, String, Boolean, String, LocalDateTime, String, LocalDateTime, String> valuesRow() {
        return (Row19) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field1() {
        return FSettlementItem.F_SETTLEMENT_ITEM.KEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return FSettlementItem.F_SETTLEMENT_ITEM.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return FSettlementItem.F_SETTLEMENT_ITEM.CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return FSettlementItem.F_SETTLEMENT_ITEM.SERIAL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field5() {
        return FSettlementItem.F_SETTLEMENT_ITEM.AMOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return FSettlementItem.F_SETTLEMENT_ITEM.COMMENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return FSettlementItem.F_SETTLEMENT_ITEM.MANUAL_NO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return FSettlementItem.F_SETTLEMENT_ITEM.CUSTOMER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return FSettlementItem.F_SETTLEMENT_ITEM.PAY_TERM_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field10() {
        return FSettlementItem.F_SETTLEMENT_ITEM.SETTLEMENT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field11() {
        return FSettlementItem.F_SETTLEMENT_ITEM.RECEIVABLE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field12() {
        return FSettlementItem.F_SETTLEMENT_ITEM.SIGMA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field13() {
        return FSettlementItem.F_SETTLEMENT_ITEM.LANGUAGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field14() {
        return FSettlementItem.F_SETTLEMENT_ITEM.ACTIVE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field15() {
        return FSettlementItem.F_SETTLEMENT_ITEM.METADATA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field16() {
        return FSettlementItem.F_SETTLEMENT_ITEM.CREATED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field17() {
        return FSettlementItem.F_SETTLEMENT_ITEM.CREATED_BY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field18() {
        return FSettlementItem.F_SETTLEMENT_ITEM.UPDATED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field19() {
        return FSettlementItem.F_SETTLEMENT_ITEM.UPDATED_BY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component1() {
        return getKey();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getSerial();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal component5() {
        return getAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component6() {
        return getComment();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component7() {
        return getManualNo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component8() {
        return getCustomerId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component9() {
        return getPayTermId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component10() {
        return getSettlementId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component11() {
        return getReceivableId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component12() {
        return getSigma();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component13() {
        return getLanguage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean component14() {
        return getActive();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component15() {
        return getMetadata();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime component16() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component17() {
        return getCreatedBy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime component18() {
        return getUpdatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component19() {
        return getUpdatedBy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value1() {
        return getKey();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getSerial();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value5() {
        return getAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getComment();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getManualNo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getCustomerId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getPayTermId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value10() {
        return getSettlementId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value11() {
        return getReceivableId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value12() {
        return getSigma();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value13() {
        return getLanguage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value14() {
        return getActive();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value15() {
        return getMetadata();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value16() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value17() {
        return getCreatedBy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value18() {
        return getUpdatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value19() {
        return getUpdatedBy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value1(String value) {
        setKey(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value2(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value3(String value) {
        setCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value4(String value) {
        setSerial(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value5(BigDecimal value) {
        setAmount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value6(String value) {
        setComment(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value7(String value) {
        setManualNo(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value8(String value) {
        setCustomerId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value9(String value) {
        setPayTermId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value10(String value) {
        setSettlementId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value11(String value) {
        setReceivableId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value12(String value) {
        setSigma(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value13(String value) {
        setLanguage(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value14(Boolean value) {
        setActive(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value15(String value) {
        setMetadata(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value16(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value17(String value) {
        setCreatedBy(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value18(LocalDateTime value) {
        setUpdatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord value19(String value) {
        setUpdatedBy(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FSettlementItemRecord values(String value1, String value2, String value3, String value4, BigDecimal value5, String value6, String value7, String value8, String value9, String value10, String value11, String value12, String value13, Boolean value14, String value15, LocalDateTime value16, String value17, LocalDateTime value18, String value19) {
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
        return this;
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public void from(IFSettlementItem from) {
        setKey(from.getKey());
        setName(from.getName());
        setCode(from.getCode());
        setSerial(from.getSerial());
        setAmount(from.getAmount());
        setComment(from.getComment());
        setManualNo(from.getManualNo());
        setCustomerId(from.getCustomerId());
        setPayTermId(from.getPayTermId());
        setSettlementId(from.getSettlementId());
        setReceivableId(from.getReceivableId());
        setSigma(from.getSigma());
        setLanguage(from.getLanguage());
        setActive(from.getActive());
        setMetadata(from.getMetadata());
        setCreatedAt(from.getCreatedAt());
        setCreatedBy(from.getCreatedBy());
        setUpdatedAt(from.getUpdatedAt());
        setUpdatedBy(from.getUpdatedBy());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends IFSettlementItem> E into(E into) {
        into.from(this);
        return into;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached FSettlementItemRecord
     */
    public FSettlementItemRecord() {
        super(FSettlementItem.F_SETTLEMENT_ITEM);
    }

    /**
     * Create a detached, initialised FSettlementItemRecord
     */
    public FSettlementItemRecord(String key, String name, String code, String serial, BigDecimal amount, String comment, String manualNo, String customerId, String payTermId, String settlementId, String receivableId, String sigma, String language, Boolean active, String metadata, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy) {
        super(FSettlementItem.F_SETTLEMENT_ITEM);

        set(0, key);
        set(1, name);
        set(2, code);
        set(3, serial);
        set(4, amount);
        set(5, comment);
        set(6, manualNo);
        set(7, customerId);
        set(8, payTermId);
        set(9, settlementId);
        set(10, receivableId);
        set(11, sigma);
        set(12, language);
        set(13, active);
        set(14, metadata);
        set(15, createdAt);
        set(16, createdBy);
        set(17, updatedAt);
        set(18, updatedBy);
    }
}