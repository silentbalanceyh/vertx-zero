/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.graphic.domain.tables.interfaces;


import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.setOrThrow;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface IGNode extends VertxPojo, Serializable {

    /**
     * Setter for <code>DB_ETERNAL.G_NODE.KEY</code>. 「key」- 节点ID
     */
    public IGNode setKey(String value);

    /**
     * Getter for <code>DB_ETERNAL.G_NODE.KEY</code>. 「key」- 节点ID
     */
    public String getKey();

    /**
     * Setter for <code>DB_ETERNAL.G_NODE.NAME</code>. 「name」- 节点呈现名称
     */
    public IGNode setName(String value);

    /**
     * Getter for <code>DB_ETERNAL.G_NODE.NAME</code>. 「name」- 节点呈现名称
     */
    public String getName();

    /**
     * Setter for <code>DB_ETERNAL.G_NODE.X</code>. 「x」- 当前节点在图上的x坐标
     */
    public IGNode setX(BigDecimal value);

    /**
     * Getter for <code>DB_ETERNAL.G_NODE.X</code>. 「x」- 当前节点在图上的x坐标
     */
    public BigDecimal getX();

    /**
     * Setter for <code>DB_ETERNAL.G_NODE.Y</code>. 「y」- 当前节点在图上的y坐标
     */
    public IGNode setY(BigDecimal value);

    /**
     * Getter for <code>DB_ETERNAL.G_NODE.Y</code>. 「y」- 当前节点在图上的y坐标
     */
    public BigDecimal getY();

    /**
     * Setter for <code>DB_ETERNAL.G_NODE.UI</code>. 「ui」- ui配置专用
     */
    public IGNode setUi(String value);

    /**
     * Getter for <code>DB_ETERNAL.G_NODE.UI</code>. 「ui」- ui配置专用
     */
    public String getUi();

    /**
     * Setter for <code>DB_ETERNAL.G_NODE.GRAPHIC_ID</code>. 「graphicId」-
     * 它所关联的图实例ID
     */
    public IGNode setGraphicId(String value);

    /**
     * Getter for <code>DB_ETERNAL.G_NODE.GRAPHIC_ID</code>. 「graphicId」-
     * 它所关联的图实例ID
     */
    public String getGraphicId();

    /**
     * Setter for <code>DB_ETERNAL.G_NODE.RECORD_DATA</code>. 「recordData」-
     * 该节点存储的数据信息
     */
    public IGNode setRecordData(String value);

    /**
     * Getter for <code>DB_ETERNAL.G_NODE.RECORD_DATA</code>. 「recordData」-
     * 该节点存储的数据信息
     */
    public String getRecordData();

    /**
     * Setter for <code>DB_ETERNAL.G_NODE.RECORD_KEY</code>. 「recordKey」- 记录主键
     */
    public IGNode setRecordKey(String value);

    /**
     * Getter for <code>DB_ETERNAL.G_NODE.RECORD_KEY</code>. 「recordKey」- 记录主键
     */
    public String getRecordKey();

    /**
     * Setter for <code>DB_ETERNAL.G_NODE.RECORD_COMPONENT</code>.
     * 「recordComponent」- 记录处理组件
     */
    public IGNode setRecordComponent(String value);

    /**
     * Getter for <code>DB_ETERNAL.G_NODE.RECORD_COMPONENT</code>.
     * 「recordComponent」- 记录处理组件
     */
    public String getRecordComponent();

    /**
     * Setter for <code>DB_ETERNAL.G_NODE.RECORD_CLASS</code>. 「recordClass」-
     * 记录绑定Pojo类型
     */
    public IGNode setRecordClass(String value);

    /**
     * Getter for <code>DB_ETERNAL.G_NODE.RECORD_CLASS</code>. 「recordClass」-
     * 记录绑定Pojo类型
     */
    public String getRecordClass();

    /**
     * Setter for <code>DB_ETERNAL.G_NODE.SIGMA</code>. 「sigma」- 统一标识
     */
    public IGNode setSigma(String value);

    /**
     * Getter for <code>DB_ETERNAL.G_NODE.SIGMA</code>. 「sigma」- 统一标识
     */
    public String getSigma();

    /**
     * Setter for <code>DB_ETERNAL.G_NODE.LANGUAGE</code>. 「language」- 使用的语言
     */
    public IGNode setLanguage(String value);

    /**
     * Getter for <code>DB_ETERNAL.G_NODE.LANGUAGE</code>. 「language」- 使用的语言
     */
    public String getLanguage();

    /**
     * Setter for <code>DB_ETERNAL.G_NODE.ACTIVE</code>. 「active」- 是否启用
     */
    public IGNode setActive(Boolean value);

    /**
     * Getter for <code>DB_ETERNAL.G_NODE.ACTIVE</code>. 「active」- 是否启用
     */
    public Boolean getActive();

    /**
     * Setter for <code>DB_ETERNAL.G_NODE.METADATA</code>. 「metadata」- 附加配置数据
     */
    public IGNode setMetadata(String value);

    /**
     * Getter for <code>DB_ETERNAL.G_NODE.METADATA</code>. 「metadata」- 附加配置数据
     */
    public String getMetadata();

    /**
     * Setter for <code>DB_ETERNAL.G_NODE.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public IGNode setCreatedAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.G_NODE.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public LocalDateTime getCreatedAt();

    /**
     * Setter for <code>DB_ETERNAL.G_NODE.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public IGNode setCreatedBy(String value);

    /**
     * Getter for <code>DB_ETERNAL.G_NODE.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public String getCreatedBy();

    /**
     * Setter for <code>DB_ETERNAL.G_NODE.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public IGNode setUpdatedAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.G_NODE.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public LocalDateTime getUpdatedAt();

    /**
     * Setter for <code>DB_ETERNAL.G_NODE.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public IGNode setUpdatedBy(String value);

    /**
     * Getter for <code>DB_ETERNAL.G_NODE.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public String getUpdatedBy();

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common
     * interface IGNode
     */
    public void from(IGNode from);

    /**
     * Copy data into another generated Record/POJO implementing the common
     * interface IGNode
     */
    public <E extends IGNode> E into(E into);

        @Override
        public default IGNode fromJson(io.vertx.core.json.JsonObject json) {
                setOrThrow(this::setKey,json::getString,"KEY","java.lang.String");
                setOrThrow(this::setName,json::getString,"NAME","java.lang.String");
                setOrThrow(this::setX,key -> {String s = json.getString(key); return s==null?null:new java.math.BigDecimal(s);},"X","java.math.BigDecimal");
                setOrThrow(this::setY,key -> {String s = json.getString(key); return s==null?null:new java.math.BigDecimal(s);},"Y","java.math.BigDecimal");
                setOrThrow(this::setUi,json::getString,"UI","java.lang.String");
                setOrThrow(this::setGraphicId,json::getString,"GRAPHIC_ID","java.lang.String");
                setOrThrow(this::setRecordData,json::getString,"RECORD_DATA","java.lang.String");
                setOrThrow(this::setRecordKey,json::getString,"RECORD_KEY","java.lang.String");
                setOrThrow(this::setRecordComponent,json::getString,"RECORD_COMPONENT","java.lang.String");
                setOrThrow(this::setRecordClass,json::getString,"RECORD_CLASS","java.lang.String");
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
                json.put("X",getX()==null?null:getX().toString());
                json.put("Y",getY()==null?null:getY().toString());
                json.put("UI",getUi());
                json.put("GRAPHIC_ID",getGraphicId());
                json.put("RECORD_DATA",getRecordData());
                json.put("RECORD_KEY",getRecordKey());
                json.put("RECORD_COMPONENT",getRecordComponent());
                json.put("RECORD_CLASS",getRecordClass());
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
