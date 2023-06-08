package io.mature.extension.uca.console;

import io.mature.extension.migration.Migrate;
import io.mature.extension.migration.MigrateService;
import io.mature.extension.scaffold.console.AbstractInstruction;
import io.vertx.core.Future;
import io.vertx.up.plugin.shell.atom.CommandInput;
import io.vertx.up.plugin.shell.cv.em.TermStatus;
import io.vertx.up.plugin.shell.refine.Sl;
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
