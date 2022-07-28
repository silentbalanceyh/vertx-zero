package io.vertx.aeon.uca.alive;

import io.vertx.aeon.atom.iras.HRepo;
import io.vertx.core.Future;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class NovaGit extends AbstractNova {
    @Override
    public Future<Boolean> configure(final HRepo input) {
        return super.configure(input);
    }
}
