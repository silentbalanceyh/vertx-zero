/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.ui.domain.tables.pojos;


import cn.vertxup.ui.domain.tables.interfaces.IUiLayout;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UiLayout implements VertxPojo, IUiLayout {

    private static final long serialVersionUID = 1L;

    private String        key;
    private String        name;
    private String        config;
    private Boolean       active;
    private String        sigma;
    private String        metadata;
    private String        language;
    private LocalDateTime createdAt;
    private String        createdBy;
    private LocalDateTime updatedAt;
    private String        updatedBy;

    public UiLayout() {}

    public UiLayout(IUiLayout value) {
        this.key = value.getKey();
        this.name = value.getName();
        this.config = value.getConfig();
        this.active = value.getActive();
        this.sigma = value.getSigma();
        this.metadata = value.getMetadata();
        this.language = value.getLanguage();
        this.createdAt = value.getCreatedAt();
        this.createdBy = value.getCreatedBy();
        this.updatedAt = value.getUpdatedAt();
        this.updatedBy = value.getUpdatedBy();
    }

    public UiLayout(
        String        key,
        String        name,
        String        config,
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
        this.name = name;
        this.config = config;
        this.active = active;
        this.sigma = sigma;
        this.metadata = metadata;
        this.language = language;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

        public UiLayout(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.UI_LAYOUT.KEY</code>. 「key」- Tpl模板唯一主键
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LAYOUT.KEY</code>. 「key」- Tpl模板唯一主键
     */
    @Override
    public UiLayout setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LAYOUT.NAME</code>. 「name」- Tpl模板名称
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LAYOUT.NAME</code>. 「name」- Tpl模板名称
     */
    @Override
    public UiLayout setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LAYOUT.CONFIG</code>. 「config」- 主配置
     */
    @Override
    public String getConfig() {
        return this.config;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LAYOUT.CONFIG</code>. 「config」- 主配置
     */
    @Override
    public UiLayout setConfig(String config) {
        this.config = config;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LAYOUT.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LAYOUT.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public UiLayout setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LAYOUT.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LAYOUT.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public UiLayout setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LAYOUT.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LAYOUT.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public UiLayout setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LAYOUT.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LAYOUT.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public UiLayout setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LAYOUT.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LAYOUT.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public UiLayout setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LAYOUT.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LAYOUT.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public UiLayout setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LAYOUT.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LAYOUT.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public UiLayout setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LAYOUT.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LAYOUT.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public UiLayout setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("UiLayout (");

        sb.append(key);
        sb.append(", ").append(name);
        sb.append(", ").append(config);
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
    public void from(IUiLayout from) {
        setKey(from.getKey());
        setName(from.getName());
        setConfig(from.getConfig());
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
    public <E extends IUiLayout> E into(E into) {
        into.from(this);
        return into;
    }
}
