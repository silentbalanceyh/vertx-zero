/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.fm.domain.tables.pojos;


import cn.vertxup.fm.domain.tables.interfaces.IFCurrency;
import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FCurrency implements VertxPojo, IFCurrency {

    private static final long serialVersionUID = 1L;

    private String key;
    private String name;
    private String code;
    private String comment;
    private Integer digitAmount;
    private Integer digitPrice;
    private String sigma;
    private String language;
    private Boolean active;
    private String metadata;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public FCurrency() {}

    public FCurrency(IFCurrency value) {
        this.key = value.getKey();
        this.name = value.getName();
        this.code = value.getCode();
        this.comment = value.getComment();
        this.digitAmount = value.getDigitAmount();
        this.digitPrice = value.getDigitPrice();
        this.sigma = value.getSigma();
        this.language = value.getLanguage();
        this.active = value.getActive();
        this.metadata = value.getMetadata();
        this.createdAt = value.getCreatedAt();
        this.createdBy = value.getCreatedBy();
        this.updatedAt = value.getUpdatedAt();
        this.updatedBy = value.getUpdatedBy();
    }

    public FCurrency(
        String key,
        String name,
        String code,
        String comment,
        Integer digitAmount,
        Integer digitPrice,
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
        this.comment = comment;
        this.digitAmount = digitAmount;
        this.digitPrice = digitPrice;
        this.sigma = sigma;
        this.language = language;
        this.active = active;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

        public FCurrency(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.F_CURRENCY.KEY</code>. 「key」- 币种主键
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_CURRENCY.KEY</code>. 「key」- 币种主键
     */
    @Override
    public FCurrency setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_CURRENCY.NAME</code>. 「name」- 币种名称
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_CURRENCY.NAME</code>. 「name」- 币种名称
     */
    @Override
    public FCurrency setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_CURRENCY.CODE</code>. 「code」- 币种编码
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_CURRENCY.CODE</code>. 「code」- 币种编码
     */
    @Override
    public FCurrency setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_CURRENCY.COMMENT</code>. 「comment」 - 币种备注
     */
    @Override
    public String getComment() {
        return this.comment;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_CURRENCY.COMMENT</code>. 「comment」 - 币种备注
     */
    @Override
    public FCurrency setComment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_CURRENCY.DIGIT_AMOUNT</code>.
     * 「digitAmount」- 金额小数位数
     */
    @Override
    public Integer getDigitAmount() {
        return this.digitAmount;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_CURRENCY.DIGIT_AMOUNT</code>.
     * 「digitAmount」- 金额小数位数
     */
    @Override
    public FCurrency setDigitAmount(Integer digitAmount) {
        this.digitAmount = digitAmount;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_CURRENCY.DIGIT_PRICE</code>. 「digitPrice」-
     * 单价小数位数
     */
    @Override
    public Integer getDigitPrice() {
        return this.digitPrice;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_CURRENCY.DIGIT_PRICE</code>. 「digitPrice」-
     * 单价小数位数
     */
    @Override
    public FCurrency setDigitPrice(Integer digitPrice) {
        this.digitPrice = digitPrice;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_CURRENCY.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_CURRENCY.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public FCurrency setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_CURRENCY.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_CURRENCY.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public FCurrency setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_CURRENCY.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_CURRENCY.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public FCurrency setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_CURRENCY.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_CURRENCY.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public FCurrency setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_CURRENCY.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_CURRENCY.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public FCurrency setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_CURRENCY.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_CURRENCY.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public FCurrency setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_CURRENCY.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_CURRENCY.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public FCurrency setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.F_CURRENCY.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.F_CURRENCY.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public FCurrency setUpdatedBy(String updatedBy) {
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
        final FCurrency other = (FCurrency) obj;
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
        if (this.comment == null) {
            if (other.comment != null)
                return false;
        }
        else if (!this.comment.equals(other.comment))
            return false;
        if (this.digitAmount == null) {
            if (other.digitAmount != null)
                return false;
        }
        else if (!this.digitAmount.equals(other.digitAmount))
            return false;
        if (this.digitPrice == null) {
            if (other.digitPrice != null)
                return false;
        }
        else if (!this.digitPrice.equals(other.digitPrice))
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
        result = prime * result + ((this.comment == null) ? 0 : this.comment.hashCode());
        result = prime * result + ((this.digitAmount == null) ? 0 : this.digitAmount.hashCode());
        result = prime * result + ((this.digitPrice == null) ? 0 : this.digitPrice.hashCode());
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
        StringBuilder sb = new StringBuilder("FCurrency (");

        sb.append(key);
        sb.append(", ").append(name);
        sb.append(", ").append(code);
        sb.append(", ").append(comment);
        sb.append(", ").append(digitAmount);
        sb.append(", ").append(digitPrice);
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
    public void from(IFCurrency from) {
        setKey(from.getKey());
        setName(from.getName());
        setCode(from.getCode());
        setComment(from.getComment());
        setDigitAmount(from.getDigitAmount());
        setDigitPrice(from.getDigitPrice());
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
    public <E extends IFCurrency> E into(E into) {
        into.from(this);
        return into;
    }
}
