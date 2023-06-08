package io.aeon.experiment.specification;

import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/*
 * StandBy / Active
 * 1) Most situation each component should has one output
 * 2) But for plugin, here defined two output instead of single
 * 3) There are three major properties:
 *    3.1. primary: active component
 *    3.2. secondary: stand by component
 */
public class KSwitcher implements Serializable {

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private transient Class<?> primary;
    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private transient Class<?> secondary;

    public Class<?> getPrimary() {
        return this.primary;
    }

    public void setPrimary(final Class<?> primary) {
        this.primary = primary;
    }

    public Class<?> getSecondary() {
        return this.secondary;
    }

    public void setSecondary(final Class<?> secondary) {
        this.secondary = secondary;
    }

    @Override
    public String toString() {
        return "KeSwitcher{" +
            "primary=" + this.primary +
            ", secondary=" + this.secondary +
            '}';
    }
}
