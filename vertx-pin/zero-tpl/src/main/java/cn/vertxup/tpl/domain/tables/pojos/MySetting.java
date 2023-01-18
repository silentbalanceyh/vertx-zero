/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.tpl.domain.tables.pojos;


import cn.vertxup.tpl.domain.tables.interfaces.IMySetting;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MySetting implements VertxPojo, IMySetting {

    private static final long serialVersionUID = 1L;

    private String key;
    private String pNavTheme;
    private String pColorPrimary;
    private String pLayout;
    private String pContentWidth;
    private Boolean pFixedHeader;
    private Boolean pFixSiderBar;
    private Boolean pColorWeak;
    private Boolean pPwa;
    private String pToken;
    private String myBag;
    private String owner;
    private String ownerType;
    private String type;
    private Boolean active;
    private String sigma;
    private String metadata;
    private String language;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public MySetting() {}

    public MySetting(IMySetting value) {
        this.key = value.getKey();
        this.pNavTheme = value.getPNavTheme();
        this.pColorPrimary = value.getPColorPrimary();
        this.pLayout = value.getPLayout();
        this.pContentWidth = value.getPContentWidth();
        this.pFixedHeader = value.getPFixedHeader();
        this.pFixSiderBar = value.getPFixSiderBar();
        this.pColorWeak = value.getPColorWeak();
        this.pPwa = value.getPPwa();
        this.pToken = value.getPToken();
        this.myBag = value.getMyBag();
        this.owner = value.getOwner();
        this.ownerType = value.getOwnerType();
        this.type = value.getType();
        this.active = value.getActive();
        this.sigma = value.getSigma();
        this.metadata = value.getMetadata();
        this.language = value.getLanguage();
        this.createdAt = value.getCreatedAt();
        this.createdBy = value.getCreatedBy();
        this.updatedAt = value.getUpdatedAt();
        this.updatedBy = value.getUpdatedBy();
    }

    public MySetting(
        String key,
        String pNavTheme,
        String pColorPrimary,
        String pLayout,
        String pContentWidth,
        Boolean pFixedHeader,
        Boolean pFixSiderBar,
        Boolean pColorWeak,
        Boolean pPwa,
        String pToken,
        String myBag,
        String owner,
        String ownerType,
        String type,
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
        this.pNavTheme = pNavTheme;
        this.pColorPrimary = pColorPrimary;
        this.pLayout = pLayout;
        this.pContentWidth = pContentWidth;
        this.pFixedHeader = pFixedHeader;
        this.pFixSiderBar = pFixSiderBar;
        this.pColorWeak = pColorWeak;
        this.pPwa = pPwa;
        this.pToken = pToken;
        this.myBag = myBag;
        this.owner = owner;
        this.ownerType = ownerType;
        this.type = type;
        this.active = active;
        this.sigma = sigma;
        this.metadata = metadata;
        this.language = language;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

        public MySetting(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.KEY</code>. 「key」- 个人设置主键
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.KEY</code>. 「key」- 个人设置主键
     */
    @Override
    public MySetting setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.P_NAV_THEME</code>. 「pNavTheme」-
     * navTheme, 风格处理，对应 light / realdark
     */
    @Override
    public String getPNavTheme() {
        return this.pNavTheme;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.P_NAV_THEME</code>. 「pNavTheme」-
     * navTheme, 风格处理，对应 light / realdark
     */
    @Override
    public MySetting setPNavTheme(String pNavTheme) {
        this.pNavTheme = pNavTheme;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.P_COLOR_PRIMARY</code>.
     * 「pColorPrimary」- colorPrimary，主色调
     */
    @Override
    public String getPColorPrimary() {
        return this.pColorPrimary;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.P_COLOR_PRIMARY</code>.
     * 「pColorPrimary」- colorPrimary，主色调
     */
    @Override
    public MySetting setPColorPrimary(String pColorPrimary) {
        this.pColorPrimary = pColorPrimary;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.P_LAYOUT</code>. 「pLayout」-
     * 布局类型：top, menu, mix
     */
    @Override
    public String getPLayout() {
        return this.pLayout;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.P_LAYOUT</code>. 「pLayout」-
     * 布局类型：top, menu, mix
     */
    @Override
    public MySetting setPLayout(String pLayout) {
        this.pLayout = pLayout;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.P_CONTENT_WIDTH</code>.
     * 「pContentWidth」- 两种
     */
    @Override
    public String getPContentWidth() {
        return this.pContentWidth;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.P_CONTENT_WIDTH</code>.
     * 「pContentWidth」- 两种
     */
    @Override
    public MySetting setPContentWidth(String pContentWidth) {
        this.pContentWidth = pContentWidth;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.P_FIXED_HEADER</code>.
     * 「pFixedHeader」- 标题控制
     */
    @Override
    public Boolean getPFixedHeader() {
        return this.pFixedHeader;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.P_FIXED_HEADER</code>.
     * 「pFixedHeader」- 标题控制
     */
    @Override
    public MySetting setPFixedHeader(Boolean pFixedHeader) {
        this.pFixedHeader = pFixedHeader;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.P_FIX_SIDER_BAR</code>.
     * 「pFixSiderBar」- 侧边栏控制
     */
    @Override
    public Boolean getPFixSiderBar() {
        return this.pFixSiderBar;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.P_FIX_SIDER_BAR</code>.
     * 「pFixSiderBar」- 侧边栏控制
     */
    @Override
    public MySetting setPFixSiderBar(Boolean pFixSiderBar) {
        this.pFixSiderBar = pFixSiderBar;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.P_COLOR_WEAK</code>. 「pColorWeak」-
     * 色彩控制
     */
    @Override
    public Boolean getPColorWeak() {
        return this.pColorWeak;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.P_COLOR_WEAK</code>. 「pColorWeak」-
     * 色彩控制
     */
    @Override
    public MySetting setPColorWeak(Boolean pColorWeak) {
        this.pColorWeak = pColorWeak;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.P_PWA</code>. 「pPwa」- pwa属性，暂时未知
     */
    @Override
    public Boolean getPPwa() {
        return this.pPwa;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.P_PWA</code>. 「pPwa」- pwa属性，暂时未知
     */
    @Override
    public MySetting setPPwa(Boolean pPwa) {
        this.pPwa = pPwa;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.P_TOKEN</code>. 「pToken」-
     * 保留（后续可能会使用）
     */
    @Override
    public String getPToken() {
        return this.pToken;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.P_TOKEN</code>. 「pToken」-
     * 保留（后续可能会使用）
     */
    @Override
    public MySetting setPToken(String pToken) {
        this.pToken = pToken;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.MY_BAG</code>. 「myBag」- 对应 MY_BAG
     * 设置，每个BAG有对应设置信息
     */
    @Override
    public String getMyBag() {
        return this.myBag;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.MY_BAG</code>. 「myBag」- 对应 MY_BAG
     * 设置，每个BAG有对应设置信息
     */
    @Override
    public MySetting setMyBag(String myBag) {
        this.myBag = myBag;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.OWNER</code>. 「owner」- 拥有者ID，我的 /
     * 角色级
     */
    @Override
    public String getOwner() {
        return this.owner;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.OWNER</code>. 「owner」- 拥有者ID，我的 /
     * 角色级
     */
    @Override
    public MySetting setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.OWNER_TYPE</code>. 「ownerType」-
     * ROLE 角色，USER 用户
     */
    @Override
    public String getOwnerType() {
        return this.ownerType;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.OWNER_TYPE</code>. 「ownerType」-
     * ROLE 角色，USER 用户
     */
    @Override
    public MySetting setOwnerType(String ownerType) {
        this.ownerType = ownerType;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.TYPE</code>. 「type」- 类型（默认全站）
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.TYPE</code>. 「type」- 类型（默认全站）
     */
    @Override
    public MySetting setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public MySetting setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public MySetting setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public MySetting setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public MySetting setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public MySetting setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public MySetting setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public MySetting setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_SETTING.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_SETTING.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public MySetting setUpdatedBy(String updatedBy) {
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
        final MySetting other = (MySetting) obj;
        if (this.key == null) {
            if (other.key != null)
                return false;
        }
        else if (!this.key.equals(other.key))
            return false;
        if (this.pNavTheme == null) {
            if (other.pNavTheme != null)
                return false;
        }
        else if (!this.pNavTheme.equals(other.pNavTheme))
            return false;
        if (this.pColorPrimary == null) {
            if (other.pColorPrimary != null)
                return false;
        }
        else if (!this.pColorPrimary.equals(other.pColorPrimary))
            return false;
        if (this.pLayout == null) {
            if (other.pLayout != null)
                return false;
        }
        else if (!this.pLayout.equals(other.pLayout))
            return false;
        if (this.pContentWidth == null) {
            if (other.pContentWidth != null)
                return false;
        }
        else if (!this.pContentWidth.equals(other.pContentWidth))
            return false;
        if (this.pFixedHeader == null) {
            if (other.pFixedHeader != null)
                return false;
        }
        else if (!this.pFixedHeader.equals(other.pFixedHeader))
            return false;
        if (this.pFixSiderBar == null) {
            if (other.pFixSiderBar != null)
                return false;
        }
        else if (!this.pFixSiderBar.equals(other.pFixSiderBar))
            return false;
        if (this.pColorWeak == null) {
            if (other.pColorWeak != null)
                return false;
        }
        else if (!this.pColorWeak.equals(other.pColorWeak))
            return false;
        if (this.pPwa == null) {
            if (other.pPwa != null)
                return false;
        }
        else if (!this.pPwa.equals(other.pPwa))
            return false;
        if (this.pToken == null) {
            if (other.pToken != null)
                return false;
        }
        else if (!this.pToken.equals(other.pToken))
            return false;
        if (this.myBag == null) {
            if (other.myBag != null)
                return false;
        }
        else if (!this.myBag.equals(other.myBag))
            return false;
        if (this.owner == null) {
            if (other.owner != null)
                return false;
        }
        else if (!this.owner.equals(other.owner))
            return false;
        if (this.ownerType == null) {
            if (other.ownerType != null)
                return false;
        }
        else if (!this.ownerType.equals(other.ownerType))
            return false;
        if (this.type == null) {
            if (other.type != null)
                return false;
        }
        else if (!this.type.equals(other.type))
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
        result = prime * result + ((this.pNavTheme == null) ? 0 : this.pNavTheme.hashCode());
        result = prime * result + ((this.pColorPrimary == null) ? 0 : this.pColorPrimary.hashCode());
        result = prime * result + ((this.pLayout == null) ? 0 : this.pLayout.hashCode());
        result = prime * result + ((this.pContentWidth == null) ? 0 : this.pContentWidth.hashCode());
        result = prime * result + ((this.pFixedHeader == null) ? 0 : this.pFixedHeader.hashCode());
        result = prime * result + ((this.pFixSiderBar == null) ? 0 : this.pFixSiderBar.hashCode());
        result = prime * result + ((this.pColorWeak == null) ? 0 : this.pColorWeak.hashCode());
        result = prime * result + ((this.pPwa == null) ? 0 : this.pPwa.hashCode());
        result = prime * result + ((this.pToken == null) ? 0 : this.pToken.hashCode());
        result = prime * result + ((this.myBag == null) ? 0 : this.myBag.hashCode());
        result = prime * result + ((this.owner == null) ? 0 : this.owner.hashCode());
        result = prime * result + ((this.ownerType == null) ? 0 : this.ownerType.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
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
        StringBuilder sb = new StringBuilder("MySetting (");

        sb.append(key);
        sb.append(", ").append(pNavTheme);
        sb.append(", ").append(pColorPrimary);
        sb.append(", ").append(pLayout);
        sb.append(", ").append(pContentWidth);
        sb.append(", ").append(pFixedHeader);
        sb.append(", ").append(pFixSiderBar);
        sb.append(", ").append(pColorWeak);
        sb.append(", ").append(pPwa);
        sb.append(", ").append(pToken);
        sb.append(", ").append(myBag);
        sb.append(", ").append(owner);
        sb.append(", ").append(ownerType);
        sb.append(", ").append(type);
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
    public void from(IMySetting from) {
        setKey(from.getKey());
        setPNavTheme(from.getPNavTheme());
        setPColorPrimary(from.getPColorPrimary());
        setPLayout(from.getPLayout());
        setPContentWidth(from.getPContentWidth());
        setPFixedHeader(from.getPFixedHeader());
        setPFixSiderBar(from.getPFixSiderBar());
        setPColorWeak(from.getPColorWeak());
        setPPwa(from.getPPwa());
        setPToken(from.getPToken());
        setMyBag(from.getMyBag());
        setOwner(from.getOwner());
        setOwnerType(from.getOwnerType());
        setType(from.getType());
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
    public <E extends IMySetting> E into(E into) {
        into.from(this);
        return into;
    }
}
