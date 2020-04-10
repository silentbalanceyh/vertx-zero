package io.vertx.tp.crud.atom;

import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.ke.cv.KeField;

import java.io.Serializable;
import java.util.Objects;

/*
 * New configuration in `IxModule` here
 * 1) field -> it means that the configuration dispatched
 * 2) joined -> Join configuration of `IxModule`
 * The data structure is as following:
 * {
 *     "joinedBy": "<identifier>",
 *     "joined": {
 *         "<identifier1>": "file1",
 *         "<identifier2>": "file2"
 *     },
 *     "mappedBy": "field name of current model",
 *     "mapped": {
 *         "<identifier1>": "joinedField1"
 *     }
 * }
 */
public class IxJoin implements Serializable {
    /*
     * Joined identifier
     */
    private String joinedBy;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject joined;
    /*
     * mapped field
     */
    private String mappedBy;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject mapped;

    public String getJoinedBy() {
        return this.joinedBy;
    }

    public void setJoinedBy(final String joinedBy) {
        this.joinedBy = joinedBy;
    }

    public JsonObject getJoined() {
        return this.joined;
    }

    public void setJoined(final JsonObject joined) {
        this.joined = joined;
    }

    public String getMappedBy() {
        return this.mappedBy;
    }

    public void setMappedBy(final String mappedBy) {
        this.mappedBy = mappedBy;
    }

    public JsonObject getMapped() {
        return this.mapped;
    }

    public void setMapped(final JsonObject mapped) {
        this.mapped = mapped;
    }

    public IxModule getModule(final String identifier) {
        if (Objects.isNull(this.joined)) {
            return null;
        } else {
            final String joinedId = this.joined.getString(identifier);
            if (Objects.isNull(joinedId)) {
                return null;
            } else {
                return IxPin.getActor(joinedId);
            }
        }
    }

    public String getJoined(final JsonObject data) {
        final String moduleName = this.getJoinedBy();
        final String identifier = data.getString(moduleName);
        return this.getMapped(identifier);
    }

    private String getMapped(final String identifier) {
        if (Objects.isNull(this.mapped)) {
            /*
             * The default key is key
             */
            return KeField.KEY;
        } else {
            return this.mapped.getString(identifier);
        }
    }

    @Override
    public String toString() {
        return "IxJoin{" +
                "joinedBy='" + this.joinedBy + '\'' +
                ", joined=" + this.joined +
                ", mappedBy='" + this.mappedBy + '\'' +
                ", mapped=" + this.mapped +
                '}';
    }
}
