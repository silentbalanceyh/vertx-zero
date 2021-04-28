package io.vertx.up.commune.element;

import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

/**
 * ## New Structure
 *
 * ### 1. Intro
 *
 * Store the plugin/component information such as:
 *
 * - key: The related name ( such as field name )
 * - componentCls: The related java class here.
 * - config: The related json config of current component.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class JComponent implements Serializable {
    private static final Annal LOGGER = Annal.get(JComponent.class);

    private final transient String key;
    private final transient Class<?> componentCls;
    private final transient JsonObject config = new JsonObject();

    public JComponent(final String key, final Class<?> componentCls) {
        this.key = key;
        this.componentCls = componentCls;
    }

    public JComponent bind(final JsonObject config) {
        if (Ut.notNil(config)) {
            this.config.mergeIn(config, true);
        }
        return this;
    }

    public String key() {
        return this.key;
    }

    public <T> T instance(final Class<?> interfaceCls, final Object... args) {
        final boolean valid = this.valid(interfaceCls);
        if (valid) {
            return Ut.instance(this.componentCls, args);
        } else {
            LOGGER.warn("[ Zero ] The componentCls = {0} is not implement from `{1}`", this.componentCls, interfaceCls);
            return null;
        }
    }

    public boolean valid(final Class<?> interfaceCls) {
        if (Objects.isNull(this.componentCls)) {
            return false;
        }
        return Ut.isImplement(this.componentCls, interfaceCls);
    }

    public JsonObject config() {
        return this.config.copy();
    }

    public JsonObject configSource() {
        if (this.config.containsKey("source")) {
            final JsonObject source = this.config.getJsonObject("source");
            if (Ut.notNil(source)) {
                return source.copy();
            } else {
                return new JsonObject();
            }
        } else {
            return new JsonObject();
        }
    }

    public String source() {
        /*
         * Source Key = Component Name + Configuration
         * 1. Component Name means the same component
         * 2. Configuration means different configuration.
         */
        return this.componentCls.getName() + ":" + this.config.hashCode();
    }
}
