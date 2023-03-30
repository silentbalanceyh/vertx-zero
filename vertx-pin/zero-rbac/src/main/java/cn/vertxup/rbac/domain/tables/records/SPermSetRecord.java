/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.rbac.domain.tables.records;


import cn.vertxup.rbac.domain.tables.SPermSet;
import cn.vertxup.rbac.domain.tables.interfaces.ISPermSet;
import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record13;
import org.jooq.Row13;
import org.jooq.impl.UpdatableRecordImpl;

import java.time.LocalDateTime;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SPermSetRecord extends UpdatableRecordImpl<SPermSetRecord> implements VertxPojo, Record13<String, String, String, String, String, String, Boolean, String, String, LocalDateTime, String, LocalDateTime, String>, ISPermSet {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>DB_ETERNAL.S_PERM_SET.KEY</code>. 「key」- 权限集ID
     */
    @Override
    public SPermSetRecord setKey(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERM_SET.KEY</code>. 「key」- 权限集ID
     */
    @Override
    public String getKey() {
        return (String) get(0);
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERM_SET.NAME</code>. 「name」- 权限集名称
     */
    @Override
    public SPermSetRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERM_SET.NAME</code>. 「name」- 权限集名称
     */
    @Override
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERM_SET.CODE</code>. 「code」- 权限集关联权限代码
     */
    @Override
    public SPermSetRecord setCode(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERM_SET.CODE</code>. 「code」- 权限集关联权限代码
     */
    @Override
    public String getCode() {
        return (String) get(2);
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERM_SET.TYPE</code>. 「type」- 权限集类型
     */
    @Override
    public SPermSetRecord setType(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERM_SET.TYPE</code>. 「type」- 权限集类型
     */
    @Override
    public String getType() {
        return (String) get(3);
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERM_SET.SIGMA</code>. 「sigma」- 绑定的统一标识
     */
    @Override
    public SPermSetRecord setSigma(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERM_SET.SIGMA</code>. 「sigma」- 绑定的统一标识
     */
    @Override
    public String getSigma() {
        return (String) get(4);
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERM_SET.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public SPermSetRecord setLanguage(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERM_SET.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public String getLanguage() {
        return (String) get(5);
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERM_SET.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public SPermSetRecord setActive(Boolean value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERM_SET.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return (Boolean) get(6);
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERM_SET.COMMENT</code>. 「comment」- 权限集说明
     */
    @Override
    public SPermSetRecord setComment(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERM_SET.COMMENT</code>. 「comment」- 权限集说明
     */
    @Override
    public String getComment() {
        return (String) get(7);
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERM_SET.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public SPermSetRecord setMetadata(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERM_SET.METADATA</code>. 「metadata」-
     * 附加配置数据
     */
    @Override
    public String getMetadata() {
        return (String) get(8);
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERM_SET.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public SPermSetRecord setCreatedAt(LocalDateTime value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERM_SET.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(9);
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERM_SET.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public SPermSetRecord setCreatedBy(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERM_SET.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public String getCreatedBy() {
        return (String) get(10);
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERM_SET.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public SPermSetRecord setUpdatedAt(LocalDateTime value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERM_SET.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(11);
    }

    /**
     * Setter for <code>DB_ETERNAL.S_PERM_SET.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public SPermSetRecord setUpdatedBy(String value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.S_PERM_SET.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public String getUpdatedBy() {
        return (String) get(12);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record13 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row13<String, String, String, String, String, String, Boolean, String, String, LocalDateTime, String, LocalDateTime, String> fieldsRow() {
        return (Row13) super.fieldsRow();
    }

    @Override
    public Row13<String, String, String, String, String, String, Boolean, String, String, LocalDateTime, String, LocalDateTime, String> valuesRow() {
        return (Row13) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return SPermSet.S_PERM_SET.KEY;
    }

    @Override
    public Field<String> field2() {
        return SPermSet.S_PERM_SET.NAME;
    }

    @Override
    public Field<String> field3() {
        return SPermSet.S_PERM_SET.CODE;
    }

    @Override
    public Field<String> field4() {
        return SPermSet.S_PERM_SET.TYPE;
    }

    @Override
    public Field<String> field5() {
        return SPermSet.S_PERM_SET.SIGMA;
    }

    @Override
    public Field<String> field6() {
        return SPermSet.S_PERM_SET.LANGUAGE;
    }

    @Override
    public Field<Boolean> field7() {
        return SPermSet.S_PERM_SET.ACTIVE;
    }

    @Override
    public Field<String> field8() {
        return SPermSet.S_PERM_SET.COMMENT;
    }

    @Override
    public Field<String> field9() {
        return SPermSet.S_PERM_SET.METADATA;
    }

    @Override
    public Field<LocalDateTime> field10() {
        return SPermSet.S_PERM_SET.CREATED_AT;
    }

    @Override
    public Field<String> field11() {
        return SPermSet.S_PERM_SET.CREATED_BY;
    }

    @Override
    public Field<LocalDateTime> field12() {
        return SPermSet.S_PERM_SET.UPDATED_AT;
    }

    @Override
    public Field<String> field13() {
        return SPermSet.S_PERM_SET.UPDATED_BY;
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
        return getSigma();
    }

    @Override
    public String component6() {
        return getLanguage();
    }

    @Override
    public Boolean component7() {
        return getActive();
    }

    @Override
    public String component8() {
        return getComment();
    }

    @Override
    public String component9() {
        return getMetadata();
    }

    @Override
    public LocalDateTime component10() {
        return getCreatedAt();
    }

    @Override
    public String component11() {
        return getCreatedBy();
    }

    @Override
    public LocalDateTime component12() {
        return getUpdatedAt();
    }

    @Override
    public String component13() {
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
        return getSigma();
    }

    @Override
    public String value6() {
        return getLanguage();
    }

    @Override
    public Boolean value7() {
        return getActive();
    }

    @Override
    public String value8() {
        return getComment();
    }

    @Override
    public String value9() {
        return getMetadata();
    }

    @Override
    public LocalDateTime value10() {
        return getCreatedAt();
    }

    @Override
    public String value11() {
        return getCreatedBy();
    }

    @Override
    public LocalDateTime value12() {
        return getUpdatedAt();
    }

    @Override
    public String value13() {
        return getUpdatedBy();
    }

    @Override
    public SPermSetRecord value1(String value) {
        setKey(value);
        return this;
    }

    @Override
    public SPermSetRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public SPermSetRecord value3(String value) {
        setCode(value);
        return this;
    }

    @Override
    public SPermSetRecord value4(String value) {
        setType(value);
        return this;
    }

    @Override
    public SPermSetRecord value5(String value) {
        setSigma(value);
        return this;
    }

    @Override
    public SPermSetRecord value6(String value) {
        setLanguage(value);
        return this;
    }

    @Override
    public SPermSetRecord value7(Boolean value) {
        setActive(value);
        return this;
    }

    @Override
    public SPermSetRecord value8(String value) {
        setComment(value);
        return this;
    }

    @Override
    public SPermSetRecord value9(String value) {
        setMetadata(value);
        return this;
    }

    @Override
    public SPermSetRecord value10(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public SPermSetRecord value11(String value) {
        setCreatedBy(value);
        return this;
    }

    @Override
    public SPermSetRecord value12(LocalDateTime value) {
        setUpdatedAt(value);
        return this;
    }

    @Override
    public SPermSetRecord value13(String value) {
        setUpdatedBy(value);
        return this;
    }

    @Override
    public SPermSetRecord values(String value1, String value2, String value3, String value4, String value5, String value6, Boolean value7, String value8, String value9, LocalDateTime value10, String value11, LocalDateTime value12, String value13) {
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
        return this;
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    @Override
    public void from(ISPermSet from) {
        setKey(from.getKey());
        setName(from.getName());
        setCode(from.getCode());
        setType(from.getType());
        setSigma(from.getSigma());
        setLanguage(from.getLanguage());
        setActive(from.getActive());
        setComment(from.getComment());
        setMetadata(from.getMetadata());
        setCreatedAt(from.getCreatedAt());
        setCreatedBy(from.getCreatedBy());
        setUpdatedAt(from.getUpdatedAt());
        setUpdatedBy(from.getUpdatedBy());
    }

    @Override
    public <E extends ISPermSet> E into(E into) {
        into.from(this);
        return into;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached SPermSetRecord
     */
    public SPermSetRecord() {
        super(SPermSet.S_PERM_SET);
    }

    /**
     * Create a detached, initialised SPermSetRecord
     */
    public SPermSetRecord(String key, String name, String code, String type, String sigma, String language, Boolean active, String comment, String metadata, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy) {
        super(SPermSet.S_PERM_SET);

        setKey(key);
        setName(name);
        setCode(code);
        setType(type);
        setSigma(sigma);
        setLanguage(language);
        setActive(active);
        setComment(comment);
        setMetadata(metadata);
        setCreatedAt(createdAt);
        setCreatedBy(createdBy);
        setUpdatedAt(updatedAt);
        setUpdatedBy(updatedBy);
    }

    /**
     * Create a detached, initialised SPermSetRecord
     */
    public SPermSetRecord(cn.vertxup.rbac.domain.tables.pojos.SPermSet value) {
        super(SPermSet.S_PERM_SET);

        if (value != null) {
            setKey(value.getKey());
            setName(value.getName());
            setCode(value.getCode());
            setType(value.getType());
            setSigma(value.getSigma());
            setLanguage(value.getLanguage());
            setActive(value.getActive());
            setComment(value.getComment());
            setMetadata(value.getMetadata());
            setCreatedAt(value.getCreatedAt());
            setCreatedBy(value.getCreatedBy());
            setUpdatedAt(value.getUpdatedAt());
            setUpdatedBy(value.getUpdatedBy());
        }
    }

        public SPermSetRecord(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }
}
