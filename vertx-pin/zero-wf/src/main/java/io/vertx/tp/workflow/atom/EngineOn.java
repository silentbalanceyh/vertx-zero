package io.vertx.tp.workflow.atom;

import cn.vertxup.workflow.domain.tables.pojos.WFlow;
import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.tp.workflow.uca.component.*;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class EngineOn {
    private final transient WFlow workflow;
    private final transient ConfigRecord record;

    private EngineOn(final WFlow workflow) {
        this.workflow = workflow;
        final JsonObject sure = Ut.toJObject(workflow.getStartConfig());
        this.record = Ux.fromJson(sure.getJsonObject(KName.RECORD, new JsonObject()), ConfigRecord.class);
    }

    public static EngineOn connect(final String definitionKey) {
        Objects.requireNonNull(definitionKey);
        /*
         * Thread pool here.
         */
        return Fn.poolThread(WfPool.POOL_ENGINE, () -> {
            final WFlow flow = WfPin.getFlow(definitionKey);
            return new EngineOn(flow);
        }, definitionKey);
    }

    public static EngineOn connect(final JsonObject workflow) {
        final String definitionKey = workflow.getString(KName.Flow.DEFINITION_KEY);
        return connect(definitionKey);
    }

    public Transfer componentStart() {
        return this.component(this.workflow::getStartComponent, this.workflow.getStartConfig(),
            () -> Ut.singleton(TransferEmpty.class));
    }

    public Transfer componentGenerate() {
        return this.component(this.workflow::getGenerateComponent, this.workflow.getGenerateConfig(), this::componentGenerateDefault);
    }

    public Transfer componentDraft() {
        return this.component(TransferSave.class, null);
    }

    public Movement componentRun() {
        return this.component(this.workflow::getRunComponent, this.workflow.getRunConfig(),
            () -> Ut.singleton(MovementEmpty.class));
    }

    // ----------------------- Private Method -------------------------
    private Transfer componentGenerateDefault() {
        return this.component(TransferStandard.class, this.workflow.getGenerateComponent());
    }

    private <C extends Behaviour> C component(final Supplier<String> componentCls, final String componentValue,
                                              final Supplier<C> defaultCls) {
        final Class<?> clazz = Ut.clazz(componentCls.get(), null);
        if (Objects.isNull(clazz)) {
            // Singleton Here
            return defaultCls.get();
        } else {
            // Component of Movement/Transfer
            return this.component(clazz, componentValue);
        }
    }


    @SuppressWarnings("all")
    private <C extends Behaviour> C component(final Class<?> clazz, final String componentValue) {
        final StringBuilder componentKey = new StringBuilder();
        componentKey.append(clazz.getName());
        componentKey.append(this.record.hashCode());
        if (Ut.notNil(componentValue)) {
            componentKey.append(componentValue.hashCode());
        }
        return (C) Fn.poolThread(WfPool.POOL_COMPONENT, () -> {
            final C instance = Ut.instance(clazz);
            instance.bind(Ut.toJObject(componentValue)).bind(this.record);
            return instance;
        }, componentKey.toString());       // Critical Key Pool for different record configuration
    }

    public ConfigRecord configRecord() {
        // Start Component
        return Objects.requireNonNull(this.record);
    }
}
