package io.mature.extension.uca.console;

import io.macrocosm.specification.app.HApp;
import io.mature.extension.refine.Ox;
import io.mature.extension.scaffold.console.AbstractInstruction;
import io.mature.extension.stellaris.Ok;
import io.vertx.core.Future;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.mod.atom.refine.Ao;
import io.vertx.up.plugin.shell.atom.CommandInput;
import io.vertx.up.plugin.shell.cv.em.TermStatus;
import io.vertx.up.plugin.shell.refine.Sl;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ResetEsInstruction extends AbstractInstruction {

    @Override
    public Future<TermStatus> executeAsync(final CommandInput args) {
        final String appName = this.inString(args, "a");
        return this.runEach(appName, identifier -> Ok.app().compose(ark -> {
            final HApp app = ark.app();
            final DataAtom atom = Ao.toAtom(app.name(), identifier);
            return Ox.runEs(atom).compose(client -> Ux.future(Boolean.TRUE));
        })).compose(done -> {
            Sl.output("索引重建完成，重建模型数量：{0}", done.size());
            return Ux.future(TermStatus.SUCCESS);
        });
    }
}
