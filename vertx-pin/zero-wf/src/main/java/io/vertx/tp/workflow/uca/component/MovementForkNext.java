package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRequest;
import io.vertx.tp.workflow.uca.central.AbstractTransfer;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementForkNext extends AbstractTransfer implements Movement {
    @Override
    public Future<WProcess> moveAsync(final WRequest request) {
        return null;
    }
}
