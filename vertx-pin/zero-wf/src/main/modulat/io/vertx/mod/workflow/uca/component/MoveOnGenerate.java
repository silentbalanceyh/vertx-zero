package io.vertx.mod.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.runtime.WRecord;
import io.vertx.mod.workflow.atom.runtime.WRequest;
import io.vertx.mod.workflow.atom.runtime.WTransition;
import io.vertx.mod.workflow.uca.central.AbstractMoveOn;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MoveOnGenerate extends AbstractMoveOn {
    @Override
    public Future<WRecord> transferAsync(final WRequest request, final WTransition wTransition) {
        Objects.requireNonNull(this.todoKit);
        Objects.requireNonNull(this.linkageKit);
        /*
         * Here will copy the todo from original WRecord first
         *
         * 1) The next task will be put into WTransition instance
         * 2) The Gear will determine the mode from WTransition
         *
         * Here are three parameters
         * 1. Record        -> The based original WRecord that will be generate todo
         * 2. JsonObject    -> requestJ that came from input
         * 3. WTransition   -> Transition ( The `to` will be used )
         **/
        final JsonObject requestJ = request.request();
        return this.todoKit.generateAsync(requestJ, wTransition, request.record())
            // Linkage Sync
            .compose(record -> this.linkageKit.syncAsync(requestJ, record));
    }
}
