package cn.vertxup.rbac.wall;

import io.vertx.core.VertxException;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.up.uca.web.filter.HttpFilter;
import io.vertx.up.util.Ut;

import javax.servlet.annotation.WebFilter;

@WebFilter("/*")
public class AuthenticateFilter extends HttpFilter {

    @Override
    public void doFilter(final HttpServerRequest request,
                         final HttpServerResponse response)
        throws VertxException {
        final String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Ut.notNil(token) && token.contains(" ")) {
            final String tokenString = token.substring(token.lastIndexOf(' ' ));
            /* Put Data into session instead of context */
            this.getLogger().debug("Parse token string: {0}", tokenString);
            this.put("token", tokenString);
        }
    }
}
