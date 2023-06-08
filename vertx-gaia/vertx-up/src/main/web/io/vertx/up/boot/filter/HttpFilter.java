package io.vertx.up.boot.filter;

import io.horizon.uca.log.Annal;
import io.vertx.core.http.Cookie;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.up.fn.Fn;
import io.vertx.zero.exception.FilterContextException;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class HttpFilter implements Filter {

    private transient final Annal logger = Annal.get(this.getClass());
    private transient RoutingContext context;

    @Override
    public void init(final RoutingContext context) {
        this.context = context;
        this.init();
    }

    protected void put(final String key, final Object value) {
        this.context.put(key, value);
    }

    @SuppressWarnings("unchecked")
    protected <T> T get(final String key) {
        final Object reference = this.context.get(key);
        return null == reference ? null : (T) reference;
    }

    protected void doNext(final HttpServerRequest request,
                          final HttpServerResponse response) {
        // If response end it means that it's not needed to move next.
        if (!response.ended()) {
            this.context.next();
        }
    }

    protected Session getSession() {
        return this.context.session();
    }

    protected Map<String, Cookie> getCookies() {
        return this.context.request()
            .cookies()
            .stream()
            .collect(Collectors.toMap(Cookie::getName, cookie -> cookie));
    }

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }

    public void init() {
        Fn.outBoot(null == this.context, this.logger, FilterContextException.class, this.getClass());
    }
}
