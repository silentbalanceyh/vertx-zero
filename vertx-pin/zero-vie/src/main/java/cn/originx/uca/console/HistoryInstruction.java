package cn.originx.uca.console;

import cn.originx.migration.MigrateStep;
import cn.originx.migration.backup.BackupHistory;
import cn.originx.scaffold.console.AbstractInstruction;
import io.vertx.core.Future;
import io.vertx.tp.plugin.shell.atom.CommandInput;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HistoryInstruction extends AbstractInstruction {
    @Override
    public Future<TermStatus> executeAsync(final CommandInput args) {
        return this.executeMigrate(args, (app, config) -> {
            final MigrateStep step = new BackupHistory(this.environment);
            return step.bind(app).procAsync(config).compose(result -> {
                Sl.output("历史文件备份完成，应用：{0}", app.getName());
                return Ux.future(TermStatus.SUCCESS);
            });
        });
    }
}
