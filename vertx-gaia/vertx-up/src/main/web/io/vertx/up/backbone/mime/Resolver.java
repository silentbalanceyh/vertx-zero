package io.vertx.up.backbone.mime;

import io.horizon.exception.WebException;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;

/**
 * # 「Co」Zero Resolver
 *
 * The interface that zero provide for request content resolving for
 *
 * 1. Data Format Conversation
 * 2. Default Value Setting
 *
 * @param <T> generic type
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Resolver<T> {
    /**
     * Critical: You should call `setValue` in your code logical or you'll get null value
     *
     * @param context Vertx-web RoutingContext reference
     * @param income  Zero definition of {@link io.vertx.up.atom.Epsilon} class
     *
     * @return The same type of {@link io.vertx.up.atom.Epsilon} class
     * @throws WebException When some error occurs, throw WebException out
     */
    Epsilon<T> resolve(RoutingContext context,
                       Epsilon<T> income);
}
