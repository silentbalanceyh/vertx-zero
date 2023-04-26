package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.runtime.WRecord;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.atom.runtime.WTransition;
import io.vertx.tp.workflow.uca.central.AbstractMovement;
import io.vertx.up.unity.Ux;

import static io.vertx.tp.workflow.refine.Wf.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TransferEmpty extends AbstractMovement implements Transfer {
    @Override
    public Future<WRecord> moveAsync(final WRequest request, final WTransition instance) {
        LOG.Move.warn(this.getClass(), "[ Empty ] `Transfer` component has not been configured. ");
        return Ux.future();
    }
}
