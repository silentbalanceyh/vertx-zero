package cn.originx.uca.console;

import cn.originx.migration.MigrateStep;
import cn.originx.migration.restore.MetaLimit;
import cn.originx.scaffold.console.AbstractInstruction;
import cn.originx.stellaris.Ok;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.jet.atom.JtApp;
import io.vertx.tp.plugin.shell.atom.CommandInput;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AdjustModelInstruction extends AbstractInstruction {

    @Override
    public Future<TermStatus> executeAsync(final CommandInput args) {
        final String appName = this.inString(args, "a");
        return Ok.vendor(appName).compose(okB -> {
            final JtApp app = okB.configApp();
            /* 修正模型数据专用 */
            final MigrateStep step = new MetaLimit(this.environment);
            return step.bind(app).procAsync(new JsonObject()).compose(config -> {
                Sl.output("模型数据修正完成！");
                return Ux.future(TermStatus.SUCCESS);
            });
        });
    }
}
