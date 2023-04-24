package io.vertx.up.atom;

import io.vertx.up.eon.em.MimeFlow;
import io.vertx.up.eon.web.ID;

import java.io.Serializable;
import java.lang.annotation.Annotation;

/**
 * Parameter container to getNull parameters
 */
@SuppressWarnings("unchecked")
public class Epsilon<T> implements Serializable {

    private Object defaultValue;
    private String name;
    private MimeFlow mime;
    private Class<?> argType;
    private Annotation annotation;

    private T value;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        if (ID.DIRECT.equals(name)) {
            this.mime = MimeFlow.RESOLVER;
        } else if (ID.IGNORE.equals(name)) {
            this.mime = MimeFlow.TYPED;
        } else {
            this.mime = MimeFlow.STANDARD;
        }
        this.name = name;
    }

    public Annotation getAnnotation() {
        return this.annotation;
    }

    public void setAnnotation(final Annotation annotation) {
        this.annotation = annotation;
    }

    public Class<?> getArgType() {
        return this.argType;
    }

    public void setArgType(final Class<?> argType) {
        this.argType = argType;
    }

    public T getValue() {
        if (null == this.value) {
            if (null == this.defaultValue) {
                return null;
            } else {
                return (T) this.defaultValue;
            }
        } else {
            return this.value;
        }
    }

    public Epsilon<T> setValue(final T value) {
        this.value = value;
        return this;
    }

    public Object getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(final Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public MimeFlow getMime() {
        return this.mime;
    }

    public void setMime(final MimeFlow mime) {
        this.mime = mime;
    }

    @Override
    public String toString() {
        return "Epsilon{" +
            "name='" + this.name + '\'' +
            ", mime=" + this.mime +
            ", argType=" + this.argType +
            ", defaultValue=" + this.defaultValue +
            ", annotation=" + this.annotation +
            ", value=" + this.value +
            '}';
    }
}
