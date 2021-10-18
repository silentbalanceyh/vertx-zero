package cn.originx.uca.console;

import cn.originx.migration.Migrate;
import cn.originx.migration.MigrateService;
import cn.originx.scaffold.console.AbstractInstruction;
import io.vertx.core.Future;
import io.vertx.tp.plugin.shell.atom.CommandInput;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BackUpInstruction extends AbstractInstruction {

    @Override
    public Future<TermStatus> executeAsync(final CommandInput args) {
        return this.executeMigrate(args, (app, config) -> {
            final Migrate migrate = Ut.singleton(MigrateService.class);
            return migrate.bind(this.environment).bind(app).backupAsync(config).compose(result -> {
                Sl.output("基础系统备份完成！目录：{0}", config.getString("output"));
                return Ux.future(TermStatus.SUCCESS);
            });
        });
    }
}
