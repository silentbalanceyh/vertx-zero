package io.vertx.up.backbone.mime.resolver;

import io.horizon.exception.WebException;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.backbone.mime.Resolver;

public class XFormResolver<T> implements Resolver<T> {

    @Override
    public Epsilon<T> resolve(final RoutingContext context,
                              final Epsilon<T> income)
        throws WebException {
        return income;
    }
}
