package io.vertx.tp.workflow.uca.toolkit;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.configuration.MetaInstance;
import io.vertx.tp.workflow.atom.runtime.WRecord;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.atom.runtime.WTransition;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class UGeneration {

    private final transient MetaInstance metadata;

    public UGeneration(final MetaInstance metadata) {
        this.metadata = metadata;
    }

    public Future<WRecord> runAsync(final WRequest request, final WTransition transition) {
        /*
         * Here are generation workflow for usage
         */
        return null;
    }
}
