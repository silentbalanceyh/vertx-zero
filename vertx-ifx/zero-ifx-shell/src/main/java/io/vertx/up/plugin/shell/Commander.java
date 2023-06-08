package io.vertx.up.plugin.shell;

import io.horizon.eon.em.Environment;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.plugin.shell.atom.CommandAtom;
import io.vertx.up.plugin.shell.atom.CommandInput;
import io.vertx.up.plugin.shell.cv.em.TermStatus;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Commander {

    Commander bind(Environment environment);

    Commander bind(CommandAtom options);

    Commander bind(Vertx vertx);

    TermStatus execute(CommandInput args);

    Future<TermStatus> executeAsync(CommandInput args);
}
