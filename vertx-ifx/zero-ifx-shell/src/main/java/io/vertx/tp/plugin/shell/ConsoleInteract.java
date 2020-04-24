package io.vertx.tp.plugin.shell;

import io.vertx.core.Future;
import io.vertx.tp.error.CommandMissingException;
import io.vertx.tp.plugin.shell.atom.CommandArgs;
import io.vertx.tp.plugin.shell.atom.CommandOption;
import io.vertx.tp.plugin.shell.cv.em.CommandType;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.vertx.up.eon.em.Environment;
import io.vertx.up.util.Ut;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class ConsoleInteract {

    private transient final Scanner scanner = new Scanner(System.in);

    private transient final Environment environment;

    private ConsoleInteract(final Environment environment) {
        this.environment = environment;
    }

    public static ConsoleInteract start(final Environment environment) {
        return new ConsoleInteract(environment);
    }

    /*
     * Process for commands
     */
    private Future<Boolean> run(final CommandLine parsed, final List<CommandOption> commands) {
        /*
         * Found command inner run method
         */
        final CommandOption command = commands.stream()
                .filter(each -> parsed.hasOption(each.getName()) || parsed.hasOption(each.getSimple()))
                .filter(CommandOption::valid)
                .findAny().orElse(null);
        if (Objects.isNull(command)) {
            /*
             * Internal error
             */
            throw new CommandMissingException(ConsoleInteract.class, Ut.fromJoin(parsed.getArgList()));
        } else {
            if (CommandType.SYSTEM == command.getType()) {
                return null;
            } else {
                final Options options = new Options();
                commands.stream().map(CommandOption::option).forEach(options::addOption);
                /*
                 * Create CommandArgs
                 */
                final List<String> inputArgs = parsed.getArgList();
                final List<String> inputNames = command.getArgumentsList();
                final CommandArgs commandArgs = CommandArgs.create(inputNames, inputArgs);
                commandArgs.bind(options);
                /*
                 * Plugin processing
                 */
                final Commander commander = Ut.singleton(command.getPlugin());
                return commander.bind(command).bind(this.environment).executeAsync(commandArgs);
            }
        }
    }

    public void run(final String... args) {
        /* Welcome first */
        ConsoleMessage.welcome();

        /* Environment and Input */
        ConsoleMessage.input(this.environment);

        /* Pass reference of Scanner ( System.in ) */
        final Scanner scanner = new Scanner(System.in);

        /* Data in */
        ConsoleInput.dataIn(scanner, () -> ConsoleMessage.input(this.environment), (normalized) -> {

            /* Main Processing */
            final List<CommandOption> commands = Sl.commands();

            /* Parsing Line for processing */
            final CommandLine parsed = ConsoleInput.dataLine(commands, normalized);

            /* Build Commander based on `CommandLine` and `List<CommonOption>` */
            return this.run(parsed, commands);
        });
    }
}
