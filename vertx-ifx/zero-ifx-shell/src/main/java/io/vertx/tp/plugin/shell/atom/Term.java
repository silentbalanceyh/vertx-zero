package io.vertx.tp.plugin.shell.atom;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.tp.error.CommandMissingException;
import io.vertx.up.eon.Strings;
import io.vertx.up.exception.UpException;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class Term {

    private static final transient UpException ERROR_ARG_MISSING =
            new CommandMissingException(Term.class, Strings.EMPTY);

    private final transient Scanner scanner = new Scanner(System.in);
    private final transient Vertx vertx;
    private final transient List<String> inputHistory = new ArrayList<>();

    private Term(final Vertx vertx) {
        this.vertx = vertx;
        this.scanner.useDelimiter("\n");
    }

    public static Term create(final Vertx vertx) {
        return new Term(vertx);
    }

    /*
     * Run this handler once when input is Ok,
     * Callback mode
     */
    public void run(final Handler<AsyncResult<String[]>> handler) {
        /*
         * Std in to get arguments
         */
        if (this.scanner.hasNextLine()) {
            final String line = this.scanner.nextLine();
            if (Ut.isNil(line)) {
                /*
                 * No input such as enter press keyboard directly
                 */
                handler.handle(Future.failedFuture(ERROR_ARG_MISSING));
            } else {
                /*
                 * Success for result
                 */
                this.inputHistory.add(line);
                final String[] normalized = this.arguments(line);
                handler.handle(Future.succeededFuture(normalized));
            }
        } else {
            /*
             * Very small possible to go to this flow here
             * Throw exception for end user
             */
            handler.handle(Future.failedFuture(ERROR_ARG_MISSING));
        }
    }

    private String[] arguments(final String line) {
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
