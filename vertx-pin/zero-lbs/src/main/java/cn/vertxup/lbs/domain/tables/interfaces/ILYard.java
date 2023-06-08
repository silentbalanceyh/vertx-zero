/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.lbs.domain.tables.interfaces;


import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.io.Serializable;
import java.time.LocalDateTime;

import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.setOrThrow;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface ILYard extends VertxPojo, Serializable {

    /**
     * Setter for <code>DB_ETERNAL.L_YARD.KEY</code>. 「key」- 主键
     */
    public ILYard setKey(String value);

    /**
     * Getter for <code>DB_ETERNAL.L_YARD.KEY</code>. 「key」- 主键
     */
    public String getKey();

    /**
     * Setter for <code>DB_ETERNAL.L_YARD.NAME</code>. 「name」- 名称
     */
    public ILYard setName(String value);

    /**
     * Getter for <code>DB_ETERNAL.L_YARD.NAME</code>. 「name」- 名称
     */
    public String getName();

    /**
     * Setter for <code>DB_ETERNAL.L_YARD.CODE</code>. 「code」- 编码
     */
    public ILYard setCode(String value);

    /**
     * Getter for <code>DB_ETERNAL.L_YARD.CODE</code>. 「code」- 编码
     */
    public String getCode();

    /**
     * Setter for <code>DB_ETERNAL.L_YARD.METADATA</code>. 「metadata」- 附加配置
     */
    public ILYard setMetadata(String value);

    /**
     * Getter for <code>DB_ETERNAL.L_YARD.METADATA</code>. 「metadata」- 附加配置
     */
    public String getMetadata();

    /**
     * Setter for <code>DB_ETERNAL.L_YARD.ORDER</code>. 「order」- 排序
     */
    public ILYard setOrder(Integer value);

    /**
     * Getter for <code>DB_ETERNAL.L_YARD.ORDER</code>. 「order」- 排序
     */
    public Integer getOrder();

    /**
     * Setter for <code>DB_ETERNAL.L_YARD.LOCATION_ID</code>. 「locationId」-
     * 关联地址ID
     */
    public ILYard setLocationId(String value);

    /**
     * Getter for <code>DB_ETERNAL.L_YARD.LOCATION_ID</code>. 「locationId」-
     * 关联地址ID
     */
    public String getLocationId();

    /**
     * Setter for <code>DB_ETERNAL.L_YARD.ACTIVE</code>. 「active」- 是否启用
     */
    public ILYard setActive(Boolean value);

    /**
     * Getter for <code>DB_ETERNAL.L_YARD.ACTIVE</code>. 「active」- 是否启用
     */
    public Boolean getActive();

    /**
     * Setter for <code>DB_ETERNAL.L_YARD.SIGMA</code>. 「sigma」- 统一标识
     */
    public ILYard setSigma(String value);

    /**
     * Getter for <code>DB_ETERNAL.L_YARD.SIGMA</code>. 「sigma」- 统一标识
     */
    public String getSigma();

    /**
     * Setter for <code>DB_ETERNAL.L_YARD.LANGUAGE</code>. 「language」- 使用的语言
     */
    public ILYard setLanguage(String value);

    /**
     * Getter for <code>DB_ETERNAL.L_YARD.LANGUAGE</code>. 「language」- 使用的语言
     */
    public String getLanguage();

    /**
     * Setter for <code>DB_ETERNAL.L_YARD.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public ILYard setCreatedAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.L_YARD.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public LocalDateTime getCreatedAt();

    /**
     * Setter for <code>DB_ETERNAL.L_YARD.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public ILYard setCreatedBy(String value);

    /**
     * Getter for <code>DB_ETERNAL.L_YARD.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public String getCreatedBy();

    /**
     * Setter for <code>DB_ETERNAL.L_YARD.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public ILYard setUpdatedAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.L_YARD.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public LocalDateTime getUpdatedAt();

    /**
     * Setter for <code>DB_ETERNAL.L_YARD.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public ILYard setUpdatedBy(String value);

    /**
     * Getter for <code>DB_ETERNAL.L_YARD.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public String getUpdatedBy();

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common
     * interface ILYard
     */
    public void from(ILYard from);

    /**
     * Copy data into another generated Record/POJO implementing the common
     * interface ILYard
     */
    public <E extends ILYard> E into(E into);

        @Override
        public default ILYard fromJson(io.vertx.core.json.JsonObject json) {
                setOrThrow(this::setKey,json::getString,"KEY","java.lang.String");
                setOrThrow(this::setName,json::getString,"NAME","java.lang.String");
                setOrThrow(this::setCode,json::getString,"CODE","java.lang.String");
                setOrThrow(this::setMetadata,json::getString,"METADATA","java.lang.String");
                setOrThrow(this::setOrder,json::getInteger,"ORDER","java.lang.Integer");
                setOrThrow(this::setLocationId,json::getString,"LOCATION_ID","java.lang.String");
                setOrThrow(this::setActive,json::getBoolean,"ACTIVE","java.lang.Boolean");
                setOrThrow(this::setSigma,json::getString,"SIGMA","java.lang.String");
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
                json.put("METADATA",getMetadata());
                json.put("ORDER",getOrder());
                json.put("LOCATION_ID",getLocationId());
                json.put("ACTIVE",getActive());
                json.put("SIGMA",getSigma());
                json.put("LANGUAGE",getLanguage());
                json.put("CREATED_AT",getCreatedAt()==null?null:getCreatedAt().toString());
                json.put("CREATED_BY",getCreatedBy());
                json.put("UPDATED_AT",getUpdatedAt()==null?null:getUpdatedAt().toString());
                json.put("UPDATED_BY",getUpdatedBy());
                return json;
        }

}
