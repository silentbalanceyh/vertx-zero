package io.vertx.tp.plugin.cache.l1;

import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class L1Config implements Serializable {

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private transient Class<?> component;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject options;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject matrix;

    public Class<?> getComponent() {
        return this.component;
    }

    public void setComponent(final Class<?> component) {
        this.component = component;
    }

    public JsonObject getOptions() {
        return this.options;
    }

    public void setOptions(final JsonObject options) {
        this.options = options;
    }

    public JsonObject getMatrix() {
        return this.matrix;
    }

    public void setMatrix(final JsonObject matrix) {
        this.matrix = matrix;
    }

}
