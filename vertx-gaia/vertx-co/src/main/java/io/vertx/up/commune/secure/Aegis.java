package io.vertx.up.commune.secure;

import io.horizon.eon.VValue;
import io.horizon.specification.typed.TCopy;
import io.horizon.uca.log.Annal;
import io.vertx.up.eon.em.EmSecure;
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
public class Aegis implements Serializable, Comparable<Aegis>, TCopy<Aegis> {
    private static final Annal LOGGER = Annal.get(Aegis.class);
    /**
     * defined = false
     * Standard Authorization
     */
    private final Against authorizer = new Against();
    /**
     * Current config
     */
    private final ConcurrentMap<String, AegisItem> items = new ConcurrentHashMap<>();
    /**
     * The wall path to be security limitation
     */
    private String path;
    /**
     * Current wall order
     */
    private int order;
    /**
     * Current wall type
     */
    private EmSecure.AuthWall type;
    /**
     * Proxy instance
     */
    private Object proxy;

    private Class<?> handler;
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

    public EmSecure.AuthWall getType() {
        return this.type;
    }

    public Aegis setType(final EmSecure.AuthWall type) {
        this.type = type;
        return this;
    }

    public Object getProxy() {
        return this.proxy;
    }

    public void setProxy(final Object proxy) {
        this.proxy = proxy;
    }

    public boolean noAuthentication() {
        return Objects.isNull(this.proxy) || Objects.isNull(this.authorizer.getAuthenticate());
    }

    public boolean noAuthorization() {
        return Objects.isNull(this.proxy) || Objects.isNull(this.authorizer.getAuthorization());
    }

    public Set<Class<?>> providers() {
        return AegisItem.configMap().values().stream()
            .map(AegisItem::getProviderAuthenticate)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    public Class<?> getHandler() {
        return Void.class == this.handler ? null : this.handler;
    }

    public void setHandler(final Class<?> handler) {
        this.handler = handler;
    }

    // ------------------- Extension ------------------------
    public void addItem(final String key, final AegisItem item) {
        if (EmSecure.AuthWall.EXTENSION == this.type) {
            this.items.put(key, item);
        } else {
            LOGGER.warn("[ Auth ] The `key` = {0} will be ignored because of the type is: `{1}`.",
                key, this.type);
        }
    }

    public ConcurrentMap<String, AegisItem> items() {
        if (EmSecure.AuthWall.EXTENSION == this.type) {
            return AegisItem.configMap();
        } else {
            final EmSecure.AuthWall wall = this.type;
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
        if (EmSecure.AuthWall.EXTENSION != this.type) {
            this.items.put(this.type.key(), item);
        } else {
            LOGGER.warn("[ Auth ] Please use `addItem` instead of current method because your type is Extension");
        }
    }

    public AegisItem item() {
        final EmSecure.AuthWall wall = this.type;
        if (EmSecure.AuthWall.EXTENSION != wall) {
            return this.items.getOrDefault(wall.key(), null);
        } else {
            // Smart Analyzing
            if (VValue.ONE == this.items.size()) {
                // Size = 1, Return the unique one here
                return this.items.values().iterator().next();
            } else {
                LOGGER.warn("[ Auth ] Please input correct native key, now = {0}", wall.key());
                return null;
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <CHILD extends Aegis> CHILD copy() {
        final Aegis aegis = new Aegis();
        // Final
        aegis.authorizer.setResource(this.authorizer.getResource());
        aegis.authorizer.setAuthorization(this.authorizer.getAuthorization());
        aegis.authorizer.setAuthenticate(this.authorizer.getAuthenticate());
        aegis.authorizer.setUser(this.authorizer.getUser());
        // Reference
        aegis.handler = this.handler;
        aegis.items.putAll(this.items);
        aegis.proxy = this.proxy;
        // Basic
        aegis.defined = this.defined;
        aegis.order = this.order;
        aegis.path = this.path;
        aegis.type = this.type;
        return (CHILD) aegis;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final Aegis wall)) {
            return false;
        }
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
            ", handler=" + this.handler +
            '}';
    }
}
