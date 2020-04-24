package io.vertx.tp.plugin.shell.atom;

import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.plugin.shell.cv.em.CommandType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class CommandOption implements Serializable {

    private final transient CommandType type = CommandType.COMMAND;   // Default Command Type
    private transient String simple;
    private transient String name;
    private transient String description;

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private transient JsonArray arguments;

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private transient Class<?> plugin;

    private transient List<CommandType> commands = new ArrayList<>();

    public String getSimple() {
        return this.simple;
    }

    public void setSimple(final String simple) {
        this.simple = simple;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Class<?> getPlugin() {
        return this.plugin;
    }

    public void setPlugin(final Class<?> plugin) {
        this.plugin = plugin;
    }

    public CommandType getType() {
        return this.type;
    }

    public JsonArray getArguments() {
        return this.arguments;
    }

    public void setArguments(final JsonArray arguments) {
        this.arguments = arguments;
    }

    public List<CommandType> getCommands() {
        return this.commands;
    }

    public void setCommands(final List<CommandType> commands) {
        this.commands = commands;
    }
}
