package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.WMove;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRequest;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.central.AbstractTransfer;

import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementNext extends AbstractTransfer implements Movement {
    @Override
    public Future<WProcess> moveAsync(final WRequest request) {
        // Instance Building
        return Wf.process(request).compose(wProcess -> this.beforeAsync(request, wProcess)
            .compose(normalized -> {
                /* Here normalized/request shared the same reference */
                final MoveOn moveOn;
                if (wProcess.isStart()) {
                    // Divert Next ( Workflow Started )
                    moveOn = MoveOn.instance(MoveOnNext.class);
                } else {
                    // Divert Start ( Workflow Not Started )
                    moveOn = MoveOn.instance(MoveOnStart.class);
                }
                final ConcurrentMap<String, WMove> rules = this.rules();
                // Bind Metadata Instance
                moveOn.bind(this.metadataIn());
                return moveOn.bind(rules).moveAsync(normalized, wProcess);
            }));
    }
}
