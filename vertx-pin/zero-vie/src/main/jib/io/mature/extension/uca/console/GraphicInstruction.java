package io.mature.extension.uca.console;

import io.macrocosm.specification.program.HArk;
import io.mature.extension.scaffold.console.AbstractInstruction;
import io.mature.extension.stellaris.Ok;
import io.mature.extension.uca.graphic.Plotter;
import io.mature.extension.uca.graphic.TopologyPlotter;
import io.vertx.core.Future;
import io.vertx.up.plugin.shell.atom.CommandInput;
import io.vertx.up.plugin.shell.cv.em.TermStatus;
import io.vertx.up.plugin.shell.refine.Sl;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class GraphicInstruction extends AbstractInstruction {
    @Override
    public Future<TermStatus> executeAsync(final CommandInput args) {
        final String nodeId = this.inString(args, "n");
        final String edgeId = this.inString(args, "e");
        final String appName = this.inString(args, "a");
        /*
         * 绘图仪
         */
        return Ok.vendor(appName).compose(okB -> {
            final HArk app = okB.configApp();
            final Plotter plotter = new TopologyPlotter();
            plotter.bind(app);
            return plotter.drawAsync(nodeId, edgeId, this.ignores()).compose(finished -> {
                Sl.output("图引擎初始化完成！");
                return Ux.future(TermStatus.SUCCESS);
            });
        });

    }
}
