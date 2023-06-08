package io.vertx.mod.jet.uca.param;

import io.horizon.spi.jet.JtIngest;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mod.jet.atom.JtUri;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KWeb;

@Deprecated
class HeaderIngest implements JtIngest {
    @Override
    public Envelop in(final RoutingContext context, final JtUri uri) {
        /* Header */
        final MultiMap headers = context.request().headers();
        final JsonObject headerData = new JsonObject();
        headers.names().stream()
            .filter(field -> field.startsWith(KWeb.HEADER.PREFIX))
            .forEach(field -> headerData.put(field, headers.get(field)));
        return Envelop.success(new JsonObject().put(KWeb.ARGS.PARAM_HEADER, headerData));
    }
}
