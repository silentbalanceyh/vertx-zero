/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.fm.domain.tables.records;


import cn.vertxup.fm.domain.tables.FBillItem;
import cn.vertxup.fm.domain.tables.interfaces.IFBillItem;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FBillItemRecord extends UpdatableRecordImpl<FBillItemRecord> implements VertxPojo, IFBillItem {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.KEY</code>. 「key」- 账单明细主键
     */
    @Override
    public FBillItemRecord setKey(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.KEY</code>. 「key」- 账单明细主键
     */
    @Override
    public String getKey() {
        return (String) get(0);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.NAME</code>. 「name」 -  明细名称
     */
    @Override
    public FBillItemRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.NAME</code>. 「name」 -  明细名称
     */
    @Override
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.CODE</code>. 「code」 - 明细系统代码
     */
    @Override
    public FBillItemRecord setCode(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.CODE</code>. 「code」 - 明细系统代码
     */
    @Override
    public String getCode() {
        return (String) get(2);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.SERIAL</code>. 「serial」 - 明细编号
     */
    @Override
    public FBillItemRecord setSerial(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.SERIAL</code>. 「serial」 - 明细编号
     */
    @Override
    public String getSerial() {
        return (String) get(3);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.TYPE</code>. 「type」- 明细类型
     */
    @Override
    public FBillItemRecord setType(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.TYPE</code>. 「type」- 明细类型
     */
    @Override
    public String getType() {
        return (String) get(4);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.STATUS</code>. 「status」- 明细状态
     */
    @Override
    public FBillItemRecord setStatus(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.STATUS</code>. 「status」- 明细状态
     */
    @Override
    public String getStatus() {
        return (String) get(5);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.AMOUNT</code>.
     * 「amount」——价税合计，实际付款结果，有可能父项
     */
    @Override
    public FBillItemRecord setAmount(BigDecimal value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.AMOUNT</code>.
     * 「amount」——价税合计，实际付款结果，有可能父项
     */
    @Override
    public BigDecimal getAmount() {
        return (BigDecimal) get(6);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.COMMENT</code>. 「comment」 - 明细备注
     */
    @Override
    public FBillItemRecord setComment(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.COMMENT</code>. 「comment」 - 明细备注
     */
    @Override
    public String getComment() {
        return (String) get(7);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.MANUAL_NO</code>. 「manualNo」 -
     * 手工单号（线下单号专用）
     */
    @Override
    public FBillItemRecord setManualNo(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.MANUAL_NO</code>. 「manualNo」 -
     * 手工单号（线下单号专用）
     */
    @Override
    public String getManualNo() {
        return (String) get(8);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.UNIT</code>. 「unit」- 计量单位
     */
    @Override
    public FBillItemRecord setUnit(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.UNIT</code>. 「unit」- 计量单位
     */
    @Override
    public String getUnit() {
        return (String) get(9);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.PRICE</code>. 「price」- 商品单价
     */
    @Override
    public FBillItemRecord setPrice(BigDecimal value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.PRICE</code>. 「price」- 商品单价
     */
    @Override
    public BigDecimal getPrice() {
        return (BigDecimal) get(10);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.QUANTITY</code>. 「quantity」- 商品数量
     */
    @Override
    public FBillItemRecord setQuantity(Integer value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.QUANTITY</code>. 「quantity」- 商品数量
     */
    @Override
    public Integer getQuantity() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.AMOUNT_TOTAL</code>.
     * 「amountTotal」——总价，理论计算结果
     */
    @Override
    public FBillItemRecord setAmountTotal(BigDecimal value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.AMOUNT_TOTAL</code>.
     * 「amountTotal」——总价，理论计算结果
     */
    @Override
    public BigDecimal getAmountTotal() {
        return (BigDecimal) get(12);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.DELAY</code>. 「delay」——是否S账
     */
    @Override
    public FBillItemRecord setDelay(Boolean value) {
        set(13, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.DELAY</code>. 「delay」——是否S账
     */
    @Override
    public Boolean getDelay() {
        return (Boolean) get(13);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.DELAY_AT</code>.
     * 「delayAt」——S账的最终期限
     */
    @Override
    public FBillItemRecord setDelayAt(LocalDateTime value) {
        set(14, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.DELAY_AT</code>.
     * 「delayAt」——S账的最终期限
     */
    @Override
    public LocalDateTime getDelayAt() {
        return (LocalDateTime) get(14);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.OP_BY</code>. 「opBy」- 操作人员，关联员工ID
     */
    @Override
    public FBillItemRecord setOpBy(String value) {
        set(15, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.OP_BY</code>. 「opBy」- 操作人员，关联员工ID
     */
    @Override
    public String getOpBy() {
        return (String) get(15);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.OP_NUMBER</code>. 「opNumber」-
     * 操作人员工号
     */
    @Override
    public FBillItemRecord setOpNumber(String value) {
        set(16, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.OP_NUMBER</code>. 「opNumber」-
     * 操作人员工号
     */
    @Override
    public String getOpNumber() {
        return (String) get(16);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.OP_SHIFT</code>. 「opShift」-
     * 操作班次（对接排班系统）
     */
    @Override
    public FBillItemRecord setOpShift(String value) {
        set(17, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.OP_SHIFT</code>. 「opShift」-
     * 操作班次（对接排班系统）
     */
    @Override
    public String getOpShift() {
        return (String) get(17);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.OP_AT</code>. 「opAt」- 操作时间
     */
    @Override
    public FBillItemRecord setOpAt(LocalDateTime value) {
        set(18, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.OP_AT</code>. 「opAt」- 操作时间
     */
    @Override
    public LocalDateTime getOpAt() {
        return (LocalDateTime) get(18);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.OP_TRANSFER</code>. 「opTransfer」-
     * 流转信息描述填写
     */
    @Override
    public FBillItemRecord setOpTransfer(String value) {
        set(19, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.OP_TRANSFER</code>. 「opTransfer」-
     * 流转信息描述填写
     */
    @Override
    public String getOpTransfer() {
        return (String) get(19);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.RELATED_ID</code>. 「relatedId」-
     * 关联ID（保留，原系统存在）
     */
    @Override
    public FBillItemRecord setRelatedId(String value) {
        set(20, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.RELATED_ID</code>. 「relatedId」-
     * 关联ID（保留，原系统存在）
     */
    @Override
    public String getRelatedId() {
        return (String) get(20);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.SETTLEMENT_ID</code>.
     * 「settlementId」- 结算单ID，该字段有值标识已经结算
     */
    @Override
    public FBillItemRecord setSettlementId(String value) {
        set(21, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.SETTLEMENT_ID</code>.
     * 「settlementId」- 结算单ID，该字段有值标识已经结算
     */
    @Override
    public String getSettlementId() {
        return (String) get(21);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.BILL_ID</code>. 「billId」- 所属账单ID
     */
    @Override
    public FBillItemRecord setBillId(String value) {
        set(22, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.BILL_ID</code>. 「billId」- 所属账单ID
     */
    @Override
    public String getBillId() {
        return (String) get(22);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.SUBJECT_ID</code>. 「subjectId」-
     * 会计科目ID，依赖账单项选择结果
     */
    @Override
    public FBillItemRecord setSubjectId(String value) {
        set(23, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.SUBJECT_ID</code>. 「subjectId」-
     * 会计科目ID，依赖账单项选择结果
     */
    @Override
    public String getSubjectId() {
        return (String) get(23);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.PAY_TERM_ID</code>. 「payTermId」-
     * 账单项ID
     */
    @Override
    public FBillItemRecord setPayTermId(String value) {
        set(24, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.PAY_TERM_ID</code>. 「payTermId」-
     * 账单项ID
     */
    @Override
    public String getPayTermId() {
        return (String) get(24);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public FBillItemRecord setSigma(String value) {
        set(25, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return (String) get(25);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public FBillItemRecord setLanguage(String value) {
        set(26, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public String getLanguage() {
        return (String) get(26);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public FBillItemRecord setActive(Boolean value) {
        set(27, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return (Boolean) get(27);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public FBillItemRecord setMetadata(String value) {
        set(28, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public String getMetadata() {
        return (String) get(28);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public FBillItemRecord setCreatedAt(LocalDateTime value) {
        set(29, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(29);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public FBillItemRecord setCreatedBy(String value) {
        set(30, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public String getCreatedBy() {
        return (String) get(30);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public FBillItemRecord setUpdatedAt(LocalDateTime value) {
        set(31, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(31);
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public FBillItemRecord setUpdatedBy(String value) {
        set(32, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public String getUpdatedBy() {
        return (String) get(32);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    @Override
    public void from(IFBillItem from) {
        setKey(from.getKey());
        setName(from.getName());
        setCode(from.getCode());
        setSerial(from.getSerial());
        setType(from.getType());
        setStatus(from.getStatus());
        setAmount(from.getAmount());
        setComment(from.getComment());
        setManualNo(from.getManualNo());
        setUnit(from.getUnit());
        setPrice(from.getPrice());
        setQuantity(from.getQuantity());
        setAmountTotal(from.getAmountTotal());
        setDelay(from.getDelay());
        setDelayAt(from.getDelayAt());
        setOpBy(from.getOpBy());
        setOpNumber(from.getOpNumber());
        setOpShift(from.getOpShift());
        setOpAt(from.getOpAt());
        setOpTransfer(from.getOpTransfer());
        setRelatedId(from.getRelatedId());
        setSettlementId(from.getSettlementId());
        setBillId(from.getBillId());
        setSubjectId(from.getSubjectId());
        setPayTermId(from.getPayTermId());
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
    public <E extends IFBillItem> E into(E into) {
        into.from(this);
        return into;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached FBillItemRecord
     */
    public FBillItemRecord() {
        super(FBillItem.F_BILL_ITEM);
    }

    /**
     * Create a detached, initialised FBillItemRecord
     */
    public FBillItemRecord(String key, String name, String code, String serial, String type, String status, BigDecimal amount, String comment, String manualNo, String unit, BigDecimal price, Integer quantity, BigDecimal amountTotal, Boolean delay, LocalDateTime delayAt, String opBy, String opNumber, String opShift, LocalDateTime opAt, String opTransfer, String relatedId, String settlementId, String billId, String subjectId, String payTermId, String sigma, String language, Boolean active, String metadata, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy) {
        super(FBillItem.F_BILL_ITEM);

        setKey(key);
        setName(name);
        setCode(code);
        setSerial(serial);
        setType(type);
        setStatus(status);
        setAmount(amount);
        setComment(comment);
        setManualNo(manualNo);
        setUnit(unit);
        setPrice(price);
        setQuantity(quantity);
        setAmountTotal(amountTotal);
        setDelay(delay);
        setDelayAt(delayAt);
        setOpBy(opBy);
        setOpNumber(opNumber);
        setOpShift(opShift);
        setOpAt(opAt);
        setOpTransfer(opTransfer);
        setRelatedId(relatedId);
        setSettlementId(settlementId);
        setBillId(billId);
        setSubjectId(subjectId);
        setPayTermId(payTermId);
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
     * Create a detached, initialised FBillItemRecord
     */
    public FBillItemRecord(cn.vertxup.fm.domain.tables.pojos.FBillItem value) {
        super(FBillItem.F_BILL_ITEM);

        if (value != null) {
            setKey(value.getKey());
            setName(value.getName());
            setCode(value.getCode());
            setSerial(value.getSerial());
            setType(value.getType());
            setStatus(value.getStatus());
            setAmount(value.getAmount());
            setComment(value.getComment());
            setManualNo(value.getManualNo());
            setUnit(value.getUnit());
            setPrice(value.getPrice());
            setQuantity(value.getQuantity());
            setAmountTotal(value.getAmountTotal());
            setDelay(value.getDelay());
            setDelayAt(value.getDelayAt());
            setOpBy(value.getOpBy());
            setOpNumber(value.getOpNumber());
            setOpShift(value.getOpShift());
            setOpAt(value.getOpAt());
            setOpTransfer(value.getOpTransfer());
            setRelatedId(value.getRelatedId());
            setSettlementId(value.getSettlementId());
            setBillId(value.getBillId());
            setSubjectId(value.getSubjectId());
            setPayTermId(value.getPayTermId());
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

        public FBillItemRecord(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }
}
