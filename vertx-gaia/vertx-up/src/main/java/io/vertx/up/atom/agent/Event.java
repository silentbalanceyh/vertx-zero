package io.vertx.up.atom.agent;

import io.horizon.eon.VString;
import io.horizon.uca.cache.Cc;
import io.vertx.core.http.HttpMethod;
import io.vertx.up.boot.di.DiPlugin;
import io.vertx.up.eon.KWeb;
import jakarta.ws.rs.core.MediaType;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

/**
 * Scanned Uri Event ( Metadata ) for each Endpoint.
 */
public class Event implements Serializable {

    private static final DiPlugin PLUGIN = DiPlugin.create(Event.class);
    private final Cc<String, Object> cctProxy = Cc.openThread();
    /**
     * The uri address for current route
     */
    private String path;
    /**
     * order for current Event
     * It could be modified in latest version by @Adjust
     */
    private int order = KWeb.ORDER.EVENT;
    /**
     * consume mime
     */
    private Set<MediaType> consumes;
    /**
     * produce mime
     */
    private Set<MediaType> produces;
    /**
     * http method.
     */
    private HttpMethod method;
    /**
     * request action ( Will be calculated )
     */
    private Method action;
    /**
     * Proxy instance
     */
    private Class<?> proxy;

    public String getPath() {
        return this.path;
    }

    public void setPath(final String path) {
        if (null != path) {
            final String literal = path.trim();
            if (literal.endsWith(VString.SLASH)) {
                this.path = literal.substring(0, literal.length() - 1);
            } else {
                this.path = literal;
            }
        } else {
            this.path = path;
        }
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(final int order) {
        this.order = order;
    }

    public Set<MediaType> getConsumes() {
        return this.consumes;
    }

    public void setConsumes(final Set<MediaType> consumes) {
        this.consumes = consumes;
    }

    public Set<MediaType> getProduces() {
        return this.produces;
    }

    public void setProduces(final Set<MediaType> produces) {
        this.produces = produces;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public void setMethod(final HttpMethod method) {
        this.method = method;
    }

    public Method getAction() {
        return this.action;
    }

    public void setAction(final Method action) {
        this.action = action;
    }

    public Object getProxy() {
        return this.cctProxy.pick(() -> PLUGIN.createProxy(this.proxy, this.action));
    }

    public void setProxy(final Class<?> proxy) {
        this.proxy = proxy;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final Event event)) {
            return false;
        }
        return this.order == event.order &&
            Objects.equals(this.path, event.path) &&
            this.method == event.method;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.path, this.order, this.method);
    }

    @Override
    public String toString() {
        return "Event{" +
            "path='" + this.path + '\'' +
            ", order=" + this.order +
            ", consumes=" + this.consumes +
            ", produces=" + this.produces +
            ", method=" + this.method +
            ", action=" + this.action +
            ", proxy=" + this.proxy +
            '}';
    }
}
