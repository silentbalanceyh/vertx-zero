package io.vertx.mod.workflow.atom.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.horizon.eon.em.EmDS;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Database;
import io.vertx.up.runtime.env.MatureOn;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;

/**
 * The workflow config instance for deployment
 * the data came from `yml` file instead of `json`
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MetaWorkflow {
    private transient String name;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject database;

    @JsonIgnore
    private transient Database camundaDatabase;

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private transient JsonArray builtIn = new JsonArray();

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private transient JsonArray resource = new JsonArray();

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public JsonObject getDatabase() {
        return this.database;
    }

    public void setDatabase(final JsonObject database) {
        this.database = database;
    }

    public JsonArray getBuiltIn() {
        return this.builtIn;
    }

    public void setBuiltIn(final JsonArray builtIn) {
        this.builtIn = builtIn;
    }

    public JsonArray getResource() {
        return this.resource;
    }

    public void setResource(final JsonArray resource) {
        this.resource = resource;
    }

    public Set<String> camundaBuiltIn() {
        return Ut.toSet(this.builtIn);
    }

    public Set<String> camundaResource() {
        return Ut.toSet(this.resource);
    }

    public Database camundaDatabase() {
        if (Ut.isNotNil(this.database) && Objects.isNull(this.camundaDatabase)) {
            // Database Environment Connected
            final JsonObject databaseJ = MatureOn.envDatabase(this.database, EmDS.Stored.WORKFLOW);
            this.camundaDatabase = Database.configure(databaseJ);
        }
        return this.camundaDatabase;
    }

    @Override
    public String toString() {
        return "MetaWorkflow{" +
            "name='" + this.name + '\'' +
            ", database=" + this.database +
            ", camundaDatabase=" + this.camundaDatabase +
            ", builtIn=" + this.builtIn +
            ", resource=" + this.resource +
            '}';
    }
}
