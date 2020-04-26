package io.vertx.tp.plugin.shell.atom;

import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.tp.plugin.shell.Commander;
import io.vertx.tp.plugin.shell.cv.em.CommandType;
import io.vertx.up.util.Ut;
import org.apache.commons.cli.Option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class CommandOption implements Serializable {

    private transient CommandType type = CommandType.COMMAND;   // Default Command Type

    private transient String simple;
    private transient String name;
    private transient String description;
    private transient boolean args;
    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private transient Class<?> plugin;

    private transient List<CommandOption> commands = new ArrayList<>();
    private transient List<CommandArgs> arguments = new ArrayList<>();

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

    public List<CommandArgs> getArguments() {
        return this.arguments;
    }

    public void setArguments(final List<CommandArgs> arguments) {
        this.arguments = arguments;
    }

    public List<String> getArgumentsList() {
        return this.arguments.stream().map(CommandArgs::getName).collect(Collectors.toList());
    }

    public List<CommandOption> getCommands() {
        return this.commands;
    }

    public void setCommands(final List<CommandOption> commands) {
        this.commands = commands;
    }

    public boolean isArgs() {
        return this.args;
    }

    public void setArgs(final boolean args) {
        this.args = args;
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
        } else return true;
    }

    public Option option() {
        final Option option = new Option(this.simple, this.name, this.args, this.description);
        /*
         * Get option here, arguments processing
         */
        this.arguments.forEach(argument -> option.setArgName(argument.getName()));
        return option;
    }
}
