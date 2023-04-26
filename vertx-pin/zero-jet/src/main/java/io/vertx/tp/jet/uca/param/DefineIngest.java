package io.vertx.tp.jet.uca.param;

import io.horizon.spi.jet.JtIngest;
import io.vertx.ext.web.RoutingContext;
import io.vertx.tp.error._501IngestMissingException;
import io.vertx.tp.error._501IngestSpecException;
import io.vertx.tp.jet.atom.JtUri;
import io.vertx.tp.jet.cv.JtConstant;
import io.vertx.up.commune.Envelop;
import io.vertx.up.runtime.ZeroAmbient;
import io.vertx.up.util.Ut;

import java.util.Objects;

class DefineIngest implements JtIngest {
    @Override
    public Envelop in(final RoutingContext context, final JtUri uri) {
        final Class<?> clazz = ZeroAmbient.getPlugin(JtConstant.COMPONENT_INGEST_KEY);
        if (Objects.isNull(clazz)) {
            return Envelop.failure(new _501IngestMissingException(this.getClass()));
        } else if (!Ut.isImplement(clazz, JtIngest.class)) {
            return Envelop.failure(new _501IngestSpecException(this.getClass(), clazz.getName()));
        } else {
            final JtIngest ingest = Ut.instance(clazz);
            return ingest.in(context, uri);
        }
    }
}
