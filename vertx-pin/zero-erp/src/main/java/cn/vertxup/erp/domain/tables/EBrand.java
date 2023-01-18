/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.erp.domain.tables;


import cn.vertxup.erp.domain.Db;
import cn.vertxup.erp.domain.Indexes;
import cn.vertxup.erp.domain.Keys;
import cn.vertxup.erp.domain.tables.records.EBrandRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function18;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row18;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EBrand extends TableImpl<EBrandRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DB_HOTEL.E_BRAND</code>
     */
    public static final EBrand E_BRAND = new EBrand();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EBrandRecord> getRecordType() {
        return EBrandRecord.class;
    }

    /**
     * The column <code>DB_HOTEL.E_BRAND.KEY</code>. 「key」- 品牌ID
     */
    public final TableField<EBrandRecord, String> KEY = createField(DSL.name("KEY"), SQLDataType.VARCHAR(36).nullable(false), this, "「key」- 品牌ID");

    /**
     * The column <code>DB_HOTEL.E_BRAND.CODE</code>. 「code」- 品牌编码
     */
    public final TableField<EBrandRecord, String> CODE = createField(DSL.name("CODE"), SQLDataType.VARCHAR(255), this, "「code」- 品牌编码");

    /**
     * The column <code>DB_HOTEL.E_BRAND.NAME</code>. 「name」- 品牌名称
     */
    public final TableField<EBrandRecord, String> NAME = createField(DSL.name("NAME"), SQLDataType.VARCHAR(255), this, "「name」- 品牌名称");

    /**
     * The column <code>DB_HOTEL.E_BRAND.ALIAS</code>. 「alias」- 品牌别名
     */
    public final TableField<EBrandRecord, String> ALIAS = createField(DSL.name("ALIAS"), SQLDataType.VARCHAR(255), this, "「alias」- 品牌别名");

    /**
     * The column <code>DB_HOTEL.E_BRAND.COMPANY_NAME</code>. 「companyName」-
     * 品牌公司名
     */
    public final TableField<EBrandRecord, String> COMPANY_NAME = createField(DSL.name("COMPANY_NAME"), SQLDataType.VARCHAR(128), this, "「companyName」- 品牌公司名");

    /**
     * The column <code>DB_HOTEL.E_BRAND.CATEGORY_CODE</code>. 「categoryCode」-
     * 类别代码
     */
    public final TableField<EBrandRecord, String> CATEGORY_CODE = createField(DSL.name("CATEGORY_CODE"), SQLDataType.VARCHAR(16), this, "「categoryCode」- 类别代码");

    /**
     * The column <code>DB_HOTEL.E_BRAND.CATEGORY_NAME</code>. 「categoryName」-
     * 类别名称
     */
    public final TableField<EBrandRecord, String> CATEGORY_NAME = createField(DSL.name("CATEGORY_NAME"), SQLDataType.VARCHAR(128), this, "「categoryName」- 类别名称");

    /**
     * The column <code>DB_HOTEL.E_BRAND.AREA</code>. 「area」-
     * GB/T2659-2000国际标准区域码
     */
    public final TableField<EBrandRecord, Integer> AREA = createField(DSL.name("AREA"), SQLDataType.INTEGER, this, "「area」- GB/T2659-2000国际标准区域码");

    /**
     * The column <code>DB_HOTEL.E_BRAND.AREA_NAME</code>. 「areaName」- 区域名称
     */
    public final TableField<EBrandRecord, String> AREA_NAME = createField(DSL.name("AREA_NAME"), SQLDataType.VARCHAR(128), this, "「areaName」- 区域名称");

    /**
     * The column <code>DB_HOTEL.E_BRAND.AREA_CATEGORY</code>. 「areaCategory」-
     * 区域类别码
     */
    public final TableField<EBrandRecord, String> AREA_CATEGORY = createField(DSL.name("AREA_CATEGORY"), SQLDataType.VARCHAR(16), this, "「areaCategory」- 区域类别码");

    /**
     * The column <code>DB_HOTEL.E_BRAND.METADATA</code>. 「metadata」- 附加配置
     */
    public final TableField<EBrandRecord, String> METADATA = createField(DSL.name("METADATA"), SQLDataType.CLOB, this, "「metadata」- 附加配置");

    /**
     * The column <code>DB_HOTEL.E_BRAND.ACTIVE</code>. 「active」- 是否启用
     */
    public final TableField<EBrandRecord, Boolean> ACTIVE = createField(DSL.name("ACTIVE"), SQLDataType.BIT, this, "「active」- 是否启用");

    /**
     * The column <code>DB_HOTEL.E_BRAND.SIGMA</code>. 「sigma」- 统一标识（公司所属应用）
     */
    public final TableField<EBrandRecord, String> SIGMA = createField(DSL.name("SIGMA"), SQLDataType.VARCHAR(32), this, "「sigma」- 统一标识（公司所属应用）");

    /**
     * The column <code>DB_HOTEL.E_BRAND.LANGUAGE</code>. 「language」- 使用的语言
     */
    public final TableField<EBrandRecord, String> LANGUAGE = createField(DSL.name("LANGUAGE"), SQLDataType.VARCHAR(8), this, "「language」- 使用的语言");

    /**
     * The column <code>DB_HOTEL.E_BRAND.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public final TableField<EBrandRecord, LocalDateTime> CREATED_AT = createField(DSL.name("CREATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「createdAt」- 创建时间");

    /**
     * The column <code>DB_HOTEL.E_BRAND.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public final TableField<EBrandRecord, String> CREATED_BY = createField(DSL.name("CREATED_BY"), SQLDataType.VARCHAR(36), this, "「createdBy」- 创建人");

    /**
     * The column <code>DB_HOTEL.E_BRAND.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public final TableField<EBrandRecord, LocalDateTime> UPDATED_AT = createField(DSL.name("UPDATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「updatedAt」- 更新时间");

    /**
     * The column <code>DB_HOTEL.E_BRAND.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public final TableField<EBrandRecord, String> UPDATED_BY = createField(DSL.name("UPDATED_BY"), SQLDataType.VARCHAR(36), this, "「updatedBy」- 更新人");

    private EBrand(Name alias, Table<EBrandRecord> aliased) {
        this(alias, aliased, null);
    }

    private EBrand(Name alias, Table<EBrandRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>DB_HOTEL.E_BRAND</code> table reference
     */
    public EBrand(String alias) {
        this(DSL.name(alias), E_BRAND);
    }

    /**
     * Create an aliased <code>DB_HOTEL.E_BRAND</code> table reference
     */
    public EBrand(Name alias) {
        this(alias, E_BRAND);
    }

    /**
     * Create a <code>DB_HOTEL.E_BRAND</code> table reference
     */
    public EBrand() {
        this(DSL.name("E_BRAND"), null);
    }

    public <O extends Record> EBrand(Table<O> child, ForeignKey<O, EBrandRecord> key) {
        super(child, key, E_BRAND);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Db.DB_HOTEL;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.E_BRAND_IDX_E_BRAND_SIGMA);
    }

    @Override
    public UniqueKey<EBrandRecord> getPrimaryKey() {
        return Keys.KEY_E_BRAND_PRIMARY;
    }

    @Override
    public List<UniqueKey<EBrandRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_E_BRAND_CODE);
    }

    @Override
    public EBrand as(String alias) {
        return new EBrand(DSL.name(alias), this);
    }

    @Override
    public EBrand as(Name alias) {
        return new EBrand(alias, this);
    }

    @Override
    public EBrand as(Table<?> alias) {
        return new EBrand(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public EBrand rename(String name) {
        return new EBrand(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public EBrand rename(Name name) {
        return new EBrand(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public EBrand rename(Table<?> name) {
        return new EBrand(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row18 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row18<String, String, String, String, String, String, String, Integer, String, String, String, Boolean, String, String, LocalDateTime, String, LocalDateTime, String> fieldsRow() {
        return (Row18) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function18<? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super Integer, ? super String, ? super String, ? super String, ? super Boolean, ? super String, ? super String, ? super LocalDateTime, ? super String, ? super LocalDateTime, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function18<? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super Integer, ? super String, ? super String, ? super String, ? super Boolean, ? super String, ? super String, ? super LocalDateTime, ? super String, ? super LocalDateTime, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
