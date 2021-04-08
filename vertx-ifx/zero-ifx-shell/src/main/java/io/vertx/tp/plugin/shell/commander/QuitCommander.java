package io.vertx.tp.plugin.shell.commander;

import io.vertx.tp.plugin.shell.AbstractCommander;
import io.vertx.tp.plugin.shell.atom.CommandInput;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;

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
