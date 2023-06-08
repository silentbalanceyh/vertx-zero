package io.vertx.up.atom.worker;

import io.horizon.uca.cache.Cc;
import io.vertx.up.boot.di.DiPlugin;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Scanned address ( Metadata ) for Queue.
 */
public class Receipt implements Serializable {

    private static final DiPlugin PLUGIN = DiPlugin.create(Receipt.class);
    private final Cc<String, Object> cctProxy = Cc.openThread();
    /**
     * Event bus address.
     */
    private String address;
    /**
     * Proxy instance
     */
    private Class<?> proxy;
    /**
     * Consume method ( Will be calculated )
     */
    private Method method;

    public String getAddress() {
        return this.address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public Object getProxy() {
        return this.cctProxy.pick(() -> PLUGIN.createProxy(this.proxy, this.method));
    }

    public void setProxy(final Class<?> proxy) {
        this.proxy = proxy;
    }

    public Method getMethod() {
        return this.method;
    }

    public void setMethod(final Method method) {
        this.method = method;
    }

    /*
     * For new design to involve `Aeon System`, the `Receipt` object could be identified by `address` only, in this
     * kind of situation, the system could support following:
     *
     * 1. Aeon Receipt with high priority working.
     * 2. Zero Receipt with low priority working.
     *
     * It means that the system could use `Aeon System` ( Native Cloud Api ) first and then select zero
     * api, in this kind of situation the original Worker Component could be replaced by second scanning.
     *
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Receipt receipt = (Receipt) o;
        return this.address.equals(receipt.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.address);
    }

    @Override
    public String toString() {
        return "Receipt{" +
            "address='" + this.address + '\'' +
            ", proxy=" + this.proxy +
            ", method=" + this.method +
            '}';
    }
}
