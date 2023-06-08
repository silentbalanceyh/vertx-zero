package io.vertx.mod.ui.atom;

import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

/*
 * Ui Configuration data
 */
public class UiConfig implements Serializable {

    private transient String definition;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject mapping;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject attributes;

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private transient JsonArray op;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject cache;

    // Sec Expired seconds
    private transient int cacheExpired = 7200;

    public JsonObject getAttributes() {
        return this.attributes;
    }

    public void setAttributes(final JsonObject attributes) {
        this.attributes = attributes;
    }

    public String getDefinition() {
        return this.definition;
    }

    public void setDefinition(final String definition) {
        this.definition = definition;
    }

    public JsonObject getMapping() {
        return this.mapping;
    }

    public void setMapping(final JsonObject mapping) {
        this.mapping = mapping;
    }

    public JsonArray getOp() {
        return this.op;
    }

    public void setOp(final JsonArray op) {
        this.op = op;
    }

    public JsonObject getCache() {
        return this.cache;
    }

    public void setCache(final JsonObject cache) {
        this.cache = cache;
    }

    public boolean okCache() {
        return Ut.isNotNil(this.cache);
    }

    public String keyControl() {
        Objects.requireNonNull(this.cache);
        return this.cache.getString(KName.Ui.CONTROLS);
    }

    public String keyOps() {
        Objects.requireNonNull(this.cache);
        return this.cache.getString("ops");
    }

    public int getCacheExpired() {
        Objects.requireNonNull(this.cache);
        return this.cacheExpired;
    }

    public void setCacheExpired(final int cacheExpired) {
        this.cacheExpired = cacheExpired;
    }

    @Override
    public String toString() {
        return "UiConfig{" +
            "definition='" + this.definition + '\'' +
            ", mapping=" + this.mapping +
            ", attributes=" + this.attributes +
            ", op=" + this.op +
            ", cache=" + this.cache +
            ", cacheExpired=" + this.cacheExpired +
            '}';
    }
}
