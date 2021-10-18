/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.lbs.domain.tables.pojos;


import cn.vertxup.lbs.domain.tables.interfaces.ILCountry;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LCountry implements VertxPojo, ILCountry {

    private static final long serialVersionUID = 1L;

    private String        key;
    private String        name;
    private String        code;
    private String        flag;
    private String        phonePrefix;
    private String        currency;
    private String        metadata;
    private Integer       order;
    private Boolean       active;
    private String        sigma;
    private String        language;
    private LocalDateTime createdAt;
    private String        createdBy;
    private LocalDateTime updatedAt;
    private String        updatedBy;

    public LCountry() {}

    public LCountry(ILCountry value) {
        this.key = value.getKey();
        this.name = value.getName();
        this.code = value.getCode();
        this.flag = value.getFlag();
        this.phonePrefix = value.getPhonePrefix();
        this.currency = value.getCurrency();
        this.metadata = value.getMetadata();
        this.order = value.getOrder();
        this.active = value.getActive();
        this.sigma = value.getSigma();
        this.language = value.getLanguage();
        this.createdAt = value.getCreatedAt();
        this.createdBy = value.getCreatedBy();
        this.updatedAt = value.getUpdatedAt();
        this.updatedBy = value.getUpdatedBy();
    }

    public LCountry(
        String        key,
        String        name,
        String        code,
        String        flag,
        String        phonePrefix,
        String        currency,
        String        metadata,
        Integer       order,
        Boolean       active,
        String        sigma,
        String        language,
        LocalDateTime createdAt,
        String        createdBy,
        LocalDateTime updatedAt,
        String        updatedBy
    ) {
        this.key = key;
        this.name = name;
        this.code = code;
        this.flag = flag;
        this.phonePrefix = phonePrefix;
        this.currency = currency;
        this.metadata = metadata;
        this.order = order;
        this.active = active;
        this.sigma = sigma;
        this.language = language;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

        public LCountry(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.L_COUNTRY.KEY</code>. 「key」- 国家主键
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_COUNTRY.KEY</code>. 「key」- 国家主键
     */
    @Override
    public LCountry setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_COUNTRY.NAME</code>. 「name」- 国家名称
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_COUNTRY.NAME</code>. 「name」- 国家名称
     */
    @Override
    public LCountry setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_COUNTRY.CODE</code>. 「code」- 国家编号
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_COUNTRY.CODE</code>. 「code」- 国家编号
     */
    @Override
    public LCountry setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_COUNTRY.FLAG</code>. 「flag」- 国旗
     */
    @Override
    public String getFlag() {
        return this.flag;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_COUNTRY.FLAG</code>. 「flag」- 国旗
     */
    @Override
    public LCountry setFlag(String flag) {
        this.flag = flag;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_COUNTRY.PHONE_PREFIX</code>. 「phonePrefix」-
     * 电话前缀
     */
    @Override
    public String getPhonePrefix() {
        return this.phonePrefix;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_COUNTRY.PHONE_PREFIX</code>. 「phonePrefix」-
     * 电话前缀
     */
    @Override
    public LCountry setPhonePrefix(String phonePrefix) {
        this.phonePrefix = phonePrefix;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_COUNTRY.CURRENCY</code>. 「currency」- 使用货币
     */
    @Override
    public String getCurrency() {
        return this.currency;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_COUNTRY.CURRENCY</code>. 「currency」- 使用货币
     */
    @Override
    public LCountry setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_COUNTRY.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_COUNTRY.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public LCountry setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_COUNTRY.ORDER</code>. 「order」- 排序
     */
    @Override
    public Integer getOrder() {
        return this.order;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_COUNTRY.ORDER</code>. 「order」- 排序
     */
    @Override
    public LCountry setOrder(Integer order) {
        this.order = order;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_COUNTRY.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_COUNTRY.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public LCountry setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_COUNTRY.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_COUNTRY.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public LCountry setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_COUNTRY.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_COUNTRY.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public LCountry setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_COUNTRY.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_COUNTRY.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LCountry setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_COUNTRY.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_COUNTRY.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public LCountry setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_COUNTRY.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_COUNTRY.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LCountry setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_COUNTRY.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_COUNTRY.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public LCountry setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("LCountry (");

        sb.append(key);
        sb.append(", ").append(name);
        sb.append(", ").append(code);
        sb.append(", ").append(flag);
        sb.append(", ").append(phonePrefix);
        sb.append(", ").append(currency);
        sb.append(", ").append(metadata);
        sb.append(", ").append(order);
        sb.append(", ").append(active);
        sb.append(", ").append(sigma);
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
    public void from(ILCountry from) {
        setKey(from.getKey());
        setName(from.getName());
        setCode(from.getCode());
        setFlag(from.getFlag());
        setPhonePrefix(from.getPhonePrefix());
        setCurrency(from.getCurrency());
        setMetadata(from.getMetadata());
        setOrder(from.getOrder());
        setActive(from.getActive());
        setSigma(from.getSigma());
        setLanguage(from.getLanguage());
        setCreatedAt(from.getCreatedAt());
        setCreatedBy(from.getCreatedBy());
        setUpdatedAt(from.getUpdatedAt());
        setUpdatedBy(from.getUpdatedBy());
    }

    @Override
    public <E extends ILCountry> E into(E into) {
        into.from(this);
        return into;
    }
}
