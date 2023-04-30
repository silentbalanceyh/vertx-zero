package io.vertx.tp.plugin.shell.atom;

import io.horizon.uca.cache.Cc;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.tp.error.CommandMissingException;
import io.horizon.exception.BootingException;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Terminal {

    private static final BootingException ERROR_ARG_MISSING =
        new CommandMissingException(Terminal.class);
    private static final Cc<Integer, Scanner> CC_SCANNER = Cc.open();

    private final transient Scanner scanner;
    private final transient Vertx vertx;
    private final transient List<String> inputHistory = new ArrayList<>();

    private Terminal(final Vertx vertx) {
        this.vertx = vertx;
        this.scanner = CC_SCANNER.pick(() -> new Scanner(System.in), vertx.hashCode());
        // Fn.po?l(POOL_SCANNER, vertx.hashCode(), () -> new Scanner(System.in));
        this.scanner.useDelimiter("\n");
    }

    public static Terminal create(final Vertx vertx) {
        return new Terminal(vertx);
    }

    /*
     * Run this handler once when input is Ok,
     * Callback mode
     */
    public void run(final Handler<AsyncResult<String[]>> handler) {
        /*
         * Std in to get arguments
         * Fix bug: java.lang.IndexOutOfBoundsException: end
         */
        try {
            if (this.scanner.hasNextLine()) {
                this.runLine(handler);
            } else {
                /*
                 * Very small possible to go to this flow here
                 * Throw exception for end user
                 * handler.handle(Future.failedFuture(ERROR_ARG_MISSING));
                 * When click terminal operation here
                 */
                final ConcurrentMap<Integer, Scanner> cdScanner = CC_SCANNER.store();
                cdScanner.values().forEach(scanner -> Fn.jvmAt(scanner::close));
                System.exit(0);
                // handler.handle(Future.failedFuture(ERROR_ARG_MISSING));
            }
        } catch (final IndexOutOfBoundsException ex) {
            this.runLine(handler);
        }
    }

    private void runLine(final Handler<AsyncResult<String[]>> handler) {
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
            /*
             * Standard Options, remove `-` here
             */
            // interactArgs[0] = "-" + interactArgs[0];
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
