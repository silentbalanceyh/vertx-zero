package io.vertx.tp.plugin.shell;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.tp.plugin.shell.atom.CommandArgs;
import io.vertx.up.util.Ut;

import java.util.Scanner;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class ConsoleCommander extends AbstractCommander {

    @Override
    public Future<Boolean> executeAsync(final CommandArgs args) {
        final Promise<Boolean> future = Promise.promise();
        /*
         * Sub-System Welcome here
         */
        this.prompt();
        final Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (Ut.isNil(line)) {
                /*
                 * prompt
                 */
                ConsoleMessage.failInput();
                this.prompt();
                line = scanner.nextLine();
                System.out.println(line);
            }
        }
        return future.future();
    }

    private void prompt() {
        ConsoleMessage.input(this.option);
    }
}
