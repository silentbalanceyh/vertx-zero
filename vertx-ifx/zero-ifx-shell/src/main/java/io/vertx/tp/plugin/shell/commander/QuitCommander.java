package io.vertx.tp.plugin.shell.commander;

import io.vertx.tp.plugin.shell.AbstractCommander;
import io.vertx.tp.plugin.shell.ConsoleMessage;
import io.vertx.tp.plugin.shell.atom.CommandArgs;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class QuitCommander extends AbstractCommander {
    @Override
    public boolean execute(final CommandArgs args) {
        ConsoleMessage.quit();
        System.exit(0);
        return false;
    }
}
