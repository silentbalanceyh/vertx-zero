package io.mature.extension.uca.console;

import io.mature.extension.scaffold.console.AbstractInstruction;
import io.vertx.core.Future;
import io.vertx.mod.ke.booter.Bt;
import io.vertx.up.plugin.database.DataPool;
import io.vertx.up.plugin.shell.atom.CommandInput;
import io.vertx.up.plugin.shell.cv.em.TermStatus;
import io.vertx.up.plugin.shell.refine.Sl;
import io.vertx.up.unity.Ux;
import org.jooq.DSLContext;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class JobInstruction extends AbstractInstruction {
    @Override
    public Future<TermStatus> executeAsync(final CommandInput args) {
        /*
         * 删除原始的Job
         */
        final DataPool pool = DataPool.create();
        final DSLContext context = pool.getExecutor();
        context.execute("DELETE FROM I_SERVICE WHERE `KEY` IN (SELECT SERVICE_ID FROM I_JOB)");
        context.execute("DELETE FROM I_JOB");
        /*
         * 重新导入
         */
        final String prefix = this.inString(args, "p");
        return Bt.loadAsync("init/oob/", prefix).compose(done -> {
            Sl.output("您的任务更新完成，更新结果：{0}", done);
            return Ux.future(done ? TermStatus.SUCCESS : TermStatus.FAILURE);
        });
    }
}
