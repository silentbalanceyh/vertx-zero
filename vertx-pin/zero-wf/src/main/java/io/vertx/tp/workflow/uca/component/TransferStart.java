package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.runtime.WRecord;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.atom.runtime.WTransition;
import io.vertx.tp.workflow.uca.central.AbstractMovement;
import io.vertx.tp.workflow.uca.modeling.Register;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TransferStart extends AbstractMovement implements Transfer {
    @Override
    public Future<WRecord> moveAsync(final WRequest request, final WTransition wTransition) {
        /*
         * Record processing first, here the parameters are following:
         *
         * 1. Process Record
         * 2. Todo Record
         *
         * Record support ADD / UPDATE operation combined
         */
        return this.inputAsync(request.request())


            /* Entity / Extension Ticket Record Execution, ( Insert or Update ) */
            .compose(normalized -> {
                final Register register = Register.phantom(normalized, this.metadataIn());
                return register.saveAsync(normalized, this.metadataIn());
            })


            /* Todo Execution ( Todo Insert ) */
            .compose(processed -> this.insertAsync(processed, wTransition))
            .compose(record -> wTransition.start()
                .compose(started -> this.trackerKit.afterAsync(record, started))
            );
    }
}
