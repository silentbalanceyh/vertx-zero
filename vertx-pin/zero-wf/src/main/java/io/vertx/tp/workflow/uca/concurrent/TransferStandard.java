package io.vertx.tp.workflow.uca.concurrent;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.tp.workflow.atom.WRequest;
import io.vertx.tp.workflow.uca.central.AbstractMovement;
import io.vertx.tp.workflow.uca.central.Transfer;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TransferStandard extends AbstractMovement implements Transfer {
    @Override
    public Future<WRecord> moveAsync(final WRequest request, final WProcess wProcess) {
        return null;
    }
}
