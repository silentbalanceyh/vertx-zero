/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.ambient.domain.tables.interfaces;


import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.io.Serializable;
import java.time.LocalDateTime;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface IXModule extends VertxPojo, Serializable {

    /**
     * Setter for <code>DB_ETERNAL.X_MODULE.KEY</code>. 「key」- 模块唯一主键
     */
    public IXModule setKey(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_MODULE.KEY</code>. 「key」- 模块唯一主键
     */
    public String getKey();

    /**
     * Setter for <code>DB_ETERNAL.X_MODULE.NAME</code>. 「name」- 模块名称
     */
    public IXModule setName(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_MODULE.NAME</code>. 「name」- 模块名称
     */
    public String getName();

    /**
     * Setter for <code>DB_ETERNAL.X_MODULE.CODE</code>. 「code」- 模块编码
     */
    public IXModule setCode(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_MODULE.CODE</code>. 「code」- 模块编码
     */
    public String getCode();

    /**
     * Setter for <code>DB_ETERNAL.X_MODULE.ENTRY</code>. 「entry」— 模块入口地址
     */
    public IXModule setEntry(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_MODULE.ENTRY</code>. 「entry」— 模块入口地址
     */
    public String getEntry();

    /**
     * Setter for <code>DB_ETERNAL.X_MODULE.APP_ID</code>. 「appId」- 关联的应用程序ID
     */
    public IXModule setAppId(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_MODULE.APP_ID</code>. 「appId」- 关联的应用程序ID
     */
    public String getAppId();

    /**
     * Setter for <code>DB_ETERNAL.X_MODULE.MODEL_ID</code>. 「modelId」-
     * 当前模块关联的主模型ID
     */
    public IXModule setModelId(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_MODULE.MODEL_ID</code>. 「modelId」-
     * 当前模块关联的主模型ID
     */
    public String getModelId();

    /**
     * Setter for <code>DB_ETERNAL.X_MODULE.ACTIVE</code>. 「active」- 是否启用
     */
    public IXModule setActive(Boolean value);

    /**
     * Getter for <code>DB_ETERNAL.X_MODULE.ACTIVE</code>. 「active」- 是否启用
     */
    public Boolean getActive();

    /**
     * Setter for <code>DB_ETERNAL.X_MODULE.SIGMA</code>. 「sigma」- 统一标识
     */
    public IXModule setSigma(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_MODULE.SIGMA</code>. 「sigma」- 统一标识
     */
    public String getSigma();

    /**
     * Setter for <code>DB_ETERNAL.X_MODULE.METADATA</code>. 「metadata」- 附加配置
     */
    public IXModule setMetadata(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_MODULE.METADATA</code>. 「metadata」- 附加配置
     */
    public String getMetadata();

    /**
     * Setter for <code>DB_ETERNAL.X_MODULE.LANGUAGE</code>. 「language」- 使用的语言
     */
    public IXModule setLanguage(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_MODULE.LANGUAGE</code>. 「language」- 使用的语言
     */
    public String getLanguage();

    /**
     * Setter for <code>DB_ETERNAL.X_MODULE.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public IXModule setCreatedAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.X_MODULE.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public LocalDateTime getCreatedAt();

    /**
     * Setter for <code>DB_ETERNAL.X_MODULE.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public IXModule setCreatedBy(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_MODULE.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public String getCreatedBy();

    /**
     * Setter for <code>DB_ETERNAL.X_MODULE.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public IXModule setUpdatedAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.X_MODULE.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public LocalDateTime getUpdatedAt();

    /**
     * Setter for <code>DB_ETERNAL.X_MODULE.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public IXModule setUpdatedBy(String value);

    /**
     * Getter for <code>DB_ETERNAL.X_MODULE.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public String getUpdatedBy();

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common
     * interface IXModule
     */
    public void from(IXModule from);

    /**
     * Copy data into another generated Record/POJO implementing the common
     * interface IXModule
     */
    public <E extends IXModule> E into(E into);

        @Override
        public default IXModule fromJson(io.vertx.core.json.JsonObject json) {
                setOrThrow(this::setKey,json::getString,"KEY","java.lang.String");
                setOrThrow(this::setName,json::getString,"NAME","java.lang.String");
                setOrThrow(this::setCode,json::getString,"CODE","java.lang.String");
                setOrThrow(this::setEntry,json::getString,"ENTRY","java.lang.String");
                setOrThrow(this::setAppId,json::getString,"APP_ID","java.lang.String");
                setOrThrow(this::setModelId,json::getString,"MODEL_ID","java.lang.String");
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
                json.put("ENTRY",getEntry());
                json.put("APP_ID",getAppId());
                json.put("MODEL_ID",getModelId());
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
