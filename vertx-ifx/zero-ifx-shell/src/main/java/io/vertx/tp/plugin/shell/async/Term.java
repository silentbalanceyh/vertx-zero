package io.vertx.tp.plugin.shell.async;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.tp.error.CommandMissingException;
import io.vertx.up.eon.Strings;
import io.vertx.up.exception.UpException;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
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
    private transient String input;

    private Term(final Vertx vertx) {
        this.vertx = vertx;
        this.scanner.useDelimiter("\n");
    }

    public static Term create(final Vertx vertx) {
        return new Term(vertx);
    }

    public void run(final Handler<AsyncResult<String[]>> handler) {
        /*
         * Std in to get arguments
         */
        if (this.scanner.hasNextLine()) {
            final String line = this.scanner.nextLine();
            if (Ut.isNil(line)) {
                System.out.println("Hello Ok");
            } else {
                System.out.println("Valid");
            }
        } else {
            System.out.println("Hello");
        }
    }

    private void runInternal(final Handler<AsyncResult<String[]>> handler) {

    }

    public void waitFor(final Handler<AsyncResult<String[]>> handler) {

    }
}
