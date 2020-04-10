package io.vertx.up.atom.worker;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Scanned address ( Metadata ) for Queue.
 */
public class Receipt implements Serializable {
    /**
     * Event bus address.
     */
    private String address;
    /**
     * Proxy instance
     */
    private Object proxy;
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
        return this.proxy;
    }

    public void setProxy(final Object proxy) {
        this.proxy = proxy;
    }

    public Method getMethod() {
        return this.method;
    }

    public void setMethod(final Method method) {
        this.method = method;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        final Receipt receipt = (Receipt) o;

        if (!this.address.equals(receipt.address)) {
            return false;
        }
        return this.method.equals(receipt.method);
    }

    @Override
    public int hashCode() {
        int result = this.address.hashCode();
        result = 31 * result + this.method.hashCode();
        return result;
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
