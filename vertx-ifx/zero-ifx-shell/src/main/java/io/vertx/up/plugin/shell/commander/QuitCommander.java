package io.vertx.up.plugin.shell.commander;

import io.vertx.up.plugin.shell.AbstractCommander;
import io.vertx.up.plugin.shell.atom.CommandInput;
import io.vertx.up.plugin.shell.cv.em.TermStatus;
import io.vertx.up.plugin.shell.refine.Sl;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class QuitCommander extends AbstractCommander {
    @Override
    public TermStatus execute(final CommandInput args) {
        Sl.goodbye();
        return TermStatus.EXIT;
    }
}
