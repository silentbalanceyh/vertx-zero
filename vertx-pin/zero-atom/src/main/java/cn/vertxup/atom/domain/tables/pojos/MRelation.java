/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.atom.domain.tables.pojos;


import cn.vertxup.atom.domain.tables.interfaces.IMRelation;
import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MRelation implements VertxPojo, IMRelation {

    private static final long serialVersionUID = 1L;

    private String key;
    private String type;
    private String upstream;
    private String downstream;
    private String comments;
    private String sigma;
    private String language;
    private Boolean active;
    private String metadata;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public MRelation() {}

    public MRelation(IMRelation value) {
        this.key = value.getKey();
        this.type = value.getType();
        this.upstream = value.getUpstream();
        this.downstream = value.getDownstream();
        this.comments = value.getComments();
        this.sigma = value.getSigma();
        this.language = value.getLanguage();
        this.active = value.getActive();
        this.metadata = value.getMetadata();
        this.createdAt = value.getCreatedAt();
        this.createdBy = value.getCreatedBy();
        this.updatedAt = value.getUpdatedAt();
        this.updatedBy = value.getUpdatedBy();
    }

    public MRelation(
        String key,
        String type,
        String upstream,
        String downstream,
        String comments,
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
        this.type = type;
        this.upstream = upstream;
        this.downstream = downstream;
        this.comments = comments;
        this.sigma = sigma;
        this.language = language;
        this.active = active;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

        public MRelation(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.M_RELATION.KEY</code>. 「key」- 关系定义的主键
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.M_RELATION.KEY</code>. 「key」- 关系定义的主键
     */
    @Override
    public MRelation setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.M_RELATION.TYPE</code>. 「type」- 关系类型 - 来自（字典）
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * Setter for <code>DB_ETERNAL.M_RELATION.TYPE</code>. 「type」- 关系类型 - 来自（字典）
     */
    @Override
    public MRelation setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.M_RELATION.UPSTREAM</code>. 「upstream」- 当前关系是
     * upstream，表示上级
     */
    @Override
    public String getUpstream() {
        return this.upstream;
    }

    /**
     * Setter for <code>DB_ETERNAL.M_RELATION.UPSTREAM</code>. 「upstream」- 当前关系是
     * upstream，表示上级
     */
    @Override
    public MRelation setUpstream(String upstream) {
        this.upstream = upstream;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.M_RELATION.DOWNSTREAM</code>. 「downstream」-
     * 当前关系是 downstream，表示下级
     */
    @Override
    public String getDownstream() {
        return this.downstream;
    }

    /**
     * Setter for <code>DB_ETERNAL.M_RELATION.DOWNSTREAM</code>. 「downstream」-
     * 当前关系是 downstream，表示下级
     */
    @Override
    public MRelation setDownstream(String downstream) {
        this.downstream = downstream;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.M_RELATION.COMMENTS</code>. 「comments」-
     * 关系定义的描述信息
     */
    @Override
    public String getComments() {
        return this.comments;
    }

    /**
     * Setter for <code>DB_ETERNAL.M_RELATION.COMMENTS</code>. 「comments」-
     * 关系定义的描述信息
     */
    @Override
    public MRelation setComments(String comments) {
        this.comments = comments;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.M_RELATION.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.M_RELATION.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public MRelation setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.M_RELATION.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.M_RELATION.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public MRelation setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.M_RELATION.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.M_RELATION.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public MRelation setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.M_RELATION.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.M_RELATION.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public MRelation setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.M_RELATION.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.M_RELATION.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public MRelation setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.M_RELATION.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.M_RELATION.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public MRelation setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.M_RELATION.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.M_RELATION.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public MRelation setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.M_RELATION.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.M_RELATION.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public MRelation setUpdatedBy(String updatedBy) {
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
        final MRelation other = (MRelation) obj;
        if (this.key == null) {
            if (other.key != null)
                return false;
        }
        else if (!this.key.equals(other.key))
            return false;
        if (this.type == null) {
            if (other.type != null)
                return false;
        }
        else if (!this.type.equals(other.type))
            return false;
        if (this.upstream == null) {
            if (other.upstream != null)
                return false;
        }
        else if (!this.upstream.equals(other.upstream))
            return false;
        if (this.downstream == null) {
            if (other.downstream != null)
                return false;
        }
        else if (!this.downstream.equals(other.downstream))
            return false;
        if (this.comments == null) {
            if (other.comments != null)
                return false;
        }
        else if (!this.comments.equals(other.comments))
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
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        result = prime * result + ((this.upstream == null) ? 0 : this.upstream.hashCode());
        result = prime * result + ((this.downstream == null) ? 0 : this.downstream.hashCode());
        result = prime * result + ((this.comments == null) ? 0 : this.comments.hashCode());
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
        StringBuilder sb = new StringBuilder("MRelation (");

        sb.append(key);
        sb.append(", ").append(type);
        sb.append(", ").append(upstream);
        sb.append(", ").append(downstream);
        sb.append(", ").append(comments);
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
    public void from(IMRelation from) {
        setKey(from.getKey());
        setType(from.getType());
        setUpstream(from.getUpstream());
        setDownstream(from.getDownstream());
        setComments(from.getComments());
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
    public <E extends IMRelation> E into(E into) {
        into.from(this);
        return into;
    }
}
