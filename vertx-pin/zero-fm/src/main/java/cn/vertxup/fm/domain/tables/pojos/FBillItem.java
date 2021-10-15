/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.fm.domain.tables.pojos;


import cn.vertxup.fm.domain.tables.interfaces.IFBillItem;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FBillItem implements VertxPojo, IFBillItem {

    private static final long serialVersionUID = 1L;

    private String        key;
    private String        name;
    private String        code;
    private String        serial;
    private String        type;
    private String        status;
    private BigDecimal    amount;
    private String        comment;
    private String        manualNo;
    private BigDecimal    price;
    private Integer       quantity;
    private BigDecimal    amountTotal;
    private Boolean       delay;
    private LocalDateTime delayAt;
    private String        opBy;
    private String        opNumber;
    private String        opShift;
    private LocalDateTime opAt;
    private String        opTransfer;
    private String        relatedId;
    private String        settlementId;
    private String        billId;
    private String        subjectId;
    private String        payTermId;
    private String        sigma;
    private String        language;
    private Boolean       active;
    private String        metadata;
    private LocalDateTime createdAt;
    private String        createdBy;
    private LocalDateTime updatedAt;
    private String        updatedBy;

    public FBillItem() {}

    public FBillItem(IFBillItem value) {
        this.key = value.getKey();
        this.name = value.getName();
        this.code = value.getCode();
        this.serial = value.getSerial();
        this.type = value.getType();
        this.status = value.getStatus();
        this.amount = value.getAmount();
        this.comment = value.getComment();
        this.manualNo = value.getManualNo();
        this.price = value.getPrice();
        this.quantity = value.getQuantity();
        this.amountTotal = value.getAmountTotal();
        this.delay = value.getDelay();
        this.delayAt = value.getDelayAt();
        this.opBy = value.getOpBy();
        this.opNumber = value.getOpNumber();
        this.opShift = value.getOpShift();
        this.opAt = value.getOpAt();
        this.opTransfer = value.getOpTransfer();
        this.relatedId = value.getRelatedId();
        this.settlementId = value.getSettlementId();
        this.billId = value.getBillId();
        this.subjectId = value.getSubjectId();
        this.payTermId = value.getPayTermId();
        this.sigma = value.getSigma();
        this.language = value.getLanguage();
        this.active = value.getActive();
        this.metadata = value.getMetadata();
        this.createdAt = value.getCreatedAt();
        this.createdBy = value.getCreatedBy();
        this.updatedAt = value.getUpdatedAt();
        this.updatedBy = value.getUpdatedBy();
    }

    public FBillItem(
        String        key,
        String        name,
        String        code,
        String        serial,
        String        type,
        String        status,
        BigDecimal    amount,
        String        comment,
        String        manualNo,
        BigDecimal    price,
        Integer       quantity,
        BigDecimal    amountTotal,
        Boolean       delay,
        LocalDateTime delayAt,
        String        opBy,
        String        opNumber,
        String        opShift,
        LocalDateTime opAt,
        String        opTransfer,
        String        relatedId,
        String        settlementId,
        String        billId,
        String        subjectId,
        String        payTermId,
        String        sigma,
        String        language,
        Boolean       active,
        String        metadata,
        LocalDateTime createdAt,
        String        createdBy,
        LocalDateTime updatedAt,
        String        updatedBy
    ) {
        this.key = key;
        this.name = name;
        this.code = code;
        this.serial = serial;
        this.type = type;
        this.status = status;
        this.amount = amount;
        this.comment = comment;
        this.manualNo = manualNo;
        this.price = price;
        this.quantity = quantity;
        this.amountTotal = amountTotal;
        this.delay = delay;
        this.delayAt = delayAt;
        this.opBy = opBy;
        this.opNumber = opNumber;
        this.opShift = opShift;
        this.opAt = opAt;
        this.opTransfer = opTransfer;
        this.relatedId = relatedId;
        this.settlementId = settlementId;
        this.billId = billId;
        this.subjectId = subjectId;
        this.payTermId = payTermId;
        this.sigma = sigma;
        this.language = language;
        this.active = active;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

        public FBillItem(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.KEY</code>. 「key」- 账单明细主键
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.KEY</code>. 「key」- 账单明细主键
     */
    @Override
    public FBillItem setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.NAME</code>. 「name」 -  明细名称
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.NAME</code>. 「name」 -  明细名称
     */
    @Override
    public FBillItem setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.CODE</code>. 「code」 - 明细系统代码
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.CODE</code>. 「code」 - 明细系统代码
     */
    @Override
    public FBillItem setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.SERIAL</code>. 「serial」 - 明细编号
     */
    @Override
    public String getSerial() {
        return this.serial;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.SERIAL</code>. 「serial」 - 明细编号
     */
    @Override
    public FBillItem setSerial(String serial) {
        this.serial = serial;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.TYPE</code>. 「type」- 明细类型
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.TYPE</code>. 「type」- 明细类型
     */
    @Override
    public FBillItem setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.STATUS</code>. 「status」- 明细状态
     */
    @Override
    public String getStatus() {
        return this.status;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.STATUS</code>. 「status」- 明细状态
     */
    @Override
    public FBillItem setStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.AMOUNT</code>.
     * 「amount」——价税合计，实际付款结果，有可能父项
     */
    @Override
    public BigDecimal getAmount() {
        return this.amount;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.AMOUNT</code>.
     * 「amount」——价税合计，实际付款结果，有可能父项
     */
    @Override
    public FBillItem setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.COMMENT</code>. 「comment」 - 明细备注
     */
    @Override
    public String getComment() {
        return this.comment;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.COMMENT</code>. 「comment」 - 明细备注
     */
    @Override
    public FBillItem setComment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.MANUAL_NO</code>. 「manualNo」 -
     * 手工单号（线下单号专用）
     */
    @Override
    public String getManualNo() {
        return this.manualNo;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.MANUAL_NO</code>. 「manualNo」 -
     * 手工单号（线下单号专用）
     */
    @Override
    public FBillItem setManualNo(String manualNo) {
        this.manualNo = manualNo;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.PRICE</code>. 「price」- 商品单价
     */
    @Override
    public BigDecimal getPrice() {
        return this.price;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.PRICE</code>. 「price」- 商品单价
     */
    @Override
    public FBillItem setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.QUANTITY</code>. 「quantity」- 商品数量
     */
    @Override
    public Integer getQuantity() {
        return this.quantity;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.QUANTITY</code>. 「quantity」- 商品数量
     */
    @Override
    public FBillItem setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.AMOUNT_TOTAL</code>.
     * 「amountTotal」——总价，理论计算结果
     */
    @Override
    public BigDecimal getAmountTotal() {
        return this.amountTotal;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.AMOUNT_TOTAL</code>.
     * 「amountTotal」——总价，理论计算结果
     */
    @Override
    public FBillItem setAmountTotal(BigDecimal amountTotal) {
        this.amountTotal = amountTotal;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.DELAY</code>. 「delay」——是否S账
     */
    @Override
    public Boolean getDelay() {
        return this.delay;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.DELAY</code>. 「delay」——是否S账
     */
    @Override
    public FBillItem setDelay(Boolean delay) {
        this.delay = delay;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.DELAY_AT</code>.
     * 「delayAt」——S账的最终期限
     */
    @Override
    public LocalDateTime getDelayAt() {
        return this.delayAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.DELAY_AT</code>.
     * 「delayAt」——S账的最终期限
     */
    @Override
    public FBillItem setDelayAt(LocalDateTime delayAt) {
        this.delayAt = delayAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.OP_BY</code>. 「opBy」- 操作人员，关联员工ID
     */
    @Override
    public String getOpBy() {
        return this.opBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.OP_BY</code>. 「opBy」- 操作人员，关联员工ID
     */
    @Override
    public FBillItem setOpBy(String opBy) {
        this.opBy = opBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.OP_NUMBER</code>. 「opNumber」-
     * 操作人员工号
     */
    @Override
    public String getOpNumber() {
        return this.opNumber;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.OP_NUMBER</code>. 「opNumber」-
     * 操作人员工号
     */
    @Override
    public FBillItem setOpNumber(String opNumber) {
        this.opNumber = opNumber;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.OP_SHIFT</code>. 「opShift」-
     * 操作班次（对接排班系统）
     */
    @Override
    public String getOpShift() {
        return this.opShift;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.OP_SHIFT</code>. 「opShift」-
     * 操作班次（对接排班系统）
     */
    @Override
    public FBillItem setOpShift(String opShift) {
        this.opShift = opShift;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.OP_AT</code>. 「opAt」- 操作时间
     */
    @Override
    public LocalDateTime getOpAt() {
        return this.opAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.OP_AT</code>. 「opAt」- 操作时间
     */
    @Override
    public FBillItem setOpAt(LocalDateTime opAt) {
        this.opAt = opAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.OP_TRANSFER</code>. 「opTransfer」-
     * 流转信息描述填写
     */
    @Override
    public String getOpTransfer() {
        return this.opTransfer;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.OP_TRANSFER</code>. 「opTransfer」-
     * 流转信息描述填写
     */
    @Override
    public FBillItem setOpTransfer(String opTransfer) {
        this.opTransfer = opTransfer;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.RELATED_ID</code>. 「relatedId」-
     * 关联ID（保留，原系统存在）
     */
    @Override
    public String getRelatedId() {
        return this.relatedId;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.RELATED_ID</code>. 「relatedId」-
     * 关联ID（保留，原系统存在）
     */
    @Override
    public FBillItem setRelatedId(String relatedId) {
        this.relatedId = relatedId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.SETTLEMENT_ID</code>.
     * 「settlementId」- 结算单ID，该字段有值标识已经结算
     */
    @Override
    public String getSettlementId() {
        return this.settlementId;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.SETTLEMENT_ID</code>.
     * 「settlementId」- 结算单ID，该字段有值标识已经结算
     */
    @Override
    public FBillItem setSettlementId(String settlementId) {
        this.settlementId = settlementId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.BILL_ID</code>. 「billId」- 所属账单ID
     */
    @Override
    public String getBillId() {
        return this.billId;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.BILL_ID</code>. 「billId」- 所属账单ID
     */
    @Override
    public FBillItem setBillId(String billId) {
        this.billId = billId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.SUBJECT_ID</code>. 「subjectId」-
     * 会计科目ID，依赖账单项选择结果
     */
    @Override
    public String getSubjectId() {
        return this.subjectId;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.SUBJECT_ID</code>. 「subjectId」-
     * 会计科目ID，依赖账单项选择结果
     */
    @Override
    public FBillItem setSubjectId(String subjectId) {
        this.subjectId = subjectId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.PAY_TERM_ID</code>. 「payTermId」-
     * 账单项ID
     */
    @Override
    public String getPayTermId() {
        return this.payTermId;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.PAY_TERM_ID</code>. 「payTermId」-
     * 账单项ID
     */
    @Override
    public FBillItem setPayTermId(String payTermId) {
        this.payTermId = payTermId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public FBillItem setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public FBillItem setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public FBillItem setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public FBillItem setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public FBillItem setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public FBillItem setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public FBillItem setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public FBillItem setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("FBillItem (");

        sb.append(key);
        sb.append(", ").append(name);
        sb.append(", ").append(code);
        sb.append(", ").append(serial);
        sb.append(", ").append(type);
        sb.append(", ").append(status);
        sb.append(", ").append(amount);
        sb.append(", ").append(comment);
        sb.append(", ").append(manualNo);
        sb.append(", ").append(price);
        sb.append(", ").append(quantity);
        sb.append(", ").append(amountTotal);
        sb.append(", ").append(delay);
        sb.append(", ").append(delayAt);
        sb.append(", ").append(opBy);
        sb.append(", ").append(opNumber);
        sb.append(", ").append(opShift);
        sb.append(", ").append(opAt);
        sb.append(", ").append(opTransfer);
        sb.append(", ").append(relatedId);
        sb.append(", ").append(settlementId);
        sb.append(", ").append(billId);
        sb.append(", ").append(subjectId);
        sb.append(", ").append(payTermId);
        sb.append(", ").append(sigma);
        sb.append(", ").append(language);
        sb.append(", ").append(active);
        sb.append(", ").append(metadata);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(createdBy);
        sb.append(", ").append(updatedAt);
        sb.append(", ").append(updatedBy);

        sb.append(")");
        return sb.toString();
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
}
