/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.tpl.domain.tables.records;


import cn.vertxup.tpl.domain.tables.TplMessage;
import cn.vertxup.tpl.domain.tables.interfaces.ITplMessage;
import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record16;
import org.jooq.Row16;
import org.jooq.impl.UpdatableRecordImpl;

import java.time.LocalDateTime;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TplMessageRecord extends UpdatableRecordImpl<TplMessageRecord> implements VertxPojo, Record16<String, String, String, String, String, String, String, String, Boolean, String, String, String, LocalDateTime, String, LocalDateTime, String>, ITplMessage {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.KEY</code>. 「key」- 模板唯一主键
     */
    @Override
    public TplMessageRecord setKey(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.KEY</code>. 「key」- 模板唯一主键
     */
    @Override
    public String getKey() {
        return (String) get(0);
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.NAME</code>. 「name」- 模板名称
     */
    @Override
    public TplMessageRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.NAME</code>. 「name」- 模板名称
     */
    @Override
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.CODE</code>. 「code」- 模板编码
     */
    @Override
    public TplMessageRecord setCode(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.CODE</code>. 「code」- 模板编码
     */
    @Override
    public String getCode() {
        return (String) get(2);
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.TYPE</code>. 「type」- 模板类型
     */
    @Override
    public TplMessageRecord setType(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.TYPE</code>. 「type」- 模板类型
     */
    @Override
    public String getType() {
        return (String) get(3);
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.EXPR_SUBJECT</code>.
     * 「exprSubject」- 模板标题，支持表达式
     */
    @Override
    public TplMessageRecord setExprSubject(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.EXPR_SUBJECT</code>.
     * 「exprSubject」- 模板标题，支持表达式
     */
    @Override
    public String getExprSubject() {
        return (String) get(4);
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.EXPR_CONTENT</code>.
     * 「exprContent」- 模板内容，支持表达式
     */
    @Override
    public TplMessageRecord setExprContent(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.EXPR_CONTENT</code>.
     * 「exprContent」- 模板内容，支持表达式
     */
    @Override
    public String getExprContent() {
        return (String) get(5);
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.EXPR_COMPONENT</code>.
     * 「exprComponent」- 模板扩展处理程序，Java类名
     */
    @Override
    public TplMessageRecord setExprComponent(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.EXPR_COMPONENT</code>.
     * 「exprComponent」- 模板扩展处理程序，Java类名
     */
    @Override
    public String getExprComponent() {
        return (String) get(6);
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.APP_ID</code>. 「appId」- 所属应用ID
     */
    @Override
    public TplMessageRecord setAppId(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.APP_ID</code>. 「appId」- 所属应用ID
     */
    @Override
    public String getAppId() {
        return (String) get(7);
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public TplMessageRecord setActive(Boolean value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return (Boolean) get(8);
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public TplMessageRecord setSigma(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return (String) get(9);
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public TplMessageRecord setMetadata(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public String getMetadata() {
        return (String) get(10);
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public TplMessageRecord setLanguage(String value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public String getLanguage() {
        return (String) get(11);
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public TplMessageRecord setCreatedAt(LocalDateTime value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(12);
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public TplMessageRecord setCreatedBy(String value) {
        set(13, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public String getCreatedBy() {
        return (String) get(13);
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public TplMessageRecord setUpdatedAt(LocalDateTime value) {
        set(14, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(14);
    }

    /**
     * Setter for <code>DB_ETERNAL.TPL_MESSAGE.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public TplMessageRecord setUpdatedBy(String value) {
        set(15, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.TPL_MESSAGE.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public String getUpdatedBy() {
        return (String) get(15);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record16 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row16<String, String, String, String, String, String, String, String, Boolean, String, String, String, LocalDateTime, String, LocalDateTime, String> fieldsRow() {
        return (Row16) super.fieldsRow();
    }

    @Override
    public Row16<String, String, String, String, String, String, String, String, Boolean, String, String, String, LocalDateTime, String, LocalDateTime, String> valuesRow() {
        return (Row16) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return TplMessage.TPL_MESSAGE.KEY;
    }

    @Override
    public Field<String> field2() {
        return TplMessage.TPL_MESSAGE.NAME;
    }

    @Override
    public Field<String> field3() {
        return TplMessage.TPL_MESSAGE.CODE;
    }

    @Override
    public Field<String> field4() {
        return TplMessage.TPL_MESSAGE.TYPE;
    }

    @Override
    public Field<String> field5() {
        return TplMessage.TPL_MESSAGE.EXPR_SUBJECT;
    }

    @Override
    public Field<String> field6() {
        return TplMessage.TPL_MESSAGE.EXPR_CONTENT;
    }

    @Override
    public Field<String> field7() {
        return TplMessage.TPL_MESSAGE.EXPR_COMPONENT;
    }

    @Override
    public Field<String> field8() {
        return TplMessage.TPL_MESSAGE.APP_ID;
    }

    @Override
    public Field<Boolean> field9() {
        return TplMessage.TPL_MESSAGE.ACTIVE;
    }

    @Override
    public Field<String> field10() {
        return TplMessage.TPL_MESSAGE.SIGMA;
    }

    @Override
    public Field<String> field11() {
        return TplMessage.TPL_MESSAGE.METADATA;
    }

    @Override
    public Field<String> field12() {
        return TplMessage.TPL_MESSAGE.LANGUAGE;
    }

    @Override
    public Field<LocalDateTime> field13() {
        return TplMessage.TPL_MESSAGE.CREATED_AT;
    }

    @Override
    public Field<String> field14() {
        return TplMessage.TPL_MESSAGE.CREATED_BY;
    }

    @Override
    public Field<LocalDateTime> field15() {
        return TplMessage.TPL_MESSAGE.UPDATED_AT;
    }

    @Override
    public Field<String> field16() {
        return TplMessage.TPL_MESSAGE.UPDATED_BY;
    }

    @Override
    public String component1() {
        return getKey();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public String component3() {
        return getCode();
    }

    @Override
    public String component4() {
        return getType();
    }

    @Override
    public String component5() {
        return getExprSubject();
    }

    @Override
    public String component6() {
        return getExprContent();
    }

    @Override
    public String component7() {
        return getExprComponent();
    }

    @Override
    public String component8() {
        return getAppId();
    }

    @Override
    public Boolean component9() {
        return getActive();
    }

    @Override
    public String component10() {
        return getSigma();
    }

    @Override
    public String component11() {
        return getMetadata();
    }

    @Override
    public String component12() {
        return getLanguage();
    }

    @Override
    public LocalDateTime component13() {
        return getCreatedAt();
    }

    @Override
    public String component14() {
        return getCreatedBy();
    }

    @Override
    public LocalDateTime component15() {
        return getUpdatedAt();
    }

    @Override
    public String component16() {
        return getUpdatedBy();
    }

    @Override
    public String value1() {
        return getKey();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public String value3() {
        return getCode();
    }

    @Override
    public String value4() {
        return getType();
    }

    @Override
    public String value5() {
        return getExprSubject();
    }

    @Override
    public String value6() {
        return getExprContent();
    }

    @Override
    public String value7() {
        return getExprComponent();
    }

    @Override
    public String value8() {
        return getAppId();
    }

    @Override
    public Boolean value9() {
        return getActive();
    }

    @Override
    public String value10() {
        return getSigma();
    }

    @Override
    public String value11() {
        return getMetadata();
    }

    @Override
    public String value12() {
        return getLanguage();
    }

    @Override
    public LocalDateTime value13() {
        return getCreatedAt();
    }

    @Override
    public String value14() {
        return getCreatedBy();
    }

    @Override
    public LocalDateTime value15() {
        return getUpdatedAt();
    }

    @Override
    public String value16() {
        return getUpdatedBy();
    }

    @Override
    public TplMessageRecord value1(String value) {
        setKey(value);
        return this;
    }

    @Override
    public TplMessageRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public TplMessageRecord value3(String value) {
        setCode(value);
        return this;
    }

    @Override
    public TplMessageRecord value4(String value) {
        setType(value);
        return this;
    }

    @Override
    public TplMessageRecord value5(String value) {
        setExprSubject(value);
        return this;
    }

    @Override
    public TplMessageRecord value6(String value) {
        setExprContent(value);
        return this;
    }

    @Override
    public TplMessageRecord value7(String value) {
        setExprComponent(value);
        return this;
    }

    @Override
    public TplMessageRecord value8(String value) {
        setAppId(value);
        return this;
    }

    @Override
    public TplMessageRecord value9(Boolean value) {
        setActive(value);
        return this;
    }

    @Override
    public TplMessageRecord value10(String value) {
        setSigma(value);
        return this;
    }

    @Override
    public TplMessageRecord value11(String value) {
        setMetadata(value);
        return this;
    }

    @Override
    public TplMessageRecord value12(String value) {
        setLanguage(value);
        return this;
    }

    @Override
    public TplMessageRecord value13(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public TplMessageRecord value14(String value) {
        setCreatedBy(value);
        return this;
    }

    @Override
    public TplMessageRecord value15(LocalDateTime value) {
        setUpdatedAt(value);
        return this;
    }

    @Override
    public TplMessageRecord value16(String value) {
        setUpdatedBy(value);
        return this;
    }

    @Override
    public TplMessageRecord values(String value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8, Boolean value9, String value10, String value11, String value12, LocalDateTime value13, String value14, LocalDateTime value15, String value16) {
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
        return this;
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    @Override
    public void from(ITplMessage from) {
        setKey(from.getKey());
        setName(from.getName());
        setCode(from.getCode());
        setType(from.getType());
        setExprSubject(from.getExprSubject());
        setExprContent(from.getExprContent());
        setExprComponent(from.getExprComponent());
        setAppId(from.getAppId());
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
    public <E extends ITplMessage> E into(E into) {
        into.from(this);
        return into;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TplMessageRecord
     */
    public TplMessageRecord() {
        super(TplMessage.TPL_MESSAGE);
    }

    /**
     * Create a detached, initialised TplMessageRecord
     */
    public TplMessageRecord(String key, String name, String code, String type, String exprSubject, String exprContent, String exprComponent, String appId, Boolean active, String sigma, String metadata, String language, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy) {
        super(TplMessage.TPL_MESSAGE);

        setKey(key);
        setName(name);
        setCode(code);
        setType(type);
        setExprSubject(exprSubject);
        setExprContent(exprContent);
        setExprComponent(exprComponent);
        setAppId(appId);
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
     * Create a detached, initialised TplMessageRecord
     */
    public TplMessageRecord(cn.vertxup.tpl.domain.tables.pojos.TplMessage value) {
        super(TplMessage.TPL_MESSAGE);

        if (value != null) {
            setKey(value.getKey());
            setName(value.getName());
            setCode(value.getCode());
            setType(value.getType());
            setExprSubject(value.getExprSubject());
            setExprContent(value.getExprContent());
            setExprComponent(value.getExprComponent());
            setAppId(value.getAppId());
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

        public TplMessageRecord(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }
}
