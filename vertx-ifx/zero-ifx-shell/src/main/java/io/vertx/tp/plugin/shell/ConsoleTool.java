package io.vertx.tp.plugin.shell;

import io.vertx.core.Future;
import io.vertx.tp.error.CommandMissingException;
import io.vertx.tp.error.CommandParseException;
import io.vertx.tp.error.CommandUnknownException;
import io.vertx.tp.plugin.shell.atom.CommandAtom;
import io.vertx.tp.plugin.shell.atom.CommandInput;
import io.vertx.tp.plugin.shell.cv.em.CommandType;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.vertx.up.exception.UpException;
import io.vertx.up.util.Ut;
import org.apache.commons.cli.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class ConsoleTool {

    static Future<CommandLine> parseAsync(final String[] args, final List<CommandAtom> definition) {
        /*
         * LineParser
         */
        final CommandLineParser parser = new DefaultParser();
        /*
         * Command Verifier
         */
        final CommandAtom atom = findCommand(args, definition);
        if (Objects.isNull(atom)) {
            /*
             * Unknown command of input.
             */
            final UpException error = new CommandUnknownException(ConsoleTool.class, args[0]);
            return Future.failedFuture(error);
        } else {
            try {
                /*
                 * Command valid
                 */
                final CommandLine parsed = parser.parse(atom.options(), args);
                return Future.succeededFuture(parsed);
            } catch (final ParseException ex) {
                final UpException error = new CommandParseException(ConsoleTool.class, Ut.fromJoin(args), ex);
                return Future.failedFuture(error);
            }
        }

    }

    static Future<TermStatus> runAsync(final CommandLine parsed, final List<CommandAtom> commands,
                                       final Function<Commander, Commander> binder) {
        /*
         * Found command inner run method
         */
        final CommandAtom command = commands.stream()
                .filter(each -> parsed.hasOption(each.getName()) || parsed.hasOption(each.getSimple()))
                .findAny().orElse(null);
        if (Objects.isNull(command) || !command.valid()) {
            /*
             * Internal error
             */
            if (Objects.nonNull(command)) {
                Sl.failWarn(" Plugin null -> name = {0},{1}, type = {2}",
                        command.getSimple(), command.getName(), command.getType());
            }
            throw new CommandMissingException(ConsoleInteract.class, Ut.fromJoin(parsed.getArgs()));
        } else {
            final Options options = new Options();
            commands.stream().map(CommandAtom::option).forEach(options::addOption);
            /*
             * Create CommandArgs
             */
            final List<String> inputArgs = parsed.getArgList();
            final List<String> inputNames = command.getOptionNames();
            final CommandInput commandInput = CommandInput.create(inputNames, inputArgs);
            commandInput.bind(options);
            /*
             * Commander
             */
            final Commander commander;
            if (CommandType.SYSTEM == command.getType()) {
                /*
                 * Sub-System call
                 */
                commander = Ut.instance(ConsoleCommander.class);
            } else {
                /*
                 * Plugin processing
                 * instance instead of single for shared usage
                 */
                commander = Ut.instance(command.getPlugin());
            }
            /*
             * binder processing
             */
            Sl.welcomeCommand(command);
            return binder.apply(commander.bind(command)).executeAsync(commandInput);
        }
    }

    private static CommandAtom findCommand(final String[] args, final List<CommandAtom> definition) {
        final String commandName = args[0];
        return definition.stream()
                .filter(each -> commandName.equals(each.getSimple()) || commandName.equals(each.getName()))
                .findAny().orElse(null);
    }
}
