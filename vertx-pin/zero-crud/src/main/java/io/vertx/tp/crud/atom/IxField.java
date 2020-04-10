package io.vertx.tp.crud.atom;

import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;

public class IxField implements Serializable {

    private String key;

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray unique;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject created;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject updated;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject numbers;

    public String getKey() {
        return this.key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public JsonArray getUnique() {
        return this.unique;
    }

    public void setUnique(final JsonArray unique) {
        this.unique = unique;
    }

    public JsonObject getCreated() {
        return this.created;
    }

    public void setCreated(final JsonObject created) {
        this.created = created;
    }

    public JsonObject getUpdated() {
        return this.updated;
    }

    public void setUpdated(final JsonObject updated) {
        this.updated = updated;
    }

    public JsonObject getNumbers() {
        return this.numbers;
    }

    public void setNumbers(final JsonObject numbers) {
        this.numbers = numbers;
    }

    @Override
    public String toString() {
        return "IxField{" +
                "key='" + this.key + '\'' +
                ", unique=" + this.unique +
                ", created=" + this.created +
                ", updated=" + this.updated +
                ", numbers=" + this.numbers +
                '}';
    }
}
