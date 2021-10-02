package io.vertx.up.atom.secure;

import io.vertx.up.eon.em.AuthWall;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Secure class container for special class extraction.
 * Scanned ( Metadata ) for each @Wall.
 */
public class Aegis implements Serializable, Comparable<Aegis> {
    private static final Annal LOGGER = Annal.get(Aegis.class);
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
    private ConcurrentMap<String, AegisItem> items;
    /**
     * Current wall type
     */
    private AuthWall type;
    /**
     * Proxy instance
     */
    private Object proxy;
    /**
     * User-Defined authorization
     */
    private boolean defined = false;

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

    public AuthWall getType() {
        return this.type;
    }

    public void setType(final AuthWall type) {
        this.type = type;
    }

    public Object getProxy() {
        return this.proxy;
    }

    public void setProxy(final Object proxy) {
        this.proxy = proxy;
    }

    public boolean noAuthentication() {
        return Objects.nonNull(this.proxy) && Objects.nonNull(this.authorizer.getAuthenticate());
    }

    public boolean noAuthorization() {
        return Objects.nonNull(this.proxy) && Objects.nonNull(this.authorizer.getAuthorize());
    }

    public Set<Class<?>> providers() {
        return AegisItem.configMap().values().stream()
            .map(AegisItem::getProviderAuthenticate)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    // ------------------- Extension ------------------------
    public void addItem(final String key, final AegisItem item) {
        if (AuthWall.EXTENSION == this.type) {
            this.items.put(key, item);
        } else {
            LOGGER.warn("[ Auth ] The `key` = {0} will be ignored because of the type is: `{1}`.",
                key, this.type);
        }
    }

    public ConcurrentMap<String, AegisItem> item() {
        if (AuthWall.EXTENSION == this.type) {
            return AegisItem.configMap();
        } else {
            final AuthWall wall = this.type;
            LOGGER.warn("[ Auth ] We recommend use 'item(AuthWall)' instead of item() because of the type.");
            return new ConcurrentHashMap<>() {
                {
                    this.put(wall.key(), AegisItem.configMap(wall));
                }
            };
        }
    }

    // ------------------- Native ------------------------
    public void setItem(final AegisItem item) {
        if (AuthWall.EXTENSION != this.type) {
            this.items.put(this.type.key(), item);
        } else {
            LOGGER.warn("[ Auth ] Please use `addItem` instead of current method because your type is Extension");
        }
    }

    public AegisItem item(final AuthWall wall) {
        if (AuthWall.EXTENSION != this.type) {
            return this.items.getOrDefault(wall.key(), null);
        } else {
            LOGGER.warn("[ Auth ] Please input correct native key, now = {0}", wall.key());
            return null;
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
            ", items=" + this.items +
            ", type=" + this.type +
            ", proxy=" + this.proxy +
            ", defined=" + this.defined +
            '}';
    }
}
