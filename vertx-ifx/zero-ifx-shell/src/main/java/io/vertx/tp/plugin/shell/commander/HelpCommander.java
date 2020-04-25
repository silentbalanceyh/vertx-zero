package io.vertx.tp.plugin.shell.commander;

import io.vertx.tp.plugin.shell.AbstractCommander;
import io.vertx.tp.plugin.shell.atom.CommandArgs;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;
import org.apache.commons.cli.HelpFormatter;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class HelpCommander extends AbstractCommander {
    public String header() {
        return Sl.message("header",
                /* Default supplier for "header" */
                () -> "Zero Framework Console/Shell!");
    }

    public String footer() {
        return Sl.message("footer",
                /* Default supplier for "header" */
                () -> "CopyRight: http://www.vertxup.cn");
    }

    public String usage() {
        return Sl.message("usage",
                /* Default supplier for "usage" */
                () -> "Syntax for different type:" +
                        "\t SYSTEM: <command> Go to sub-system of console." +
                        "\t COMMAND: <command> [options] Execute actual command" +
                        "\t Options: [ -name1 value1 -name2 value2 ]");
    }

    @Override
    public TermStatus execute(final CommandArgs args) {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(240);
        formatter.printHelp(this.usage(), this.header(),
                args.options(),
                this.footer()
        );
        return TermStatus.SUCCESS;
    }
}
