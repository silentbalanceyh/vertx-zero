package io.vertx.tp.plugin.shell.commander;

import io.vertx.tp.plugin.shell.AbstractCommander;
import io.vertx.tp.plugin.shell.ConsoleMessage;
import io.vertx.tp.plugin.shell.atom.CommandArgs;
import io.vertx.tp.plugin.shell.refine.Sl;
import org.apache.commons.cli.HelpFormatter;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class HelpCommander extends AbstractCommander {
    @Override
    public boolean execute(final CommandArgs args) {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(240);
        formatter.printHelp(ConsoleMessage.usage(),
                Sl.message(ConsoleMessage.header()),
                args.options(),
                Sl.message(ConsoleMessage.footer()));
        return true;
    }
}
