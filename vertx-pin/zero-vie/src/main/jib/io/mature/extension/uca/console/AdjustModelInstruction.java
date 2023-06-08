package io.mature.extension.uca.console;

import io.macrocosm.specification.program.HArk;
import io.mature.extension.migration.MigrateStep;
import io.mature.extension.migration.restore.MetaLimit;
import io.mature.extension.scaffold.console.AbstractInstruction;
import io.mature.extension.stellaris.Ok;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.plugin.shell.atom.CommandInput;
import io.vertx.up.plugin.shell.cv.em.TermStatus;
import io.vertx.up.plugin.shell.refine.Sl;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AdjustModelInstruction extends AbstractInstruction {

    @Override
    public Future<TermStatus> executeAsync(final CommandInput args) {
        final String appName = this.inString(args, "a");
        return Ok.vendor(appName).compose(okB -> {
            final HArk ark = okB.configApp();
            /* 修正模型数据专用 */
            final MigrateStep step = new MetaLimit(this.environment);
            return step.bind(ark).procAsync(new JsonObject()).compose(config -> {
                Sl.output("模型数据修正完成！");
                return Ux.future(TermStatus.SUCCESS);
            });
        });
    }
}
