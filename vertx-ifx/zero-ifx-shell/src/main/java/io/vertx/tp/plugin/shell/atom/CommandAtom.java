package io.vertx.tp.plugin.shell.atom;

import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.shell.Commander;
import io.vertx.tp.plugin.shell.cv.em.CommandType;
import io.vertx.up.util.Ut;
import org.apache.commons.cli.Options;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CommandAtom implements Serializable {

    private transient CommandType type = CommandType.COMMAND;   // Default Command Type

    private transient String simple;
    private transient String name;
    private transient String description;
    private transient boolean args;

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private transient Class<?> plugin;

    private transient List<CommandAtom> commands = new ArrayList<>();
    private transient List<CommandOption> options = new ArrayList<>();

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject config = new JsonObject();

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

    public void setType(final CommandType type) {
        this.type = type;
    }

    public List<CommandOption> getOptions() {
        return this.options;
    }

    public void setOptions(final List<CommandOption> options) {
        this.options = options;
    }

    public List<String> getOptionNames() {
        return this.options.stream().map(CommandOption::getName).toList();
    }

    public List<CommandAtom> getCommands() {
        return this.commands;
    }

    public void setCommands(final List<CommandAtom> commands) {
        this.commands = commands;
    }

    public boolean isArgs() {
        return this.args;
    }

    public void setArgs(final boolean args) {
        this.args = args;
    }

    public JsonObject getConfig() {
        return this.config;
    }

    public void setConfig(final JsonObject config) {
        this.config = config;
    }

    /*
     * Whether this command is valid
     */
    public boolean valid() {
        if (CommandType.COMMAND == this.type) {
            if (Objects.isNull(this.plugin)) {
                return false;
            } else {
                return Ut.isImplement(this.plugin, Commander.class);
            }
        } else {
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getDefault(final String simple) {
        final CommandOption option = this.options.stream()
            .filter(each -> simple.equals(each.getSimple()))
            .findAny().orElse(null);
        if (Objects.isNull(option)) {
            return null;
        } else {
            if (option.isRequired()) {
                /*
                 * required parameter must provide values
                 */
                return null;
            } else {
                return (T) option.getDefaultValue();
            }
        }
    }

    public Options options() {
        final Options options = new Options();
        /*
         * Parse options
         */
        this.options.stream().map(CommandOption::option).forEach(options::addOption);
        return options;
    }

    public CommandOption option(final String name) {
        return this.options.stream().filter(item -> name.equals(item.getSimple()))
            .findAny().orElse(null);
    }
}
