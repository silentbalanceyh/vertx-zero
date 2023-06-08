/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.ambient.domain.tables.interfaces;


import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.io.Serializable;
import java.time.LocalDateTime;

import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.setOrThrow;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface IXApp extends VertxPojo, Serializable {

    /**
     * Setter for <code>DB_ETERNAL.X_APP.KEY</code>. 「key」- 应用程序主键
     */
    public IXApp setKey(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.KEY</code>. 「key」- 应用程序主键
     */
    public String getKey();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.NAME</code>. 「name」- 应用程序名称
     */
    public IXApp setName(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.NAME</code>. 「name」- 应用程序名称
     */
    public String getName();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.CODE</code>. 「code」- 应用程序编码
     */
    public IXApp setCode(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.CODE</code>. 「code」- 应用程序编码
     */
    public String getCode();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.TITLE</code>. 「title」- 应用程序标题
     */
    public IXApp setTitle(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.TITLE</code>. 「title」- 应用程序标题
     */
    public String getTitle();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.LOGO</code>. 「logo」- 应用程序图标
     */
    public IXApp setLogo(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.LOGO</code>. 「logo」- 应用程序图标
     */
    public String getLogo();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.ICP</code>. 「icp」- ICP备案号
     */
    public IXApp setIcp(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.ICP</code>. 「icp」- ICP备案号
     */
    public String getIcp();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.COPY_RIGHT</code>. 「copyRight」-
     * CopyRight版权信息
     */
    public IXApp setCopyRight(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.COPY_RIGHT</code>. 「copyRight」-
     * CopyRight版权信息
     */
    public String getCopyRight();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.EMAIL</code>. 「email」- 应用Email信息
     */
    public IXApp setEmail(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.EMAIL</code>. 「email」- 应用Email信息
     */
    public String getEmail();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.DOMAIN</code>. 「domain」- 应用程序所在域
     */
    public IXApp setDomain(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.DOMAIN</code>. 「domain」- 应用程序所在域
     */
    public String getDomain();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.APP_PORT</code>. 「appPort」-
     * 应用程序端口号，和SOURCE的端口号区别开
     */
    public IXApp setAppPort(Integer value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.APP_PORT</code>. 「appPort」-
     * 应用程序端口号，和SOURCE的端口号区别开
     */
    public Integer getAppPort();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.URL_ENTRY</code>. 「urlEntry」—
     * 应用程序入口页面（登录页）
     */
    public IXApp setUrlEntry(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.URL_ENTRY</code>. 「urlEntry」—
     * 应用程序入口页面（登录页）
     */
    public String getUrlEntry();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.URL_MAIN</code>. 「urlMain」-
     * 应用程序内置主页（带安全）
     */
    public IXApp setUrlMain(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.URL_MAIN</code>. 「urlMain」-
     * 应用程序内置主页（带安全）
     */
    public String getUrlMain();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.PATH</code>. 「path」- 应用程序路径
     */
    public IXApp setPath(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.PATH</code>. 「path」- 应用程序路径
     */
    public String getPath();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.ROUTE</code>. 「route」- 后端API的根路径，启动时需要
     */
    public IXApp setRoute(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.ROUTE</code>. 「route」- 后端API的根路径，启动时需要
     */
    public String getRoute();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.APP_KEY</code>. 「appKey」-
     * 应用程序专用唯一hashKey
     */
    public IXApp setAppKey(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.APP_KEY</code>. 「appKey」-
     * 应用程序专用唯一hashKey
     */
    public String getAppKey();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.ACTIVE</code>. 「active」- 是否启用
     */
    public IXApp setActive(Boolean value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.ACTIVE</code>. 「active」- 是否启用
     */
    public Boolean getActive();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.SIGMA</code>. 「sigma」- 统一标识
     */
    public IXApp setSigma(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.SIGMA</code>. 「sigma」- 统一标识
     */
    public String getSigma();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.METADATA</code>. 「metadata」- 附加配置
     */
    public IXApp setMetadata(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.METADATA</code>. 「metadata」- 附加配置
     */
    public String getMetadata();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.LANGUAGE</code>. 「language」- 使用的语言
     */
    public IXApp setLanguage(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.LANGUAGE</code>. 「language」- 使用的语言
     */
    public String getLanguage();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public IXApp setCreatedAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public LocalDateTime getCreatedAt();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public IXApp setCreatedBy(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public String getCreatedBy();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public IXApp setUpdatedAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public LocalDateTime getUpdatedAt();

    /**
     * Setter for <code>DB_ETERNAL.X_APP.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public IXApp setUpdatedBy(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_APP.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public String getUpdatedBy();

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common
     * interface IXApp
     */
    public void from(IXApp from);

    /**
     * Copy data into another generated Record/POJO implementing the common
     * interface IXApp
     */
    public <E extends IXApp> E into(E into);

        @Override
        public default IXApp fromJson(io.vertx.core.json.JsonObject json) {
                setOrThrow(this::setKey,json::getString,"KEY","java.lang.String");
                setOrThrow(this::setName,json::getString,"NAME","java.lang.String");
                setOrThrow(this::setCode,json::getString,"CODE","java.lang.String");
                setOrThrow(this::setTitle,json::getString,"TITLE","java.lang.String");
                setOrThrow(this::setLogo,json::getString,"LOGO","java.lang.String");
                setOrThrow(this::setIcp,json::getString,"ICP","java.lang.String");
                setOrThrow(this::setCopyRight,json::getString,"COPY_RIGHT","java.lang.String");
                setOrThrow(this::setEmail,json::getString,"EMAIL","java.lang.String");
                setOrThrow(this::setDomain,json::getString,"DOMAIN","java.lang.String");
                setOrThrow(this::setAppPort,json::getInteger,"APP_PORT","java.lang.Integer");
                setOrThrow(this::setUrlEntry,json::getString,"URL_ENTRY","java.lang.String");
                setOrThrow(this::setUrlMain,json::getString,"URL_MAIN","java.lang.String");
                setOrThrow(this::setPath,json::getString,"PATH","java.lang.String");
                setOrThrow(this::setRoute,json::getString,"ROUTE","java.lang.String");
                setOrThrow(this::setAppKey,json::getString,"APP_KEY","java.lang.String");
                setOrThrow(this::setActive,json::getBoolean,"ACTIVE","java.lang.Boolean");
                setOrThrow(this::setSigma,json::getString,"SIGMA","java.lang.String");
                setOrThrow(this::setMetadata,json::getString,"METADATA","java.lang.String");
                setOrThrow(this::setLanguage,json::getString,"LANGUAGE","java.lang.String");
                setOrThrow(this::setCreatedAt,key -> {String s = json.getString(key); return s==null?null:java.time.LocalDateTime.parse(s);},"CREATED_AT","java.time.LocalDateTime");
                setOrThrow(this::setCreatedBy,json::getString,"CREATED_BY","java.lang.String");
                setOrThrow(this::setUpdatedAt,key -> {String s = json.getString(key); return s==null?null:java.time.LocalDateTime.parse(s);},"UPDATED_AT","java.time.LocalDateTime");
                setOrThrow(this::setUpdatedBy,json::getString,"UPDATED_BY","java.lang.String");
                return this;
        }


        @Override
        public default io.vertx.core.json.JsonObject toJson() {
                io.vertx.core.json.JsonObject json = new io.vertx.core.json.JsonObject();
                json.put("KEY",getKey());
                json.put("NAME",getName());
                json.put("CODE",getCode());
                json.put("TITLE",getTitle());
                json.put("LOGO",getLogo());
                json.put("ICP",getIcp());
                json.put("COPY_RIGHT",getCopyRight());
                json.put("EMAIL",getEmail());
                json.put("DOMAIN",getDomain());
                json.put("APP_PORT",getAppPort());
                json.put("URL_ENTRY",getUrlEntry());
                json.put("URL_MAIN",getUrlMain());
                json.put("PATH",getPath());
                json.put("ROUTE",getRoute());
                json.put("APP_KEY",getAppKey());
                json.put("ACTIVE",getActive());
                json.put("SIGMA",getSigma());
                json.put("METADATA",getMetadata());
                json.put("LANGUAGE",getLanguage());
                json.put("CREATED_AT",getCreatedAt()==null?null:getCreatedAt().toString());
                json.put("CREATED_BY",getCreatedBy());
                json.put("UPDATED_AT",getUpdatedAt()==null?null:getUpdatedAt().toString());
                json.put("UPDATED_BY",getUpdatedBy());
                return json;
        }

}
