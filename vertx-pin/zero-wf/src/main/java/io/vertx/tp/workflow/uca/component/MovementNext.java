package io.vertx.tp.workflow.uca.component;

import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRequest;
import io.vertx.up.fn.Fn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementNext extends AbstractTransfer implements Movement {
    @Override
    public Future<WProcess> moveAsync(final WRequest request) {
        // Instance Building
        return request.process().compose(wProcess -> {
            final Divert divert;
            if (wProcess.isStart()) {
                // Divert Start
                divert = Fn.poolThread(WfPool.POOL_DIVERT, DivertStart::new, DivertStart.class.getName());
            } else {
                // Divert Next
                divert = Fn.poolThread(WfPool.POOL_DIVERT, DivertNext::new, DivertNext.class.getName());
            }
            return divert.bind(this.rules()).moveAsync(request, wProcess);
        });
    }
}
