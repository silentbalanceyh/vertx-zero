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
public interface ITOaVocation extends VertxPojo, Serializable {

    /**
     * Setter for <code>DB_ETERNAL.T_OA_VOCATION.KEY</code>. 「key」- Ticket
     * Primary Key
     */
    public ITOaVocation setKey(String value);

    /**
     * Getter for <code>DB_ETERNAL.T_OA_VOCATION.KEY</code>. 「key」- Ticket
     * Primary Key
     */
    public String getKey();

    /**
     * Setter for <code>DB_ETERNAL.T_OA_VOCATION.COMMENT_EXTENSION</code>.
     * 「commentExtension」- Extension Comment
     */
    public ITOaVocation setCommentExtension(String value);

    /**
     * Getter for <code>DB_ETERNAL.T_OA_VOCATION.COMMENT_EXTENSION</code>.
     * 「commentExtension」- Extension Comment
     */
    public String getCommentExtension();

    /**
     * Setter for <code>DB_ETERNAL.T_OA_VOCATION.REQUEST_BY</code>. 「requestBy」-
     * Request User
     */
    public ITOaVocation setRequestBy(String value);

    /**
     * Getter for <code>DB_ETERNAL.T_OA_VOCATION.REQUEST_BY</code>. 「requestBy」-
     * Request User
     */
    public String getRequestBy();

    /**
     * Setter for <code>DB_ETERNAL.T_OA_VOCATION.CLASSIFICATION</code>.
     * 「classification」- The ticket related business type
     */
    public ITOaVocation setClassification(String value);

    /**
     * Getter for <code>DB_ETERNAL.T_OA_VOCATION.CLASSIFICATION</code>.
     * 「classification」- The ticket related business type
     */
    public String getClassification();

    /**
     * Setter for <code>DB_ETERNAL.T_OA_VOCATION.START_AT</code>. 「startAt」-
     * From
     */
    public ITOaVocation setStartAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.T_OA_VOCATION.START_AT</code>. 「startAt」-
     * From
     */
    public LocalDateTime getStartAt();

    /**
     * Setter for <code>DB_ETERNAL.T_OA_VOCATION.END_AT</code>. 「endAt」- To
     */
    public ITOaVocation setEndAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.T_OA_VOCATION.END_AT</code>. 「endAt」- To
     */
    public LocalDateTime getEndAt();

    /**
     * Setter for <code>DB_ETERNAL.T_OA_VOCATION.DAYS</code>. 「days」- Duration
     */
    public ITOaVocation setDays(Integer value);

    /**
     * Getter for <code>DB_ETERNAL.T_OA_VOCATION.DAYS</code>. 「days」- Duration
     */
    public Integer getDays();

    /**
     * Setter for <code>DB_ETERNAL.T_OA_VOCATION.REASON</code>. 「reason」- The
     * reason to be done
     */
    public ITOaVocation setReason(String value);

    /**
     * Getter for <code>DB_ETERNAL.T_OA_VOCATION.REASON</code>. 「reason」- The
     * reason to be done
     */
    public String getReason();

    /**
     * Setter for <code>DB_ETERNAL.T_OA_VOCATION.WORK_CONTENT</code>.
     * 「workContent」- Working Assignment Content
     */
    public ITOaVocation setWorkContent(String value);

    /**
     * Getter for <code>DB_ETERNAL.T_OA_VOCATION.WORK_CONTENT</code>.
     * 「workContent」- Working Assignment Content
     */
    public String getWorkContent();

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common
     * interface ITOaVocation
     */
    public void from(ITOaVocation from);

    /**
     * Copy data into another generated Record/POJO implementing the common
     * interface ITOaVocation
     */
    public <E extends ITOaVocation> E into(E into);

        @Override
        public default ITOaVocation fromJson(io.vertx.core.json.JsonObject json) {
                setOrThrow(this::setKey,json::getString,"KEY","java.lang.String");
                setOrThrow(this::setCommentExtension,json::getString,"COMMENT_EXTENSION","java.lang.String");
                setOrThrow(this::setRequestBy,json::getString,"REQUEST_BY","java.lang.String");
                setOrThrow(this::setClassification,json::getString,"CLASSIFICATION","java.lang.String");
                setOrThrow(this::setStartAt,key -> {String s = json.getString(key); return s==null?null:java.time.LocalDateTime.parse(s);},"START_AT","java.time.LocalDateTime");
                setOrThrow(this::setEndAt,key -> {String s = json.getString(key); return s==null?null:java.time.LocalDateTime.parse(s);},"END_AT","java.time.LocalDateTime");
                setOrThrow(this::setDays,json::getInteger,"DAYS","java.lang.Integer");
                setOrThrow(this::setReason,json::getString,"REASON","java.lang.String");
                setOrThrow(this::setWorkContent,json::getString,"WORK_CONTENT","java.lang.String");
                return this;
        }


        @Override
        public default io.vertx.core.json.JsonObject toJson() {
                io.vertx.core.json.JsonObject json = new io.vertx.core.json.JsonObject();
                json.put("KEY",getKey());
                json.put("COMMENT_EXTENSION",getCommentExtension());
                json.put("REQUEST_BY",getRequestBy());
                json.put("CLASSIFICATION",getClassification());
                json.put("START_AT",getStartAt()==null?null:getStartAt().toString());
                json.put("END_AT",getEndAt()==null?null:getEndAt().toString());
                json.put("DAYS",getDays());
                json.put("REASON",getReason());
                json.put("WORK_CONTENT",getWorkContent());
                return json;
        }

}