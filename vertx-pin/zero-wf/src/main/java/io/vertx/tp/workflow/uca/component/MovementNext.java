package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.WMove;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRequest;
import io.vertx.tp.workflow.uca.top.AbstractTransfer;
import io.vertx.up.atom.Refer;

import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MovementNext extends AbstractTransfer implements Movement {
    @Override
    public Future<WProcess> moveAsync(final WRequest request) {
        // Instance Building
        final Refer process = new Refer();
        return this.beforeAsync(request, process).compose(normalized
            /* Here normalized/request shared the same reference */ -> {
            final Divert divert;
            final WProcess wProcess = process.get();
            if (wProcess.isStart()) {
                // Divert Next ( Workflow Started )
                divert = Divert.instance(DivertNext.class);
            } else {
                // Divert Start ( Workflow Not Started )
                divert = Divert.instance(DivertStart.class);
            }
            final ConcurrentMap<String, WMove> rules = this.rules();
            // Bind Metadata Instance
            divert.bind(this.metadataIn());
            return divert.bind(rules).moveAsync(normalized, wProcess);
        });
    }
}
