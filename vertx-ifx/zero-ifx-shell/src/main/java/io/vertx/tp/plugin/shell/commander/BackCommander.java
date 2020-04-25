package io.vertx.tp.plugin.shell.commander;

import io.vertx.tp.plugin.shell.AbstractCommander;
import io.vertx.tp.plugin.shell.atom.CommandArgs;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class BackCommander extends AbstractCommander {
    @Override
    public TermStatus execute(final CommandArgs args) {

        return TermStatus.WAIT;
    }
}
