/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.erp.domain.tables;


import cn.vertxup.erp.domain.Db;
import cn.vertxup.erp.domain.Keys;
import cn.vertxup.erp.domain.tables.records.TOaTripRecord;

import java.time.LocalDateTime;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function11;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row11;
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
public class TOaTrip extends TableImpl<TOaTripRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DB_HOTEL.T_OA_TRIP</code>
     */
    public static final TOaTrip T_OA_TRIP = new TOaTrip();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TOaTripRecord> getRecordType() {
        return TOaTripRecord.class;
    }

    /**
     * The column <code>DB_HOTEL.T_OA_TRIP.KEY</code>. 「key」- Ticket Primary Key
     */
    public final TableField<TOaTripRecord, String> KEY = createField(DSL.name("KEY"), SQLDataType.VARCHAR(36).nullable(false), this, "「key」- Ticket Primary Key");

    /**
     * The column <code>DB_HOTEL.T_OA_TRIP.COMMENT_EXTENSION</code>.
     * 「commentExtension」- Extension Comment
     */
    public final TableField<TOaTripRecord, String> COMMENT_EXTENSION = createField(DSL.name("COMMENT_EXTENSION"), SQLDataType.CLOB, this, "「commentExtension」- Extension Comment");

    /**
     * The column <code>DB_HOTEL.T_OA_TRIP.REQUEST_BY</code>. 「requestBy」-
     * Request User
     */
    public final TableField<TOaTripRecord, String> REQUEST_BY = createField(DSL.name("REQUEST_BY"), SQLDataType.VARCHAR(36), this, "「requestBy」- Request User");

    /**
     * The column <code>DB_HOTEL.T_OA_TRIP.START_AT</code>. 「startAt」- From
     */
    public final TableField<TOaTripRecord, LocalDateTime> START_AT = createField(DSL.name("START_AT"), SQLDataType.LOCALDATETIME(0), this, "「startAt」- From");

    /**
     * The column <code>DB_HOTEL.T_OA_TRIP.END_AT</code>. 「endAt」- To
     */
    public final TableField<TOaTripRecord, LocalDateTime> END_AT = createField(DSL.name("END_AT"), SQLDataType.LOCALDATETIME(0), this, "「endAt」- To");

    /**
     * The column <code>DB_HOTEL.T_OA_TRIP.DAYS</code>. 「days」- Duration
     */
    public final TableField<TOaTripRecord, Integer> DAYS = createField(DSL.name("DAYS"), SQLDataType.INTEGER, this, "「days」- Duration");

    /**
     * The column <code>DB_HOTEL.T_OA_TRIP.TRIP_PROVINCE</code>. 「tripProvince」-
     * Trip Province
     */
    public final TableField<TOaTripRecord, String> TRIP_PROVINCE = createField(DSL.name("TRIP_PROVINCE"), SQLDataType.VARCHAR(36), this, "「tripProvince」- Trip Province");

    /**
     * The column <code>DB_HOTEL.T_OA_TRIP.TRIP_CITY</code>. 「tripCity」- Trip
     * City
     */
    public final TableField<TOaTripRecord, String> TRIP_CITY = createField(DSL.name("TRIP_CITY"), SQLDataType.VARCHAR(36), this, "「tripCity」- Trip City");

    /**
     * The column <code>DB_HOTEL.T_OA_TRIP.TRIP_ADDRESS</code>. 「tripAddress」-
     * Trip Address
     */
    public final TableField<TOaTripRecord, String> TRIP_ADDRESS = createField(DSL.name("TRIP_ADDRESS"), SQLDataType.CLOB, this, "「tripAddress」- Trip Address");

    /**
     * The column <code>DB_HOTEL.T_OA_TRIP.REASON</code>. 「reason」- The reason
     * to be done
     */
    public final TableField<TOaTripRecord, String> REASON = createField(DSL.name("REASON"), SQLDataType.CLOB, this, "「reason」- The reason to be done");

    /**
     * The column <code>DB_HOTEL.T_OA_TRIP.WORK_CONTENT</code>. 「workContent」-
     * Working Assignment Content
     */
    public final TableField<TOaTripRecord, String> WORK_CONTENT = createField(DSL.name("WORK_CONTENT"), SQLDataType.CLOB, this, "「workContent」- Working Assignment Content");

    private TOaTrip(Name alias, Table<TOaTripRecord> aliased) {
        this(alias, aliased, null);
    }

    private TOaTrip(Name alias, Table<TOaTripRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>DB_HOTEL.T_OA_TRIP</code> table reference
     */
    public TOaTrip(String alias) {
        this(DSL.name(alias), T_OA_TRIP);
    }

    /**
     * Create an aliased <code>DB_HOTEL.T_OA_TRIP</code> table reference
     */
    public TOaTrip(Name alias) {
        this(alias, T_OA_TRIP);
    }

    /**
     * Create a <code>DB_HOTEL.T_OA_TRIP</code> table reference
     */
    public TOaTrip() {
        this(DSL.name("T_OA_TRIP"), null);
    }

    public <O extends Record> TOaTrip(Table<O> child, ForeignKey<O, TOaTripRecord> key) {
        super(child, key, T_OA_TRIP);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Db.DB_HOTEL;
    }

    @Override
    public UniqueKey<TOaTripRecord> getPrimaryKey() {
        return Keys.KEY_T_OA_TRIP_PRIMARY;
    }

    @Override
    public TOaTrip as(String alias) {
        return new TOaTrip(DSL.name(alias), this);
    }

    @Override
    public TOaTrip as(Name alias) {
        return new TOaTrip(alias, this);
    }

    @Override
    public TOaTrip as(Table<?> alias) {
        return new TOaTrip(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public TOaTrip rename(String name) {
        return new TOaTrip(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TOaTrip rename(Name name) {
        return new TOaTrip(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public TOaTrip rename(Table<?> name) {
        return new TOaTrip(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row11 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row11<String, String, String, LocalDateTime, LocalDateTime, Integer, String, String, String, String, String> fieldsRow() {
        return (Row11) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function11<? super String, ? super String, ? super String, ? super LocalDateTime, ? super LocalDateTime, ? super Integer, ? super String, ? super String, ? super String, ? super String, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function11<? super String, ? super String, ? super String, ? super LocalDateTime, ? super LocalDateTime, ? super Integer, ? super String, ? super String, ? super String, ? super String, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
