package cn.originx.uca.console;

import cn.originx.refine.Ox;
import cn.originx.scaffold.console.AbstractInstruction;
import io.vertx.tp.error._400EnvUnsupportException;
import io.vertx.tp.plugin.shell.atom.CommandInput;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class EnvironmentInstruction extends AbstractInstruction {
    private static final Set<String> ENVS = new HashSet<String>() {
        {
            this.add("prod");    // 生产
            this.add("dev");     // 开发测试
            this.add("devs");     // 开发测试
            this.add("home");    // 本地
            this.add("zw");      // 招为
        }
    };

    @Override
    public TermStatus execute(final CommandInput args) {
        return this.runDevelopment(() -> {
            /*
             * 执行程序 ./configuration.js
             */
            final String envName = this.inString(args, "e");
            if (ENVS.contains(envName)) {
                /*
                 * 执行的命令：
                 */
                final List<String> commands = new ArrayList<>();
                commands.add("node");
                commands.add("configuration.js");
                commands.add(envName);
                Ox.runCmd(commands);
                return TermStatus.SUCCESS;
            } else {
                throw new _400EnvUnsupportException(this.getClass(), envName);
            }
        });
    }
}
