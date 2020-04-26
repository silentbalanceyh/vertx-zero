package io.vertx.tp.plugin.shell;

import io.vertx.core.Future;
import io.vertx.tp.error.CommandParseException;
import io.vertx.tp.error.CommandUnknownException;
import io.vertx.tp.plugin.shell.atom.CommandAtom;
import io.vertx.tp.plugin.shell.atom.CommandInput;
import io.vertx.tp.plugin.shell.cv.em.CommandType;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.UpException;
import io.vertx.up.util.Ut;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
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
         * The command must existing in your configuration files
         */
        final String commandName = args[0];
        return findAsync(commandName, definition).compose(command -> {
            try {
                /*
                 * Command valid
                 */
                final CommandLine parsed = parser.parse(command.options(), args);
                return Future.succeededFuture(parsed);
            } catch (final ParseException ex) {
                final UpException error = new CommandParseException(ConsoleTool.class, Ut.fromJoin(args), ex);
                return Future.failedFuture(error);
            }
        });
    }

    static Future<TermStatus> runAsync(final CommandLine parsed, final List<CommandAtom> commands,
                                       final Function<Commander, Commander> binder) {
        /*
         * Found command inner run method, double check for CommandAtom
         */
        final List<String> args = parsed.getArgList();
        return findAsync(args.get(Values.IDX), commands).compose(command -> {
            /*
             * Create CommandArgs
             */
            final CommandInput input = getInput(parsed).bind(command);
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
            return binder.apply(commander.bind(command)).executeAsync(input);
        });
    }

    private static CommandInput getInput(final CommandLine parsed) {
        final List<String> names = new ArrayList<>();
        final List<String> values = new ArrayList<>();
        Arrays.stream(parsed.getOptions()).forEach(option -> {
            final String name = option.getLongOpt();
            final String value = parsed.getOptionValue(name);
            names.add(name);
            values.add(value);
        });
        return CommandInput.create(names, values);
    }

    private static Future<CommandAtom> findAsync(final String commandName, final List<CommandAtom> commands) {
        final CommandAtom atom = commands.stream()
                /*
                 * Filter by commandName here
                 */
                .filter(each -> commandName.equals(each.getSimple()) || commandName.equals(each.getName()))
                .findAny().orElse(null);
        if (Objects.isNull(atom)) {
            /*
             * Unknown command of input throw out exception
             */
            return Future.failedFuture(new CommandUnknownException(ConsoleTool.class, commandName));
        } else {
            /*
             * Returned Command Atom
             */
            return Future.succeededFuture(atom);
        }
    }
}
