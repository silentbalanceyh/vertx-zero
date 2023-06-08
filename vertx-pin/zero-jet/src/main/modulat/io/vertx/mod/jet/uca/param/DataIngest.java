package io.vertx.mod.jet.uca.param;

import io.horizon.spi.jet.JtIngest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mod.jet.atom.JtUri;
import io.vertx.mod.jet.cv.em.ParamMode;
import io.vertx.mod.jet.monitor.JtMonitor;
import io.vertx.up.commune.Envelop;

import java.util.Objects;
import java.util.function.Supplier;

/*
 * Public required because interface is in extension
 */
public class DataIngest implements JtIngest {

    private final transient JtMonitor monitor = JtMonitor.create(this.getClass());

    @Override
    public Envelop in(final RoutingContext context, final JtUri uri) {
        /* Read parameter mode */
        final ParamMode mode = uri.paramMode();
        final Supplier<JtIngest> supplier = Pool.INNER_INGEST.get(mode);
        /* Inner assert */
        assert null != supplier : "Function must not be null here.";
        final JtIngest ingest = supplier.get();
        /*
         * Verifier on request data
         */
        final Envelop envelop = ingest.in(context, uri);
        /*
         * Monitor here
         */
        final JsonObject params = envelop.data();
        final Envelop error = this.validate(envelop, uri);
        return Objects.isNull(error) ? envelop : error;
    }

    private Envelop validate(final Envelop envelop, final JtUri uri) {
        final JsonObject data = envelop.data();
        Envelop error = Verifier.validateRequired(this.getClass(), data, uri.paramRequired());
        /* Specification of mode */
        final ParamMode mode = uri.paramMode();
        if ((ParamMode.BODY == mode || ParamMode.DEFINE == mode) && null == error) {
            /* Body validation */
            error = Verifier.validateContained(this.getClass(), data, uri.paramContained());
        }
        return error;
    }
}
