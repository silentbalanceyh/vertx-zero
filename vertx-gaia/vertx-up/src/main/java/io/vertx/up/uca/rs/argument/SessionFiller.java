package io.vertx.up.uca.rs.argument;

import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.up.uca.rs.Filler;

public class SessionFiller implements Filler {
    @Override
    public Object apply(final String name,
                        final Class<?> paramType,
                        final RoutingContext context) {
        final Session session = context.session();
        return session.get(name);
    }
}
