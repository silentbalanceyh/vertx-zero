package io.vertx.tp.plugin.shell;

import io.vertx.core.Future;
import io.vertx.tp.error.CommandParseException;
import io.vertx.tp.plugin.shell.atom.CommandOption;
import io.vertx.up.fn.Actuator;
import io.vertx.up.util.Ut;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class ConsoleInput {
    /*
     * Read input to uniform argument[];
     */
    static void dataIn(final Scanner scanner, final Actuator prompt,
                       final Function<String[], Future<Boolean>> actuator) {
        scanner.useDelimiter("\n");
        if (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            if (Ut.isNil(line)) {
                /*
                 * prompt
                 */
                ConsoleMessage.failInput();
                prompt.execute();
                dataIn(scanner, prompt, actuator);
            } else {
                /*
                 * Process major code logical
                 */
                final String[] normalized = dataLine(line);
                try {
                    actuator.apply(normalized).onComplete(handler -> {
                        if (handler.succeeded()) {
                            /*
                             * Successfully executed
                             */
                            final boolean result = handler.result();
                            if (result) {
                                prompt.execute();
                                dataIn(scanner, prompt, actuator);
                            }
                        } else {
                            /*
                             * Failure here
                             */
                            ConsoleMessage.failError(handler.cause());
                            prompt.execute();
                            dataIn(scanner, prompt, actuator);
                        }
                    });
                } catch (final Throwable error) {
                    /*
                     * Failure here
                     */
                    ConsoleMessage.failError(error);
                    prompt.execute();
                    dataIn(scanner, prompt, actuator);
                }
            }
        }
    }

    /*
     * Get commandLine for `options`
     */
    static CommandLine dataLine(final List<CommandOption> definition, final String[] args) {
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
            return parser.parse(options, args);
        } catch (final ParseException ex) {
            throw new CommandParseException(ConsoleInput.class, Ut.fromJoin(args), ex);
        }
    }

    private static String[] dataLine(final String line) {
        /*
         * Format Processing
         */
        final String[] interactArgs = line.split(" ");
        /*
         * First is command and could be as following:
         *
         * j -out=xxx .etc
         *
         * Instead of this situation, append - to command
         */
        final String[] normalized;
        if (0 < interactArgs.length) {
            interactArgs[0] = "-" + interactArgs[0];
            /*
             * Normalize
             */
            normalized = Arrays.stream(interactArgs)
                    .map(String::trim)
                    .toArray(String[]::new);
        } else {
            normalized = interactArgs;
        }
        return normalized;
    }

}
