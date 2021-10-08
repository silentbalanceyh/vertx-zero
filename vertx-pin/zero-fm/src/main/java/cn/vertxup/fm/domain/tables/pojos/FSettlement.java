/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.fm.domain.tables.pojos;


import cn.vertxup.fm.domain.tables.interfaces.IFSettlement;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FSettlement implements VertxPojo, IFSettlement {

    private static final long serialVersionUID = 1L;

    private String        key;
    private String        code;
    private String        serial;
    private BigDecimal    amount;
    private String        comment;
    private Byte          rounded;
    private Boolean       finished;
    private LocalDateTime finishedAt;
    private String        signName;
    private String        signMobile;
    private String        orderId;
    private String        sigma;
    private String        language;
    private Boolean       active;
    private String        metadata;
    private LocalDateTime createdAt;
    private String        createdBy;
    private LocalDateTime updatedAt;
    private String        updatedBy;

    public FSettlement() {}

    public FSettlement(IFSettlement value) {
        this.key = value.getKey();
        this.code = value.getCode();
        this.serial = value.getSerial();
        this.amount = value.getAmount();
        this.comment = value.getComment();
        this.rounded = value.getRounded();
        this.finished = value.getFinished();
        this.finishedAt = value.getFinishedAt();
        this.signName = value.getSignName();
        this.signMobile = value.getSignMobile();
        this.orderId = value.getOrderId();
        this.sigma = value.getSigma();
        this.language = value.getLanguage();
        this.active = value.getActive();
        this.metadata = value.getMetadata();
        this.createdAt = value.getCreatedAt();
        this.createdBy = value.getCreatedBy();
        this.updatedAt = value.getUpdatedAt();
        this.updatedBy = value.getUpdatedBy();
    }

    public FSettlement(
        String        key,
        String        code,
        String        serial,
        BigDecimal    amount,
        String        comment,
        Byte          rounded,
        Boolean       finished,
        LocalDateTime finishedAt,
        String        signName,
        String        signMobile,
        String        orderId,
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
        this.code = code;
        this.serial = serial;
        this.amount = amount;
        this.comment = comment;
        this.rounded = rounded;
        this.finished = finished;
        this.finishedAt = finishedAt;
        this.signName = signName;
        this.signMobile = signMobile;
        this.orderId = orderId;
        this.sigma = sigma;
        this.language = language;
        this.active = active;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

        public FSettlement(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.KEY</code>. 「key」- 结算单主键
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.KEY</code>. 「key」- 结算单主键
     */
    @Override
    public FSettlement setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.CODE</code>. 「code」 - 结算单编号
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.CODE</code>. 「code」 - 结算单编号
     */
    @Override
    public FSettlement setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.SERIAL</code>. 「serial」 - 结算单据号
     */
    @Override
    public String getSerial() {
        return this.serial;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.SERIAL</code>. 「serial」 - 结算单据号
     */
    @Override
    public FSettlement setSerial(String serial) {
        this.serial = serial;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.AMOUNT</code>.
     * 「amount」——价税合计，所有明细对应的实际结算金额
     */
    @Override
    public BigDecimal getAmount() {
        return this.amount;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.AMOUNT</code>.
     * 「amount」——价税合计，所有明细对应的实际结算金额
     */
    @Override
    public FSettlement setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.COMMENT</code>. 「comment」 -
     * 结算单备注
     */
    @Override
    public String getComment() {
        return this.comment;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.COMMENT</code>. 「comment」 -
     * 结算单备注
     */
    @Override
    public FSettlement setComment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.ROUNDED</code>. 「rounded」抹零方式 =
     * true：四舍五入、round = false：零头舍掉,round,IS_ROUND
     */
    @Override
    public Byte getRounded() {
        return this.rounded;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.ROUNDED</code>. 「rounded」抹零方式 =
     * true：四舍五入、round = false：零头舍掉,round,IS_ROUND
     */
    @Override
    public FSettlement setRounded(Byte rounded) {
        this.rounded = rounded;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.FINISHED</code>. 「finished」-
     * 是否完成
     */
    @Override
    public Boolean getFinished() {
        return this.finished;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.FINISHED</code>. 「finished」-
     * 是否完成
     */
    @Override
    public FSettlement setFinished(Boolean finished) {
        this.finished = finished;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.FINISHED_AT</code>. 「createdAt」-
     * 完成时间
     */
    @Override
    public LocalDateTime getFinishedAt() {
        return this.finishedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.FINISHED_AT</code>. 「createdAt」-
     * 完成时间
     */
    @Override
    public FSettlement setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.SIGN_NAME</code>.
     * 「signName」签单人姓名
     */
    @Override
    public String getSignName() {
        return this.signName;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.SIGN_NAME</code>.
     * 「signName」签单人姓名
     */
    @Override
    public FSettlement setSignName(String signName) {
        this.signName = signName;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.SIGN_MOBILE</code>.
     * 「signMobile」签单人电话
     */
    @Override
    public String getSignMobile() {
        return this.signMobile;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.SIGN_MOBILE</code>.
     * 「signMobile」签单人电话
     */
    @Override
    public FSettlement setSignMobile(String signMobile) {
        this.signMobile = signMobile;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.ORDER_ID</code>. 「orderId」-
     * 预授权所属订单ID
     */
    @Override
    public String getOrderId() {
        return this.orderId;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.ORDER_ID</code>. 「orderId」-
     * 预授权所属订单ID
     */
    @Override
    public FSettlement setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public FSettlement setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public FSettlement setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public FSettlement setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public FSettlement setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public FSettlement setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public FSettlement setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public FSettlement setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_SETTLEMENT.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_SETTLEMENT.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public FSettlement setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("FSettlement (");

        sb.append(key);
        sb.append(", ").append(code);
        sb.append(", ").append(serial);
        sb.append(", ").append(amount);
        sb.append(", ").append(comment);
        sb.append(", ").append(rounded);
        sb.append(", ").append(finished);
        sb.append(", ").append(finishedAt);
        sb.append(", ").append(signName);
        sb.append(", ").append(signMobile);
        sb.append(", ").append(orderId);
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
    public void from(IFSettlement from) {
        setKey(from.getKey());
        setCode(from.getCode());
        setSerial(from.getSerial());
        setAmount(from.getAmount());
        setComment(from.getComment());
        setRounded(from.getRounded());
        setFinished(from.getFinished());
        setFinishedAt(from.getFinishedAt());
        setSignName(from.getSignName());
        setSignMobile(from.getSignMobile());
        setOrderId(from.getOrderId());
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
    public <E extends IFSettlement> E into(E into) {
        into.from(this);
        return into;
    }
}
