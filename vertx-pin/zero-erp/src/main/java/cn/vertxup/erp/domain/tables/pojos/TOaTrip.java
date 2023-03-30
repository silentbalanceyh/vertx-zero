/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.erp.domain.tables.pojos;


import cn.vertxup.erp.domain.tables.interfaces.ITOaTrip;
import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TOaTrip implements VertxPojo, ITOaTrip {

    private static final long serialVersionUID = 1L;

    private String key;
    private String commentExtension;
    private String requestBy;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Integer days;
    private String tripProvince;
    private String tripCity;
    private String tripAddress;
    private String reason;
    private String workContent;

    public TOaTrip() {}

    public TOaTrip(ITOaTrip value) {
        this.key = value.getKey();
        this.commentExtension = value.getCommentExtension();
        this.requestBy = value.getRequestBy();
        this.startAt = value.getStartAt();
        this.endAt = value.getEndAt();
        this.days = value.getDays();
        this.tripProvince = value.getTripProvince();
        this.tripCity = value.getTripCity();
        this.tripAddress = value.getTripAddress();
        this.reason = value.getReason();
        this.workContent = value.getWorkContent();
    }

    public TOaTrip(
        String key,
        String commentExtension,
        String requestBy,
        LocalDateTime startAt,
        LocalDateTime endAt,
        Integer days,
        String tripProvince,
        String tripCity,
        String tripAddress,
        String reason,
        String workContent
    ) {
        this.key = key;
        this.commentExtension = commentExtension;
        this.requestBy = requestBy;
        this.startAt = startAt;
        this.endAt = endAt;
        this.days = days;
        this.tripProvince = tripProvince;
        this.tripCity = tripCity;
        this.tripAddress = tripAddress;
        this.reason = reason;
        this.workContent = workContent;
    }

        public TOaTrip(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.T_OA_TRIP.KEY</code>. 「key」- Ticket Primary
     * Key
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.T_OA_TRIP.KEY</code>. 「key」- Ticket Primary
     * Key
     */
    @Override
    public TOaTrip setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.T_OA_TRIP.COMMENT_EXTENSION</code>.
     * 「commentExtension」- Extension Comment
     */
    @Override
    public String getCommentExtension() {
        return this.commentExtension;
    }

    /**
     * Setter for <code>DB_ETERNAL.T_OA_TRIP.COMMENT_EXTENSION</code>.
     * 「commentExtension」- Extension Comment
     */
    @Override
    public TOaTrip setCommentExtension(String commentExtension) {
        this.commentExtension = commentExtension;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.T_OA_TRIP.REQUEST_BY</code>. 「requestBy」-
     * Request User
     */
    @Override
    public String getRequestBy() {
        return this.requestBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.T_OA_TRIP.REQUEST_BY</code>. 「requestBy」-
     * Request User
     */
    @Override
    public TOaTrip setRequestBy(String requestBy) {
        this.requestBy = requestBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.T_OA_TRIP.START_AT</code>. 「startAt」- From
     */
    @Override
    public LocalDateTime getStartAt() {
        return this.startAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.T_OA_TRIP.START_AT</code>. 「startAt」- From
     */
    @Override
    public TOaTrip setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.T_OA_TRIP.END_AT</code>. 「endAt」- To
     */
    @Override
    public LocalDateTime getEndAt() {
        return this.endAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.T_OA_TRIP.END_AT</code>. 「endAt」- To
     */
    @Override
    public TOaTrip setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.T_OA_TRIP.DAYS</code>. 「days」- Duration
     */
    @Override
    public Integer getDays() {
        return this.days;
    }

    /**
     * Setter for <code>DB_ETERNAL.T_OA_TRIP.DAYS</code>. 「days」- Duration
     */
    @Override
    public TOaTrip setDays(Integer days) {
        this.days = days;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.T_OA_TRIP.TRIP_PROVINCE</code>.
     * 「tripProvince」- Trip Province
     */
    @Override
    public String getTripProvince() {
        return this.tripProvince;
    }

    /**
     * Setter for <code>DB_ETERNAL.T_OA_TRIP.TRIP_PROVINCE</code>.
     * 「tripProvince」- Trip Province
     */
    @Override
    public TOaTrip setTripProvince(String tripProvince) {
        this.tripProvince = tripProvince;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.T_OA_TRIP.TRIP_CITY</code>. 「tripCity」- Trip
     * City
     */
    @Override
    public String getTripCity() {
        return this.tripCity;
    }

    /**
     * Setter for <code>DB_ETERNAL.T_OA_TRIP.TRIP_CITY</code>. 「tripCity」- Trip
     * City
     */
    @Override
    public TOaTrip setTripCity(String tripCity) {
        this.tripCity = tripCity;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.T_OA_TRIP.TRIP_ADDRESS</code>. 「tripAddress」-
     * Trip Address
     */
    @Override
    public String getTripAddress() {
        return this.tripAddress;
    }

    /**
     * Setter for <code>DB_ETERNAL.T_OA_TRIP.TRIP_ADDRESS</code>. 「tripAddress」-
     * Trip Address
     */
    @Override
    public TOaTrip setTripAddress(String tripAddress) {
        this.tripAddress = tripAddress;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.T_OA_TRIP.REASON</code>. 「reason」- The reason
     * to be done
     */
    @Override
    public String getReason() {
        return this.reason;
    }

    /**
     * Setter for <code>DB_ETERNAL.T_OA_TRIP.REASON</code>. 「reason」- The reason
     * to be done
     */
    @Override
    public TOaTrip setReason(String reason) {
        this.reason = reason;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.T_OA_TRIP.WORK_CONTENT</code>. 「workContent」-
     * Working Assignment Content
     */
    @Override
    public String getWorkContent() {
        return this.workContent;
    }

    /**
     * Setter for <code>DB_ETERNAL.T_OA_TRIP.WORK_CONTENT</code>. 「workContent」-
     * Working Assignment Content
     */
    @Override
    public TOaTrip setWorkContent(String workContent) {
        this.workContent = workContent;
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
        final TOaTrip other = (TOaTrip) obj;
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
        if (this.requestBy == null) {
            if (other.requestBy != null)
                return false;
        }
        else if (!this.requestBy.equals(other.requestBy))
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
        if (this.tripProvince == null) {
            if (other.tripProvince != null)
                return false;
        }
        else if (!this.tripProvince.equals(other.tripProvince))
            return false;
        if (this.tripCity == null) {
            if (other.tripCity != null)
                return false;
        }
        else if (!this.tripCity.equals(other.tripCity))
            return false;
        if (this.tripAddress == null) {
            if (other.tripAddress != null)
                return false;
        }
        else if (!this.tripAddress.equals(other.tripAddress))
            return false;
        if (this.reason == null) {
            if (other.reason != null)
                return false;
        }
        else if (!this.reason.equals(other.reason))
            return false;
        if (this.workContent == null) {
            if (other.workContent != null)
                return false;
        }
        else if (!this.workContent.equals(other.workContent))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.key == null) ? 0 : this.key.hashCode());
        result = prime * result + ((this.commentExtension == null) ? 0 : this.commentExtension.hashCode());
        result = prime * result + ((this.requestBy == null) ? 0 : this.requestBy.hashCode());
        result = prime * result + ((this.startAt == null) ? 0 : this.startAt.hashCode());
        result = prime * result + ((this.endAt == null) ? 0 : this.endAt.hashCode());
        result = prime * result + ((this.days == null) ? 0 : this.days.hashCode());
        result = prime * result + ((this.tripProvince == null) ? 0 : this.tripProvince.hashCode());
        result = prime * result + ((this.tripCity == null) ? 0 : this.tripCity.hashCode());
        result = prime * result + ((this.tripAddress == null) ? 0 : this.tripAddress.hashCode());
        result = prime * result + ((this.reason == null) ? 0 : this.reason.hashCode());
        result = prime * result + ((this.workContent == null) ? 0 : this.workContent.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TOaTrip (");

        sb.append(key);
        sb.append(", ").append(commentExtension);
        sb.append(", ").append(requestBy);
        sb.append(", ").append(startAt);
        sb.append(", ").append(endAt);
        sb.append(", ").append(days);
        sb.append(", ").append(tripProvince);
        sb.append(", ").append(tripCity);
        sb.append(", ").append(tripAddress);
        sb.append(", ").append(reason);
        sb.append(", ").append(workContent);

        sb.append(")");
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    @Override
    public void from(ITOaTrip from) {
        setKey(from.getKey());
        setCommentExtension(from.getCommentExtension());
        setRequestBy(from.getRequestBy());
        setStartAt(from.getStartAt());
        setEndAt(from.getEndAt());
        setDays(from.getDays());
        setTripProvince(from.getTripProvince());
        setTripCity(from.getTripCity());
        setTripAddress(from.getTripAddress());
        setReason(from.getReason());
        setWorkContent(from.getWorkContent());
    }

    @Override
    public <E extends ITOaTrip> E into(E into) {
        into.from(this);
        return into;
    }
}
