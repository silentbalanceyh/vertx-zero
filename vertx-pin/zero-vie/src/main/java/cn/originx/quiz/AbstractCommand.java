package cn.originx.quiz;

import io.horizon.eon.em.Environment;
import io.vertx.ext.unit.TestContext;
import io.vertx.tp.plugin.shell.Commander;
import io.vertx.tp.plugin.shell.atom.CommandAtom;
import io.vertx.tp.plugin.shell.atom.CommandInput;
import io.vertx.tp.plugin.shell.cv.em.CommandType;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.vertx.up.util.Ut;
import org.junit.BeforeClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AbstractCommand extends AbstractPlatform {

    public AbstractCommand() {
        super(Environment.Development);
    }

    @BeforeClass
    public static void initialize() {
        Sl.init();
    }

    /**
     * @param context TestContext
     * @param expr    subsystem,command
     */
    protected void tcRun(final TestContext context, final String expr) {
        this.tcRun(context, expr, new ConcurrentHashMap<>());
    }

    protected void tcRun(final TestContext context, final String expr, final String filename) {
        final ConcurrentMap<String, String> arguments = new ConcurrentHashMap<>();
        Ut.<String>itJObject(this.ioJObject(filename), (value, field) -> arguments.put(field, value));
        this.tcRun(context, expr, arguments);
    }

    private void tcRun(final TestContext context, final String expr, final ConcurrentMap<String, String> arguments) {
        final String[] commands = expr.replace(" ", "").split(",");
        final Commander commander = this.commander(commands[0], commands[1]);
        Objects.requireNonNull(commander);
        final CommandInput args = this.arguments(commands[0], commands[1], arguments);
        this.tcAsync(context, commander.executeAsync(args),
            actual -> context.assertEquals(TermStatus.SUCCESS, actual));
    }

    // -------------------- 私有方法 ---------------------
    private Commander commander(final String subSystem, final String command) {
        final CommandAtom atom = this.findCommand(subSystem, command);
        if (Objects.isNull(atom)) {
            return null;
        }
        /* Plugin 配置 */
        if (CommandType.SYSTEM == atom.getType()) {
            /* 子系统不可测 */
            return null;
        } else {
            /* Commander 初始化 */
            final Commander commander = Ut.instance(atom.getPlugin());
            return commander.bind(atom).bind(this.environment);
        }
    }

    private CommandInput arguments(final String subSystem, final String command,
                                   final ConcurrentMap<String, String> argumentMap) {
        final CommandAtom atom = this.findCommand(subSystem, command);
        if (Objects.isNull(atom)) {
            return null;
        }
        final List<String> names = new ArrayList<>();
        final List<String> values = new ArrayList<>();
        argumentMap.forEach((name, value) -> {
            names.add(name);
            values.add(value);
        });
        return CommandInput.create(names, values).bind(atom);
    }

    private CommandAtom findCommand(final String subSystem, final String command) {
        final List<CommandAtom> commands = Sl.commands();
        final CommandAtom atomSub = this.findCommand(commands, subSystem);
        if (Objects.isNull(atomSub)) {
            this.logger().error("[ Qz ] SubSystem = `{0}` could not be found!", subSystem);
            return null;
        }
        if (Objects.isNull(command)) {
            this.logger().error("[ Qz ] SubSystem = `{0}` and Command is null!", subSystem);
            return null;
        }
        final CommandAtom found = this.findCommand(atomSub.getCommands(), command);
        if (Objects.isNull(found)) {
            this.logger().error("[ Qz ] SubSystem = `{0}`, Command = `{1}` could not be found!",
                subSystem, command);
        }
        return found;
    }

    private CommandAtom findCommand(final List<CommandAtom> atoms, final String command) {
        return atoms.stream()
            .filter(each -> command.equals(each.getSimple()) || command.equals(each.getName()))
            .findAny().orElse(null);
    }
}
