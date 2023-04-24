package io.vertx.up.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Worker;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.em.Etat;
import io.horizon.eon.em.container.MessageModel;
import io.vertx.up.eon.web.ID;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.registry.Uddi;
import io.vertx.up.uca.registry.UddiRegistry;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * Get data from event bus and push metadata to Etcd.
 */
@Worker(value = MessageModel.REQUEST_MICRO_WORKER, instances = 1)
public class ZeroHttpRegistry extends AbstractVerticle {

    private static final Annal LOGGER = Annal.get(ZeroHttpRegistry.class);

    private transient final UddiRegistry registry = Uddi.registry(this.getClass());

    @Override
    public void start() {
        final EventBus bus = this.vertx.eventBus();
        bus.<JsonObject>consumer(ID.Addr.REGISTRY_START, result -> {
            final JsonObject data = result.body();
            final String name = data.getString(Registry.NAME);
            final HttpServerOptions options =
                new HttpServerOptions(data.getJsonObject(Registry.OPTIONS));
            final String[] uris =
                data.getString(Registry.URIS).split(Strings.COMMA);
            final Set<String> uriData = new TreeSet<>(Arrays.asList(uris));
            // Write the data to registry.
            this.registry.registryHttp(name, options, Etat.RUNNING);
            this.registry.registryRoute(name, options, uriData);

            LOGGER.info(Info.MICRO_REGISTRY_CONSUME, this.getClass().getSimpleName(),
                name, ID.Addr.REGISTRY_START);
        });
    }
}
