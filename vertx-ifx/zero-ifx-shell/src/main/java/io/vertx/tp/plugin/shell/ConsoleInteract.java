package io.vertx.tp.plugin.shell;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.tp.error.CommandMissingException;
import io.vertx.tp.error.CommandParseException;
import io.vertx.tp.plugin.shell.atom.CommandArgs;
import io.vertx.tp.plugin.shell.atom.CommandOption;
import io.vertx.tp.plugin.shell.atom.Term;
import io.vertx.tp.plugin.shell.cv.em.CommandType;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.vertx.up.eon.em.Environment;
import io.vertx.up.exception.UpException;
import io.vertx.up.util.Ut;
import org.apache.commons.cli.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

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
    private Future<TermStatus> run(final CommandLine parsed, final List<CommandOption> commands) {
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
                return commander.bind(command).bind(this.environment).bind(this.vertx)
                        .executeAsync(commandArgs);
            } else {
                /*
                 * Plugin processing
                 * instance instead of single for shared usage
                 */
                final Commander commander = Ut.instance(command.getPlugin());
                return commander.bind(command).bind(this.environment).bind(this.vertx)
                        .executeAsync(commandArgs);
            }
        }
    }

    void run(final String... args) {
        /* Welcome first */
        Sl.welcome();

        /* Environment and Input */
        Sl.welcomeCommand(this.environment);

        /* Create once */
        final Term term = Term.create(this.vertx);

        /* Must be wrapper here */
        this.run(term);
    }

    void run(final Term term) {
        final Consumer<Term> consumer = termRef -> {
            /* Environment input again */
            Sl.welcomeCommand(this.environment);
            /* Continue here */
            this.run(termRef);
        };
        term.run(handler -> {
            if (handler.succeeded()) {
                /* Process result of input */
                final String[] args = handler.result();

                /* Major code logical should returned Future<TermStatus> instead */
                final Future<TermStatus> future = this.runAsync(args);

                future.onComplete(callback -> {
                    if (callback.succeeded()) {
                        final TermStatus status = callback.result();
                        if (TermStatus.EXIT == status) {
                            /*
                             * EXIT -> Application End
                             */
                            System.exit(0);
                        } else {
                            /*
                             * SUCCESS, FAILURE
                             */
                            if (TermStatus.WAIT != status) {
                                consumer.accept(term);
                            }
                        }
                    } else {
                        consumer.accept(term);
                    }
                });
            } else {
                /* Error Input */
                Sl.failEmpty();
                consumer.accept(term);
            }
        });
    }

    private Future<TermStatus> runAsync(final String[] args) {
        /* Critical CommandOption */
        final List<CommandOption> commands = Sl.commands();
        return this.runAsync(args, commands)
                .compose(commandLine -> this.run(commandLine, commands))
                .otherwise(Sl::failError);
    }

    private Future<CommandLine> runAsync(final String[] args, final List<CommandOption> definition) {
        /*
         * LineParser
         */
        final CommandLineParser parser = new DefaultParser();
        /*
         * Options
         */
        final Options options = new Options();
        definition.stream().map(CommandOption::option).forEach(options::addOption);
        try {
            final CommandLine parsed = parser.parse(options, args);
            return Future.succeededFuture(parsed);
        } catch (final ParseException ex) {
            final UpException error = new CommandParseException(this.getClass(), Ut.fromJoin(args), ex);
            return Future.failedFuture(error);
        }
    }
}
