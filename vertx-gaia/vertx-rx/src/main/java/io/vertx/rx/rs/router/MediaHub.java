package io.vertx.rx.rs.router;

import io.horizon.eon.VString;
import io.vertx.rxjava3.ext.web.Route;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.backbone.router.Hub;
import jakarta.ws.rs.core.MediaType;

import java.util.Set;

public class MediaHub implements Hub<Route> {
    @Override
    public void mount(final Route route, final Event event) {
        // produces
        final Set<MediaType> produces = event.getProduces();
        for (final MediaType type : produces) {
            final String item = type.getType() + VString.SLASH + type.getSubtype();
            route.consumes(item);
        }
        // consumes
        final Set<MediaType> consumes = event.getConsumes();
        for (final MediaType type : consumes) {
            final String item = type.getType() + VString.SLASH + type.getSubtype();
            route.consumes(item);
        }
    }
}
