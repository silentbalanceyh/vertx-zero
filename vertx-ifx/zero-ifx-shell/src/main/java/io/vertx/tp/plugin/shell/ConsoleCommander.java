package io.vertx.tp.plugin.shell;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.tp.plugin.shell.atom.CommandArgs;
import io.vertx.tp.plugin.shell.atom.CommandOption;
import io.vertx.tp.plugin.shell.atom.Term;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class ConsoleCommander extends AbstractCommander {

    @Override
    public Future<TermStatus> executeAsync(final CommandArgs args) {
        /* Welcome first */
        Sl.welcomeSub(this.environment, this.option);
        /* Async Result Captured */
        /* Term create */
        final Term term = Term.create(this.vertxRef);

        return this.run(term);
    }

    private Future<TermStatus> run(final Term term) {
        final Promise<TermStatus> promise = Promise.promise();

        final BiConsumer<Term, TermStatus> consumer = (termRef, status) -> {
            /* Environment input again */
            Sl.welcomeSub(this.environment, this.option);
            /* Continue here */
            this.run(termRef);
            /* Promise complete */
            promise.complete(status);
        };
        term.run(handler -> {
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
                                consumer.accept(term, status);
                            }
                        }
                    } else {
                        consumer.accept(term, TermStatus.FAILURE);
                    }
                });
            } else {
                Sl.failEmpty();
                consumer.accept(term, TermStatus.FAILURE);
            }
        });
        return promise.future();
    }

    private Future<TermStatus> runAsync(final String[] args) {
        /* Critical CommandOption */
        final List<CommandOption> commands = Sl.commands(this.option.getCommands());

        /* Parse Arguments */
        return ConsoleTool.parseAsync(args, commands)

                /* Execute Command */
                .compose(commandLine -> ConsoleTool.runAsync(commandLine, commands,

                        /* Binder Function */
                        commander -> commander.bind(this.environment).bind(this.vertxRef)))
                .otherwise(Sl::failError);
    }
}
