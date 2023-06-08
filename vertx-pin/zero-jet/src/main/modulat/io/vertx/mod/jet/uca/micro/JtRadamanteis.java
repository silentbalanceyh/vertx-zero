package io.vertx.mod.jet.uca.micro;

import io.horizon.spi.jet.JtIngest;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mod.jet.atom.JtUri;
import io.vertx.up.commune.Envelop;

/**
 * 「Ingest」
 * Parameter extraction when paramMode = DEFINE, it's valid
 */
public class JtRadamanteis implements JtIngest {
    @Override
    public Envelop in(final RoutingContext context, final JtUri uri) {
        // TODO: JtIngest code logical
        return null;
    }
}
