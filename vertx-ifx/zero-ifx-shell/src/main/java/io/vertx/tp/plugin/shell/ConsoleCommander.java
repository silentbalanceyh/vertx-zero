package io.vertx.tp.plugin.shell;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.tp.plugin.shell.atom.CommandAtom;
import io.vertx.tp.plugin.shell.atom.CommandInput;
import io.vertx.tp.plugin.shell.atom.Terminal;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ConsoleCommander extends AbstractCommander {

    @Override
    public Future<TermStatus> executeAsync(final CommandInput args) {
        /* Welcome first */
        Sl.welcomeSub(this.environment, this.atom);
        /* Async Result Captured */
        /* Term create */
        final Terminal terminal = Terminal.create(this.vertxRef);

        return this.run(terminal);
    }

    private Future<TermStatus> run(final Terminal terminal) {
        final Promise<TermStatus> promise = Promise.promise();

        final BiConsumer<Terminal, TermStatus> consumer = (terminalRef, status) -> {
            /* Environment input again */
            Sl.welcomeSub(this.environment, this.atom);
            /* Continue here */
            this.run(terminalRef);
            /* Promise complete */
            promise.complete(status);
        };
        terminal.run(handler -> {
            if (handler.succeeded()) {
                /* New Arguments */
                final String[] args = handler.result();

                /* Major code logical should returned Future<TermStatus> instead */
                final Future<TermStatus> future = this.runAsync(args);
                future.onComplete(callback -> {
                    if (callback.succeeded()) {
                        final TermStatus status = callback.result();
                        if (TermStatus.EXIT == status) {
                            /*
                             * EXIT -> Back To Major
                             */
                            promise.complete(TermStatus.SUCCESS);
                        } else {
                            /*
                             * SUCCESS, FAILURE
                             */
                            if (TermStatus.WAIT != status) {
                                consumer.accept(terminal, status);
                            }
                        }
                    } else {
                        consumer.accept(terminal, TermStatus.FAILURE);
                    }
                });
            } else {
                Sl.failEmpty();
                consumer.accept(terminal, TermStatus.FAILURE);
            }
        });
        return promise.future();
    }

    private Future<TermStatus> runAsync(final String[] args) {
        /* Critical CommandOption */
        final List<CommandAtom> commands = Sl.commands(this.atom.getCommands());

        /* Parse Arguments */
        return ConsoleTool.parseAsync(args, commands)

            /* Execute Command */
            .compose(commandLine -> ConsoleTool.runAsync(commandLine, commands,

                /* Binder Function */
                commander -> commander.bind(this.environment).bind(this.vertxRef)))
            .otherwise(Sl::failError);
    }
}
