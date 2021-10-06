/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.lbs.domain.tables.records;


import cn.vertxup.lbs.domain.tables.LLocation;
import cn.vertxup.lbs.domain.tables.interfaces.ILLocation;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record22;
import org.jooq.Row22;
import org.jooq.impl.UpdatableRecordImpl;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LLocationRecord extends UpdatableRecordImpl<LLocationRecord> implements VertxPojo, Record22<String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, Boolean, String, String, LocalDateTime, String, LocalDateTime, String>, ILLocation {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.KEY</code>. 「key」- 主键
     */
    @Override
    public LLocationRecord setKey(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.KEY</code>. 「key」- 主键
     */
    @Override
    public String getKey() {
        return (String) get(0);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.NAME</code>. 「name」- 名称
     */
    @Override
    public LLocationRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.NAME</code>. 「name」- 名称
     */
    @Override
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.CODE</code>. 「code」- 编码
     */
    @Override
    public LLocationRecord setCode(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.CODE</code>. 「code」- 编码
     */
    @Override
    public String getCode() {
        return (String) get(2);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.ADDRESS</code>. 「address」- 详细地址
     */
    @Override
    public LLocationRecord setAddress(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.ADDRESS</code>. 「address」- 详细地址
     */
    @Override
    public String getAddress() {
        return (String) get(3);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.CITY</code>. 「city」- 3.城市
     */
    @Override
    public LLocationRecord setCity(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.CITY</code>. 「city」- 3.城市
     */
    @Override
    public String getCity() {
        return (String) get(4);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.COUNTRY</code>. 「country」- 1.国家
     */
    @Override
    public LLocationRecord setCountry(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.COUNTRY</code>. 「country」- 1.国家
     */
    @Override
    public String getCountry() {
        return (String) get(5);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.REGION</code>. 「region」- 4.区域
     */
    @Override
    public LLocationRecord setRegion(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.REGION</code>. 「region」- 4.区域
     */
    @Override
    public String getRegion() {
        return (String) get(6);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.FULL_NAME</code>. 「fullName」- 地址全称
     */
    @Override
    public LLocationRecord setFullName(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.FULL_NAME</code>. 「fullName」- 地址全称
     */
    @Override
    public String getFullName() {
        return (String) get(7);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.STATE</code>. 「state」- 2.省会
     */
    @Override
    public LLocationRecord setState(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.STATE</code>. 「state」- 2.省会
     */
    @Override
    public String getState() {
        return (String) get(8);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.STREET1</code>. 「street1」- 街道1
     */
    @Override
    public LLocationRecord setStreet1(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.STREET1</code>. 「street1」- 街道1
     */
    @Override
    public String getStreet1() {
        return (String) get(9);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.STREET2</code>. 「street2」- 街道2
     */
    @Override
    public LLocationRecord setStreet2(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.STREET2</code>. 「street2」- 街道2
     */
    @Override
    public String getStreet2() {
        return (String) get(10);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.STREET3</code>. 「street3」- 街道3
     */
    @Override
    public LLocationRecord setStreet3(String value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.STREET3</code>. 「street3」- 街道3
     */
    @Override
    public String getStreet3() {
        return (String) get(11);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.POSTAL</code>. 「postal」- 邮政编码
     */
    @Override
    public LLocationRecord setPostal(String value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.POSTAL</code>. 「postal」- 邮政编码
     */
    @Override
    public String getPostal() {
        return (String) get(12);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public LLocationRecord setMetadata(String value) {
        set(13, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public String getMetadata() {
        return (String) get(13);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.REGION_ID</code>. 「regionId」- 区域ID
     */
    @Override
    public LLocationRecord setRegionId(String value) {
        set(14, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.REGION_ID</code>. 「regionId」- 区域ID
     */
    @Override
    public String getRegionId() {
        return (String) get(14);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public LLocationRecord setActive(Boolean value) {
        set(15, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return (Boolean) get(15);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public LLocationRecord setSigma(String value) {
        set(16, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return (String) get(16);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public LLocationRecord setLanguage(String value) {
        set(17, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public String getLanguage() {
        return (String) get(17);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LLocationRecord setCreatedAt(LocalDateTime value) {
        set(18, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(18);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public LLocationRecord setCreatedBy(String value) {
        set(19, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public String getCreatedBy() {
        return (String) get(19);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LLocationRecord setUpdatedAt(LocalDateTime value) {
        set(20, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(20);
    }

    /**
     * Setter for <code>DB_ETERNAL.L_LOCATION.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public LLocationRecord setUpdatedBy(String value) {
        set(21, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.L_LOCATION.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public String getUpdatedBy() {
        return (String) get(21);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record22 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row22<String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, Boolean, String, String, LocalDateTime, String, LocalDateTime, String> fieldsRow() {
        return (Row22) super.fieldsRow();
    }

    @Override
    public Row22<String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, Boolean, String, String, LocalDateTime, String, LocalDateTime, String> valuesRow() {
        return (Row22) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return LLocation.L_LOCATION.KEY;
    }

    @Override
    public Field<String> field2() {
        return LLocation.L_LOCATION.NAME;
    }

    @Override
    public Field<String> field3() {
        return LLocation.L_LOCATION.CODE;
    }

    @Override
    public Field<String> field4() {
        return LLocation.L_LOCATION.ADDRESS;
    }

    @Override
    public Field<String> field5() {
        return LLocation.L_LOCATION.CITY;
    }

    @Override
    public Field<String> field6() {
        return LLocation.L_LOCATION.COUNTRY;
    }

    @Override
    public Field<String> field7() {
        return LLocation.L_LOCATION.REGION;
    }

    @Override
    public Field<String> field8() {
        return LLocation.L_LOCATION.FULL_NAME;
    }

    @Override
    public Field<String> field9() {
        return LLocation.L_LOCATION.STATE;
    }

    @Override
    public Field<String> field10() {
        return LLocation.L_LOCATION.STREET1;
    }

    @Override
    public Field<String> field11() {
        return LLocation.L_LOCATION.STREET2;
    }

    @Override
    public Field<String> field12() {
        return LLocation.L_LOCATION.STREET3;
    }

    @Override
    public Field<String> field13() {
        return LLocation.L_LOCATION.POSTAL;
    }

    @Override
    public Field<String> field14() {
        return LLocation.L_LOCATION.METADATA;
    }

    @Override
    public Field<String> field15() {
        return LLocation.L_LOCATION.REGION_ID;
    }

    @Override
    public Field<Boolean> field16() {
        return LLocation.L_LOCATION.ACTIVE;
    }

    @Override
    public Field<String> field17() {
        return LLocation.L_LOCATION.SIGMA;
    }

    @Override
    public Field<String> field18() {
        return LLocation.L_LOCATION.LANGUAGE;
    }

    @Override
    public Field<LocalDateTime> field19() {
        return LLocation.L_LOCATION.CREATED_AT;
    }

    @Override
    public Field<String> field20() {
        return LLocation.L_LOCATION.CREATED_BY;
    }

    @Override
    public Field<LocalDateTime> field21() {
        return LLocation.L_LOCATION.UPDATED_AT;
    }

    @Override
    public Field<String> field22() {
        return LLocation.L_LOCATION.UPDATED_BY;
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
        return getAddress();
    }

    @Override
    public String component5() {
        return getCity();
    }

    @Override
    public String component6() {
        return getCountry();
    }

    @Override
    public String component7() {
        return getRegion();
    }

    @Override
    public String component8() {
        return getFullName();
    }

    @Override
    public String component9() {
        return getState();
    }

    @Override
    public String component10() {
        return getStreet1();
    }

    @Override
    public String component11() {
        return getStreet2();
    }

    @Override
    public String component12() {
        return getStreet3();
    }

    @Override
    public String component13() {
        return getPostal();
    }

    @Override
    public String component14() {
        return getMetadata();
    }

    @Override
    public String component15() {
        return getRegionId();
    }

    @Override
    public Boolean component16() {
        return getActive();
    }

    @Override
    public String component17() {
        return getSigma();
    }

    @Override
    public String component18() {
        return getLanguage();
    }

    @Override
    public LocalDateTime component19() {
        return getCreatedAt();
    }

    @Override
    public String component20() {
        return getCreatedBy();
    }

    @Override
    public LocalDateTime component21() {
        return getUpdatedAt();
    }

    @Override
    public String component22() {
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
        return getAddress();
    }

    @Override
    public String value5() {
        return getCity();
    }

    @Override
    public String value6() {
        return getCountry();
    }

    @Override
    public String value7() {
        return getRegion();
    }

    @Override
    public String value8() {
        return getFullName();
    }

    @Override
    public String value9() {
        return getState();
    }

    @Override
    public String value10() {
        return getStreet1();
    }

    @Override
    public String value11() {
        return getStreet2();
    }

    @Override
    public String value12() {
        return getStreet3();
    }

    @Override
    public String value13() {
        return getPostal();
    }

    @Override
    public String value14() {
        return getMetadata();
    }

    @Override
    public String value15() {
        return getRegionId();
    }

    @Override
    public Boolean value16() {
        return getActive();
    }

    @Override
    public String value17() {
        return getSigma();
    }

    @Override
    public String value18() {
        return getLanguage();
    }

    @Override
    public LocalDateTime value19() {
        return getCreatedAt();
    }

    @Override
    public String value20() {
        return getCreatedBy();
    }

    @Override
    public LocalDateTime value21() {
        return getUpdatedAt();
    }

    @Override
    public String value22() {
        return getUpdatedBy();
    }

    @Override
    public LLocationRecord value1(String value) {
        setKey(value);
        return this;
    }

    @Override
    public LLocationRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public LLocationRecord value3(String value) {
        setCode(value);
        return this;
    }

    @Override
    public LLocationRecord value4(String value) {
        setAddress(value);
        return this;
    }

    @Override
    public LLocationRecord value5(String value) {
        setCity(value);
        return this;
    }

    @Override
    public LLocationRecord value6(String value) {
        setCountry(value);
        return this;
    }

    @Override
    public LLocationRecord value7(String value) {
        setRegion(value);
        return this;
    }

    @Override
    public LLocationRecord value8(String value) {
        setFullName(value);
        return this;
    }

    @Override
    public LLocationRecord value9(String value) {
        setState(value);
        return this;
    }

    @Override
    public LLocationRecord value10(String value) {
        setStreet1(value);
        return this;
    }

    @Override
    public LLocationRecord value11(String value) {
        setStreet2(value);
        return this;
    }

    @Override
    public LLocationRecord value12(String value) {
        setStreet3(value);
        return this;
    }

    @Override
    public LLocationRecord value13(String value) {
        setPostal(value);
        return this;
    }

    @Override
    public LLocationRecord value14(String value) {
        setMetadata(value);
        return this;
    }

    @Override
    public LLocationRecord value15(String value) {
        setRegionId(value);
        return this;
    }

    @Override
    public LLocationRecord value16(Boolean value) {
        setActive(value);
        return this;
    }

    @Override
    public LLocationRecord value17(String value) {
        setSigma(value);
        return this;
    }

    @Override
    public LLocationRecord value18(String value) {
        setLanguage(value);
        return this;
    }

    @Override
    public LLocationRecord value19(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public LLocationRecord value20(String value) {
        setCreatedBy(value);
        return this;
    }

    @Override
    public LLocationRecord value21(LocalDateTime value) {
        setUpdatedAt(value);
        return this;
    }

    @Override
    public LLocationRecord value22(String value) {
        setUpdatedBy(value);
        return this;
    }

    @Override
    public LLocationRecord values(String value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8, String value9, String value10, String value11, String value12, String value13, String value14, String value15, Boolean value16, String value17, String value18, LocalDateTime value19, String value20, LocalDateTime value21, String value22) {
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
        value20(value20);
        value21(value21);
        value22(value22);
        return this;
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    @Override
    public void from(ILLocation from) {
        setKey(from.getKey());
        setName(from.getName());
        setCode(from.getCode());
        setAddress(from.getAddress());
        setCity(from.getCity());
        setCountry(from.getCountry());
        setRegion(from.getRegion());
        setFullName(from.getFullName());
        setState(from.getState());
        setStreet1(from.getStreet1());
        setStreet2(from.getStreet2());
        setStreet3(from.getStreet3());
        setPostal(from.getPostal());
        setMetadata(from.getMetadata());
        setRegionId(from.getRegionId());
        setActive(from.getActive());
        setSigma(from.getSigma());
        setLanguage(from.getLanguage());
        setCreatedAt(from.getCreatedAt());
        setCreatedBy(from.getCreatedBy());
        setUpdatedAt(from.getUpdatedAt());
        setUpdatedBy(from.getUpdatedBy());
    }

    @Override
    public <E extends ILLocation> E into(E into) {
        into.from(this);
        return into;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached LLocationRecord
     */
    public LLocationRecord() {
        super(LLocation.L_LOCATION);
    }

    /**
     * Create a detached, initialised LLocationRecord
     */
    public LLocationRecord(String key, String name, String code, String address, String city, String country, String region, String fullName, String state, String street1, String street2, String street3, String postal, String metadata, String regionId, Boolean active, String sigma, String language, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy) {
        super(LLocation.L_LOCATION);

        setKey(key);
        setName(name);
        setCode(code);
        setAddress(address);
        setCity(city);
        setCountry(country);
        setRegion(region);
        setFullName(fullName);
        setState(state);
        setStreet1(street1);
        setStreet2(street2);
        setStreet3(street3);
        setPostal(postal);
        setMetadata(metadata);
        setRegionId(regionId);
        setActive(active);
        setSigma(sigma);
        setLanguage(language);
        setCreatedAt(createdAt);
        setCreatedBy(createdBy);
        setUpdatedAt(updatedAt);
        setUpdatedBy(updatedBy);
    }

    /**
     * Create a detached, initialised LLocationRecord
     */
    public LLocationRecord(cn.vertxup.lbs.domain.tables.pojos.LLocation value) {
        super(LLocation.L_LOCATION);

        if (value != null) {
            setKey(value.getKey());
            setName(value.getName());
            setCode(value.getCode());
            setAddress(value.getAddress());
            setCity(value.getCity());
            setCountry(value.getCountry());
            setRegion(value.getRegion());
            setFullName(value.getFullName());
            setState(value.getState());
            setStreet1(value.getStreet1());
            setStreet2(value.getStreet2());
            setStreet3(value.getStreet3());
            setPostal(value.getPostal());
            setMetadata(value.getMetadata());
            setRegionId(value.getRegionId());
            setActive(value.getActive());
            setSigma(value.getSigma());
            setLanguage(value.getLanguage());
            setCreatedAt(value.getCreatedAt());
            setCreatedBy(value.getCreatedBy());
            setUpdatedAt(value.getUpdatedAt());
            setUpdatedBy(value.getUpdatedBy());
        }
    }

        public LLocationRecord(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }
}
