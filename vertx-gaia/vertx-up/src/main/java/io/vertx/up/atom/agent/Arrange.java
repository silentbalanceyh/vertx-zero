package io.vertx.up.atom.agent;

import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.DeployMode;

import java.io.Serializable;

public class Arrange implements Serializable {
    /* Default mode of deployment */
    private transient DeployMode mode = DeployMode.CODE;
    /* Options */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject options;
    /* Options */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject delivery;

    public DeployMode getMode() {
        return this.mode;
    }

    public void setMode(final DeployMode mode) {
        this.mode = mode;
    }

    public JsonObject getOptions() {
        return this.options;
    }

    public void setOptions(final JsonObject options) {
        this.options = options;
    }

    public JsonObject getDelivery() {
        return this.delivery;
    }

    public void setDelivery(final JsonObject delivery) {
        this.delivery = delivery;
    }

    @Override
    public String toString() {
        return "Arrange{" +
            "mode=" + this.mode +
            ", options=" + this.options +
            ", delivery=" + this.delivery +
            '}';
    }
}
