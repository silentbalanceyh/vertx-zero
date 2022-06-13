package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.runtime.WProcess;
import io.vertx.tp.workflow.atom.runtime.WRecord;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.central.AbstractMovement;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TransferEmpty extends AbstractMovement implements Transfer {
    @Override
    public Future<WRecord> moveAsync(final WRequest request, final WProcess instance) {
        Wf.Log.warnMove(this.getClass(), "[ Empty ] `Transfer` component has not been configured. ");
        return Ux.future();
    }
}
