package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.atom.runtime.WTransition;
import io.vertx.tp.workflow.uca.central.AbstractTransfer;
import io.vertx.up.unity.Ux;

import static io.vertx.tp.workflow.refine.Wf.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementEmpty extends AbstractTransfer implements Movement {
    @Override
    public Future<WTransition> moveAsync(final WRequest request) {
        LOG.Move.warn(this.getClass(), "[ Empty ] `Movement` component has not been configured. ");
        return Ux.future();
    }
}
