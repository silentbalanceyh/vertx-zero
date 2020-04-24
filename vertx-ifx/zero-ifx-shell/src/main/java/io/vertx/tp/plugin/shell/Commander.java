package io.vertx.tp.plugin.shell;

import io.vertx.core.Future;
import io.vertx.up.eon.em.Environment;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public interface Commander {

    Commander bind(Environment environment);

    boolean execute(String[] args);

    Future<Boolean> executeAsync(String[] args);
}
