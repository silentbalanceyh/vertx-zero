package io.vertx.tp.jet.uca.param;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.tp.jet.atom.JtUri;
import io.vertx.tp.optic.jet.JtIngest;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.ID;

@Deprecated
class HeaderIngest implements JtIngest {
    @Override
    public Envelop in(final RoutingContext context, final JtUri uri) {
        /* Header */
        final MultiMap headers = context.request().headers();
        final JsonObject headerData = new JsonObject();
        headers.names().stream()
            .filter(field -> field.startsWith(ID.Header.PREFIX))
            .forEach(field -> headerData.put(field, headers.get(field)));
        return Envelop.success(new JsonObject().put(ID.PARAM_HEADER, headerData));
    }
}
