/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.workflow.domain.tables.records;


import cn.vertxup.workflow.domain.tables.WFlow;
import cn.vertxup.workflow.domain.tables.interfaces.IWFlow;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record19;
import org.jooq.Row19;
import org.jooq.impl.UpdatableRecordImpl;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class WFlowRecord extends UpdatableRecordImpl<WFlowRecord> implements VertxPojo, Record19<String, String, String, String, String, String, String, String, String, String, String, Boolean, String, String, String, LocalDateTime, String, LocalDateTime, String>, IWFlow {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.KEY</code>. 「key」- 流程定义主键
     */
    @Override
    public WFlowRecord setKey(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.KEY</code>. 「key」- 流程定义主键
     */
    @Override
    public String getKey() {
        return (String) get(0);
    }

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.NAMESPACE</code>. 「namespace」-
     * 所在名空间（区分应用的第二维度）
     */
    @Override
    public WFlowRecord setNamespace(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.NAMESPACE</code>. 「namespace」-
     * 所在名空间（区分应用的第二维度）
     */
    @Override
    public String getNamespace() {
        return (String) get(1);
    }

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.NAME</code>. 「name」- 流程定义名称
     */
    @Override
    public WFlowRecord setName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.NAME</code>. 「name」- 流程定义名称
     */
    @Override
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.CODE</code>. 「code」- 流程定义编号（系统可用）
     */
    @Override
    public WFlowRecord setCode(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.CODE</code>. 「code」- 流程定义编号（系统可用）
     */
    @Override
    public String getCode() {
        return (String) get(3);
    }

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.TYPE</code>. 「type」- 流程类型，BPMN，JBPM等
     */
    @Override
    public WFlowRecord setType(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.TYPE</code>. 「type」- 流程类型，BPMN，JBPM等
     */
    @Override
    public String getType() {
        return (String) get(4);
    }

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.GRAPHIC_ID</code>. 「graphicId」-
     * 图关联，1对1
     */
    @Override
    public WFlowRecord setGraphicId(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.GRAPHIC_ID</code>. 「graphicId」-
     * 图关联，1对1
     */
    @Override
    public String getGraphicId() {
        return (String) get(5);
    }

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.RUN_COMPONENT</code>. 「runComponent」-
     * 执行组件
     */
    @Override
    public WFlowRecord setRunComponent(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.RUN_COMPONENT</code>. 「runComponent」-
     * 执行组件
     */
    @Override
    public String getRunComponent() {
        return (String) get(6);
    }

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.DATA_XML</code>. 「dataXml」- 内容的XML格式
     */
    @Override
    public WFlowRecord setDataXml(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.DATA_XML</code>. 「dataXml」- 内容的XML格式
     */
    @Override
    public String getDataXml() {
        return (String) get(7);
    }

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.DATA_JSON</code>. 「dataJson」-
     * 内容的JSON格式
     */
    @Override
    public WFlowRecord setDataJson(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.DATA_JSON</code>. 「dataJson」-
     * 内容的JSON格式
     */
    @Override
    public String getDataJson() {
        return (String) get(8);
    }

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.DATA_FILE</code>. 「dataFile」- 内容的文件格式
     */
    @Override
    public WFlowRecord setDataFile(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.DATA_FILE</code>. 「dataFile」- 内容的文件格式
     */
    @Override
    public String getDataFile() {
        return (String) get(9);
    }

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.COMMENT</code>. 「comment」 - 流程定义备注
     */
    @Override
    public WFlowRecord setComment(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.COMMENT</code>. 「comment」 - 流程定义备注
     */
    @Override
    public String getComment() {
        return (String) get(10);
    }

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public WFlowRecord setActive(Boolean value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return (Boolean) get(11);
    }

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public WFlowRecord setSigma(String value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return (String) get(12);
    }

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public WFlowRecord setMetadata(String value) {
        set(13, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public String getMetadata() {
        return (String) get(13);
    }

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public WFlowRecord setLanguage(String value) {
        set(14, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public String getLanguage() {
        return (String) get(14);
    }

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    @Override
    public WFlowRecord setCreatedAt(LocalDateTime value) {
        set(15, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(15);
    }

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public WFlowRecord setCreatedBy(String value) {
        set(16, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public String getCreatedBy() {
        return (String) get(16);
    }

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    @Override
    public WFlowRecord setUpdatedAt(LocalDateTime value) {
        set(17, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(17);
    }

    /**
     * Setter for <code>DB_ETERNAL.W_FLOW.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public WFlowRecord setUpdatedBy(String value) {
        set(18, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.W_FLOW.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public String getUpdatedBy() {
        return (String) get(18);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record19 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row19<String, String, String, String, String, String, String, String, String, String, String, Boolean, String, String, String, LocalDateTime, String, LocalDateTime, String> fieldsRow() {
        return (Row19) super.fieldsRow();
    }

    @Override
    public Row19<String, String, String, String, String, String, String, String, String, String, String, Boolean, String, String, String, LocalDateTime, String, LocalDateTime, String> valuesRow() {
        return (Row19) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return WFlow.W_FLOW.KEY;
    }

    @Override
    public Field<String> field2() {
        return WFlow.W_FLOW.NAMESPACE;
    }

    @Override
    public Field<String> field3() {
        return WFlow.W_FLOW.NAME;
    }

    @Override
    public Field<String> field4() {
        return WFlow.W_FLOW.CODE;
    }

    @Override
    public Field<String> field5() {
        return WFlow.W_FLOW.TYPE;
    }

    @Override
    public Field<String> field6() {
        return WFlow.W_FLOW.GRAPHIC_ID;
    }

    @Override
    public Field<String> field7() {
        return WFlow.W_FLOW.RUN_COMPONENT;
    }

    @Override
    public Field<String> field8() {
        return WFlow.W_FLOW.DATA_XML;
    }

    @Override
    public Field<String> field9() {
        return WFlow.W_FLOW.DATA_JSON;
    }

    @Override
    public Field<String> field10() {
        return WFlow.W_FLOW.DATA_FILE;
    }

    @Override
    public Field<String> field11() {
        return WFlow.W_FLOW.COMMENT;
    }

    @Override
    public Field<Boolean> field12() {
        return WFlow.W_FLOW.ACTIVE;
    }

    @Override
    public Field<String> field13() {
        return WFlow.W_FLOW.SIGMA;
    }

    @Override
    public Field<String> field14() {
        return WFlow.W_FLOW.METADATA;
    }

    @Override
    public Field<String> field15() {
        return WFlow.W_FLOW.LANGUAGE;
    }

    @Override
    public Field<LocalDateTime> field16() {
        return WFlow.W_FLOW.CREATED_AT;
    }

    @Override
    public Field<String> field17() {
        return WFlow.W_FLOW.CREATED_BY;
    }

    @Override
    public Field<LocalDateTime> field18() {
        return WFlow.W_FLOW.UPDATED_AT;
    }

    @Override
    public Field<String> field19() {
        return WFlow.W_FLOW.UPDATED_BY;
    }

    @Override
    public String component1() {
        return getKey();
    }

    @Override
    public String component2() {
        return getNamespace();
    }

    @Override
    public String component3() {
        return getName();
    }

    @Override
    public String component4() {
        return getCode();
    }

    @Override
    public String component5() {
        return getType();
    }

    @Override
    public String component6() {
        return getGraphicId();
    }

    @Override
    public String component7() {
        return getRunComponent();
    }

    @Override
    public String component8() {
        return getDataXml();
    }

    @Override
    public String component9() {
        return getDataJson();
    }

    @Override
    public String component10() {
        return getDataFile();
    }

    @Override
    public String component11() {
        return getComment();
    }

    @Override
    public Boolean component12() {
        return getActive();
    }

    @Override
    public String component13() {
        return getSigma();
    }

    @Override
    public String component14() {
        return getMetadata();
    }

    @Override
    public String component15() {
        return getLanguage();
    }

    @Override
    public LocalDateTime component16() {
        return getCreatedAt();
    }

    @Override
    public String component17() {
        return getCreatedBy();
    }

    @Override
    public LocalDateTime component18() {
        return getUpdatedAt();
    }

    @Override
    public String component19() {
        return getUpdatedBy();
    }

    @Override
    public String value1() {
        return getKey();
    }

    @Override
    public String value2() {
        return getNamespace();
    }

    @Override
    public String value3() {
        return getName();
    }

    @Override
    public String value4() {
        return getCode();
    }

    @Override
    public String value5() {
        return getType();
    }

    @Override
    public String value6() {
        return getGraphicId();
    }

    @Override
    public String value7() {
        return getRunComponent();
    }

    @Override
    public String value8() {
        return getDataXml();
    }

    @Override
    public String value9() {
        return getDataJson();
    }

    @Override
    public String value10() {
        return getDataFile();
    }

    @Override
    public String value11() {
        return getComment();
    }

    @Override
    public Boolean value12() {
        return getActive();
    }

    @Override
    public String value13() {
        return getSigma();
    }

    @Override
    public String value14() {
        return getMetadata();
    }

    @Override
    public String value15() {
        return getLanguage();
    }

    @Override
    public LocalDateTime value16() {
        return getCreatedAt();
    }

    @Override
    public String value17() {
        return getCreatedBy();
    }

    @Override
    public LocalDateTime value18() {
        return getUpdatedAt();
    }

    @Override
    public String value19() {
        return getUpdatedBy();
    }

    @Override
    public WFlowRecord value1(String value) {
        setKey(value);
        return this;
    }

    @Override
    public WFlowRecord value2(String value) {
        setNamespace(value);
        return this;
    }

    @Override
    public WFlowRecord value3(String value) {
        setName(value);
        return this;
    }

    @Override
    public WFlowRecord value4(String value) {
        setCode(value);
        return this;
    }

    @Override
    public WFlowRecord value5(String value) {
        setType(value);
        return this;
    }

    @Override
    public WFlowRecord value6(String value) {
        setGraphicId(value);
        return this;
    }

    @Override
    public WFlowRecord value7(String value) {
        setRunComponent(value);
        return this;
    }

    @Override
    public WFlowRecord value8(String value) {
        setDataXml(value);
        return this;
    }

    @Override
    public WFlowRecord value9(String value) {
        setDataJson(value);
        return this;
    }

    @Override
    public WFlowRecord value10(String value) {
        setDataFile(value);
        return this;
    }

    @Override
    public WFlowRecord value11(String value) {
        setComment(value);
        return this;
    }

    @Override
    public WFlowRecord value12(Boolean value) {
        setActive(value);
        return this;
    }

    @Override
    public WFlowRecord value13(String value) {
        setSigma(value);
        return this;
    }

    @Override
    public WFlowRecord value14(String value) {
        setMetadata(value);
        return this;
    }

    @Override
    public WFlowRecord value15(String value) {
        setLanguage(value);
        return this;
    }

    @Override
    public WFlowRecord value16(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public WFlowRecord value17(String value) {
        setCreatedBy(value);
        return this;
    }

    @Override
    public WFlowRecord value18(LocalDateTime value) {
        setUpdatedAt(value);
        return this;
    }

    @Override
    public WFlowRecord value19(String value) {
        setUpdatedBy(value);
        return this;
    }

    @Override
    public WFlowRecord values(String value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8, String value9, String value10, String value11, Boolean value12, String value13, String value14, String value15, LocalDateTime value16, String value17, LocalDateTime value18, String value19) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        value14(value14);
        value15(value15);
        value16(value16);
        value17(value17);
        value18(value18);
        value19(value19);
        return this;
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    @Override
    public void from(IWFlow from) {
        setKey(from.getKey());
        setNamespace(from.getNamespace());
        setName(from.getName());
        setCode(from.getCode());
        setType(from.getType());
        setGraphicId(from.getGraphicId());
        setRunComponent(from.getRunComponent());
        setDataXml(from.getDataXml());
        setDataJson(from.getDataJson());
        setDataFile(from.getDataFile());
        setComment(from.getComment());
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
    public <E extends IWFlow> E into(E into) {
        into.from(this);
        return into;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached WFlowRecord
     */
    public WFlowRecord() {
        super(WFlow.W_FLOW);
    }

    /**
     * Create a detached, initialised WFlowRecord
     */
    public WFlowRecord(String key, String namespace, String name, String code, String type, String graphicId, String runComponent, String dataXml, String dataJson, String dataFile, String comment, Boolean active, String sigma, String metadata, String language, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy) {
        super(WFlow.W_FLOW);

        setKey(key);
        setNamespace(namespace);
        setName(name);
        setCode(code);
        setType(type);
        setGraphicId(graphicId);
        setRunComponent(runComponent);
        setDataXml(dataXml);
        setDataJson(dataJson);
        setDataFile(dataFile);
        setComment(comment);
        setActive(active);
        setSigma(sigma);
        setMetadata(metadata);
        setLanguage(language);
        setCreatedAt(createdAt);
        setCreatedBy(createdBy);
        setUpdatedAt(updatedAt);
        setUpdatedBy(updatedBy);
    }

    /**
     * Create a detached, initialised WFlowRecord
     */
    public WFlowRecord(cn.vertxup.workflow.domain.tables.pojos.WFlow value) {
        super(WFlow.W_FLOW);

        if (value != null) {
            setKey(value.getKey());
            setNamespace(value.getNamespace());
            setName(value.getName());
            setCode(value.getCode());
            setType(value.getType());
            setGraphicId(value.getGraphicId());
            setRunComponent(value.getRunComponent());
            setDataXml(value.getDataXml());
            setDataJson(value.getDataJson());
            setDataFile(value.getDataFile());
            setComment(value.getComment());
            setActive(value.getActive());
            setSigma(value.getSigma());
            setMetadata(value.getMetadata());
            setLanguage(value.getLanguage());
            setCreatedAt(value.getCreatedAt());
            setCreatedBy(value.getCreatedBy());
            setUpdatedAt(value.getUpdatedAt());
            setUpdatedBy(value.getUpdatedBy());
        }
    }

        public WFlowRecord(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }
}