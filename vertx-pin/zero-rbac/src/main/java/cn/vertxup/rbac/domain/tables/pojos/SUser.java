/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.rbac.domain.tables.pojos;


import cn.vertxup.rbac.domain.tables.interfaces.ISUser;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SUser implements VertxPojo, ISUser {

    private static final long serialVersionUID = 1L;

    private String        key;
    private String        username;
    private String        realname;
    private String        alias;
    private String        mobile;
    private String        email;
    private String        password;
    private String        modelId;
    private String        modelKey;
    private String        category;
    private String        sigma;
    private String        language;
    private Boolean       active;
    private String        metadata;
    private LocalDateTime createdAt;
    private String        createdBy;
    private LocalDateTime updatedAt;
    private String        updatedBy;

    public SUser() {}

    public SUser(ISUser value) {
        this.key = value.getKey();
        this.username = value.getUsername();
        this.realname = value.getRealname();
        this.alias = value.getAlias();
        this.mobile = value.getMobile();
        this.email = value.getEmail();
        this.password = value.getPassword();
        this.modelId = value.getModelId();
        this.modelKey = value.getModelKey();
        this.category = value.getCategory();
        this.sigma = value.getSigma();
        this.language = value.getLanguage();
        this.active = value.getActive();
        this.metadata = value.getMetadata();
        this.createdAt = value.getCreatedAt();
        this.createdBy = value.getCreatedBy();
        this.updatedAt = value.getUpdatedAt();
        this.updatedBy = value.getUpdatedBy();
    }

    public SUser(
        String        key,
        String        username,
        String        realname,
        String        alias,
        String        mobile,
        String        email,
        String        password,
        String        modelId,
        String        modelKey,
        String        category,
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
        this.username = username;
        this.realname = realname;
        this.alias = alias;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.modelId = modelId;
        this.modelKey = modelKey;
        this.category = category;
        this.sigma = sigma;
        this.language = language;
        this.active = active;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

        public SUser(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.S_USER.KEY</code>. 「key」- 用户ID
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_USER.KEY</code>. 「key」- 用户ID
     */
    @Override
    public SUser setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_USER.USERNAME</code>. 「username」- 用户登录账号
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_USER.USERNAME</code>. 「username」- 用户登录账号
     */
    @Override
    public SUser setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_USER.REALNAME</code>. 「realname」- 用户真实姓名
     */
    @Override
    public String getRealname() {
        return this.realname;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_USER.REALNAME</code>. 「realname」- 用户真实姓名
     */
    @Override
    public SUser setRealname(String realname) {
        this.realname = realname;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_USER.ALIAS</code>. 「alias」- 用户昵称
     */
    @Override
    public String getAlias() {
        return this.alias;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_USER.ALIAS</code>. 「alias」- 用户昵称
     */
    @Override
    public SUser setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_USER.MOBILE</code>. 「mobile」- 用户登录手机
     */
    @Override
    public String getMobile() {
        return this.mobile;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_USER.MOBILE</code>. 「mobile」- 用户登录手机
     */
    @Override
    public SUser setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_USER.EMAIL</code>. 「email」- 用户登录EMAIL地址
     */
    @Override
    public String getEmail() {
        return this.email;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_USER.EMAIL</code>. 「email」- 用户登录EMAIL地址
     */
    @Override
    public SUser setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_USER.PASSWORD</code>. 「password」- 用户登录密码
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_USER.PASSWORD</code>. 「password」- 用户登录密码
     */
    @Override
    public SUser setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_USER.MODEL_ID</code>. 「modelId」-
     * 组所关联的模型identifier，用于描述
     */
    @Override
    public String getModelId() {
        return this.modelId;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_USER.MODEL_ID</code>. 「modelId」-
     * 组所关联的模型identifier，用于描述
     */
    @Override
    public SUser setModelId(String modelId) {
        this.modelId = modelId;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_USER.MODEL_KEY</code>. 「modelKey」-
     * 组所关联的模型记录ID，用于描述哪一个Model中的记录
     */
    @Override
    public String getModelKey() {
        return this.modelKey;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_USER.MODEL_KEY</code>. 「modelKey」-
     * 组所关联的模型记录ID，用于描述哪一个Model中的记录
     */
    @Override
    public SUser setModelKey(String modelKey) {
        this.modelKey = modelKey;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_USER.CATEGORY</code>. 「category」- 用户分类
     */
    @Override
    public String getCategory() {
        return this.category;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_USER.CATEGORY</code>. 「category」- 用户分类
     */
    @Override
    public SUser setCategory(String category) {
        this.category = category;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_USER.SIGMA</code>. 「sigma」- 用户绑定的统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_USER.SIGMA</code>. 「sigma」- 用户绑定的统一标识
     */
    @Override
    public SUser setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_USER.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_USER.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public SUser setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_USER.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_USER.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public SUser setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_USER.METADATA</code>. 「metadata」- 附加配置数据
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_USER.METADATA</code>. 「metadata」- 附加配置数据
     */
    @Override
    public SUser setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_USER.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_USER.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    @Override
    public SUser setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_USER.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_USER.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public SUser setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_USER.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_USER.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    @Override
    public SUser setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_USER.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.S_USER.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public SUser setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SUser (");

        sb.append(key);
        sb.append(", ").append(username);
        sb.append(", ").append(realname);
        sb.append(", ").append(alias);
        sb.append(", ").append(mobile);
        sb.append(", ").append(email);
        sb.append(", ").append(password);
        sb.append(", ").append(modelId);
        sb.append(", ").append(modelKey);
        sb.append(", ").append(category);
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
    public void from(ISUser from) {
        setKey(from.getKey());
        setUsername(from.getUsername());
        setRealname(from.getRealname());
        setAlias(from.getAlias());
        setMobile(from.getMobile());
        setEmail(from.getEmail());
        setPassword(from.getPassword());
        setModelId(from.getModelId());
        setModelKey(from.getModelKey());
        setCategory(from.getCategory());
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
    public <E extends ISUser> E into(E into) {
        into.from(this);
        return into;
    }
}
