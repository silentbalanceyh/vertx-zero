/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.erp.domain.tables.interfaces;


import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.io.Serializable;
import java.time.LocalDateTime;

import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.setOrThrow;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface ITVendorHour extends VertxPojo, Serializable {

    /**
     * Setter for <code>DB_ETERNAL.T_VENDOR_HOUR.KEY</code>. 「key」- Ticket
     * Primary Key
     */
    public ITVendorHour setKey(String value);

    /**
     * Getter for <code>DB_ETERNAL.T_VENDOR_HOUR.KEY</code>. 「key」- Ticket
     * Primary Key
     */
    public String getKey();

    /**
     * Setter for <code>DB_ETERNAL.T_VENDOR_HOUR.COMMENT_EXTENSION</code>.
     * 「commentExtension」- Extension Comment
     */
    public ITVendorHour setCommentExtension(String value);

    /**
     * Getter for <code>DB_ETERNAL.T_VENDOR_HOUR.COMMENT_EXTENSION</code>.
     * 「commentExtension」- Extension Comment
     */
    public String getCommentExtension();

    /**
     * Setter for <code>DB_ETERNAL.T_VENDOR_HOUR.CLASSIFICATION</code>.
     * 「classification」- The ticket related business type
     */
    public ITVendorHour setClassification(String value);

    /**
     * Getter for <code>DB_ETERNAL.T_VENDOR_HOUR.CLASSIFICATION</code>.
     * 「classification」- The ticket related business type
     */
    public String getClassification();

    /**
     * Setter for <code>DB_ETERNAL.T_VENDOR_HOUR.START_AT</code>. 「startAt」-
     * From
     */
    public ITVendorHour setStartAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.T_VENDOR_HOUR.START_AT</code>. 「startAt」-
     * From
     */
    public LocalDateTime getStartAt();

    /**
     * Setter for <code>DB_ETERNAL.T_VENDOR_HOUR.END_AT</code>. 「endAt」- To
     */
    public ITVendorHour setEndAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.T_VENDOR_HOUR.END_AT</code>. 「endAt」- To
     */
    public LocalDateTime getEndAt();

    /**
     * Setter for <code>DB_ETERNAL.T_VENDOR_HOUR.DAYS</code>. 「days」- Duration
     */
    public ITVendorHour setDays(Integer value);

    /**
     * Getter for <code>DB_ETERNAL.T_VENDOR_HOUR.DAYS</code>. 「days」- Duration
     */
    public Integer getDays();

    /**
     * Setter for <code>DB_ETERNAL.T_VENDOR_HOUR.REQUEST_TYPE</code>.
     * 「requestType」- Request type of hour
     */
    public ITVendorHour setRequestType(String value);

    /**
     * Getter for <code>DB_ETERNAL.T_VENDOR_HOUR.REQUEST_TYPE</code>.
     * 「requestType」- Request type of hour
     */
    public String getRequestType();

    /**
     * Setter for <code>DB_ETERNAL.T_VENDOR_HOUR.FROM_TYPE</code>. 「fromType」
     */
    public ITVendorHour setFromType(String value);

    /**
     * Getter for <code>DB_ETERNAL.T_VENDOR_HOUR.FROM_TYPE</code>. 「fromType」
     */
    public String getFromType();

    /**
     * Setter for <code>DB_ETERNAL.T_VENDOR_HOUR.FROM_AT</code>. 「fromAt」
     */
    public ITVendorHour setFromAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.T_VENDOR_HOUR.FROM_AT</code>. 「fromAt」
     */
    public LocalDateTime getFromAt();

    /**
     * Setter for <code>DB_ETERNAL.T_VENDOR_HOUR.TO_TYPE</code>. 「toType」
     */
    public ITVendorHour setToType(String value);

    /**
     * Getter for <code>DB_ETERNAL.T_VENDOR_HOUR.TO_TYPE</code>. 「toType」
     */
    public String getToType();

    /**
     * Setter for <code>DB_ETERNAL.T_VENDOR_HOUR.TO_AT</code>. 「toAt」
     */
    public ITVendorHour setToAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.T_VENDOR_HOUR.TO_AT</code>. 「toAt」
     */
    public LocalDateTime getToAt();

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common
     * interface ITVendorHour
     */
    public void from(ITVendorHour from);

    /**
     * Copy data into another generated Record/POJO implementing the common
     * interface ITVendorHour
     */
    public <E extends ITVendorHour> E into(E into);

        @Override
        public default ITVendorHour fromJson(io.vertx.core.json.JsonObject json) {
                setOrThrow(this::setKey,json::getString,"KEY","java.lang.String");
                setOrThrow(this::setCommentExtension,json::getString,"COMMENT_EXTENSION","java.lang.String");
                setOrThrow(this::setClassification,json::getString,"CLASSIFICATION","java.lang.String");
                setOrThrow(this::setStartAt,key -> {String s = json.getString(key); return s==null?null:java.time.LocalDateTime.parse(s);},"START_AT","java.time.LocalDateTime");
                setOrThrow(this::setEndAt,key -> {String s = json.getString(key); return s==null?null:java.time.LocalDateTime.parse(s);},"END_AT","java.time.LocalDateTime");
                setOrThrow(this::setDays,json::getInteger,"DAYS","java.lang.Integer");
                setOrThrow(this::setRequestType,json::getString,"REQUEST_TYPE","java.lang.String");
                setOrThrow(this::setFromType,json::getString,"FROM_TYPE","java.lang.String");
                setOrThrow(this::setFromAt,key -> {String s = json.getString(key); return s==null?null:java.time.LocalDateTime.parse(s);},"FROM_AT","java.time.LocalDateTime");
                setOrThrow(this::setToType,json::getString,"TO_TYPE","java.lang.String");
                setOrThrow(this::setToAt,key -> {String s = json.getString(key); return s==null?null:java.time.LocalDateTime.parse(s);},"TO_AT","java.time.LocalDateTime");
                return this;
        }


        @Override
        public default io.vertx.core.json.JsonObject toJson() {
                io.vertx.core.json.JsonObject json = new io.vertx.core.json.JsonObject();
                json.put("KEY",getKey());
                json.put("COMMENT_EXTENSION",getCommentExtension());
                json.put("CLASSIFICATION",getClassification());
                json.put("START_AT",getStartAt()==null?null:getStartAt().toString());
                json.put("END_AT",getEndAt()==null?null:getEndAt().toString());
                json.put("DAYS",getDays());
                json.put("REQUEST_TYPE",getRequestType());
                json.put("FROM_TYPE",getFromType());
                json.put("FROM_AT",getFromAt()==null?null:getFromAt().toString());
                json.put("TO_TYPE",getToType());
                json.put("TO_AT",getToAt()==null?null:getToAt().toString());
                return json;
        }

}
