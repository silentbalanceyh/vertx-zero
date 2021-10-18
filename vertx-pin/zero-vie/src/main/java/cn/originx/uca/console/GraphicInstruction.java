package cn.originx.uca.console;

import cn.originx.scaffold.console.AbstractInstruction;
import cn.originx.stellaris.Ok;
import cn.originx.uca.graphic.Plotter;
import cn.originx.uca.graphic.TopologyPlotter;
import io.vertx.core.Future;
import io.vertx.tp.jet.atom.JtApp;
import io.vertx.tp.plugin.shell.atom.CommandInput;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;
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
            final JtApp app = okB.configApp();
            final Plotter plotter = new TopologyPlotter();
            plotter.bind(app);
            return plotter.drawAsync(nodeId, edgeId, this.ignores()).compose(finished -> {
                Sl.output("图引擎初始化完成！");
                return Ux.future(TermStatus.SUCCESS);
            });
        });

    }
}
