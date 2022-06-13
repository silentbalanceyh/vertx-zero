package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.runtime.WMove;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.atom.runtime.WTransition;
import io.vertx.tp.workflow.uca.central.AbstractTransfer;

import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementNext extends AbstractTransfer implements Movement {
    @Override
    public Future<WTransition> moveAsync(final WRequest request) {
        // Instance Building
        final WTransition wTransition = WTransition.create(request);
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
            final ConcurrentMap<String, WMove> rules = this.rules();
            // Bind Metadata Instance
            moveOn.bind(this.metadataIn());
            return moveOn.bind(rules).moveAsync(normalized, wTransition);
        });
    }
}
