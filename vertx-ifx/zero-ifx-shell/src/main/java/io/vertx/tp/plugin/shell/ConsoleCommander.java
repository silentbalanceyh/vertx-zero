package io.vertx.tp.plugin.shell;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.tp.plugin.shell.atom.CommandArgs;
import io.vertx.tp.plugin.shell.atom.Term;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.vertx.up.unity.Ux;

import java.util.function.Consumer;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class ConsoleCommander extends AbstractCommander {

    @Override
    public Future<TermStatus> executeAsync(final CommandArgs args) {
        /* Welcome first */
        Sl.welcomeSub(this.option);
        /* Async Result Captured */
        return Ux.handler(this::run);
    }

    private void run(final Handler<AsyncResult<TermStatus>> callback) {
        /* Term create */
        final Term term = Term.create(this.vertxRef);
        /* Process */
        this.run(term);
    }

    private void run(final Term term) {
        final Consumer<Term> consumer = termRef -> {
            /* Environment input again */
            Sl.welcomeSub(this.option);
            /* Continue here */
            this.run(termRef);
        };
        term.run(handler -> {
            if (handler.succeeded()) {
                System.out.println("Hello" + handler.result());
            } else {
                Sl.failEmpty();
                consumer.accept(term);
            }
        });
    }
}
