package io.vertx.tp.plugin.shell.atom;

import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonObject;
import org.apache.commons.cli.Option;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CommandOption implements Serializable {
    private transient String name;
    private transient String simple;
    private transient String description;
    private transient Object defaultValue;
    private transient boolean required = false;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject config = new JsonObject();

    public String getSimple() {
        return this.simple;
    }

    public void setSimple(final String simple) {
        this.simple = simple;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(final boolean required) {
        this.required = required;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Object getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(final Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public JsonObject getConfig() {
        return this.config;
    }

    public void setConfig(final JsonObject config) {
        this.config = config;
    }

    public Option option() {
        final Option option =
                new Option(this.simple, this.name, true, this.description);
        option.setRequired(this.required);
        option.setArgName(this.name);
        return option;
    }
}
