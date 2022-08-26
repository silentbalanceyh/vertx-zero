/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.template.domain.tables.pojos;


import cn.vertxup.template.domain.tables.interfaces.ITplMessage;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TplMessage implements VertxPojo, ITplMessage {

    private static final long serialVersionUID = 1L;

    private String key;
    private String name;
    private String code;
    private String type;
    private String exprSubject;
    private String exprContent;
    private String exprComponent;
    private String appId;
    private Boolean active;
    private String sigma;
    private String metadata;
    private String language;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public TplMessage() {}

    public TplMessage(ITplMessage value) {
        this.key = value.getKey();
        this.name = value.getName();
        this.code = value.getCode();
        this.type = value.getType();
        this.exprSubject = value.getExprSubject();
        this.exprContent = value.getExprContent();
        this.exprComponent = value.getExprComponent();
        this.appId = value.getAppId();
        this.active = value.getActive();
        this.sigma = value.getSigma();
        this.metadata = value.getMetadata();
        this.language = value.getLanguage();
        this.createdAt = value.getCreatedAt();
        this.createdBy = value.getCreatedBy();
        this.updatedAt = value.getUpdatedAt();
        this.updatedBy = value.getUpdatedBy();
    }

    public TplMessage(
        String key,
        String name,
        String code,
        String type,
        String exprSubject,
        String exprContent,
        String exprComponent,
        String appId,
        Boolean active,
        String sigma,
        String metadata,
        String language,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy
    ) {
        this.key = key;
        this.name = name;
        this.code = code;
        this.type = type;
        this.exprSubject = exprSubject;
        this.exprContent = exprContent;
        this.exprComponent = exprComponent;
        this.appId = appId;
        this.active = active;
        this.sigma = sigma;
        this.metadata = metadata;
        this.language = language;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

        public TplMessage(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.KEY</code>. 「key」- 模板唯一主键
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.KEY</code>. 「key」- 模板唯一主键
     */
    @Override
    public TplMessage setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.NAME</code>. 「name」- 模板名称
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.NAME</code>. 「name」- 模板名称
     */
    @Override
    public TplMessage setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.CODE</code>. 「code」- 模板编码
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.CODE</code>. 「code」- 模板编码
     */
    @Override
    public TplMessage setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.TYPE</code>. 「type」- 模板类型
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.TYPE</code>. 「type」- 模板类型
     */
    @Override
    public TplMessage setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.EXPR_SUBJECT</code>.
     * 「exprSubject」- 模板标题，支持表达式
     */
    @Override
    public String getExprSubject() {
        return this.exprSubject;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.EXPR_SUBJECT</code>.
     * 「exprSubject」- 模板标题，支持表达式
     */
    @Override
    public TplMessage setExprSubject(String exprSubject) {
        this.exprSubject = exprSubject;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.EXPR_CONTENT</code>.
     * 「exprContent」- 模板内容，支持表达式
     */
    @Override
    public String getExprContent() {
        return this.exprContent;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.EXPR_CONTENT</code>.
     * 「exprContent」- 模板内容，支持表达式
     */
    @Override
    public TplMessage setExprContent(String exprContent) {
        this.exprContent = exprContent;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.EXPR_COMPONENT</code>.
     * 「exprComponent」- 模板扩展处理程序，Java类名
     */
    @Override
    public String getExprComponent() {
        return this.exprComponent;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.EXPR_COMPONENT</code>.
     * 「exprComponent」- 模板扩展处理程序，Java类名
     */
    @Override
    public TplMessage setExprComponent(String exprComponent) {
        this.exprComponent = exprComponent;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.APP_ID</code>. 「appId」- 所属应用ID
     */
    @Override
    public String getAppId() {
        return this.appId;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.APP_ID</code>. 「appId」- 所属应用ID
     */
    @Override
    public TplMessage setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public TplMessage setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public TplMessage setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public TplMessage setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public TplMessage setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public TplMessage setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public TplMessage setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public TplMessage setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public TplMessage setUpdatedBy(String updatedBy) {
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
        final TplMessage other = (TplMessage) obj;
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
        if (this.type == null) {
            if (other.type != null)
                return false;
        }
        else if (!this.type.equals(other.type))
            return false;
        if (this.exprSubject == null) {
            if (other.exprSubject != null)
                return false;
        }
        else if (!this.exprSubject.equals(other.exprSubject))
            return false;
        if (this.exprContent == null) {
            if (other.exprContent != null)
                return false;
        }
        else if (!this.exprContent.equals(other.exprContent))
            return false;
        if (this.exprComponent == null) {
            if (other.exprComponent != null)
                return false;
        }
        else if (!this.exprComponent.equals(other.exprComponent))
            return false;
        if (this.appId == null) {
            if (other.appId != null)
                return false;
        }
        else if (!this.appId.equals(other.appId))
            return false;
        if (this.active == null) {
            if (other.active != null)
                return false;
        }
        else if (!this.active.equals(other.active))
            return false;
        if (this.sigma == null) {
            if (other.sigma != null)
                return false;
        }
        else if (!this.sigma.equals(other.sigma))
            return false;
        if (this.metadata == null) {
            if (other.metadata != null)
                return false;
        }
        else if (!this.metadata.equals(other.metadata))
            return false;
        if (this.language == null) {
            if (other.language != null)
                return false;
        }
        else if (!this.language.equals(other.language))
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
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        result = prime * result + ((this.exprSubject == null) ? 0 : this.exprSubject.hashCode());
        result = prime * result + ((this.exprContent == null) ? 0 : this.exprContent.hashCode());
        result = prime * result + ((this.exprComponent == null) ? 0 : this.exprComponent.hashCode());
        result = prime * result + ((this.appId == null) ? 0 : this.appId.hashCode());
        result = prime * result + ((this.active == null) ? 0 : this.active.hashCode());
        result = prime * result + ((this.sigma == null) ? 0 : this.sigma.hashCode());
        result = prime * result + ((this.metadata == null) ? 0 : this.metadata.hashCode());
        result = prime * result + ((this.language == null) ? 0 : this.language.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        result = prime * result + ((this.createdBy == null) ? 0 : this.createdBy.hashCode());
        result = prime * result + ((this.updatedAt == null) ? 0 : this.updatedAt.hashCode());
        result = prime * result + ((this.updatedBy == null) ? 0 : this.updatedBy.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TplMessage (");

        sb.append(key);
        sb.append(", ").append(name);
        sb.append(", ").append(code);
        sb.append(", ").append(type);
        sb.append(", ").append(exprSubject);
        sb.append(", ").append(exprContent);
        sb.append(", ").append(exprComponent);
        sb.append(", ").append(appId);
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
    public void from(ITplMessage from) {
        setKey(from.getKey());
        setName(from.getName());
        setCode(from.getCode());
        setType(from.getType());
        setExprSubject(from.getExprSubject());
        setExprContent(from.getExprContent());
        setExprComponent(from.getExprComponent());
        setAppId(from.getAppId());
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
    public <E extends ITplMessage> E into(E into) {
        into.from(this);
        return into;
    }
}
