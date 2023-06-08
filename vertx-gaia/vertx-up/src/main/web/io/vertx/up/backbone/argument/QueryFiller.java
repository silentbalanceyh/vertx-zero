package io.vertx.up.backbone.argument;

import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.backbone.Filler;
import io.vertx.up.runtime.ZeroSerializer;

/**
 * 「Co」JSR311 for .@QueryParam
 *
 * This `Filler` is for query string `/api/xxx?name={name}` format to extract to
 *
 * ```shell
 * // <pre><code>
 *    name = value
 * // </code></pre>
 * ```
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
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
