package cn.originx.uca.console;

import cn.originx.migration.MigrateStep;
import cn.originx.migration.restore.MetaLimit;
import cn.originx.refine.Ox;
import cn.originx.scaffold.console.AbstractInstruction;
import cn.originx.stellaris.Ok;
import cn.vertxup.ambient.service.InitStub;
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
public class InitInstruction extends AbstractInstruction {
    @Override
    public Future<TermStatus> executeAsync(final CommandInput args) {
        final String appName = this.inString(args, "a");
        return Ok.vendor(appName).compose(okB -> {
            final JtApp app = okB.configApp();
            final InitStub stub = Ox.pluginInitializer();
            /*
             * 全部导入完成，执行初始化
             */
            return stub.initModeling(app.getName()).compose(inited -> {
                /*
                 * 执行初始化
                 */
                Sl.output("业务环境初始化完成！");
                final MigrateStep step = new MetaLimit(this.environment);
                /* 建模修正数据 */
                return step.bind(app).procAsync(new JsonObject()).compose(config -> {
                    Sl.output("模型数据修正完成！");
                    return Ux.future(TermStatus.SUCCESS);
                });
            }).otherwise(Sl::failError);
        });
    }
}
