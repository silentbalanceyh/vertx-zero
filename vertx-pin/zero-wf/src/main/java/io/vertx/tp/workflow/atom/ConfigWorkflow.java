package io.vertx.tp.workflow.atom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Database;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ConfigWorkflow {
    private transient String name;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject database;

    @JsonIgnore
    private transient Database camundaDatabase;

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

    public Database camundaDatabase() {
        if (Ut.notNil(this.database) && Objects.isNull(this.camundaDatabase)) {
            this.camundaDatabase = new Database();
            this.camundaDatabase.fromJson(this.database);
        }
        return this.camundaDatabase;
    }
}
