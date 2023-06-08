package io.vertx.up.backbone.argument;

import io.vertx.ext.web.RoutingContext;
import io.vertx.up.backbone.Filler;

/**
 * 「Co」Critical Specific
 *
 * This `Filler` is placeholder for Body/Stream data in RESTful web request here
 *
 * In current Filler, it do nothing because the body extracting will go through
 * `Resolver` for different MIME here.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class EmptyFiller implements Filler {
    @Override
    public Object apply(final String name,
                        final Class<?> paramType,
                        final RoutingContext context) {
        return null;
    }
}
