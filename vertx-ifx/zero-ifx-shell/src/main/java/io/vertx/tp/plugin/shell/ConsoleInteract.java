package io.vertx.tp.plugin.shell;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
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
class ConsoleInteract {

    private transient final Scanner scanner = new Scanner(System.in);

    private transient final Environment environment;
    private transient final Vertx vertx;

    private ConsoleInteract(final Vertx vertx, final Environment environment) {
        this.environment = environment;
        this.vertx = vertx;
        this.scanner.useDelimiter("\n");
    }

    public static ConsoleInteract start(final Vertx vertx, final Environment environment) {
        return new ConsoleInteract(vertx, environment);
    }

    /*
     * Process for commands
     */
    Future<Boolean> run(final CommandLine parsed, final List<CommandOption> commands) {
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
            final Options options = new Options();
            commands.stream().map(CommandOption::option).forEach(options::addOption);
            /*
             * Create CommandArgs
             */
            final List<String> inputArgs = parsed.getArgList();
            final List<String> inputNames = command.getArgumentsList();
            final CommandArgs commandArgs = CommandArgs.create(inputNames, inputArgs);
            commandArgs.bind(options);

            if (CommandType.SYSTEM == command.getType()) {
                /*
                 * Sub-System calling
                 */
                final ConsoleCommander commander = Ut.singleton(ConsoleCommander.class);
                return commander.bind(command).bind(this.environment).executeAsync(commandArgs);
            } else {
                /*
                 * Plugin processing
                 */
                final Commander commander = Ut.singleton(command.getPlugin());
                return commander.bind(command).bind(this.environment).executeAsync(commandArgs);
            }
        }
    }

    void run(final String... args) {
        /* Welcome first */
        ConsoleMessage.welcome();

        /* Environment and Input */
        ConsoleMessage.input(this.environment);

        /* Data in */
        ConsoleInput.dataIn(this.scanner, () -> ConsoleMessage.input(this.environment), (normalized) -> {

            /* Main Processing */
            final List<CommandOption> commands = Sl.commands();

            /* Parsing Line for processing */
            final CommandLine parsed = ConsoleInput.dataLine(commands, normalized);

            /* Build Commander based on `CommandLine` and `List<CommonOption>` */
            return this.run(parsed, commands);
        });
    }
}
