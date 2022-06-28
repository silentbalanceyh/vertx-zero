/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.template.domain.tables.pojos;


import cn.vertxup.template.domain.tables.interfaces.ITplTicket;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TplTicket implements VertxPojo, ITplTicket {

    private static final long serialVersionUID = 1L;

    private String        key;
    private String        code;
    private String        name;
    private String        description;
    private String        type;
    private String        status;
    private Boolean       system;
    private String        modelId;
    private String        modelKey;
    private String        modelCategory;
    private String        recordJson;
    private String        recordComponent;
    private String        uiConfig;
    private String        uiComponent;
    private String        sigma;
    private String        language;
    private Boolean       active;
    private String        metadata;
    private LocalDateTime createdAt;
    private String        createdBy;
    private LocalDateTime updatedAt;
    private String        updatedBy;

    public TplTicket() {}

    public TplTicket(ITplTicket value) {
        this.key = value.getKey();
        this.code = value.getCode();
        this.name = value.getName();
        this.description = value.getDescription();
        this.type = value.getType();
        this.status = value.getStatus();
        this.system = value.getSystem();
        this.modelId = value.getModelId();
        this.modelKey = value.getModelKey();
        this.modelCategory = value.getModelCategory();
        this.recordJson = value.getRecordJson();
        this.recordComponent = value.getRecordComponent();
        this.uiConfig = value.getUiConfig();
        this.uiComponent = value.getUiComponent();
        this.sigma = value.getSigma();
        this.language = value.getLanguage();
        this.active = value.getActive();
        this.metadata = value.getMetadata();
        this.createdAt = value.getCreatedAt();
        this.createdBy = value.getCreatedBy();
        this.updatedAt = value.getUpdatedAt();
        this.updatedBy = value.getUpdatedBy();
    }

    public TplTicket(
        String        key,
        String        code,
        String        name,
        String        description,
        String        type,
        String        status,
        Boolean       system,
        String        modelId,
        String        modelKey,
        String        modelCategory,
        String        recordJson,
        String        recordComponent,
        String        uiConfig,
        String        uiComponent,
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
        this.name = name;
        this.description = description;
        this.type = type;
        this.status = status;
        this.system = system;
        this.modelId = modelId;
        this.modelKey = modelKey;
        this.modelCategory = modelCategory;
        this.recordJson = recordJson;
        this.recordComponent = recordComponent;
        this.uiConfig = uiConfig;
        this.uiComponent = uiComponent;
        this.sigma = sigma;
        this.language = language;
        this.active = active;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

        public TplTicket(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.KEY</code>. 「key」- 增量记录ID
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.KEY</code>. 「key」- 增量记录ID
     */
    @Override
    public TplTicket setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.CODE</code>. 「code」- 编码
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.CODE</code>. 「code」- 编码
     */
    @Override
    public TplTicket setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.NAME</code>. 「name」- 名称
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.NAME</code>. 「name」- 名称
     */
    @Override
    public TplTicket setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.DESCRIPTION</code>. 「description」-
     * 描述
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.DESCRIPTION</code>. 「description」-
     * 描述
     */
    @Override
    public TplTicket setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.TYPE</code>. 「type」- 分类
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.TYPE</code>. 「type」- 分类
     */
    @Override
    public TplTicket setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.STATUS</code>. 「status」- 状态
     */
    @Override
    public String getStatus() {
        return this.status;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.STATUS</code>. 「status」- 状态
     */
    @Override
    public TplTicket setStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.SYSTEM</code>. 「system」- 是否属于系统模板
     */
    @Override
    public Boolean getSystem() {
        return this.system;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.SYSTEM</code>. 「system」- 是否属于系统模板
     */
    @Override
    public TplTicket setSystem(Boolean system) {
        this.system = system;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.MODEL_ID</code>. 「modelId」-
     * 关联的模型identifier，用于描述
     */
    @Override
    public String getModelId() {
        return this.modelId;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.MODEL_ID</code>. 「modelId」-
     * 关联的模型identifier，用于描述
     */
    @Override
    public TplTicket setModelId(String modelId) {
        this.modelId = modelId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.MODEL_KEY</code>. 「modelKey」-
     * 关联的模型记录ID，用于描述哪一个Model中的记录
     */
    @Override
    public String getModelKey() {
        return this.modelKey;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.MODEL_KEY</code>. 「modelKey」-
     * 关联的模型记录ID，用于描述哪一个Model中的记录
     */
    @Override
    public TplTicket setModelKey(String modelKey) {
        this.modelKey = modelKey;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.MODEL_CATEGORY</code>.
     * 「modelCategory」- 模型分类
     */
    @Override
    public String getModelCategory() {
        return this.modelCategory;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.MODEL_CATEGORY</code>.
     * 「modelCategory」- 模型分类
     */
    @Override
    public TplTicket setModelCategory(String modelCategory) {
        this.modelCategory = modelCategory;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.RECORD_JSON</code>. 「recordJson」-
     * 上一次的记录内容（Json格式）
     */
    @Override
    public String getRecordJson() {
        return this.recordJson;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.RECORD_JSON</code>. 「recordJson」-
     * 上一次的记录内容（Json格式）
     */
    @Override
    public TplTicket setRecordJson(String recordJson) {
        this.recordJson = recordJson;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.RECORD_COMPONENT</code>.
     * 「recordComponent」- 处理记录的组件
     */
    @Override
    public String getRecordComponent() {
        return this.recordComponent;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.RECORD_COMPONENT</code>.
     * 「recordComponent」- 处理记录的组件
     */
    @Override
    public TplTicket setRecordComponent(String recordComponent) {
        this.recordComponent = recordComponent;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.UI_CONFIG</code>. 「uiConfig」-
     * UI的配置（Json格式）
     */
    @Override
    public String getUiConfig() {
        return this.uiConfig;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.UI_CONFIG</code>. 「uiConfig」-
     * UI的配置（Json格式）
     */
    @Override
    public TplTicket setUiConfig(String uiConfig) {
        this.uiConfig = uiConfig;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.UI_COMPONENT</code>.
     * 「uiComponent」- 处理UI的组件
     */
    @Override
    public String getUiComponent() {
        return this.uiComponent;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.UI_COMPONENT</code>.
     * 「uiComponent」- 处理UI的组件
     */
    @Override
    public TplTicket setUiComponent(String uiComponent) {
        this.uiComponent = uiComponent;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public TplTicket setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public TplTicket setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public TplTicket setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public TplTicket setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public TplTicket setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public TplTicket setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public TplTicket setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_TICKET.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_TICKET.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public TplTicket setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TplTicket (");

        sb.append(key);
        sb.append(", ").append(code);
        sb.append(", ").append(name);
        sb.append(", ").append(description);
        sb.append(", ").append(type);
        sb.append(", ").append(status);
        sb.append(", ").append(system);
        sb.append(", ").append(modelId);
        sb.append(", ").append(modelKey);
        sb.append(", ").append(modelCategory);
        sb.append(", ").append(recordJson);
        sb.append(", ").append(recordComponent);
        sb.append(", ").append(uiConfig);
        sb.append(", ").append(uiComponent);
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
    public void from(ITplTicket from) {
        setKey(from.getKey());
        setCode(from.getCode());
        setName(from.getName());
        setDescription(from.getDescription());
        setType(from.getType());
        setStatus(from.getStatus());
        setSystem(from.getSystem());
        setModelId(from.getModelId());
        setModelKey(from.getModelKey());
        setModelCategory(from.getModelCategory());
        setRecordJson(from.getRecordJson());
        setRecordComponent(from.getRecordComponent());
        setUiConfig(from.getUiConfig());
        setUiComponent(from.getUiComponent());
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
    public <E extends ITplTicket> E into(E into) {
        into.from(this);
        return into;
    }
}
