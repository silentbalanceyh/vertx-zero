/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.rbac.domain.tables.pojos;


import cn.vertxup.rbac.domain.tables.interfaces.ISPermission;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SPermission implements VertxPojo, ISPermission {

    private static final long serialVersionUID = 1L;

    private String        key;
    private String        name;
    private String        code;
    private String        identifier;
    private String        sigma;
    private String        language;
    private Boolean       active;
    private String        comment;
    private String        metadata;
    private LocalDateTime createdAt;
    private String        createdBy;
    private LocalDateTime updatedAt;
    private String        updatedBy;

    public SPermission() {}

    public SPermission(ISPermission value) {
        this.key = value.getKey();
        this.name = value.getName();
        this.code = value.getCode();
        this.identifier = value.getIdentifier();
        this.sigma = value.getSigma();
        this.language = value.getLanguage();
        this.active = value.getActive();
        this.comment = value.getComment();
        this.metadata = value.getMetadata();
        this.createdAt = value.getCreatedAt();
        this.createdBy = value.getCreatedBy();
        this.updatedAt = value.getUpdatedAt();
        this.updatedBy = value.getUpdatedBy();
    }

    public SPermission(
        String        key,
        String        name,
        String        code,
        String        identifier,
        String        sigma,
        String        language,
        Boolean       active,
        String        comment,
        String        metadata,
        LocalDateTime createdAt,
        String        createdBy,
        LocalDateTime updatedAt,
        String        updatedBy
    ) {
        this.key = key;
        this.name = name;
        this.code = code;
        this.identifier = identifier;
        this.sigma = sigma;
        this.language = language;
        this.active = active;
        this.comment = comment;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

        public SPermission(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.S_PERMISSION.KEY</code>. 「key」- 权限ID
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERMISSION.KEY</code>. 「key」- 权限ID
     */
    @Override
    public SPermission setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERMISSION.NAME</code>. 「name」- 权限名称
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERMISSION.NAME</code>. 「name」- 权限名称
     */
    @Override
    public SPermission setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERMISSION.CODE</code>. 「code」- 权限系统码
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERMISSION.CODE</code>. 「code」- 权限系统码
     */
    @Override
    public SPermission setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERMISSION.IDENTIFIER</code>. 「identifier」-
     * 当前权限所属的Model的标识
     */
    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERMISSION.IDENTIFIER</code>. 「identifier」-
     * 当前权限所属的Model的标识
     */
    @Override
    public SPermission setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERMISSION.SIGMA</code>. 「sigma」- 绑定的统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERMISSION.SIGMA</code>. 「sigma」- 绑定的统一标识
     */
    @Override
    public SPermission setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERMISSION.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERMISSION.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public SPermission setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERMISSION.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERMISSION.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public SPermission setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERMISSION.COMMENT</code>. 「comment」- 权限说明
     */
    @Override
    public String getComment() {
        return this.comment;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERMISSION.COMMENT</code>. 「comment」- 权限说明
     */
    @Override
    public SPermission setComment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERMISSION.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERMISSION.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public SPermission setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERMISSION.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERMISSION.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public SPermission setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERMISSION.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERMISSION.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public SPermission setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERMISSION.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERMISSION.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public SPermission setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERMISSION.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERMISSION.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public SPermission setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SPermission (");

        sb.append(key);
        sb.append(", ").append(name);
        sb.append(", ").append(code);
        sb.append(", ").append(identifier);
        sb.append(", ").append(sigma);
        sb.append(", ").append(language);
        sb.append(", ").append(active);
        sb.append(", ").append(comment);
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
    public void from(ISPermission from) {
        setKey(from.getKey());
        setName(from.getName());
        setCode(from.getCode());
        setIdentifier(from.getIdentifier());
        setSigma(from.getSigma());
        setLanguage(from.getLanguage());
        setActive(from.getActive());
        setComment(from.getComment());
        setMetadata(from.getMetadata());
        setCreatedAt(from.getCreatedAt());
        setCreatedBy(from.getCreatedBy());
        setUpdatedAt(from.getUpdatedAt());
        setUpdatedBy(from.getUpdatedBy());
    }

    @Override
    public <E extends ISPermission> E into(E into) {
        into.from(this);
        return into;
    }
}
