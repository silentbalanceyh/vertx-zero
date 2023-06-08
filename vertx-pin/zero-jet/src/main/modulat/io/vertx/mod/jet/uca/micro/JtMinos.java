package io.vertx.mod.jet.uca.micro;

import io.horizon.spi.jet.JtConsumer;
import io.horizon.uca.cache.Cc;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.mod.jet.JetThanatos;
import io.vertx.mod.jet.atom.JtUri;
import io.vertx.mod.jet.monitor.JtMonitor;
import io.vertx.mod.jet.refine.Jt;
import io.vertx.up.commune.Envelop;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 「Worker」
 * Important worker here
 */
public class JtMinos extends AbstractVerticle {

    private static final Cc<String, JtConsumer> CC_CONSUMER = Cc.openThread();
    private transient final JetThanatos ensurer = JetThanatos.create(this.getClass());
    private transient final JtMonitor monitor = JtMonitor.create(this.getClass());

    @Override
    public void start() {
        /*
         * 「Booting Life Cycle」
         */
        final EventBus event = this.vertx.eventBus();
        /*
         * Collect all the information from JsonObject
         */
        final ConcurrentMap<String, JtUri> uriMap = Jt.answer(this.config());

        /*
         * 「Booting Life Cycle」
         * Deploy consumer that bind to address
         * It means that except worker, consumer also could be configured in each API / Service
         */
        this.consumeAddr(uriMap).forEach((address, executor) -> event.<Envelop>consumer(address, handler -> {
            /*
             * 「Request Life Cycle」
             * Get the core data here
             */
            final Envelop message = handler.body();
            final JtUri uri = uriMap.get(message.key());
            if (Objects.isNull(uri)) {
                // Error-500: Definition Error
                handler.reply(this.ensurer.to500DefinitionError(message.key()));
            } else {
                /*
                 * Monitoring data in each requst
                 */
                this.monitor.receiveData(message.key(), uri);
                /*
                 * Consume and replied here
                 * ZApi definition cross API / SERVICE here
                 */
                final Future<Envelop> future = executor.async(message, uri);
                future.onComplete(replyHandler -> {
                    if (replyHandler.succeeded()) {
                        /*
                         * 「Callback Life Cycle」
                         * Replying message from service
                         * It could connect with ACL control data
                         */
                        final Envelop replied = replyHandler.result();
                        if (Objects.nonNull(replied.error())) {
                            replied.error().printStackTrace();
                        }
                        handler.reply(replied);
                    } else {
                        /*
                         * 「Callback Exception」
                         * Error throw of exception
                         */
                        handler.reply(Envelop.failure(replyHandler.cause()));
                    }
                });
            }
        }));
    }

    private ConcurrentMap<String, JtConsumer> consumeAddr(final ConcurrentMap<String, JtUri> uriMap) {
        final ConcurrentMap<String, JtConsumer> consumers = new ConcurrentHashMap<>();
        uriMap.values().stream().map(JtUri::worker).forEach(worker -> {
            /*
             * Address as key
             */
            final String address = worker.getWorkerAddress();
            final Class<?> consumerCls = worker.getWorkerConsumer();
            /*
             * JtConsumer reference
             */
            final JtConsumer consumer = CC_CONSUMER.pick(() -> Ut.instance(consumerCls));
            // Fn.po?lThread(Pool.CONSUMER_CLS, () -> Ut.instance(consumerCls));
            if (Ut.isNotNil(address) && Objects.nonNull(consumer)) {
                consumers.put(address, consumer);
            }
        });
        return consumers;
    }
}
