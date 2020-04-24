package io.vertx.tp.plugin.shell;

import io.vertx.tp.plugin.shell.atom.CommandArgs;
import io.vertx.tp.plugin.shell.atom.CommandOption;
import io.vertx.tp.plugin.shell.cv.em.CommandType;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.vertx.up.eon.em.Environment;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class ConsoleInteract {
    private static final Annal LOGGER = Annal.get(ConsoleInteract.class);

    static void run(final Environment environment, final String args) {
        /* Welcome first */
        ConsoleMessage.welcome();

        /* Environment and Input */
        ConsoleMessage.input(environment);
        final Scanner scanner = new Scanner(System.in);
        String line = "";
        do {
            if (scanner.hasNext()) {
                /*
                 * Default
                 */
                line = scanner.nextLine();
            }
            /*
             * Format Processing
             */
            final String[] interactArgs = line.split(" ");
            if (0 < interactArgs.length) {
                /*
                 * Process sub-system
                 */
                try {
                    /*
                     * First is command and could be as following:
                     *
                     * j -out=xxx .etc
                     *
                     * Instead of this situation, append - to command
                     */
                    interactArgs[0] = "-" + interactArgs[0];

                    final String[] normalized = Arrays.stream(interactArgs)
                            .map(String::trim)
                            .toArray(String[]::new);

                    run(environment, normalized);
                } catch (final ParseException ex) {
                    Sl.output("Commander line could not be parsed:\"{0}\"", line);
                    ConsoleMessage.input(environment);
                }
            } else {
                ConsoleMessage.input(environment);
            }
        } while (true);
    }

    private static void run(final Environment environment, final String[] args) throws ParseException {
        /*
         * LineParser
         */
        final CommandLineParser parser = new DefaultParser();
        /*
         * Options
         */
        final Options options = new Options();
        final List<CommandOption> commands = Sl.commands();
        commands.stream().map(CommandOption::option).forEach(options::addOption);
        /*
         * Parse Line
         */
        final CommandLine parsed = parser.parse(options, args);
        commands.stream()
                .filter(each -> parsed.hasOption(each.getName()) || parsed.hasOption(each.getSimple()))
                .filter(CommandOption::valid)
                .forEach(each -> {
                    /*
                     * Command
                     */
                    if (CommandType.SYSTEM == each.getType()) {
                        /*
                         * run sub-system
                         */
                    } else {
                        /*
                         * Create CommandArgs
                         */
                        final List<String> inputArgs = parsed.getArgList();
                        final List<String> inputNames = each.getArgumentsList();
                        final CommandArgs commandArgs = CommandArgs.create(inputNames, inputArgs);
                        commandArgs.bind(options);
                        /*
                         * Plugin Processing
                         */
                        final Commander commander = Ut.singleton(each.getPlugin());
                        commander.bind(environment).bind(each)
                                /*
                                 * Command Args processing
                                 */
                                .executeAsync(commandArgs).compose(processed -> {
                            /*
                             * Execute next and wait for
                             */
                            ConsoleMessage.input(environment);
                            return Ux.future(Boolean.TRUE);
                        });
                    }
                });
    }


    private static void run(final Environment environment, final CommandOption option) {
        final String name = option.getName();
    }
}
