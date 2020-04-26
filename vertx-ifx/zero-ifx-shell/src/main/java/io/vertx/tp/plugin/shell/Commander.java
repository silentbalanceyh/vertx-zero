package io.vertx.tp.plugin.shell;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.tp.plugin.shell.atom.CommandInput;
import io.vertx.tp.plugin.shell.atom.CommandOption;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.up.eon.em.Environment;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public interface Commander {

    Commander bind(Environment environment);

    Commander bind(CommandOption options);

    Commander bind(Vertx vertx);

    TermStatus execute(CommandInput args);

    Future<TermStatus> executeAsync(CommandInput args);
}
