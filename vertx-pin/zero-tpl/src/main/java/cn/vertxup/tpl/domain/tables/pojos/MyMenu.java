/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.tpl.domain.tables.pojos;


import cn.vertxup.tpl.domain.tables.interfaces.IMyMenu;
import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MyMenu implements VertxPojo, IMyMenu {

    private static final long serialVersionUID = 1L;

    private String key;
    private String icon;
    private String text;
    private String uri;
    private Long uiSort;
    private String uiParent;
    private String uiColorFg;
    private String uiColorBg;
    private String type;
    private String page;
    private String position;
    private String owner;
    private String ownerType;
    private Boolean active;
    private String sigma;
    private String metadata;
    private String language;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public MyMenu() {}

    public MyMenu(IMyMenu value) {
        this.key = value.getKey();
        this.icon = value.getIcon();
        this.text = value.getText();
        this.uri = value.getUri();
        this.uiSort = value.getUiSort();
        this.uiParent = value.getUiParent();
        this.uiColorFg = value.getUiColorFg();
        this.uiColorBg = value.getUiColorBg();
        this.type = value.getType();
        this.page = value.getPage();
        this.position = value.getPosition();
        this.owner = value.getOwner();
        this.ownerType = value.getOwnerType();
        this.active = value.getActive();
        this.sigma = value.getSigma();
        this.metadata = value.getMetadata();
        this.language = value.getLanguage();
        this.createdAt = value.getCreatedAt();
        this.createdBy = value.getCreatedBy();
        this.updatedAt = value.getUpdatedAt();
        this.updatedBy = value.getUpdatedBy();
    }

    public MyMenu(
        String key,
        String icon,
        String text,
        String uri,
        Long uiSort,
        String uiParent,
        String uiColorFg,
        String uiColorBg,
        String type,
        String page,
        String position,
        String owner,
        String ownerType,
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
        this.icon = icon;
        this.text = text;
        this.uri = uri;
        this.uiSort = uiSort;
        this.uiParent = uiParent;
        this.uiColorFg = uiColorFg;
        this.uiColorBg = uiColorBg;
        this.type = type;
        this.page = page;
        this.position = position;
        this.owner = owner;
        this.ownerType = ownerType;
        this.active = active;
        this.sigma = sigma;
        this.metadata = metadata;
        this.language = language;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

        public MyMenu(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.KEY</code>. 「key」- 菜单主键
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.KEY</code>. 「key」- 菜单主键
     */
    @Override
    public MyMenu setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.ICON</code>. 「icon」- 菜单使用的icon
     */
    @Override
    public String getIcon() {
        return this.icon;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.ICON</code>. 「icon」- 菜单使用的icon
     */
    @Override
    public MyMenu setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.TEXT</code>. 「text」- 菜单显示文字
     */
    @Override
    public String getText() {
        return this.text;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.TEXT</code>. 「text」- 菜单显示文字
     */
    @Override
    public MyMenu setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.URI</code>. 「uri」- 菜单地址（不包含应用的path）
     */
    @Override
    public String getUri() {
        return this.uri;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.URI</code>. 「uri」- 菜单地址（不包含应用的path）
     */
    @Override
    public MyMenu setUri(String uri) {
        this.uri = uri;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.UI_SORT</code>. 「uiSort」- 菜单排序
     */
    @Override
    public Long getUiSort() {
        return this.uiSort;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.UI_SORT</code>. 「uiSort」- 菜单排序
     */
    @Override
    public MyMenu setUiSort(Long uiSort) {
        this.uiSort = uiSort;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.UI_PARENT</code>. 「uiParent」- 菜单父ID
     */
    @Override
    public String getUiParent() {
        return this.uiParent;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.UI_PARENT</code>. 「uiParent」- 菜单父ID
     */
    @Override
    public MyMenu setUiParent(String uiParent) {
        this.uiParent = uiParent;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.UI_COLOR_FG</code>. 「uiColorFg」- 前景色
     */
    @Override
    public String getUiColorFg() {
        return this.uiColorFg;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.UI_COLOR_FG</code>. 「uiColorFg」- 前景色
     */
    @Override
    public MyMenu setUiColorFg(String uiColorFg) {
        this.uiColorFg = uiColorFg;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.UI_COLOR_BG</code>. 「uiColorBg」- 背景色
     */
    @Override
    public String getUiColorBg() {
        return this.uiColorBg;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.UI_COLOR_BG</code>. 「uiColorBg」- 背景色
     */
    @Override
    public MyMenu setUiColorBg(String uiColorBg) {
        this.uiColorBg = uiColorBg;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.TYPE</code>. 「type」- 菜单类型
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.TYPE</code>. 「type」- 菜单类型
     */
    @Override
    public MyMenu setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.PAGE</code>. 「page」- 菜单所在页面
     */
    @Override
    public String getPage() {
        return this.page;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.PAGE</code>. 「page」- 菜单所在页面
     */
    @Override
    public MyMenu setPage(String page) {
        this.page = page;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.POSITION</code>. 「position」- 菜单位置
     */
    @Override
    public String getPosition() {
        return this.position;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.POSITION</code>. 「position」- 菜单位置
     */
    @Override
    public MyMenu setPosition(String position) {
        this.position = position;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.OWNER</code>. 「owner」- 拥有者ID，我的 / 角色级
     */
    @Override
    public String getOwner() {
        return this.owner;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.OWNER</code>. 「owner」- 拥有者ID，我的 / 角色级
     */
    @Override
    public MyMenu setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.OWNER_TYPE</code>. 「ownerType」- ROLE
     * 角色，USER 用户
     */
    @Override
    public String getOwnerType() {
        return this.ownerType;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.OWNER_TYPE</code>. 「ownerType」- ROLE
     * 角色，USER 用户
     */
    @Override
    public MyMenu setOwnerType(String ownerType) {
        this.ownerType = ownerType;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public MyMenu setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public MyMenu setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public MyMenu setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public MyMenu setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    @Override
    public MyMenu setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public MyMenu setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    @Override
    public MyMenu setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.MY_MENU.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.MY_MENU.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public MyMenu setUpdatedBy(String updatedBy) {
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
        final MyMenu other = (MyMenu) obj;
        if (this.key == null) {
            if (other.key != null)
                return false;
        }
        else if (!this.key.equals(other.key))
            return false;
        if (this.icon == null) {
            if (other.icon != null)
                return false;
        }
        else if (!this.icon.equals(other.icon))
            return false;
        if (this.text == null) {
            if (other.text != null)
                return false;
        }
        else if (!this.text.equals(other.text))
            return false;
        if (this.uri == null) {
            if (other.uri != null)
                return false;
        }
        else if (!this.uri.equals(other.uri))
            return false;
        if (this.uiSort == null) {
            if (other.uiSort != null)
                return false;
        }
        else if (!this.uiSort.equals(other.uiSort))
            return false;
        if (this.uiParent == null) {
            if (other.uiParent != null)
                return false;
        }
        else if (!this.uiParent.equals(other.uiParent))
            return false;
        if (this.uiColorFg == null) {
            if (other.uiColorFg != null)
                return false;
        }
        else if (!this.uiColorFg.equals(other.uiColorFg))
            return false;
        if (this.uiColorBg == null) {
            if (other.uiColorBg != null)
                return false;
        }
        else if (!this.uiColorBg.equals(other.uiColorBg))
            return false;
        if (this.type == null) {
            if (other.type != null)
                return false;
        }
        else if (!this.type.equals(other.type))
            return false;
        if (this.page == null) {
            if (other.page != null)
                return false;
        }
        else if (!this.page.equals(other.page))
            return false;
        if (this.position == null) {
            if (other.position != null)
                return false;
        }
        else if (!this.position.equals(other.position))
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
        result = prime * result + ((this.icon == null) ? 0 : this.icon.hashCode());
        result = prime * result + ((this.text == null) ? 0 : this.text.hashCode());
        result = prime * result + ((this.uri == null) ? 0 : this.uri.hashCode());
        result = prime * result + ((this.uiSort == null) ? 0 : this.uiSort.hashCode());
        result = prime * result + ((this.uiParent == null) ? 0 : this.uiParent.hashCode());
        result = prime * result + ((this.uiColorFg == null) ? 0 : this.uiColorFg.hashCode());
        result = prime * result + ((this.uiColorBg == null) ? 0 : this.uiColorBg.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        result = prime * result + ((this.page == null) ? 0 : this.page.hashCode());
        result = prime * result + ((this.position == null) ? 0 : this.position.hashCode());
        result = prime * result + ((this.owner == null) ? 0 : this.owner.hashCode());
        result = prime * result + ((this.ownerType == null) ? 0 : this.ownerType.hashCode());
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
        StringBuilder sb = new StringBuilder("MyMenu (");

        sb.append(key);
        sb.append(", ").append(icon);
        sb.append(", ").append(text);
        sb.append(", ").append(uri);
        sb.append(", ").append(uiSort);
        sb.append(", ").append(uiParent);
        sb.append(", ").append(uiColorFg);
        sb.append(", ").append(uiColorBg);
        sb.append(", ").append(type);
        sb.append(", ").append(page);
        sb.append(", ").append(position);
        sb.append(", ").append(owner);
        sb.append(", ").append(ownerType);
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
    public void from(IMyMenu from) {
        setKey(from.getKey());
        setIcon(from.getIcon());
        setText(from.getText());
        setUri(from.getUri());
        setUiSort(from.getUiSort());
        setUiParent(from.getUiParent());
        setUiColorFg(from.getUiColorFg());
        setUiColorBg(from.getUiColorBg());
        setType(from.getType());
        setPage(from.getPage());
        setPosition(from.getPosition());
        setOwner(from.getOwner());
        setOwnerType(from.getOwnerType());
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
    public <E extends IMyMenu> E into(E into) {
        into.from(this);
        return into;
    }
}
