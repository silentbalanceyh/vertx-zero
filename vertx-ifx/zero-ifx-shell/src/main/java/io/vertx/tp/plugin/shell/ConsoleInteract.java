package io.vertx.tp.plugin.shell;

import io.vertx.up.eon.em.Environment;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Scanner;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class ConsoleInteract {
    private static final Annal LOGGER = Annal.get(ConsoleInteract.class);

    static void run(final Environment environment, final String args) {
        /* Welcome first */
        ConsoleWelcome.welcome();

        /* Environment and Input */
        ConsoleWelcome.input(environment);
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
                    run(environment, interactArgs);
                } catch (final ParseException ex) {
                    LOGGER.info("Commander line could not be parsed: {0}", line);
                    ConsoleWelcome.input(environment);
                }
            } else {
                ConsoleWelcome.input(environment);
            }
        } while (true);
    }

    private static void run(final Environment environment, final String[] args) throws ParseException {
        /*
         * LineParser
         */
        System.out.println(Ut.fromJoin(args));
        final CommandLineParser parser = new DefaultParser();
        /*
         * Options
         */
        final Options options = new Options();
        parser.parse(options, args);
    }
}
