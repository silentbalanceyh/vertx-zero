package io.vertx.up.uca.rs.argument;

import io.vertx.core.http.Cookie;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.uca.rs.Filler;

import java.util.Objects;

/**
 * 「Co」JSR311 for .@CookieParam
 *
 * This `Filler` is for cookie attributes extracting
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CookieFiller implements Filler {
    @Override
    public Object apply(final String name,
                        final Class<?> paramType,
                        final RoutingContext context) {
        if (Cookie.class.isAssignableFrom(paramType)) {
            /*
             * Declared `Cookie` directly
             */
            return context.getCookie(name);
        } else {
            /*
             * Declared other type
             */
            final Cookie cookie = context.getCookie(name);
            return Objects.isNull(cookie) ? null : cookie.getValue();
        }
    }
}
