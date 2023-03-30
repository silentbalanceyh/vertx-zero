/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.lbs.domain.tables.pojos;


import cn.vertxup.lbs.domain.tables.interfaces.ILTent;
import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LTent implements VertxPojo, ILTent {

    private static final long serialVersionUID = 1L;

    private String        key;
    private String        name;
    private String        code;
    private String        contactPhone;
    private String        contactName;
    private String        metadata;
    private Integer       order;
    private String        locationId;
    private String        yardId;
    private Boolean       active;
    private String        sigma;
    private String        language;
    private LocalDateTime createdAt;
    private String        createdBy;
    private LocalDateTime updatedAt;
    private String        updatedBy;

    public LTent() {}

    public LTent(ILTent value) {
        this.key = value.getKey();
        this.name = value.getName();
        this.code = value.getCode();
        this.contactPhone = value.getContactPhone();
        this.contactName = value.getContactName();
        this.metadata = value.getMetadata();
        this.order = value.getOrder();
        this.locationId = value.getLocationId();
        this.yardId = value.getYardId();
        this.active = value.getActive();
        this.sigma = value.getSigma();
        this.language = value.getLanguage();
        this.createdAt = value.getCreatedAt();
        this.createdBy = value.getCreatedBy();
        this.updatedAt = value.getUpdatedAt();
        this.updatedBy = value.getUpdatedBy();
    }

    public LTent(
        String        key,
        String        name,
        String        code,
        String        contactPhone,
        String        contactName,
        String        metadata,
        Integer       order,
        String        locationId,
        String        yardId,
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
        this.contactPhone = contactPhone;
        this.contactName = contactName;
        this.metadata = metadata;
        this.order = order;
        this.locationId = locationId;
        this.yardId = yardId;
        this.active = active;
        this.sigma = sigma;
        this.language = language;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

        public LTent(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.L_TENT.KEY</code>. 「key」- 主键
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_TENT.KEY</code>. 「key」- 主键
     */
    @Override
    public LTent setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_TENT.NAME</code>. 「name」- 名称
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_TENT.NAME</code>. 「name」- 名称
     */
    @Override
    public LTent setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_TENT.CODE</code>. 「code」- 编码
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_TENT.CODE</code>. 「code」- 编码
     */
    @Override
    public LTent setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_TENT.CONTACT_PHONE</code>. 「contactPhone」-
     * 联系电话
     */
    @Override
    public String getContactPhone() {
        return this.contactPhone;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_TENT.CONTACT_PHONE</code>. 「contactPhone」-
     * 联系电话
     */
    @Override
    public LTent setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_TENT.CONTACT_NAME</code>. 「contactName」-
     * 联系人姓名
     */
    @Override
    public String getContactName() {
        return this.contactName;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_TENT.CONTACT_NAME</code>. 「contactName」-
     * 联系人姓名
     */
    @Override
    public LTent setContactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_TENT.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_TENT.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public LTent setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_TENT.ORDER</code>. 「order」- 排序
     */
    @Override
    public Integer getOrder() {
        return this.order;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_TENT.ORDER</code>. 「order」- 排序
     */
    @Override
    public LTent setOrder(Integer order) {
        this.order = order;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_TENT.LOCATION_ID</code>. 「locationId」-
     * 关联地址ID
     */
    @Override
    public String getLocationId() {
        return this.locationId;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_TENT.LOCATION_ID</code>. 「locationId」-
     * 关联地址ID
     */
    @Override
    public LTent setLocationId(String locationId) {
        this.locationId = locationId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_TENT.YARD_ID</code>. 「yardId」- 关联小区ID
     */
    @Override
    public String getYardId() {
        return this.yardId;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_TENT.YARD_ID</code>. 「yardId」- 关联小区ID
     */
    @Override
    public LTent setYardId(String yardId) {
        this.yardId = yardId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_TENT.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_TENT.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public LTent setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_TENT.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_TENT.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public LTent setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_TENT.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_TENT.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public LTent setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_TENT.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_TENT.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    @Override
    public LTent setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_TENT.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_TENT.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public LTent setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_TENT.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_TENT.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    @Override
    public LTent setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_TENT.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.L_TENT.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public LTent setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("LTent (");

        sb.append(key);
        sb.append(", ").append(name);
        sb.append(", ").append(code);
        sb.append(", ").append(contactPhone);
        sb.append(", ").append(contactName);
        sb.append(", ").append(metadata);
        sb.append(", ").append(order);
        sb.append(", ").append(locationId);
        sb.append(", ").append(yardId);
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
    public void from(ILTent from) {
        setKey(from.getKey());
        setName(from.getName());
        setCode(from.getCode());
        setContactPhone(from.getContactPhone());
        setContactName(from.getContactName());
        setMetadata(from.getMetadata());
        setOrder(from.getOrder());
        setLocationId(from.getLocationId());
        setYardId(from.getYardId());
        setActive(from.getActive());
        setSigma(from.getSigma());
        setLanguage(from.getLanguage());
        setCreatedAt(from.getCreatedAt());
        setCreatedBy(from.getCreatedBy());
        setUpdatedAt(from.getUpdatedAt());
        setUpdatedBy(from.getUpdatedBy());
    }

    @Override
    public <E extends ILTent> E into(E into) {
        into.from(this);
        return into;
    }
}
