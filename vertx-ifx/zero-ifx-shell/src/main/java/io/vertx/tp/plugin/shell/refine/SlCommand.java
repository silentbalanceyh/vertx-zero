package io.vertx.tp.plugin.shell.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.tp.plugin.shell.ConsoleCommander;
import io.vertx.tp.plugin.shell.atom.CommandAtom;
import io.vertx.tp.plugin.shell.commander.BackCommander;
import io.vertx.tp.plugin.shell.commander.HelpCommander;
import io.vertx.tp.plugin.shell.commander.QuitCommander;
import io.vertx.tp.plugin.shell.cv.em.CommandType;
import io.vertx.up.unity.Ux;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class SlCommand {
    private static final List<CommandAtom> commands = new ArrayList<>();
    private static final ConcurrentMap<String, Class<?>> COMMAND_PLUGINS = new ConcurrentHashMap<String, Class<?>>() {
        {
            this.put("h", HelpCommander.class);
            this.put("q", QuitCommander.class);
            this.put("b", BackCommander.class);
        }
    };

    public static List<CommandAtom> commands() {
        if (commands.isEmpty()) {
            /*
             * Default Commands
             */
            final List<CommandAtom> defaults = mountDefault("h", "q");
            commands.addAll(defaults);

            /*
             * Mount default plugin
             */
            final JsonArray commandJson = SlConfig.commands();
            final List<CommandAtom> commandsList = Ux.fromJson(commandJson, CommandAtom.class);
            commands.addAll(mountPlugin(commandsList));
        }
        return commands;
    }

    public static List<CommandAtom> commands(final List<CommandAtom> commands) {
        /*
         * Default Commands
         */
        final List<CommandAtom> normalized = Objects.isNull(commands) ? new ArrayList<>() : commands.stream()
                .filter(command -> !COMMAND_PLUGINS.containsKey(command.getSimple()))
                .collect(Collectors.toList());
        /*
         * Default Commands
         */
        final List<CommandAtom> defaults = mountDefault("h", "b");
        normalized.addAll(defaults);

        /*
         * Mount default plugin
         */
        mountPlugin(normalized);

        return normalized;
    }

    private static List<CommandAtom> mountDefault(final String... includes) {
        final JsonArray commandsDefault = SlConfig.commandsDefault();
        final List<CommandAtom> commandsDefaultList = Ux.fromJson(commandsDefault, CommandAtom.class);
        /* Set contains */
        final Set<String> includeSet = new HashSet<>(Arrays.asList(includes));
        return commandsDefaultList.stream().filter(command -> includeSet.contains(command.getSimple()))
                .peek(command -> {
                    command.setArgs(false);
                    command.setType(CommandType.DEFAULT);
                    command.setPlugin(COMMAND_PLUGINS.get(command.getSimple()));
                }).collect(Collectors.toList());
    }

    private static List<CommandAtom> mountPlugin(final List<CommandAtom> commands) {
        commands.stream().filter(item -> CommandType.SYSTEM == item.getType()).forEach(command -> {
            command.setArgs(false);
            command.setPlugin(ConsoleCommander.class);
        });
        return commands;
    }
}
