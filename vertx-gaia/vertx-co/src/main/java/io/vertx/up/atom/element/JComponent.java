package io.vertx.up.atom.element;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonObject;
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
 * Replace the old class 'Common component yaml/json configuration'
 * - 'io.vertx.up.atom.config.ComponentOption'
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class JComponent implements Serializable {
    private static final Annal LOGGER = Annal.get(JComponent.class);

    @JsonIgnore
    private String key;

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> component;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject config = new JsonObject();

    public JComponent() {
    }


    // ---------------- 静态专用方法 -------------------
    public static JComponent create(final String key, final Class<?> component) {
        final JComponent instance = new JComponent();
        instance.key = key;
        instance.setComponent(component);
        return instance;
    }


    // ---------------- Fluent绑定方法 -------------------
    public JComponent bind(final JsonObject config) {
        this.setConfig(config);
        return this;
    }

    public JComponent bind(final String key) {
        this.key = key;
        return this;
    }

    public JComponent bind(final Class<?> component) {
        this.setComponent(component);
        return this;
    }

    // ---------------- Java Bean方法 -------------------

    public Class<?> getComponent() {
        return this.component;
    }

    public void setComponent(final Class<?> component) {
        this.component = component;
    }

    public JsonObject getConfig() {
        return this.config.copy();
    }

    public void setConfig(final JsonObject config) {
        if (Ut.isNotNil(config)) {
            this.config.mergeIn(config, true);
        }
    }

    // ---------------- 特殊API -------------------
    /* 实例化创建一个 class = component 的实例（直接实例化方便执行）*/
    public <T> T instance(final Class<?> interfaceCls, final Object... args) {
        final boolean valid = this.isImplement(interfaceCls);
        if (valid) {
            return Ut.instance(this.component, args);
        } else {
            LOGGER.warn("[ Zero ] The componentCls = {0} is not implement from `{1}`", this.component, interfaceCls);
            return null;
        }
    }

    public boolean isImplement(final Class<?> interfaceCls) {
        if (Objects.isNull(this.component)) {
            return false;
        }
        return Ut.isImplement(this.component, interfaceCls);
    }

    public String key() {
        return this.key;
    }

    /*
     * 字典管理中会使用的唯一键
     * Source Key = Component Name + Configuration
     * 1. Component Name means the same component
     * 2. Configuration means different configuration.
     * 维度：组件名 + 配置的HashCode
     */
    public String keyUnique() {
        return this.component.getName() + ":" + this.config.hashCode();
    }
}
