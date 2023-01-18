/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.erp.domain.tables.pojos;


import cn.vertxup.erp.domain.tables.interfaces.ITVendorHour;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TVendorHour implements VertxPojo, ITVendorHour {

    private static final long serialVersionUID = 1L;

    private String key;
    private String commentExtension;
    private String classification;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Integer days;
    private String requestType;
    private String fromType;
    private LocalDateTime fromAt;
    private String toType;
    private LocalDateTime toAt;

    public TVendorHour() {}

    public TVendorHour(ITVendorHour value) {
        this.key = value.getKey();
        this.commentExtension = value.getCommentExtension();
        this.classification = value.getClassification();
        this.startAt = value.getStartAt();
        this.endAt = value.getEndAt();
        this.days = value.getDays();
        this.requestType = value.getRequestType();
        this.fromType = value.getFromType();
        this.fromAt = value.getFromAt();
        this.toType = value.getToType();
        this.toAt = value.getToAt();
    }

    public TVendorHour(
        String key,
        String commentExtension,
        String classification,
        LocalDateTime startAt,
        LocalDateTime endAt,
        Integer days,
        String requestType,
        String fromType,
        LocalDateTime fromAt,
        String toType,
        LocalDateTime toAt
    ) {
        this.key = key;
        this.commentExtension = commentExtension;
        this.classification = classification;
        this.startAt = startAt;
        this.endAt = endAt;
        this.days = days;
        this.requestType = requestType;
        this.fromType = fromType;
        this.fromAt = fromAt;
        this.toType = toType;
        this.toAt = toAt;
    }

        public TVendorHour(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_HOTEL.T_VENDOR_HOUR.KEY</code>. 「key」- Ticket Primary
     * Key
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_HOTEL.T_VENDOR_HOUR.KEY</code>. 「key」- Ticket Primary
     * Key
     */
    @Override
    public TVendorHour setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.T_VENDOR_HOUR.COMMENT_EXTENSION</code>.
     * 「commentExtension」- Extension Comment
     */
    @Override
    public String getCommentExtension() {
        return this.commentExtension;
    }

    /**
     * Setter for <code>DB_HOTEL.T_VENDOR_HOUR.COMMENT_EXTENSION</code>.
     * 「commentExtension」- Extension Comment
     */
    @Override
    public TVendorHour setCommentExtension(String commentExtension) {
        this.commentExtension = commentExtension;
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.T_VENDOR_HOUR.CLASSIFICATION</code>.
     * 「classification」- The ticket related business type
     */
    @Override
    public String getClassification() {
        return this.classification;
    }

    /**
     * Setter for <code>DB_HOTEL.T_VENDOR_HOUR.CLASSIFICATION</code>.
     * 「classification」- The ticket related business type
     */
    @Override
    public TVendorHour setClassification(String classification) {
        this.classification = classification;
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.T_VENDOR_HOUR.START_AT</code>. 「startAt」- From
     */
    @Override
    public LocalDateTime getStartAt() {
        return this.startAt;
    }

    /**
     * Setter for <code>DB_HOTEL.T_VENDOR_HOUR.START_AT</code>. 「startAt」- From
     */
    @Override
    public TVendorHour setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.T_VENDOR_HOUR.END_AT</code>. 「endAt」- To
     */
    @Override
    public LocalDateTime getEndAt() {
        return this.endAt;
    }

    /**
     * Setter for <code>DB_HOTEL.T_VENDOR_HOUR.END_AT</code>. 「endAt」- To
     */
    @Override
    public TVendorHour setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.T_VENDOR_HOUR.DAYS</code>. 「days」- Duration
     */
    @Override
    public Integer getDays() {
        return this.days;
    }

    /**
     * Setter for <code>DB_HOTEL.T_VENDOR_HOUR.DAYS</code>. 「days」- Duration
     */
    @Override
    public TVendorHour setDays(Integer days) {
        this.days = days;
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.T_VENDOR_HOUR.REQUEST_TYPE</code>.
     * 「requestType」- Request type of hour
     */
    @Override
    public String getRequestType() {
        return this.requestType;
    }

    /**
     * Setter for <code>DB_HOTEL.T_VENDOR_HOUR.REQUEST_TYPE</code>.
     * 「requestType」- Request type of hour
     */
    @Override
    public TVendorHour setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.T_VENDOR_HOUR.FROM_TYPE</code>. 「fromType」
     */
    @Override
    public String getFromType() {
        return this.fromType;
    }

    /**
     * Setter for <code>DB_HOTEL.T_VENDOR_HOUR.FROM_TYPE</code>. 「fromType」
     */
    @Override
    public TVendorHour setFromType(String fromType) {
        this.fromType = fromType;
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.T_VENDOR_HOUR.FROM_AT</code>. 「fromAt」
     */
    @Override
    public LocalDateTime getFromAt() {
        return this.fromAt;
    }

    /**
     * Setter for <code>DB_HOTEL.T_VENDOR_HOUR.FROM_AT</code>. 「fromAt」
     */
    @Override
    public TVendorHour setFromAt(LocalDateTime fromAt) {
        this.fromAt = fromAt;
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.T_VENDOR_HOUR.TO_TYPE</code>. 「toType」
     */
    @Override
    public String getToType() {
        return this.toType;
    }

    /**
     * Setter for <code>DB_HOTEL.T_VENDOR_HOUR.TO_TYPE</code>. 「toType」
     */
    @Override
    public TVendorHour setToType(String toType) {
        this.toType = toType;
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.T_VENDOR_HOUR.TO_AT</code>. 「toAt」
     */
    @Override
    public LocalDateTime getToAt() {
        return this.toAt;
    }

    /**
     * Setter for <code>DB_HOTEL.T_VENDOR_HOUR.TO_AT</code>. 「toAt」
     */
    @Override
    public TVendorHour setToAt(LocalDateTime toAt) {
        this.toAt = toAt;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TVendorHour other = (TVendorHour) obj;
        if (this.key == null) {
            if (other.key != null)
                return false;
        }
        else if (!this.key.equals(other.key))
            return false;
        if (this.commentExtension == null) {
            if (other.commentExtension != null)
                return false;
        }
        else if (!this.commentExtension.equals(other.commentExtension))
            return false;
        if (this.classification == null) {
            if (other.classification != null)
                return false;
        }
        else if (!this.classification.equals(other.classification))
            return false;
        if (this.startAt == null) {
            if (other.startAt != null)
                return false;
        }
        else if (!this.startAt.equals(other.startAt))
            return false;
        if (this.endAt == null) {
            if (other.endAt != null)
                return false;
        }
        else if (!this.endAt.equals(other.endAt))
            return false;
        if (this.days == null) {
            if (other.days != null)
                return false;
        }
        else if (!this.days.equals(other.days))
            return false;
        if (this.requestType == null) {
            if (other.requestType != null)
                return false;
        }
        else if (!this.requestType.equals(other.requestType))
            return false;
        if (this.fromType == null) {
            if (other.fromType != null)
                return false;
        }
        else if (!this.fromType.equals(other.fromType))
            return false;
        if (this.fromAt == null) {
            if (other.fromAt != null)
                return false;
        }
        else if (!this.fromAt.equals(other.fromAt))
            return false;
        if (this.toType == null) {
            if (other.toType != null)
                return false;
        }
        else if (!this.toType.equals(other.toType))
            return false;
        if (this.toAt == null) {
            if (other.toAt != null)
                return false;
        }
        else if (!this.toAt.equals(other.toAt))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.key == null) ? 0 : this.key.hashCode());
        result = prime * result + ((this.commentExtension == null) ? 0 : this.commentExtension.hashCode());
        result = prime * result + ((this.classification == null) ? 0 : this.classification.hashCode());
        result = prime * result + ((this.startAt == null) ? 0 : this.startAt.hashCode());
        result = prime * result + ((this.endAt == null) ? 0 : this.endAt.hashCode());
        result = prime * result + ((this.days == null) ? 0 : this.days.hashCode());
        result = prime * result + ((this.requestType == null) ? 0 : this.requestType.hashCode());
        result = prime * result + ((this.fromType == null) ? 0 : this.fromType.hashCode());
        result = prime * result + ((this.fromAt == null) ? 0 : this.fromAt.hashCode());
        result = prime * result + ((this.toType == null) ? 0 : this.toType.hashCode());
        result = prime * result + ((this.toAt == null) ? 0 : this.toAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TVendorHour (");

        sb.append(key);
        sb.append(", ").append(commentExtension);
        sb.append(", ").append(classification);
        sb.append(", ").append(startAt);
        sb.append(", ").append(endAt);
        sb.append(", ").append(days);
        sb.append(", ").append(requestType);
        sb.append(", ").append(fromType);
        sb.append(", ").append(fromAt);
        sb.append(", ").append(toType);
        sb.append(", ").append(toAt);

        sb.append(")");
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    @Override
    public void from(ITVendorHour from) {
        setKey(from.getKey());
        setCommentExtension(from.getCommentExtension());
        setClassification(from.getClassification());
        setStartAt(from.getStartAt());
        setEndAt(from.getEndAt());
        setDays(from.getDays());
        setRequestType(from.getRequestType());
        setFromType(from.getFromType());
        setFromAt(from.getFromAt());
        setToType(from.getToType());
        setToAt(from.getToAt());
    }

    @Override
    public <E extends ITVendorHour> E into(E into) {
        into.from(this);
        return into;
    }
}
