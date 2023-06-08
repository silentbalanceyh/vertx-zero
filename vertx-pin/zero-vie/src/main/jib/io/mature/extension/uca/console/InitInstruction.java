package io.mature.extension.uca.console;

import cn.vertxup.ambient.service.application.InitStub;
import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.mature.extension.migration.MigrateStep;
import io.mature.extension.migration.restore.MetaLimit;
import io.mature.extension.refine.Ox;
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
public class InitInstruction extends AbstractInstruction {
    @Override
    public Future<TermStatus> executeAsync(final CommandInput args) {
        final String appName = this.inString(args, "a");
        /*
         * appName为null，直接获取app
         */
        if (appName == null) {
            return Ok.app().compose(this::defaultValue);
        } else {
            return Ok.vendor(appName).compose(okB -> this.defaultValue(okB.configApp()));
        }
    }

    private Future<TermStatus> defaultValue(final HArk ark) {
        final InitStub stub = Ox.pluginInitializer();
        /*
         * 全部导入完成，执行初始化
         */
        final HApp app = ark.app();
        return stub.initModeling(app.name()).compose(inited -> {
            /*
             * 执行初始化
             */
            Sl.output("业务环境初始化完成！");
            final MigrateStep step = new MetaLimit(this.environment);
            /* 建模修正数据 */
            return step.bind(ark).procAsync(new JsonObject()).compose(config -> {
                Sl.output("模型数据修正完成！");
                return Ux.future(TermStatus.SUCCESS);
            });
        }).otherwise(Sl::failError);
    }
}
