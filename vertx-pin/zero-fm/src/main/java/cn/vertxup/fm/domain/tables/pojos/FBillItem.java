/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.fm.domain.tables.pojos;


import cn.vertxup.fm.domain.tables.interfaces.IFBillItem;
import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FBillItem implements VertxPojo, IFBillItem {

    private static final long serialVersionUID = 1L;

    private String key;
    private String name;
    private String code;
    private String serial;
    private Boolean income;
    private String type;
    private String status;
    private BigDecimal amount;
    private String comment;
    private String manualNo;
    private String unit;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal amountTotal;
    private String opBy;
    private String opNumber;
    private String opShift;
    private LocalDateTime opAt;
    private String relatedId;
    private String settlementId;
    private String billId;
    private String subjectId;
    private String payTermId;
    private String sigma;
    private String language;
    private Boolean active;
    private String metadata;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public FBillItem() {}

    public FBillItem(IFBillItem value) {
        this.key = value.getKey();
        this.name = value.getName();
        this.code = value.getCode();
        this.serial = value.getSerial();
        this.income = value.getIncome();
        this.type = value.getType();
        this.status = value.getStatus();
        this.amount = value.getAmount();
        this.comment = value.getComment();
        this.manualNo = value.getManualNo();
        this.unit = value.getUnit();
        this.price = value.getPrice();
        this.quantity = value.getQuantity();
        this.amountTotal = value.getAmountTotal();
        this.opBy = value.getOpBy();
        this.opNumber = value.getOpNumber();
        this.opShift = value.getOpShift();
        this.opAt = value.getOpAt();
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
        String key,
        String name,
        String code,
        String serial,
        Boolean income,
        String type,
        String status,
        BigDecimal amount,
        String comment,
        String manualNo,
        String unit,
        BigDecimal price,
        Integer quantity,
        BigDecimal amountTotal,
        String opBy,
        String opNumber,
        String opShift,
        LocalDateTime opAt,
        String relatedId,
        String settlementId,
        String billId,
        String subjectId,
        String payTermId,
        String sigma,
        String language,
        Boolean active,
        String metadata,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy
    ) {
        this.key = key;
        this.name = name;
        this.code = code;
        this.serial = serial;
        this.income = income;
        this.type = type;
        this.status = status;
        this.amount = amount;
        this.comment = comment;
        this.manualNo = manualNo;
        this.unit = unit;
        this.price = price;
        this.quantity = quantity;
        this.amountTotal = amountTotal;
        this.opBy = opBy;
        this.opNumber = opNumber;
        this.opShift = opShift;
        this.opAt = opAt;
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
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.INCOME</code>. 「income」- true =
     * 消费类，false = 付款类
     */
    @Override
    public Boolean getIncome() {
        return this.income;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.INCOME</code>. 「income」- true =
     * 消费类，false = 付款类
     */
    @Override
    public FBillItem setIncome(Boolean income) {
        this.income = income;
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
     * Getter for <code>DB_ETERNAL.F_BILL_ITEM.UNIT</code>. 「unit」- 计量单位
     */
    @Override
    public String getUnit() {
        return this.unit;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_BILL_ITEM.UNIT</code>. 「unit」- 计量单位
     */
    @Override
    public FBillItem setUnit(String unit) {
        this.unit = unit;
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final FBillItem other = (FBillItem) obj;
        if (this.key == null) {
            if (other.key != null)
                return false;
        }
        else if (!this.key.equals(other.key))
            return false;
        if (this.name == null) {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        if (this.code == null) {
            if (other.code != null)
                return false;
        }
        else if (!this.code.equals(other.code))
            return false;
        if (this.serial == null) {
            if (other.serial != null)
                return false;
        }
        else if (!this.serial.equals(other.serial))
            return false;
        if (this.income == null) {
            if (other.income != null)
                return false;
        }
        else if (!this.income.equals(other.income))
            return false;
        if (this.type == null) {
            if (other.type != null)
                return false;
        }
        else if (!this.type.equals(other.type))
            return false;
        if (this.status == null) {
            if (other.status != null)
                return false;
        }
        else if (!this.status.equals(other.status))
            return false;
        if (this.amount == null) {
            if (other.amount != null)
                return false;
        }
        else if (!this.amount.equals(other.amount))
            return false;
        if (this.comment == null) {
            if (other.comment != null)
                return false;
        }
        else if (!this.comment.equals(other.comment))
            return false;
        if (this.manualNo == null) {
            if (other.manualNo != null)
                return false;
        }
        else if (!this.manualNo.equals(other.manualNo))
            return false;
        if (this.unit == null) {
            if (other.unit != null)
                return false;
        }
        else if (!this.unit.equals(other.unit))
            return false;
        if (this.price == null) {
            if (other.price != null)
                return false;
        }
        else if (!this.price.equals(other.price))
            return false;
        if (this.quantity == null) {
            if (other.quantity != null)
                return false;
        }
        else if (!this.quantity.equals(other.quantity))
            return false;
        if (this.amountTotal == null) {
            if (other.amountTotal != null)
                return false;
        }
        else if (!this.amountTotal.equals(other.amountTotal))
            return false;
        if (this.opBy == null) {
            if (other.opBy != null)
                return false;
        }
        else if (!this.opBy.equals(other.opBy))
            return false;
        if (this.opNumber == null) {
            if (other.opNumber != null)
                return false;
        }
        else if (!this.opNumber.equals(other.opNumber))
            return false;
        if (this.opShift == null) {
            if (other.opShift != null)
                return false;
        }
        else if (!this.opShift.equals(other.opShift))
            return false;
        if (this.opAt == null) {
            if (other.opAt != null)
                return false;
        }
        else if (!this.opAt.equals(other.opAt))
            return false;
        if (this.relatedId == null) {
            if (other.relatedId != null)
                return false;
        }
        else if (!this.relatedId.equals(other.relatedId))
            return false;
        if (this.settlementId == null) {
            if (other.settlementId != null)
                return false;
        }
        else if (!this.settlementId.equals(other.settlementId))
            return false;
        if (this.billId == null) {
            if (other.billId != null)
                return false;
        }
        else if (!this.billId.equals(other.billId))
            return false;
        if (this.subjectId == null) {
            if (other.subjectId != null)
                return false;
        }
        else if (!this.subjectId.equals(other.subjectId))
            return false;
        if (this.payTermId == null) {
            if (other.payTermId != null)
                return false;
        }
        else if (!this.payTermId.equals(other.payTermId))
            return false;
        if (this.sigma == null) {
            if (other.sigma != null)
                return false;
        }
        else if (!this.sigma.equals(other.sigma))
            return false;
        if (this.language == null) {
            if (other.language != null)
                return false;
        }
        else if (!this.language.equals(other.language))
            return false;
        if (this.active == null) {
            if (other.active != null)
                return false;
        }
        else if (!this.active.equals(other.active))
            return false;
        if (this.metadata == null) {
            if (other.metadata != null)
                return false;
        }
        else if (!this.metadata.equals(other.metadata))
            return false;
        if (this.createdAt == null) {
            if (other.createdAt != null)
                return false;
        }
        else if (!this.createdAt.equals(other.createdAt))
            return false;
        if (this.createdBy == null) {
            if (other.createdBy != null)
                return false;
        }
        else if (!this.createdBy.equals(other.createdBy))
            return false;
        if (this.updatedAt == null) {
            if (other.updatedAt != null)
                return false;
        }
        else if (!this.updatedAt.equals(other.updatedAt))
            return false;
        if (this.updatedBy == null) {
            if (other.updatedBy != null)
                return false;
        }
        else if (!this.updatedBy.equals(other.updatedBy))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.key == null) ? 0 : this.key.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.code == null) ? 0 : this.code.hashCode());
        result = prime * result + ((this.serial == null) ? 0 : this.serial.hashCode());
        result = prime * result + ((this.income == null) ? 0 : this.income.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        result = prime * result + ((this.status == null) ? 0 : this.status.hashCode());
        result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
        result = prime * result + ((this.comment == null) ? 0 : this.comment.hashCode());
        result = prime * result + ((this.manualNo == null) ? 0 : this.manualNo.hashCode());
        result = prime * result + ((this.unit == null) ? 0 : this.unit.hashCode());
        result = prime * result + ((this.price == null) ? 0 : this.price.hashCode());
        result = prime * result + ((this.quantity == null) ? 0 : this.quantity.hashCode());
        result = prime * result + ((this.amountTotal == null) ? 0 : this.amountTotal.hashCode());
        result = prime * result + ((this.opBy == null) ? 0 : this.opBy.hashCode());
        result = prime * result + ((this.opNumber == null) ? 0 : this.opNumber.hashCode());
        result = prime * result + ((this.opShift == null) ? 0 : this.opShift.hashCode());
        result = prime * result + ((this.opAt == null) ? 0 : this.opAt.hashCode());
        result = prime * result + ((this.relatedId == null) ? 0 : this.relatedId.hashCode());
        result = prime * result + ((this.settlementId == null) ? 0 : this.settlementId.hashCode());
        result = prime * result + ((this.billId == null) ? 0 : this.billId.hashCode());
        result = prime * result + ((this.subjectId == null) ? 0 : this.subjectId.hashCode());
        result = prime * result + ((this.payTermId == null) ? 0 : this.payTermId.hashCode());
        result = prime * result + ((this.sigma == null) ? 0 : this.sigma.hashCode());
        result = prime * result + ((this.language == null) ? 0 : this.language.hashCode());
        result = prime * result + ((this.active == null) ? 0 : this.active.hashCode());
        result = prime * result + ((this.metadata == null) ? 0 : this.metadata.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        result = prime * result + ((this.createdBy == null) ? 0 : this.createdBy.hashCode());
        result = prime * result + ((this.updatedAt == null) ? 0 : this.updatedAt.hashCode());
        result = prime * result + ((this.updatedBy == null) ? 0 : this.updatedBy.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("FBillItem (");

        sb.append(key);
        sb.append(", ").append(name);
        sb.append(", ").append(code);
        sb.append(", ").append(serial);
        sb.append(", ").append(income);
        sb.append(", ").append(type);
        sb.append(", ").append(status);
        sb.append(", ").append(amount);
        sb.append(", ").append(comment);
        sb.append(", ").append(manualNo);
        sb.append(", ").append(unit);
        sb.append(", ").append(price);
        sb.append(", ").append(quantity);
        sb.append(", ").append(amountTotal);
        sb.append(", ").append(opBy);
        sb.append(", ").append(opNumber);
        sb.append(", ").append(opShift);
        sb.append(", ").append(opAt);
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
        setIncome(from.getIncome());
        setType(from.getType());
        setStatus(from.getStatus());
        setAmount(from.getAmount());
        setComment(from.getComment());
        setManualNo(from.getManualNo());
        setUnit(from.getUnit());
        setPrice(from.getPrice());
        setQuantity(from.getQuantity());
        setAmountTotal(from.getAmountTotal());
        setOpBy(from.getOpBy());
        setOpNumber(from.getOpNumber());
        setOpShift(from.getOpShift());
        setOpAt(from.getOpAt());
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
