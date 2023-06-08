package io.vertx.up.backbone.argument;

import io.vertx.ext.web.RoutingContext;
import io.vertx.up.backbone.Filler;
import io.vertx.up.runtime.ZeroSerializer;

/**
 * 「Co」JSR311 for .@PathParam
 *
 * This `Filler` is for path parsing `/api/xxx/name/{name}` format to extract to
 *
 * ```shell
 * // <pre><code>
 *    name = value
 * // </code></pre>
 * ```
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class PathFiller implements Filler {
    @Override
    public Object apply(final String name,
                        final Class<?> paramType,
                        final RoutingContext context) {
        // 1. Get path information
        return ZeroSerializer.getValue(paramType, context.pathParam(name));
    }
}
