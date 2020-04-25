package io.vertx.tp.plugin.shell;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.tp.error.CommandMissingException;
import io.vertx.tp.plugin.shell.async.Term;
import io.vertx.tp.plugin.shell.atom.CommandArgs;
import io.vertx.tp.plugin.shell.atom.CommandOption;
import io.vertx.tp.plugin.shell.cv.em.CommandType;
import io.vertx.up.eon.em.Environment;
import io.vertx.up.util.Ut;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class ConsoleInteract {

    private transient final Environment environment;
    private transient final Vertx vertx;

    private ConsoleInteract(final Vertx vertx, final Environment environment) {
        this.environment = environment;
        this.vertx = vertx;
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

        final Term term = Term.create(this.vertx);
        term.run(handler -> {
            if (handler.succeeded()) {

            } else {
                /* Environment input again */
                ConsoleMessage.input(this.environment);
            }
        });
        /*
        ConsoleInput.dataIn(this.scanner, () -> ConsoleMessage.input(this.environment), (normalized) -> {

            final List<CommandOption> commands = Sl.commands();

            final CommandLine parsed = ConsoleInput.dataLine(commands, normalized);

            return this.run(parsed, commands);
        });*/
    }
}
