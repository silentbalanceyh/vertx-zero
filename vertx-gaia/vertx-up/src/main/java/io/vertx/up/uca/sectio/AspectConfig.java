package io.vertx.up.uca.sectio;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * The configuration data structure:
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AspectConfig implements Serializable {
    /*
     *  {
     *      "components": {
     *          "plugin.component.before": [],
     *          "plugin.component.job": [],
     *          "plugin.component.after": [],
     *          "plugin.config": {
     *          }
     *      }
     *  }
     */
    private final ConcurrentMap<Class<?>, JsonObject> configMap = new ConcurrentHashMap<>();

    private final List<Class<?>> nameBefore = new ArrayList<>();
    private final List<Class<?>> nameAfter = new ArrayList<>();
    private final List<Class<?>> nameJob = new ArrayList<>();

    private AspectConfig(final JsonObject components) {
        // Plugin Before
        this.initialize(components, KName.Aop.COMPONENT_BEFORE, (clazz, config) -> {
            this.nameBefore.add(clazz);
            this.configMap.put(clazz, config);
        }, clazz -> Ut.isImplement(clazz, Before.class) || Ut.isImplement(clazz, Around.class));

        // Plugin After
        this.initialize(components, KName.Aop.COMPONENT_AFTER, (clazz, config) -> {
            this.nameAfter.add(clazz);
            this.configMap.put(clazz, config);
        }, clazz -> Ut.isImplement(clazz, After.class) || Ut.isImplement(clazz, Around.class));

        // Plugin Job
        this.initialize(components, KName.Aop.COMPONENT_JOB, (clazz, config) -> {
            this.nameJob.add(clazz);
            this.configMap.put(clazz, config);
        }, clazz -> Ut.isImplement(clazz, After.class) || Ut.isImplement(clazz, Around.class));
    }

    public static AspectConfig create(final JsonObject components) {
        return new AspectConfig(components);
    }

    private void initialize(
        final JsonObject configuration, final String field,
        final BiConsumer<Class<?>, JsonObject> fnConsumer,
        final Predicate<Class<?>> fnCheck) {
        final JsonArray source = Ut.valueJArray(configuration, field);
        Ut.itJArray(source, String.class, (item, index) -> {
            final Class<?> clazz = Ut.clazz(item, null);
            if (Objects.nonNull(clazz) && fnCheck.test(clazz)) {
                final JsonObject config = Ut.valueJObject(configuration, KName.Aop.COMPONENT_CONFIG);
                final JsonObject valueJ = Ut.valueJObject(config, clazz.getName());
                fnConsumer.accept(clazz, valueJ);
            }
        });
    }

    public List<Class<?>> nameBefore() {
        return this.nameBefore;
    }

    public List<Class<?>> nameAfter() {
        return this.nameAfter;
    }

    public List<Class<?>> nameJob() {
        return this.nameJob;
    }

    public JsonObject config(final Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return this.configMap.getOrDefault(clazz, new JsonObject());
    }
}
