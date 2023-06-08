package io.vertx.up.backbone.hunt;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.backbone.Aim;
import io.vertx.up.commune.Envelop;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.registry.Uddi;
import io.vertx.up.uca.registry.UddiClient;

public class IpcAim extends BaseAim implements Aim<RoutingContext> {

    @Override
    public Handler<RoutingContext> attack(final Event event) {
        return Fn.runOr(() -> (context) -> this.exec(() -> {
            /*
             * Build TypedArgument by java reflection metadata definition
             */
            final Object[] arguments = this.buildArgs(context, event);
            /*
             * Method callxx
             */
            final Object result = this.invoke(event, arguments);

            /*
             * Call Flower next method to get future
             */
            // final Envelop data = Flower.continuous(context, result);
            final Future<Envelop> future = Flower.next(context, result);

            /*
             * Set handler to wait for future result instead of other
             */
            future.onComplete(dataRes -> {
                /*
                 * To avoid null dot result when the handler triggered result here
                 * SUCCESS
                 */
                if (dataRes.succeeded()) {
                    final Envelop data = dataRes.result();
                    /*
                     * Rpc handler as next handler to process data continuous
                     */
                    final UddiClient client = Uddi.client(this.getClass());
                    final Future<Envelop> handler = client
                        .bind(context.vertx()).bind(event.getAction())
                        .connect(data);
                    /*
                     * The last method is for
                     * 1) Standard Future workflow -> dataRest
                     * 2) dataRes -> Rpc Handler
                     * 3) Answer reply with Rpc data ( handler result )
                     */
                    handler.onComplete(res -> {
                        /*
                         * To avoid null dot result
                         * SUCCESS
                         */
                        if (res.succeeded()) {
                            Answer.reply(context, res.result());
                        }
                    });
                }
            });
            /*
             * Please refer following old code
            // 4. Rpc Client Call to send the data.
            final Future<Envelop> handler = TunnelClient.create(getClass())
                    .connect(context.vertx())
                    .connect(event.getAction())
                    .send(data);
            // 5. Reply
            handler.setHandler(res -> Answer.reply(context, res.result()));
             */
        }, context, event), event);
    }
}
