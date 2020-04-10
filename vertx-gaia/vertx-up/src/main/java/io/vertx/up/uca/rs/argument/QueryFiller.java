package io.vertx.up.uca.rs.argument;

import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.runtime.ZeroSerializer;
import io.vertx.up.uca.rs.Filler;

/**
 * Parse JqTool Params
 *
 * @QueryParam
 */
public class QueryFiller implements Filler {

    @Override
    public Object apply(final String name,
                        final Class<?> paramType,
                        final RoutingContext context) {
        // 1. Get query information.
        final MultiMap map = context.queryParams();
        // 2. Get name
        return ZeroSerializer.getValue(paramType, map.get(name));
    }
}
