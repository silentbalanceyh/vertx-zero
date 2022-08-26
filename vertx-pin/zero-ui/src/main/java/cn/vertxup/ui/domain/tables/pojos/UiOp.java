/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.ui.domain.tables.pojos;


import cn.vertxup.ui.domain.tables.interfaces.IUiOp;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UiOp implements VertxPojo, IUiOp {

    private static final long serialVersionUID = 1L;

    private String key;
    private String action;
    private String text;
    private String event;
    private String clientKey;
    private String clientId;
    private String config;
    private String plugin;
    private String controlId;
    private Boolean active;
    private String sigma;
    private String metadata;
    private String language;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public UiOp() {}

    public UiOp(IUiOp value) {
        this.key = value.getKey();
        this.action = value.getAction();
        this.text = value.getText();
        this.event = value.getEvent();
        this.clientKey = value.getClientKey();
        this.clientId = value.getClientId();
        this.config = value.getConfig();
        this.plugin = value.getPlugin();
        this.controlId = value.getControlId();
        this.active = value.getActive();
        this.sigma = value.getSigma();
        this.metadata = value.getMetadata();
        this.language = value.getLanguage();
        this.createdAt = value.getCreatedAt();
        this.createdBy = value.getCreatedBy();
        this.updatedAt = value.getUpdatedAt();
        this.updatedBy = value.getUpdatedBy();
    }

    public UiOp(
        String key,
        String action,
        String text,
        String event,
        String clientKey,
        String clientId,
        String config,
        String plugin,
        String controlId,
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
        this.action = action;
        this.text = text;
        this.event = event;
        this.clientKey = clientKey;
        this.clientId = clientId;
        this.config = config;
        this.plugin = plugin;
        this.controlId = controlId;
        this.active = active;
        this.sigma = sigma;
        this.metadata = metadata;
        this.language = language;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

        public UiOp(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.UI_OP.KEY</code>. 「key」- 操作主键
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_OP.KEY</code>. 「key」- 操作主键
     */
    @Override
    public UiOp setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_OP.ACTION</code>. 「action」-
     * S_ACTION中的code（权限检查专用）
     */
    @Override
    public String getAction() {
        return this.action;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_OP.ACTION</code>. 「action」-
     * S_ACTION中的code（权限检查专用）
     */
    @Override
    public UiOp setAction(String action) {
        this.action = action;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_OP.TEXT</code>. 「text」- 该操作上的文字信息
     */
    @Override
    public String getText() {
        return this.text;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_OP.TEXT</code>. 「text」- 该操作上的文字信息
     */
    @Override
    public UiOp setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_OP.EVENT</code>. 「event」- 操作中的 event 事件名称
     */
    @Override
    public String getEvent() {
        return this.event;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_OP.EVENT</code>. 「event」- 操作中的 event 事件名称
     */
    @Override
    public UiOp setEvent(String event) {
        this.event = event;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_OP.CLIENT_KEY</code>. 「clientKey」-
     * 一般是Html中对应的key信息，如 $opSave
     */
    @Override
    public String getClientKey() {
        return this.clientKey;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_OP.CLIENT_KEY</code>. 「clientKey」-
     * 一般是Html中对应的key信息，如 $opSave
     */
    @Override
    public UiOp setClientKey(String clientKey) {
        this.clientKey = clientKey;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_OP.CLIENT_ID</code>. 「clientId」-
     * 没有特殊情况，clientId = clientKey
     */
    @Override
    public String getClientId() {
        return this.clientId;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_OP.CLIENT_ID</code>. 「clientId」-
     * 没有特殊情况，clientId = clientKey
     */
    @Override
    public UiOp setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_OP.CONFIG</code>. 「config」-
     * 该按钮操作对应的配置数据信息, icon, type
     */
    @Override
    public String getConfig() {
        return this.config;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_OP.CONFIG</code>. 「config」-
     * 该按钮操作对应的配置数据信息, icon, type
     */
    @Override
    public UiOp setConfig(String config) {
        this.config = config;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_OP.PLUGIN</code>. 「plugin」- 该按钮中的插件，如
     * tooltip，component等
     */
    @Override
    public String getPlugin() {
        return this.plugin;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_OP.PLUGIN</code>. 「plugin」- 该按钮中的插件，如
     * tooltip，component等
     */
    @Override
    public UiOp setPlugin(String plugin) {
        this.plugin = plugin;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_OP.CONTROL_ID</code>. 「controlId」- 挂载专用的ID
     */
    @Override
    public String getControlId() {
        return this.controlId;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_OP.CONTROL_ID</code>. 「controlId」- 挂载专用的ID
     */
    @Override
    public UiOp setControlId(String controlId) {
        this.controlId = controlId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_OP.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_OP.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public UiOp setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_OP.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_OP.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public UiOp setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_OP.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_OP.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public UiOp setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_OP.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_OP.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public UiOp setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_OP.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_OP.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    @Override
    public UiOp setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_OP.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_OP.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public UiOp setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_OP.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_OP.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    @Override
    public UiOp setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_OP.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_OP.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public UiOp setUpdatedBy(String updatedBy) {
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
        final UiOp other = (UiOp) obj;
        if (this.key == null) {
            if (other.key != null)
                return false;
        }
        else if (!this.key.equals(other.key))
            return false;
        if (this.action == null) {
            if (other.action != null)
                return false;
        }
        else if (!this.action.equals(other.action))
            return false;
        if (this.text == null) {
            if (other.text != null)
                return false;
        }
        else if (!this.text.equals(other.text))
            return false;
        if (this.event == null) {
            if (other.event != null)
                return false;
        }
        else if (!this.event.equals(other.event))
            return false;
        if (this.clientKey == null) {
            if (other.clientKey != null)
                return false;
        }
        else if (!this.clientKey.equals(other.clientKey))
            return false;
        if (this.clientId == null) {
            if (other.clientId != null)
                return false;
        }
        else if (!this.clientId.equals(other.clientId))
            return false;
        if (this.config == null) {
            if (other.config != null)
                return false;
        }
        else if (!this.config.equals(other.config))
            return false;
        if (this.plugin == null) {
            if (other.plugin != null)
                return false;
        }
        else if (!this.plugin.equals(other.plugin))
            return false;
        if (this.controlId == null) {
            if (other.controlId != null)
                return false;
        }
        else if (!this.controlId.equals(other.controlId))
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
        result = prime * result + ((this.action == null) ? 0 : this.action.hashCode());
        result = prime * result + ((this.text == null) ? 0 : this.text.hashCode());
        result = prime * result + ((this.event == null) ? 0 : this.event.hashCode());
        result = prime * result + ((this.clientKey == null) ? 0 : this.clientKey.hashCode());
        result = prime * result + ((this.clientId == null) ? 0 : this.clientId.hashCode());
        result = prime * result + ((this.config == null) ? 0 : this.config.hashCode());
        result = prime * result + ((this.plugin == null) ? 0 : this.plugin.hashCode());
        result = prime * result + ((this.controlId == null) ? 0 : this.controlId.hashCode());
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
        StringBuilder sb = new StringBuilder("UiOp (");

        sb.append(key);
        sb.append(", ").append(action);
        sb.append(", ").append(text);
        sb.append(", ").append(event);
        sb.append(", ").append(clientKey);
        sb.append(", ").append(clientId);
        sb.append(", ").append(config);
        sb.append(", ").append(plugin);
        sb.append(", ").append(controlId);
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
    public void from(IUiOp from) {
        setKey(from.getKey());
        setAction(from.getAction());
        setText(from.getText());
        setEvent(from.getEvent());
        setClientKey(from.getClientKey());
        setClientId(from.getClientId());
        setConfig(from.getConfig());
        setPlugin(from.getPlugin());
        setControlId(from.getControlId());
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
    public <E extends IUiOp> E into(E into) {
        into.from(this);
        return into;
    }
}
