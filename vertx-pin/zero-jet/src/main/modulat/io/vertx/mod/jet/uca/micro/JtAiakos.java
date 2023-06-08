package io.vertx.mod.jet.uca.micro;

import io.horizon.spi.jet.JtConsumer;
import io.vertx.core.Future;
import io.vertx.mod.jet.monitor.JtMonitor;
import io.vertx.up.commune.Envelop;
import io.vertx.up.specification.action.Commercial;

/**
 * 「Consumer」
 * Default consumer to consume request, complex code logical
 */
public class JtAiakos implements JtConsumer {

    private transient final JtMonitor monitor = JtMonitor.create(this.getClass());

    /*
     * Data example
     */
    @Override
    public Future<Envelop> async(final Envelop envelop, final Commercial commercial) {
        return JtPandora.async(envelop, commercial, this.monitor);
    }
}
