/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.erp.domain.tables.interfaces;


import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.io.Serializable;
import java.time.LocalDateTime;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface ITVendorCheckIn extends VertxPojo, Serializable {

    /**
     * Setter for <code>DB_HOTEL.T_VENDOR_CHECK_IN.KEY</code>. 「key」- Ticket
     * Primary Key
     */
    public ITVendorCheckIn setKey(String value);

    /**
     * Getter for <code>DB_HOTEL.T_VENDOR_CHECK_IN.KEY</code>. 「key」- Ticket
     * Primary Key
     */
    public String getKey();

    /**
     * Setter for <code>DB_HOTEL.T_VENDOR_CHECK_IN.COMMENT_EXTENSION</code>.
     * 「commentExtension」- Extension Comment
     */
    public ITVendorCheckIn setCommentExtension(String value);

    /**
     * Getter for <code>DB_HOTEL.T_VENDOR_CHECK_IN.COMMENT_EXTENSION</code>.
     * 「commentExtension」- Extension Comment
     */
    public String getCommentExtension();

    /**
     * Setter for <code>DB_HOTEL.T_VENDOR_CHECK_IN.CLASSIFICATION</code>.
     * 「classification」- The ticket related business type
     */
    public ITVendorCheckIn setClassification(String value);

    /**
     * Getter for <code>DB_HOTEL.T_VENDOR_CHECK_IN.CLASSIFICATION</code>.
     * 「classification」- The ticket related business type
     */
    public String getClassification();

    /**
     * Setter for <code>DB_HOTEL.T_VENDOR_CHECK_IN.START_AT</code>. 「startAt」-
     * From
     */
    public ITVendorCheckIn setStartAt(LocalDateTime value);

    /**
     * Getter for <code>DB_HOTEL.T_VENDOR_CHECK_IN.START_AT</code>. 「startAt」-
     * From
     */
    public LocalDateTime getStartAt();

    /**
     * Setter for <code>DB_HOTEL.T_VENDOR_CHECK_IN.END_AT</code>. 「endAt」- To
     */
    public ITVendorCheckIn setEndAt(LocalDateTime value);

    /**
     * Getter for <code>DB_HOTEL.T_VENDOR_CHECK_IN.END_AT</code>. 「endAt」- To
     */
    public LocalDateTime getEndAt();

    /**
     * Setter for <code>DB_HOTEL.T_VENDOR_CHECK_IN.DAYS</code>. 「days」- Duration
     */
    public ITVendorCheckIn setDays(Integer value);

    /**
     * Getter for <code>DB_HOTEL.T_VENDOR_CHECK_IN.DAYS</code>. 「days」- Duration
     */
    public Integer getDays();

    /**
     * Setter for <code>DB_HOTEL.T_VENDOR_CHECK_IN.ONBOARD_AT</code>.
     * 「onboardAt」- To
     */
    public ITVendorCheckIn setOnboardAt(LocalDateTime value);

    /**
     * Getter for <code>DB_HOTEL.T_VENDOR_CHECK_IN.ONBOARD_AT</code>.
     * 「onboardAt」- To
     */
    public LocalDateTime getOnboardAt();

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common
     * interface ITVendorCheckIn
     */
    public void from(ITVendorCheckIn from);

    /**
     * Copy data into another generated Record/POJO implementing the common
     * interface ITVendorCheckIn
     */
    public <E extends ITVendorCheckIn> E into(E into);

        @Override
        public default ITVendorCheckIn fromJson(io.vertx.core.json.JsonObject json) {
                setOrThrow(this::setKey,json::getString,"KEY","java.lang.String");
                setOrThrow(this::setCommentExtension,json::getString,"COMMENT_EXTENSION","java.lang.String");
                setOrThrow(this::setClassification,json::getString,"CLASSIFICATION","java.lang.String");
                setOrThrow(this::setStartAt,key -> {String s = json.getString(key); return s==null?null:java.time.LocalDateTime.parse(s);},"START_AT","java.time.LocalDateTime");
                setOrThrow(this::setEndAt,key -> {String s = json.getString(key); return s==null?null:java.time.LocalDateTime.parse(s);},"END_AT","java.time.LocalDateTime");
                setOrThrow(this::setDays,json::getInteger,"DAYS","java.lang.Integer");
                setOrThrow(this::setOnboardAt,key -> {String s = json.getString(key); return s==null?null:java.time.LocalDateTime.parse(s);},"ONBOARD_AT","java.time.LocalDateTime");
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
                json.put("ONBOARD_AT",getOnboardAt()==null?null:getOnboardAt().toString());
                return json;
        }

}
