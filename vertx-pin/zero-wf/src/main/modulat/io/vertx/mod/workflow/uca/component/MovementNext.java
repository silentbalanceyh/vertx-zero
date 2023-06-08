package io.vertx.mod.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.mod.workflow.atom.runtime.WRequest;
import io.vertx.mod.workflow.atom.runtime.WTransition;
import io.vertx.mod.workflow.uca.central.AbstractTransfer;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementNext extends AbstractTransfer implements Movement {
    @Override
    public Future<WTransition> moveAsync(final WRequest request) {
        // Instance Building
        final WTransition wTransition = this.createTransition(request);
        return this.beforeAsync(request, wTransition).compose(normalized -> {
            /* Here normalized/request shared the same reference */
            final MoveOn moveOn;
            if (wTransition.isStarted()) {
                // MoveOn Next ( Workflow Started )
                moveOn = MoveOn.instance(MoveOnNext.class);
            } else {
                // MoveOn Start ( Workflow Not Started )
                moveOn = MoveOn.instance(MoveOnStart.class);
            }
            // Bind Metadata Instance
            moveOn.bind(this.metadataIn());
            return moveOn.moveAsync(normalized, wTransition);
        });
    }
}
