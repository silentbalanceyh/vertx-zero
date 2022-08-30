/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.integration.domain.tables.pojos;


import cn.vertxup.integration.domain.tables.interfaces.IIDirectory;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class IDirectory implements VertxPojo, IIDirectory {

    private static final long serialVersionUID = 1L;

    private String key;
    private String name;
    private String code;
    private String storePath;
    private String linkedPath;
    private String parentId;
    private String category;
    private String type;
    private String owner;
    private String integrationId;
    private String runComponent;
    private Boolean visit;
    private String visitMode;
    private String visitRole;
    private String visitGroup;
    private String visitComponent;
    private String sigma;
    private String language;
    private Boolean active;
    private String metadata;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public IDirectory() {}

    public IDirectory(IIDirectory value) {
        this.key = value.getKey();
        this.name = value.getName();
        this.code = value.getCode();
        this.storePath = value.getStorePath();
        this.linkedPath = value.getLinkedPath();
        this.parentId = value.getParentId();
        this.category = value.getCategory();
        this.type = value.getType();
        this.owner = value.getOwner();
        this.integrationId = value.getIntegrationId();
        this.runComponent = value.getRunComponent();
        this.visit = value.getVisit();
        this.visitMode = value.getVisitMode();
        this.visitRole = value.getVisitRole();
        this.visitGroup = value.getVisitGroup();
        this.visitComponent = value.getVisitComponent();
        this.sigma = value.getSigma();
        this.language = value.getLanguage();
        this.active = value.getActive();
        this.metadata = value.getMetadata();
        this.createdAt = value.getCreatedAt();
        this.createdBy = value.getCreatedBy();
        this.updatedAt = value.getUpdatedAt();
        this.updatedBy = value.getUpdatedBy();
    }

    public IDirectory(
        String key,
        String name,
        String code,
        String storePath,
        String linkedPath,
        String parentId,
        String category,
        String type,
        String owner,
        String integrationId,
        String runComponent,
        Boolean visit,
        String visitMode,
        String visitRole,
        String visitGroup,
        String visitComponent,
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
        this.storePath = storePath;
        this.linkedPath = linkedPath;
        this.parentId = parentId;
        this.category = category;
        this.type = type;
        this.owner = owner;
        this.integrationId = integrationId;
        this.runComponent = runComponent;
        this.visit = visit;
        this.visitMode = visitMode;
        this.visitRole = visitRole;
        this.visitGroup = visitGroup;
        this.visitComponent = visitComponent;
        this.sigma = sigma;
        this.language = language;
        this.active = active;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

        public IDirectory(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.KEY</code>. 「key」- 目录主键
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.KEY</code>. 「key」- 目录主键
     */
    @Override
    public IDirectory setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.NAME</code>. 「name」- 目录名称
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.NAME</code>. 「name」- 目录名称
     */
    @Override
    public IDirectory setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.CODE</code>. 「code」- 目录编号
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.CODE</code>. 「code」- 目录编号
     */
    @Override
    public IDirectory setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.STORE_PATH</code>. 「storePath」-
     * 目录相对路径
     */
    @Override
    public String getStorePath() {
        return this.storePath;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.STORE_PATH</code>. 「storePath」-
     * 目录相对路径
     */
    @Override
    public IDirectory setStorePath(String storePath) {
        this.storePath = storePath;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.LINKED_PATH</code>. 「linkedPath」-
     * 链接路径，type = LINK 时专用
     */
    @Override
    public String getLinkedPath() {
        return this.linkedPath;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.LINKED_PATH</code>. 「linkedPath」-
     * 链接路径，type = LINK 时专用
     */
    @Override
    public IDirectory setLinkedPath(String linkedPath) {
        this.linkedPath = linkedPath;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.PARENT_ID</code>. 「parentId」-
     * 父目录ID
     */
    @Override
    public String getParentId() {
        return this.parentId;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.PARENT_ID</code>. 「parentId」-
     * 父目录ID
     */
    @Override
    public IDirectory setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.CATEGORY</code>. 「category」-
     * 目录连接的类型树
     */
    @Override
    public String getCategory() {
        return this.category;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.CATEGORY</code>. 「category」-
     * 目录连接的类型树
     */
    @Override
    public IDirectory setCategory(String category) {
        this.category = category;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.TYPE</code>. 「type」-
     * 目录类型：INTEGRATION / STORE / LINK
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.TYPE</code>. 「type」-
     * 目录类型：INTEGRATION / STORE / LINK
     */
    @Override
    public IDirectory setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.OWNER</code>. 「owner」- 目录访问人
     */
    @Override
    public String getOwner() {
        return this.owner;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.OWNER</code>. 「owner」- 目录访问人
     */
    @Override
    public IDirectory setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.INTEGRATION_ID</code>.
     * 「integrationId」- 该目录关联的 Integration，不关联则不转存
     */
    @Override
    public String getIntegrationId() {
        return this.integrationId;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.INTEGRATION_ID</code>.
     * 「integrationId」- 该目录关联的 Integration，不关联则不转存
     */
    @Override
    public IDirectory setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.RUN_COMPONENT</code>.
     * 「runComponent」- 目录执行组件，抓文件专用
     */
    @Override
    public String getRunComponent() {
        return this.runComponent;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.RUN_COMPONENT</code>.
     * 「runComponent」- 目录执行组件，抓文件专用
     */
    @Override
    public IDirectory setRunComponent(String runComponent) {
        this.runComponent = runComponent;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.VISIT</code>. 「visit」- 公有 / 私有
     */
    @Override
    public Boolean getVisit() {
        return this.visit;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.VISIT</code>. 「visit」- 公有 / 私有
     */
    @Override
    public IDirectory setVisit(Boolean visit) {
        this.visit = visit;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.VISIT_MODE</code>. 「visitMode」-
     * 目录模式：只读 / 可写，以后扩展为其他
     */
    @Override
    public String getVisitMode() {
        return this.visitMode;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.VISIT_MODE</code>. 「visitMode」-
     * 目录模式：只读 / 可写，以后扩展为其他
     */
    @Override
    public IDirectory setVisitMode(String visitMode) {
        this.visitMode = visitMode;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.VISIT_ROLE</code>. 「visitRole」-
     * 目录访问角色
     */
    @Override
    public String getVisitRole() {
        return this.visitRole;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.VISIT_ROLE</code>. 「visitRole」-
     * 目录访问角色
     */
    @Override
    public IDirectory setVisitRole(String visitRole) {
        this.visitRole = visitRole;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.VISIT_GROUP</code>. 「visitGroup」-
     * 目录访问组
     */
    @Override
    public String getVisitGroup() {
        return this.visitGroup;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.VISIT_GROUP</code>. 「visitGroup」-
     * 目录访问组
     */
    @Override
    public IDirectory setVisitGroup(String visitGroup) {
        this.visitGroup = visitGroup;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.VISIT_COMPONENT</code>.
     * 「visitComponent」- 目录访问控制专用组件
     */
    @Override
    public String getVisitComponent() {
        return this.visitComponent;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.VISIT_COMPONENT</code>.
     * 「visitComponent」- 目录访问控制专用组件
     */
    @Override
    public IDirectory setVisitComponent(String visitComponent) {
        this.visitComponent = visitComponent;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public IDirectory setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public IDirectory setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public IDirectory setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public IDirectory setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public IDirectory setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public IDirectory setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public IDirectory setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_DIRECTORY.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.I_DIRECTORY.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public IDirectory setUpdatedBy(String updatedBy) {
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
        final IDirectory other = (IDirectory) obj;
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
        if (this.storePath == null) {
            if (other.storePath != null)
                return false;
        }
        else if (!this.storePath.equals(other.storePath))
            return false;
        if (this.linkedPath == null) {
            if (other.linkedPath != null)
                return false;
        }
        else if (!this.linkedPath.equals(other.linkedPath))
            return false;
        if (this.parentId == null) {
            if (other.parentId != null)
                return false;
        }
        else if (!this.parentId.equals(other.parentId))
            return false;
        if (this.category == null) {
            if (other.category != null)
                return false;
        }
        else if (!this.category.equals(other.category))
            return false;
        if (this.type == null) {
            if (other.type != null)
                return false;
        }
        else if (!this.type.equals(other.type))
            return false;
        if (this.owner == null) {
            if (other.owner != null)
                return false;
        }
        else if (!this.owner.equals(other.owner))
            return false;
        if (this.integrationId == null) {
            if (other.integrationId != null)
                return false;
        }
        else if (!this.integrationId.equals(other.integrationId))
            return false;
        if (this.runComponent == null) {
            if (other.runComponent != null)
                return false;
        }
        else if (!this.runComponent.equals(other.runComponent))
            return false;
        if (this.visit == null) {
            if (other.visit != null)
                return false;
        }
        else if (!this.visit.equals(other.visit))
            return false;
        if (this.visitMode == null) {
            if (other.visitMode != null)
                return false;
        }
        else if (!this.visitMode.equals(other.visitMode))
            return false;
        if (this.visitRole == null) {
            if (other.visitRole != null)
                return false;
        }
        else if (!this.visitRole.equals(other.visitRole))
            return false;
        if (this.visitGroup == null) {
            if (other.visitGroup != null)
                return false;
        }
        else if (!this.visitGroup.equals(other.visitGroup))
            return false;
        if (this.visitComponent == null) {
            if (other.visitComponent != null)
                return false;
        }
        else if (!this.visitComponent.equals(other.visitComponent))
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
        result = prime * result + ((this.storePath == null) ? 0 : this.storePath.hashCode());
        result = prime * result + ((this.linkedPath == null) ? 0 : this.linkedPath.hashCode());
        result = prime * result + ((this.parentId == null) ? 0 : this.parentId.hashCode());
        result = prime * result + ((this.category == null) ? 0 : this.category.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        result = prime * result + ((this.owner == null) ? 0 : this.owner.hashCode());
        result = prime * result + ((this.integrationId == null) ? 0 : this.integrationId.hashCode());
        result = prime * result + ((this.runComponent == null) ? 0 : this.runComponent.hashCode());
        result = prime * result + ((this.visit == null) ? 0 : this.visit.hashCode());
        result = prime * result + ((this.visitMode == null) ? 0 : this.visitMode.hashCode());
        result = prime * result + ((this.visitRole == null) ? 0 : this.visitRole.hashCode());
        result = prime * result + ((this.visitGroup == null) ? 0 : this.visitGroup.hashCode());
        result = prime * result + ((this.visitComponent == null) ? 0 : this.visitComponent.hashCode());
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
        StringBuilder sb = new StringBuilder("IDirectory (");

        sb.append(key);
        sb.append(", ").append(name);
        sb.append(", ").append(code);
        sb.append(", ").append(storePath);
        sb.append(", ").append(linkedPath);
        sb.append(", ").append(parentId);
        sb.append(", ").append(category);
        sb.append(", ").append(type);
        sb.append(", ").append(owner);
        sb.append(", ").append(integrationId);
        sb.append(", ").append(runComponent);
        sb.append(", ").append(visit);
        sb.append(", ").append(visitMode);
        sb.append(", ").append(visitRole);
        sb.append(", ").append(visitGroup);
        sb.append(", ").append(visitComponent);
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
    public void from(IIDirectory from) {
        setKey(from.getKey());
        setName(from.getName());
        setCode(from.getCode());
        setStorePath(from.getStorePath());
        setLinkedPath(from.getLinkedPath());
        setParentId(from.getParentId());
        setCategory(from.getCategory());
        setType(from.getType());
        setOwner(from.getOwner());
        setIntegrationId(from.getIntegrationId());
        setRunComponent(from.getRunComponent());
        setVisit(from.getVisit());
        setVisitMode(from.getVisitMode());
        setVisitRole(from.getVisitRole());
        setVisitGroup(from.getVisitGroup());
        setVisitComponent(from.getVisitComponent());
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
    public <E extends IIDirectory> E into(E into) {
        into.from(this);
        return into;
    }
}
