package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.WMove;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRequest;

import java.util.concurrent.ConcurrentMap;

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
                divert = Divert.instance(DivertStart.class);
            } else {
                // Divert Next
                divert = Divert.instance(DivertNext.class);
            }
            final ConcurrentMap<String, WMove> rules = this.rules();
            divert.bind(this.metadataIn());
            return divert.bind(rules).moveAsync(request, wProcess);
        });
    }
}
