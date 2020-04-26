package io.vertx.tp.plugin.shell.commander;

import io.vertx.tp.plugin.shell.AbstractCommander;
import io.vertx.tp.plugin.shell.atom.CommandInput;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;
import org.apache.commons.cli.HelpFormatter;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class HelpCommander extends AbstractCommander {
    public String header() {
        /* Default supplier for "header" */
        return Sl.message("header", () -> "Zero Framework Console/Shell!");
    }

    public String footer() {
        /* Default supplier for "header" */
        return Sl.message("footer", () -> "CopyRight: http://www.vertxup.cn");
    }

    public String usage() {
        /* Default supplier for "usage" */
        return Sl.message("usage", () -> "Syntax for different type:" +
                "\t SYSTEM: <command> Go to sub-system of console." +
                "\t COMMAND: <command> [options] Execute actual command" +
                "\t Options: [ -name1 value1 -name2 value2 ]");
    }

    @Override
    public TermStatus execute(final CommandInput args) {
        System.out.println(args.get());
        /*
         * Print Help
         */
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(240);


        // formatter.printHelp(this.option.getName() + ": " + this.usage(), args.options());
        return TermStatus.SUCCESS;
    }
}
