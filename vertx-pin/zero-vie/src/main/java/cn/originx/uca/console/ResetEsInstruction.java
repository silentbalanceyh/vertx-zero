package cn.originx.uca.console;

import cn.originx.refine.Ox;
import cn.originx.scaffold.console.AbstractInstruction;
import cn.originx.stellaris.Ok;
import io.vertx.core.Future;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.plugin.shell.atom.CommandInput;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ResetEsInstruction extends AbstractInstruction {

    @Override
    public Future<TermStatus> executeAsync(final CommandInput args) {
        final String appName = this.inString(args, "a");
        return this.runEach(appName, identifier -> Ok.app().compose(app -> {
            final DataAtom atom = Ao.toAtom(app.getName(), identifier);
            return Ox.runEs(atom).compose(client -> Ux.future(Boolean.TRUE));
        })).compose(done -> {
            Sl.output("索引重建完成，重建模型数量：{0}", done.size());
            return Ux.future(TermStatus.SUCCESS);
        });
    }
}
