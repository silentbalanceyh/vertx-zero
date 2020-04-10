package cn.vertxup.route.wall;

import io.vertx.core.VertxException;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.up.uca.web.filter.HttpFilter;
import io.vertx.up.util.Ut;

import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/*")
public class MicroFilter extends HttpFilter {
    @Override
    public void doFilter(final HttpServerRequest request,
                         final HttpServerResponse response)
            throws IOException, VertxException {
        final String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Ut.notNil(token) && token.contains(" ")) {
            final String tokenString = token.substring(token.lastIndexOf(' '));
            put("token", tokenString);
        }
    }
}
