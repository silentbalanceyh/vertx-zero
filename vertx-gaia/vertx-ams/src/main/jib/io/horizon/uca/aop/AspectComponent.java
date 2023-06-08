package io.horizon.uca.aop;

import io.horizon.util.HUt;
import io.modello.eon.configure.VPC;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * <pre><code>
 *  {
 *      "plugin.component.before": [],
 *      "plugin.component.job": [],
 *      "plugin.component.after": []
 *  }
 * </code></pre>
 *
 * @author lang : 2023-06-03
 */
class AspectComponent {

    private final List<Class<?>> nameBefore = new ArrayList<>();
    private final List<Class<?>> nameAfter = new ArrayList<>();
    private final List<Class<?>> nameJob = new ArrayList<>();

    AspectComponent(final JsonObject configuration) {
        // Plugin Before
        this.initialize(configuration, VPC.aop.PLUGIN_COMPONENT_BEFORE,
            this.nameBefore::add,
            clazz -> HUt.isImplement(clazz, Before.class) || HUt.isImplement(clazz, Around.class));

        // Plugin After
        this.initialize(configuration, VPC.aop.PLUGIN_COMPONENT_AFTER,
            this.nameAfter::add,
            clazz -> HUt.isImplement(clazz, After.class) || HUt.isImplement(clazz, Around.class));

        // Plugin Job
        this.initialize(configuration, VPC.aop.PLUGIN_COMPONENT_JOB,
            this.nameJob::add,
            clazz -> HUt.isImplement(clazz, After.class) || HUt.isImplement(clazz, Around.class));
    }

    private void initialize(
        final JsonObject configuration, final String field,
        final Consumer<Class<?>> fnConsumer,
        final Predicate<Class<?>> fnCheck) {
        final JsonArray source = HUt.valueJArray(configuration, field);
        HUt.itJArray(source, String.class, (item, index) -> {
            final Class<?> clazz = HUt.clazz(item, null);
            if (Objects.nonNull(clazz) && fnCheck.test(clazz)) {
                fnConsumer.accept(clazz);
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
}
