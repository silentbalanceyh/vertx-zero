package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRequest;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.canal.AbstractTransfer;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementEmpty extends AbstractTransfer implements Movement {
    @Override
    public Future<WProcess> moveAsync(final WRequest request) {
        Wf.Log.warnMove(this.getClass(), "[ Empty ] `Movement` component has not been configured. ");
        return Ux.future();
    }
}
