package io.vertx.tp.jet.uca.aim;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.tp.jet.atom.JtUri;
import io.vertx.tp.jet.monitor.JtMonitor;
import io.vertx.up.commune.Envelop;
import io.vertx.up.extension.pointer.PluginExtension;
import io.vertx.up.uca.rs.hunt.Answer;
import io.vertx.up.unity.Ux;

import java.util.Objects;

public class SendAim implements JtAim {

    private transient final JtMonitor monitor = JtMonitor.create(this.getClass());

    @Override
    public Handler<RoutingContext> attack(final JtUri uri) {
        /*
         * 「Booting Life Cycle」
         *  Code Area
         */
        return context -> {
            /*
             * 「Request Life Cycle」
             */
            final Envelop request = Answer.previous(context);
            /*
             * Set id here, consumer will extract api data in worker
             */
            request.key(uri.key());
            /*
             * Mount the same extension / plug-in in web request
             */
            final Future<Envelop> future = PluginExtension.Flower.next(context, request);
            future.onComplete(res -> {
                if (res.succeeded()) {
                    final Envelop normalized = res.result();
                    final JsonObject data = normalized.data();
                    final String address = uri.worker().getWorkerAddress(); // Address
                    /*
                     * Monitor data and address
                     */
                    this.monitor.aimSend(data, address);

                    final Vertx vertx = context.vertx();
                    final EventBus event = vertx.eventBus();

                    event.<Envelop>request(address, normalized, Ux.Opt.on().delivery(), handler -> {
                        if (handler.succeeded()) {
                            /*
                             * 「Success」
                             */
                            final Message<Envelop> result = handler.result();
                            Answer.reply(context, result.body(), uri::producesMime);
                        } else {
                            /*
                             * 「Failure」
                             */
                            final Envelop error = Envelop.failure(handler.cause());
                            Answer.reply(context, error);
                        }
                    });
                } else {
                    if (Objects.nonNull(res.cause())) {
                        res.cause().printStackTrace();
                    }
                }
            });

        };
    }
}
