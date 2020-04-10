package io.vertx.tp.ui.atom;

import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;

/*
 * Ui Configuration data
 */
public class UiConfig implements Serializable {

    private transient String definition;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject mapping;

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

    @Override
    public String toString() {
        return "UiConfig{" +
                "definition='" + this.definition + '\'' +
                ", mapping=" + this.mapping +
                '}';
    }
}
