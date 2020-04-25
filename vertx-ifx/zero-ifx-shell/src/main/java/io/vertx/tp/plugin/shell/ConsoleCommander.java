package io.vertx.tp.plugin.shell;

import io.vertx.core.Future;
import io.vertx.tp.plugin.shell.atom.CommandArgs;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class ConsoleCommander extends AbstractCommander {

    @Override
    public Future<TermStatus> executeAsync(final CommandArgs args) {
        /* Welcome first */
        Sl.welcomeSub(this.option);
        return Future.succeededFuture();
    }
}
