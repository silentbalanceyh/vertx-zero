package io.vertx.mod.jet.atom;

import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.Objects;

/*
 * Configuration in lime for dynamic router
 * {
 *      "wall": "/api"
 *      "worker":{
 *          "instances": 64
 *      },
 *      "agent":{
 *          "instances": 32
 *      }
 * }
 */
public class JtConfig implements Serializable {
    private transient String wall;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject worker;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject agent;
    /* Options here */
    private transient JsonObject options;

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private transient Class<?> unity;

    public String getWall() {
        return this.wall;
    }

    public void setWall(final String wall) {
        this.wall = wall;
    }

    public JsonObject getWorker() {
        return this.worker;
    }

    public void setWorker(final JsonObject worker) {
        this.worker = worker;
    }

    public JsonObject getAgent() {
        return this.agent;
    }

    public void setAgent(final JsonObject agent) {
        this.agent = agent;
    }

    public JsonObject getOptions() {
        return this.options;
    }

    public void setOptions(final JsonObject options) {
        this.options = options;
    }

    public Class<?> getUnity() {
        return this.unity;
    }

    public void setUnity(final Class<?> unity) {
        this.unity = unity;
    }

    public DeploymentOptions getWorkerOptions() {
        final DeploymentOptions options = new DeploymentOptions(Objects.isNull(this.worker) ? new JsonObject() : this.worker);
        /*
         * Specific configuration
         */
        options.setWorker(true);
        options.setHa(true);
        /* BUG: workerPoolSize is not in fromJson */
        if (this.worker.containsKey("workerPoolSize")) {
            options.setWorkerPoolSize(this.worker.getInteger("workerPoolSize"));
        }
        return options;
    }

    @Override
    public String toString() {
        return "JtConfig{" +
            "wall='" + this.wall + '\'' +
            ", worker=" + this.worker +
            ", agent=" + this.agent +
            ", options=" + this.options +
            ", unity=" + this.unity +
            '}';
    }
}
