package io.vertx.mod.jet.uca.param;

import io.horizon.spi.jet.JtIngest;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mod.jet.atom.JtUri;
import io.vertx.mod.jet.cv.em.ParamMode;
import io.vertx.up.commune.Envelop;

import java.util.function.Supplier;

/*
 * package scope,
 * /api/xxx/:name?email=lang.yu@hp.com
 *
 * Parsed uri and query string both
 * -->
 *    name = xxx
 *    email = lang.yu@hp.com
 */
class QueryIngest implements JtIngest {
    private transient final Supplier<JtIngest> supplier = Pool.INNER_INGEST.get(ParamMode.PATH);

    @Override
    public Envelop in(final RoutingContext context, final JtUri uri) {
        final JtIngest ingest = this.supplier.get();
        final Envelop envelop = ingest.in(context, uri);
        /*
         * JqTool extracting
         */
        final MultiMap queryParams = context.queryParams();
        queryParams.forEach(item -> envelop.value(item.getKey(), item.getValue()));
        return envelop;
    }
}
