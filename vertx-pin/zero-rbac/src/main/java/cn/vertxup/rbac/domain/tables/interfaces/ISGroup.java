/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.rbac.domain.tables.interfaces;


import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.io.Serializable;
import java.time.LocalDateTime;

import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.setOrThrow;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface ISGroup extends VertxPojo, Serializable {

    /**
     * Setter for <code>DB_ETERNAL.S_GROUP.KEY</code>. 「key」- 组ID
     */
    public ISGroup setKey(String value);

    /**
     * Getter for <code>DB_ETERNAL.S_GROUP.KEY</code>. 「key」- 组ID
     */
    public String getKey();

    /**
     * Setter for <code>DB_ETERNAL.S_GROUP.NAME</code>. 「name」- 组名称
     */
    public ISGroup setName(String value);

    /**
     * Getter for <code>DB_ETERNAL.S_GROUP.NAME</code>. 「name」- 组名称
     */
    public String getName();

    /**
     * Setter for <code>DB_ETERNAL.S_GROUP.CODE</code>. 「code」- 组系统码
     */
    public ISGroup setCode(String value);

    /**
     * Getter for <code>DB_ETERNAL.S_GROUP.CODE</code>. 「code」- 组系统码
     */
    public String getCode();

    /**
     * Setter for <code>DB_ETERNAL.S_GROUP.PARENT_ID</code>. 「parentId」-
     * 父组ID（组支持树形结构，角色平行结构）
     */
    public ISGroup setParentId(String value);

    /**
     * Getter for <code>DB_ETERNAL.S_GROUP.PARENT_ID</code>. 「parentId」-
     * 父组ID（组支持树形结构，角色平行结构）
     */
    public String getParentId();

    /**
     * Setter for <code>DB_ETERNAL.S_GROUP.MODEL_ID</code>. 「modelId」-
     * 组所关联的模型identifier，用于描述
     */
    public ISGroup setModelId(String value);

    /**
     * Getter for <code>DB_ETERNAL.S_GROUP.MODEL_ID</code>. 「modelId」-
     * 组所关联的模型identifier，用于描述
     */
    public String getModelId();

    /**
     * Setter for <code>DB_ETERNAL.S_GROUP.MODEL_KEY</code>. 「modelKey」-
     * 组所关联的模型记录ID，用于描述哪一个Model中的记录
     */
    public ISGroup setModelKey(String value);

    /**
     * Getter for <code>DB_ETERNAL.S_GROUP.MODEL_KEY</code>. 「modelKey」-
     * 组所关联的模型记录ID，用于描述哪一个Model中的记录
     */
    public String getModelKey();

    /**
     * Setter for <code>DB_ETERNAL.S_GROUP.CATEGORY</code>. 「category」- 组类型
     */
    public ISGroup setCategory(String value);

    /**
     * Getter for <code>DB_ETERNAL.S_GROUP.CATEGORY</code>. 「category」- 组类型
     */
    public String getCategory();

    /**
     * Setter for <code>DB_ETERNAL.S_GROUP.SIGMA</code>. 「sigma」- 用户组绑定的统一标识
     */
    public ISGroup setSigma(String value);

    /**
     * Getter for <code>DB_ETERNAL.S_GROUP.SIGMA</code>. 「sigma」- 用户组绑定的统一标识
     */
    public String getSigma();

    /**
     * Setter for <code>DB_ETERNAL.S_GROUP.LANGUAGE</code>. 「language」- 使用的语言
     */
    public ISGroup setLanguage(String value);

    /**
     * Getter for <code>DB_ETERNAL.S_GROUP.LANGUAGE</code>. 「language」- 使用的语言
     */
    public String getLanguage();

    /**
     * Setter for <code>DB_ETERNAL.S_GROUP.ACTIVE</code>. 「active」- 是否启用
     */
    public ISGroup setActive(Boolean value);

    /**
     * Getter for <code>DB_ETERNAL.S_GROUP.ACTIVE</code>. 「active」- 是否启用
     */
    public Boolean getActive();

    /**
     * Setter for <code>DB_ETERNAL.S_GROUP.METADATA</code>. 「metadata」- 附加配置数据
     */
    public ISGroup setMetadata(String value);

    /**
     * Getter for <code>DB_ETERNAL.S_GROUP.METADATA</code>. 「metadata」- 附加配置数据
     */
    public String getMetadata();

    /**
     * Setter for <code>DB_ETERNAL.S_GROUP.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public ISGroup setCreatedAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.S_GROUP.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public LocalDateTime getCreatedAt();

    /**
     * Setter for <code>DB_ETERNAL.S_GROUP.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public ISGroup setCreatedBy(String value);

    /**
     * Getter for <code>DB_ETERNAL.S_GROUP.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public String getCreatedBy();

    /**
     * Setter for <code>DB_ETERNAL.S_GROUP.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public ISGroup setUpdatedAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.S_GROUP.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public LocalDateTime getUpdatedAt();

    /**
     * Setter for <code>DB_ETERNAL.S_GROUP.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public ISGroup setUpdatedBy(String value);

    /**
     * Getter for <code>DB_ETERNAL.S_GROUP.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public String getUpdatedBy();

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common
     * interface ISGroup
     */
    public void from(ISGroup from);

    /**
     * Copy data into another generated Record/POJO implementing the common
     * interface ISGroup
     */
    public <E extends ISGroup> E into(E into);

        @Override
        public default ISGroup fromJson(io.vertx.core.json.JsonObject json) {
                setOrThrow(this::setKey,json::getString,"KEY","java.lang.String");
                setOrThrow(this::setName,json::getString,"NAME","java.lang.String");
                setOrThrow(this::setCode,json::getString,"CODE","java.lang.String");
                setOrThrow(this::setParentId,json::getString,"PARENT_ID","java.lang.String");
                setOrThrow(this::setModelId,json::getString,"MODEL_ID","java.lang.String");
                setOrThrow(this::setModelKey,json::getString,"MODEL_KEY","java.lang.String");
                setOrThrow(this::setCategory,json::getString,"CATEGORY","java.lang.String");
                setOrThrow(this::setSigma,json::getString,"SIGMA","java.lang.String");
                setOrThrow(this::setLanguage,json::getString,"LANGUAGE","java.lang.String");
                setOrThrow(this::setActive,json::getBoolean,"ACTIVE","java.lang.Boolean");
                setOrThrow(this::setMetadata,json::getString,"METADATA","java.lang.String");
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
                json.put("PARENT_ID",getParentId());
                json.put("MODEL_ID",getModelId());
                json.put("MODEL_KEY",getModelKey());
                json.put("CATEGORY",getCategory());
                json.put("SIGMA",getSigma());
                json.put("LANGUAGE",getLanguage());
                json.put("ACTIVE",getActive());
                json.put("METADATA",getMetadata());
                json.put("CREATED_AT",getCreatedAt()==null?null:getCreatedAt().toString());
                json.put("CREATED_BY",getCreatedBy());
                json.put("UPDATED_AT",getUpdatedAt()==null?null:getUpdatedAt().toString());
                json.put("UPDATED_BY",getUpdatedBy());
                return json;
        }

}
