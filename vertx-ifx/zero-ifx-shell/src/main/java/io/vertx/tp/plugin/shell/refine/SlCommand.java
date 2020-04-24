package io.vertx.tp.plugin.shell.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.tp.plugin.shell.atom.CommandOption;
import io.vertx.tp.plugin.shell.commander.HelpCommander;
import io.vertx.tp.plugin.shell.commander.QuitCommander;
import io.vertx.tp.plugin.shell.commander.SystemCommander;
import io.vertx.tp.plugin.shell.cv.em.CommandType;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class SlCommand {
    private static final Annal LOGGER = Annal.get(SlCommand.class);
    private static final List<CommandOption> commands = new ArrayList<>();
    private static final ConcurrentMap<String, Class<?>> COMMAND_PLUGINS = new ConcurrentHashMap<String, Class<?>>() {
        {
            this.put("h", HelpCommander.class);
            this.put("q", QuitCommander.class);
        }
    };

    public static List<CommandOption> commands() {
        if (commands.isEmpty()) {
            /*
             * Default Commands
             */
            final JsonArray commandsDefault = SlConfig.commandsDefault();
            final List<CommandOption> commandsDefaultList = Ux.fromJson(commandsDefault, CommandOption.class);
            commandsDefaultList.forEach(command -> {
                command.setArgs(false);
                command.setType(CommandType.DEFAULT);
                command.setPlugin(COMMAND_PLUGINS.get(command.getSimple()));
            });
            commands.addAll(commandsDefaultList);
            /*
             * Mount default plugin
             */
            final JsonArray commandJson = SlConfig.commands();
            final List<CommandOption> commandsList = Ux.fromJson(commandJson, CommandOption.class);
            commandsList.stream().filter(item -> CommandType.SYSTEM == item.getType()).forEach(command -> {
                command.setArgs(false);
                command.setPlugin(SystemCommander.class);
            });
            commands.addAll(commandsList);
            /*
             * Plugin report for commands
             */
            commands.stream().filter(item -> Objects.isNull(item.getPlugin())).forEach(item -> {
                LOGGER.warn("The commands plugin is not configured: name = {0},{1}, type = {2}",
                        item.getSimple(), item.getName(), item.getType());
            });
        }
        return commands;
    }
}
