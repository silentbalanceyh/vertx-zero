package io.vertx.up.uca.rs.router;

import io.reactivex.Observable;
import io.vertx.ext.web.Route;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.eon.Strings;

import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * Hub for mime type
 * Producer/Consumer
 * register to route to generate mime support
 */
public class MediaHub implements Hub<Route> {

    @Override
    public void mount(final Route route,
                      final Event event) {
        // produces
        final Set<MediaType> produces = event.getProduces();
        Observable.fromIterable(produces)
            .map(type -> type.getType() + Strings.SLASH + type.getSubtype())
            .subscribe(route::produces).dispose();
        // consumes
        final Set<MediaType> consumes = event.getProduces();
        Observable.fromIterable(consumes)
            .map(type -> type.getType() + Strings.SLASH + type.getSubtype())
            .subscribe(route::consumes).dispose();
    }
}
