/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.psi.domain.tables.pojos;


import cn.vertxup.psi.domain.tables.interfaces.IPInTicket;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PInTicket implements VertxPojo, IPInTicket {

    private static final long serialVersionUID = 1L;

    private String        key;
    private String        serial;
    private String        type;
    private String        typeBusiness;
    private String        status;
    private String        whId;
    private String        customerId;
    private String        customerInfo;
    private LocalDateTime payedAt;
    private Integer       payedDay;
    private LocalDateTime opAt;
    private String        opBy;
    private String        opDept;
    private String        tags;
    private String        comment;
    private String        source;
    private BigDecimal    taxAmount;
    private BigDecimal    amount;
    private BigDecimal    amountTotal;
    private String        approvedBy;
    private LocalDateTime approvedAt;
    private String        toId;
    private String        toAddress;
    private String        fromId;
    private String        fromAddress;
    private String        currencyId;
    private String        companyId;
    private Boolean       active;
    private String        sigma;
    private String        metadata;
    private String        language;
    private LocalDateTime createdAt;
    private String        createdBy;
    private LocalDateTime updatedAt;
    private String        updatedBy;

    public PInTicket() {}

    public PInTicket(IPInTicket value) {
        this.key = value.getKey();
        this.serial = value.getSerial();
        this.type = value.getType();
        this.typeBusiness = value.getTypeBusiness();
        this.status = value.getStatus();
        this.whId = value.getWhId();
        this.customerId = value.getCustomerId();
        this.customerInfo = value.getCustomerInfo();
        this.payedAt = value.getPayedAt();
        this.payedDay = value.getPayedDay();
        this.opAt = value.getOpAt();
        this.opBy = value.getOpBy();
        this.opDept = value.getOpDept();
        this.tags = value.getTags();
        this.comment = value.getComment();
        this.source = value.getSource();
        this.taxAmount = value.getTaxAmount();
        this.amount = value.getAmount();
        this.amountTotal = value.getAmountTotal();
        this.approvedBy = value.getApprovedBy();
        this.approvedAt = value.getApprovedAt();
        this.toId = value.getToId();
        this.toAddress = value.getToAddress();
        this.fromId = value.getFromId();
        this.fromAddress = value.getFromAddress();
        this.currencyId = value.getCurrencyId();
        this.companyId = value.getCompanyId();
        this.active = value.getActive();
        this.sigma = value.getSigma();
        this.metadata = value.getMetadata();
        this.language = value.getLanguage();
        this.createdAt = value.getCreatedAt();
        this.createdBy = value.getCreatedBy();
        this.updatedAt = value.getUpdatedAt();
        this.updatedBy = value.getUpdatedBy();
    }

    public PInTicket(
        String        key,
        String        serial,
        String        type,
        String        typeBusiness,
        String        status,
        String        whId,
        String        customerId,
        String        customerInfo,
        LocalDateTime payedAt,
        Integer       payedDay,
        LocalDateTime opAt,
        String        opBy,
        String        opDept,
        String        tags,
        String        comment,
        String        source,
        BigDecimal    taxAmount,
        BigDecimal    amount,
        BigDecimal    amountTotal,
        String        approvedBy,
        LocalDateTime approvedAt,
        String        toId,
        String        toAddress,
        String        fromId,
        String        fromAddress,
        String        currencyId,
        String        companyId,
        Boolean       active,
        String        sigma,
        String        metadata,
        String        language,
        LocalDateTime createdAt,
        String        createdBy,
        LocalDateTime updatedAt,
        String        updatedBy
    ) {
        this.key = key;
        this.serial = serial;
        this.type = type;
        this.typeBusiness = typeBusiness;
        this.status = status;
        this.whId = whId;
        this.customerId = customerId;
        this.customerInfo = customerInfo;
        this.payedAt = payedAt;
        this.payedDay = payedDay;
        this.opAt = opAt;
        this.opBy = opBy;
        this.opDept = opDept;
        this.tags = tags;
        this.comment = comment;
        this.source = source;
        this.taxAmount = taxAmount;
        this.amount = amount;
        this.amountTotal = amountTotal;
        this.approvedBy = approvedBy;
        this.approvedAt = approvedAt;
        this.toId = toId;
        this.toAddress = toAddress;
        this.fromId = fromId;
        this.fromAddress = fromAddress;
        this.currencyId = currencyId;
        this.companyId = companyId;
        this.active = active;
        this.sigma = sigma;
        this.metadata = metadata;
        this.language = language;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

        public PInTicket(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.KEY</code>. 「key」- 入库单主键
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.KEY</code>. 「key」- 入库单主键
     */
    @Override
    public PInTicket setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.SERIAL</code>. 「serial」-
     * 入库单号（系统可用，直接计算）
     */
    @Override
    public String getSerial() {
        return this.serial;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.SERIAL</code>. 「serial」-
     * 入库单号（系统可用，直接计算）
     */
    @Override
    public PInTicket setSerial(String serial) {
        this.serial = serial;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.TYPE</code>. 「type」- 单据类型
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.TYPE</code>. 「type」- 单据类型
     */
    @Override
    public PInTicket setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.TYPE_BUSINESS</code>.
     * 「typeBusiness」- 业务类型
     */
    @Override
    public String getTypeBusiness() {
        return this.typeBusiness;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.TYPE_BUSINESS</code>.
     * 「typeBusiness」- 业务类型
     */
    @Override
    public PInTicket setTypeBusiness(String typeBusiness) {
        this.typeBusiness = typeBusiness;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.STATUS</code>. 「status」- 订单状态
     */
    @Override
    public String getStatus() {
        return this.status;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.STATUS</code>. 「status」- 订单状态
     */
    @Override
    public PInTicket setStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.WH_ID</code>. 「whId」- 建议入库仓库
     */
    @Override
    public String getWhId() {
        return this.whId;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.WH_ID</code>. 「whId」- 建议入库仓库
     */
    @Override
    public PInTicket setWhId(String whId) {
        this.whId = whId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.CUSTOMER_ID</code>. 「customerId」-
     * 实际供应商
     */
    @Override
    public String getCustomerId() {
        return this.customerId;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.CUSTOMER_ID</code>. 「customerId」-
     * 实际供应商
     */
    @Override
    public PInTicket setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.CUSTOMER_INFO</code>.
     * 「customerInfo」- 实际供应商联系信息
     */
    @Override
    public String getCustomerInfo() {
        return this.customerInfo;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.CUSTOMER_INFO</code>.
     * 「customerInfo」- 实际供应商联系信息
     */
    @Override
    public PInTicket setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.PAYED_AT</code>. 「payedAt」- 结算日期
     */
    @Override
    public LocalDateTime getPayedAt() {
        return this.payedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.PAYED_AT</code>. 「payedAt」- 结算日期
     */
    @Override
    public PInTicket setPayedAt(LocalDateTime payedAt) {
        this.payedAt = payedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.PAYED_DAY</code>. 「payedDay」-
     * 采购期限
     */
    @Override
    public Integer getPayedDay() {
        return this.payedDay;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.PAYED_DAY</code>. 「payedDay」-
     * 采购期限
     */
    @Override
    public PInTicket setPayedDay(Integer payedDay) {
        this.payedDay = payedDay;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.OP_AT</code>. 「opAt」- 单据日期
     */
    @Override
    public LocalDateTime getOpAt() {
        return this.opAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.OP_AT</code>. 「opAt」- 单据日期
     */
    @Override
    public PInTicket setOpAt(LocalDateTime opAt) {
        this.opAt = opAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.OP_BY</code>. 「opBy」- 业务员
     */
    @Override
    public String getOpBy() {
        return this.opBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.OP_BY</code>. 「opBy」- 业务员
     */
    @Override
    public PInTicket setOpBy(String opBy) {
        this.opBy = opBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.OP_DEPT</code>. 「opDept」- 业务部门
     */
    @Override
    public String getOpDept() {
        return this.opDept;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.OP_DEPT</code>. 「opDept」- 业务部门
     */
    @Override
    public PInTicket setOpDept(String opDept) {
        this.opDept = opDept;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.TAGS</code>. 「tags」- 单据标签
     */
    @Override
    public String getTags() {
        return this.tags;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.TAGS</code>. 「tags」- 单据标签
     */
    @Override
    public PInTicket setTags(String tags) {
        this.tags = tags;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.COMMENT</code>. 「comment」- 单据备注
     */
    @Override
    public String getComment() {
        return this.comment;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.COMMENT</code>. 「comment」- 单据备注
     */
    @Override
    public PInTicket setComment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.SOURCE</code>. 「source」- 单据来源
     */
    @Override
    public String getSource() {
        return this.source;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.SOURCE</code>. 「source」- 单据来源
     */
    @Override
    public PInTicket setSource(String source) {
        this.source = source;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.TAX_AMOUNT</code>. 「taxAmount」-
     * 税额
     */
    @Override
    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.TAX_AMOUNT</code>. 「taxAmount」-
     * 税额
     */
    @Override
    public PInTicket setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.AMOUNT</code>. 「amount」- 入库单总额
     */
    @Override
    public BigDecimal getAmount() {
        return this.amount;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.AMOUNT</code>. 「amount」- 入库单总额
     */
    @Override
    public PInTicket setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.AMOUNT_TOTAL</code>.
     * 「amountTotal」- 税价合计
     */
    @Override
    public BigDecimal getAmountTotal() {
        return this.amountTotal;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.AMOUNT_TOTAL</code>.
     * 「amountTotal」- 税价合计
     */
    @Override
    public PInTicket setAmountTotal(BigDecimal amountTotal) {
        this.amountTotal = amountTotal;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.APPROVED_BY</code>. 「approvedBy」-
     * 审核人
     */
    @Override
    public String getApprovedBy() {
        return this.approvedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.APPROVED_BY</code>. 「approvedBy」-
     * 审核人
     */
    @Override
    public PInTicket setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.APPROVED_AT</code>. 「approvedAt」-
     * 审核时间
     */
    @Override
    public LocalDateTime getApprovedAt() {
        return this.approvedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.APPROVED_AT</code>. 「approvedAt」-
     * 审核时间
     */
    @Override
    public PInTicket setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.TO_ID</code>. 「toId」- 收货地址ID
     */
    @Override
    public String getToId() {
        return this.toId;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.TO_ID</code>. 「toId」- 收货地址ID
     */
    @Override
    public PInTicket setToId(String toId) {
        this.toId = toId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.TO_ADDRESS</code>. 「toAddress」-
     * 收货地址
     */
    @Override
    public String getToAddress() {
        return this.toAddress;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.TO_ADDRESS</code>. 「toAddress」-
     * 收货地址
     */
    @Override
    public PInTicket setToAddress(String toAddress) {
        this.toAddress = toAddress;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.FROM_ID</code>. 「fromId」- 发货地址ID
     */
    @Override
    public String getFromId() {
        return this.fromId;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.FROM_ID</code>. 「fromId」- 发货地址ID
     */
    @Override
    public PInTicket setFromId(String fromId) {
        this.fromId = fromId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.FROM_ADDRESS</code>.
     * 「fromAddress」- 发货地址
     */
    @Override
    public String getFromAddress() {
        return this.fromAddress;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.FROM_ADDRESS</code>.
     * 「fromAddress」- 发货地址
     */
    @Override
    public PInTicket setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.CURRENCY_ID</code>. 「currencyId」-
     * 币种
     */
    @Override
    public String getCurrencyId() {
        return this.currencyId;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.CURRENCY_ID</code>. 「currencyId」-
     * 币种
     */
    @Override
    public PInTicket setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.COMPANY_ID</code>. 「companyId」-
     * 所属公司
     */
    @Override
    public String getCompanyId() {
        return this.companyId;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.COMPANY_ID</code>. 「companyId」-
     * 所属公司
     */
    @Override
    public PInTicket setCompanyId(String companyId) {
        this.companyId = companyId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public PInTicket setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public PInTicket setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public PInTicket setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public PInTicket setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public PInTicket setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public PInTicket setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public PInTicket setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.P_IN_TICKET.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.P_IN_TICKET.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public PInTicket setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("PInTicket (");

        sb.append(key);
        sb.append(", ").append(serial);
        sb.append(", ").append(type);
        sb.append(", ").append(typeBusiness);
        sb.append(", ").append(status);
        sb.append(", ").append(whId);
        sb.append(", ").append(customerId);
        sb.append(", ").append(customerInfo);
        sb.append(", ").append(payedAt);
        sb.append(", ").append(payedDay);
        sb.append(", ").append(opAt);
        sb.append(", ").append(opBy);
        sb.append(", ").append(opDept);
        sb.append(", ").append(tags);
        sb.append(", ").append(comment);
        sb.append(", ").append(source);
        sb.append(", ").append(taxAmount);
        sb.append(", ").append(amount);
        sb.append(", ").append(amountTotal);
        sb.append(", ").append(approvedBy);
        sb.append(", ").append(approvedAt);
        sb.append(", ").append(toId);
        sb.append(", ").append(toAddress);
        sb.append(", ").append(fromId);
        sb.append(", ").append(fromAddress);
        sb.append(", ").append(currencyId);
        sb.append(", ").append(companyId);
        sb.append(", ").append(active);
        sb.append(", ").append(sigma);
        sb.append(", ").append(metadata);
        sb.append(", ").append(language);
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
    public void from(IPInTicket from) {
        setKey(from.getKey());
        setSerial(from.getSerial());
        setType(from.getType());
        setTypeBusiness(from.getTypeBusiness());
        setStatus(from.getStatus());
        setWhId(from.getWhId());
        setCustomerId(from.getCustomerId());
        setCustomerInfo(from.getCustomerInfo());
        setPayedAt(from.getPayedAt());
        setPayedDay(from.getPayedDay());
        setOpAt(from.getOpAt());
        setOpBy(from.getOpBy());
        setOpDept(from.getOpDept());
        setTags(from.getTags());
        setComment(from.getComment());
        setSource(from.getSource());
        setTaxAmount(from.getTaxAmount());
        setAmount(from.getAmount());
        setAmountTotal(from.getAmountTotal());
        setApprovedBy(from.getApprovedBy());
        setApprovedAt(from.getApprovedAt());
        setToId(from.getToId());
        setToAddress(from.getToAddress());
        setFromId(from.getFromId());
        setFromAddress(from.getFromAddress());
        setCurrencyId(from.getCurrencyId());
        setCompanyId(from.getCompanyId());
        setActive(from.getActive());
        setSigma(from.getSigma());
        setMetadata(from.getMetadata());
        setLanguage(from.getLanguage());
        setCreatedAt(from.getCreatedAt());
        setCreatedBy(from.getCreatedBy());
        setUpdatedAt(from.getUpdatedAt());
        setUpdatedBy(from.getUpdatedBy());
    }

    @Override
    public <E extends IPInTicket> E into(E into) {
        into.from(this);
        return into;
    }
}
