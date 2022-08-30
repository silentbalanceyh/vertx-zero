/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.battery.domain.tables.pojos;


import cn.vertxup.battery.domain.tables.interfaces.IBAuthority;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BAuthority implements VertxPojo, IBAuthority {

    private static final long serialVersionUID = 1L;

    private String key;
    private String code;
    private String blockId;
    private String type;
    private String licResource;
    private String licAction;
    private String licPermission;
    private String licView;
    private Boolean active;
    private String sigma;
    private String metadata;
    private String language;

    public BAuthority() {}

    public BAuthority(IBAuthority value) {
        this.key = value.getKey();
        this.code = value.getCode();
        this.blockId = value.getBlockId();
        this.type = value.getType();
        this.licResource = value.getLicResource();
        this.licAction = value.getLicAction();
        this.licPermission = value.getLicPermission();
        this.licView = value.getLicView();
        this.active = value.getActive();
        this.sigma = value.getSigma();
        this.metadata = value.getMetadata();
        this.language = value.getLanguage();
    }

    public BAuthority(
        String key,
        String code,
        String blockId,
        String type,
        String licResource,
        String licAction,
        String licPermission,
        String licView,
        Boolean active,
        String sigma,
        String metadata,
        String language
    ) {
        this.key = key;
        this.code = code;
        this.blockId = blockId;
        this.type = type;
        this.licResource = licResource;
        this.licAction = licAction;
        this.licPermission = licPermission;
        this.licView = licView;
        this.active = active;
        this.sigma = sigma;
        this.metadata = metadata;
        this.language = language;
    }

        public BAuthority(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.B_AUTHORITY.KEY</code>. 「key」- 主键
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.B_AUTHORITY.KEY</code>. 「key」- 主键
     */
    @Override
    public BAuthority setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.B_AUTHORITY.CODE</code>. 「name」- 系统内部编码
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * Setter for <code>DB_ETERNAL.B_AUTHORITY.CODE</code>. 「name」- 系统内部编码
     */
    @Override
    public BAuthority setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.B_AUTHORITY.BLOCK_ID</code>. 「blockId」-
     * 所属模块ID
     */
    @Override
    public String getBlockId() {
        return this.blockId;
    }

    /**
     * Setter for <code>DB_ETERNAL.B_AUTHORITY.BLOCK_ID</code>. 「blockId」-
     * 所属模块ID
     */
    @Override
    public BAuthority setBlockId(String blockId) {
        this.blockId = blockId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.B_AUTHORITY.TYPE</code>. 「type」- 类型保留，单独区分
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * Setter for <code>DB_ETERNAL.B_AUTHORITY.TYPE</code>. 「type」- 类型保留，单独区分
     */
    @Override
    public BAuthority setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.B_AUTHORITY.LIC_RESOURCE</code>.
     * 「licResource」- 资源编码
     */
    @Override
    public String getLicResource() {
        return this.licResource;
    }

    /**
     * Setter for <code>DB_ETERNAL.B_AUTHORITY.LIC_RESOURCE</code>.
     * 「licResource」- 资源编码
     */
    @Override
    public BAuthority setLicResource(String licResource) {
        this.licResource = licResource;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.B_AUTHORITY.LIC_ACTION</code>. 「licAction」-
     * 操作编码
     */
    @Override
    public String getLicAction() {
        return this.licAction;
    }

    /**
     * Setter for <code>DB_ETERNAL.B_AUTHORITY.LIC_ACTION</code>. 「licAction」-
     * 操作编码
     */
    @Override
    public BAuthority setLicAction(String licAction) {
        this.licAction = licAction;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.B_AUTHORITY.LIC_PERMISSION</code>.
     * 「licPermission」- 所需权限集合
     */
    @Override
    public String getLicPermission() {
        return this.licPermission;
    }

    /**
     * Setter for <code>DB_ETERNAL.B_AUTHORITY.LIC_PERMISSION</code>.
     * 「licPermission」- 所需权限集合
     */
    @Override
    public BAuthority setLicPermission(String licPermission) {
        this.licPermission = licPermission;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.B_AUTHORITY.LIC_VIEW</code>. 「licView」- 视图集合
     */
    @Override
    public String getLicView() {
        return this.licView;
    }

    /**
     * Setter for <code>DB_ETERNAL.B_AUTHORITY.LIC_VIEW</code>. 「licView」- 视图集合
     */
    @Override
    public BAuthority setLicView(String licView) {
        this.licView = licView;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.B_AUTHORITY.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.B_AUTHORITY.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public BAuthority setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.B_AUTHORITY.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.B_AUTHORITY.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public BAuthority setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.B_AUTHORITY.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.B_AUTHORITY.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public BAuthority setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.B_AUTHORITY.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.B_AUTHORITY.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public BAuthority setLanguage(String language) {
        this.language = language;
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
        final BAuthority other = (BAuthority) obj;
        if (this.key == null) {
            if (other.key != null)
                return false;
        }
        else if (!this.key.equals(other.key))
            return false;
        if (this.code == null) {
            if (other.code != null)
                return false;
        }
        else if (!this.code.equals(other.code))
            return false;
        if (this.blockId == null) {
            if (other.blockId != null)
                return false;
        }
        else if (!this.blockId.equals(other.blockId))
            return false;
        if (this.type == null) {
            if (other.type != null)
                return false;
        }
        else if (!this.type.equals(other.type))
            return false;
        if (this.licResource == null) {
            if (other.licResource != null)
                return false;
        }
        else if (!this.licResource.equals(other.licResource))
            return false;
        if (this.licAction == null) {
            if (other.licAction != null)
                return false;
        }
        else if (!this.licAction.equals(other.licAction))
            return false;
        if (this.licPermission == null) {
            if (other.licPermission != null)
                return false;
        }
        else if (!this.licPermission.equals(other.licPermission))
            return false;
        if (this.licView == null) {
            if (other.licView != null)
                return false;
        }
        else if (!this.licView.equals(other.licView))
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
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.key == null) ? 0 : this.key.hashCode());
        result = prime * result + ((this.code == null) ? 0 : this.code.hashCode());
        result = prime * result + ((this.blockId == null) ? 0 : this.blockId.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        result = prime * result + ((this.licResource == null) ? 0 : this.licResource.hashCode());
        result = prime * result + ((this.licAction == null) ? 0 : this.licAction.hashCode());
        result = prime * result + ((this.licPermission == null) ? 0 : this.licPermission.hashCode());
        result = prime * result + ((this.licView == null) ? 0 : this.licView.hashCode());
        result = prime * result + ((this.active == null) ? 0 : this.active.hashCode());
        result = prime * result + ((this.sigma == null) ? 0 : this.sigma.hashCode());
        result = prime * result + ((this.metadata == null) ? 0 : this.metadata.hashCode());
        result = prime * result + ((this.language == null) ? 0 : this.language.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BAuthority (");

        sb.append(key);
        sb.append(", ").append(code);
        sb.append(", ").append(blockId);
        sb.append(", ").append(type);
        sb.append(", ").append(licResource);
        sb.append(", ").append(licAction);
        sb.append(", ").append(licPermission);
        sb.append(", ").append(licView);
        sb.append(", ").append(active);
        sb.append(", ").append(sigma);
        sb.append(", ").append(metadata);
        sb.append(", ").append(language);

        sb.append(")");
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    @Override
    public void from(IBAuthority from) {
        setKey(from.getKey());
        setCode(from.getCode());
        setBlockId(from.getBlockId());
        setType(from.getType());
        setLicResource(from.getLicResource());
        setLicAction(from.getLicAction());
        setLicPermission(from.getLicPermission());
        setLicView(from.getLicView());
        setActive(from.getActive());
        setSigma(from.getSigma());
        setMetadata(from.getMetadata());
        setLanguage(from.getLanguage());
    }

    @Override
    public <E extends IBAuthority> E into(E into) {
        into.from(this);
        return into;
    }
}
