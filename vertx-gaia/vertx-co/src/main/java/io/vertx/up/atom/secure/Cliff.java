package io.vertx.up.atom.secure;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.WallType;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

/**
 * Secure class container for special class extraction.
 * Scanned ( Metadata ) for each @Wall.
 */
public class Cliff implements Serializable, Comparable<Cliff> {
    /**
     * defined = false
     * Standard Authorization
     */
    private final Phylum authorizer = new Phylum();
    /**
     * defined = true
     * Custom Authorization
     */
    private final Ostium authorizor = new Ostium();
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

    public Phylum getAuthorizer() {
        return this.authorizer;
    }

    public Ostium getAuthorizor() {
        return this.authorizor;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliff)) {
            return false;
        }
        final Cliff wall = (Cliff) o;
        return this.order == wall.order &&
            Objects.equals(this.path, wall.path) &&
            this.type == wall.type &&
            Objects.equals(this.proxy, wall.proxy);
    }

    @Override
    public int compareTo(final Cliff target) {
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
        return "Cliff{" +
            "path='" + this.path + '\'' +
            ", order=" + this.order +
            ", config=" + this.config +
            ", type=" + this.type +
            ", proxy=" + this.proxy +
            ", defined=" + this.defined +
            '}';
    }
}
