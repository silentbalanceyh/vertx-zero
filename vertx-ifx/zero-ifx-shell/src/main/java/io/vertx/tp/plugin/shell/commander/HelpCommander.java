package io.vertx.tp.plugin.shell.commander;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.shell.AbstractCommander;
import io.vertx.tp.plugin.shell.atom.CommandAtom;
import io.vertx.tp.plugin.shell.atom.CommandInput;
import io.vertx.tp.plugin.shell.atom.CommandOption;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.vertx.up.log.Log;
import org.apache.commons.cli.HelpFormatter;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * # 「Co」Command of Help
 *
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class HelpCommander extends AbstractCommander {

    private static final String ARG_COMMAND = "c";

    @Override
    public TermStatus execute(final CommandInput args) {
        final List<CommandAtom> atomList = this.getAtomList(args.atom());
        final ConcurrentMap<String, String> inputMap = args.get();
        if (inputMap.containsKey(ARG_COMMAND)) {
            final String commandValue = inputMap.get(ARG_COMMAND);
            final CommandAtom found = this.findAtom(atomList, commandValue);
            if (Objects.isNull(found)) {
                /*
                 * Command invalid
                 */
                Sl.failInvalid(commandValue);
                return TermStatus.FAILURE;
            } else {

                /*
                 * Valid Help
                 */
                this.printCommand(found);
                return TermStatus.SUCCESS;
            }
        } else {
            /*
             * No `command` provide
             */
            this.printCommands(atomList);
            return TermStatus.SUCCESS;
        }
    }

    private List<CommandAtom> getAtomList(final ConcurrentMap<String, CommandAtom> atomMap) {
        final Set<String> treeSet = new TreeSet<>(atomMap.keySet());
        final List<CommandAtom> atoms = new ArrayList<>();
        treeSet.stream().map(atomMap::get).forEach(atoms::add);
        return atoms;
    }

    private CommandAtom findAtom(final List<CommandAtom> atomList, final String command) {
        return atomList.stream()
                .filter(atom -> command.equals(atom.getName()) || command.equals(atom.getSimple()))
                .findAny().orElse(null);
    }

    private void printCommands(final List<CommandAtom> atoms) {
        final CommandOption option = this.atom.option(ARG_COMMAND);
        final JsonObject config = option.getConfig();
        final String name = config.containsKey("name") ? config.getString("name") : "Command Name";
        final String simple = config.containsKey("simple") ? config.getString("simple") : "Command Simple";
        final String description = config.containsKey("description") ? config.getString("description") : "Description";

        /* Format Table */
        final StringBuilder content = new StringBuilder();
        content.append(Sl.message("help", () -> "Command List: ")).append("\n");
        content.append("------------------------------------------------------\n");
        content.append(String.format("%-22s", Log.color(name, Log.COLOR_YELLOW, true)));
        content.append(String.format("%-26s", Log.color(simple, Log.COLOR_YELLOW, true)));
        content.append(String.format("%-16s", Log.color(description, Log.COLOR_YELLOW, true))).append("\n");
        content.append("------------------------------------------------------\n");

        /* Defined Map */
        atoms.forEach(atom -> {
            content.append(String.format("%-16s", atom.getName()));
            content.append(String.format("%-16s", atom.getSimple()));
            content.append(String.format("%-20s", atom.getDescription())).append("\n");
        });
        content.append("------------------------------------------------------");
        System.out.println(content.toString());
    }

    private void printCommand(final CommandAtom atom) {
        /* header */
        final String header = Sl.message("header", () -> "Zero Framework Console/Shell!");
        /* command */
        String usage = Sl.message("usage", () -> "Basic Syntax: <command> [options...]" +
                "\tCommand Name: {0}, Command Type: {1}" +
                "\tOptions Format: [-opt1 value1 -opt2 value2]");
        usage = MessageFormat.format(usage,
                Log.color(atom.getName(), Log.COLOR_CYAN, true),
                Log.color(atom.getType().name(), Log.COLOR_CYAN, true));

        /* Help */
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(240);
        formatter.printHelp(usage, header, atom.options(), null);
    }
}
