package io.vertx.up.atom.secure;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.WallType;
import io.vertx.up.util.Ut;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * Secure class container for special class extraction.
 * Scanned ( Metadata ) for each @Wall.
 */
public class Aegis implements Serializable, Comparable<Aegis> {
    /**
     * defined = false
     * Standard Authorization
     */
    private final Against authorizer = new Against();
    /**
     * The wall path to be security limitation
     */
    private String path;
    /**
     * Current wall order
     */
    private int order;
    /**
     * Current config
     */
    private JsonObject config;
    /**
     * Current wall type
     */
    private WallType type;
    /**
     * Proxy instance
     */
    private Object proxy;
    /**
     * User-Defined authorization
     */
    private boolean defined = false;
    /**
     * Executor class
     */
    private Class<?> executor;

    public Against getAuthorizer() {
        return this.authorizer;
    }

    public boolean isDefined() {
        return this.defined;
    }

    public void setDefined(final boolean defined) {
        this.defined = defined;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(final int order) {
        this.order = order;
    }

    public JsonObject getConfig() {
        return this.config;
    }

    public void setConfig(final JsonObject config) {
        this.config = config;
    }

    public WallType getType() {
        return this.type;
    }

    public void setType(final WallType type) {
        this.type = type;
    }

    public Object getProxy() {
        return this.proxy;
    }

    public void setProxy(final Object proxy) {
        this.proxy = proxy;
    }

    public boolean okForAuthorize() {
        return Objects.nonNull(this.proxy) && Objects.nonNull(this.authorizer.getAuthenticate());
    }

    public boolean okForAccess() {
        return Objects.nonNull(this.proxy) && Objects.nonNull(this.authorizer.getAuthorize());
    }

    public Class<?> getExecutor() {
        return this.executor;
    }

    public void setExecutor(final Class<?> executor) {
        if (Void.class == executor) {
            this.executor = null;
        } else {
            this.executor = executor;
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Aegis)) {
            return false;
        }
        final Aegis wall = (Aegis) o;
        return this.order == wall.order &&
            Objects.equals(this.path, wall.path) &&
            this.type == wall.type &&
            Objects.equals(this.proxy, wall.proxy);
    }

    @Override
    public int compareTo(final @NotNull Aegis target) {
        return Ut.compareTo(this, target, (left, right) -> {
            // 1. Compare Path
            int result = Ut.compareTo(left.getPath(), right.getPath());
            if (0 == result) {
                // 2. Compare Order
                result = Ut.compareTo(left.getOrder(), right.getOrder());
            }
            return result;
        });
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.path, this.order, this.type, this.proxy);
    }

    @Override
    public String toString() {
        return "Aegis{" +
            "authorizer=" + this.authorizer +
            ", path='" + this.path + '\'' +
            ", order=" + this.order +
            ", config=" + this.config +
            ", type=" + this.type +
            ", proxy=" + this.proxy +
            ", defined=" + this.defined +
            ", executor=" + this.executor +
            '}';
    }
}
