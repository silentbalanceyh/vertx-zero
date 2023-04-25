package io.vertx.up.atom.worker;

import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.horizon.eon.em.scheduler.RemindType;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * This worker is for @WebSocket annotation to configure the active sending message
 * for alert ( Internal WebSite Message )
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Remind implements Serializable {

    private String name;

    private RemindType type;

    private String subscribe;

    private String address;

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> input;

    private Object proxy;

    private Method method;

    private boolean secure = Boolean.FALSE;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public RemindType getType() {
        return this.type;
    }

    public void setType(final RemindType type) {
        this.type = type;
    }

    public String getSubscribe() {
        return this.subscribe;
    }

    public void setSubscribe(final String subscribe) {
        this.subscribe = subscribe;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public Class<?> getInput() {
        return this.input;
    }

    public void setInput(final Class<?> input) {
        this.input = input;
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

    public boolean isSecure() {
        return this.secure;
    }

    public void setSecure(final boolean secure) {
        this.secure = secure;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Remind remind = (Remind) o;
        return this.subscribe.equals(remind.subscribe) && this.method.equals(remind.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.subscribe, this.method);
    }

    @Override
    public String toString() {
        return "Remind{" +
            "name='" + this.name + '\'' +
            ", type=" + this.type +
            ", subscribe='" + this.subscribe + '\'' +
            ", address='" + this.address + '\'' +
            ", input=" + this.input +
            ", proxy=" + this.proxy +
            ", method=" + this.method +
            ", secure=" + this.secure +
            '}';
    }
}
