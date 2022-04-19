package io.vertx.tp.workflow.atom;

import cn.vertxup.workflow.domain.tables.pojos.WFlow;
import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._404WorkflowNullException;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.component.*;
import io.vertx.up.experiment.specification.KFlow;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class EngineOn {
    private final transient WFlow workflow;
    private final transient MetaInstance metadata;

    private EngineOn(final WFlow workflow) {
        Objects.requireNonNull(workflow);
        this.workflow = workflow;
        this.metadata = MetaInstance.input(
            Ut.toJObject(workflow.getStartConfig()),
            Ut.toJObject(workflow.getUiLinkage())
        );
    }

    public static EngineOn connect(final String definitionKey) {
        Objects.requireNonNull(definitionKey);
        /* Thread pool here. */
        Wf.Log.infoWeb(EngineOn.class, "The system will detect `{0}` workflow.", definitionKey);
        return Fn.poolThread(WfPool.POOL_ENGINE, () -> {
            final WFlow flow = WfPin.getFlow(definitionKey);
            /* Defined Exception throw out because of configuration data */
            Fn.out(Objects.isNull(flow), _404WorkflowNullException.class, EngineOn.class, definitionKey);
            return new EngineOn(flow);
        }, definitionKey);
    }

    public static EngineOn connect(final JsonObject params) {
        final KFlow key = KFlow.build(params);
        return connect(key.definitionKey());
    }

    // ----------------------- Configured Component -------------------------
    public Transfer componentStart() {
        return this.component(this.workflow::getStartComponent,
            this.workflow.getStartConfig(),
            () -> Ut.singleton(TransferEmpty.class));
    }

    public Transfer componentGenerate() {
        return this.component(this.workflow::getGenerateComponent,
            this.workflow.getGenerateConfig(),
            this::componentGenerateStandard);
    }

    public Movement componentRun() {
        return this.component(this.workflow::getRunComponent,
            this.workflow.getRunConfig(),
            () -> Ut.singleton(MovementEmpty.class));
    }

    // ----------------------- Fixed Save -------------------------
    public Movement environmentPre() {
        return this.component(MovementPre.class, null);
    }

    public Stay stayDraft() {
        return this.component(StaySave.class, null);
    }

    public Stay stayCancel() {
        return this.component(StayCancel.class, null);
    }

    // ----------------------- Private Method -------------------------
    private Transfer componentGenerateStandard() {
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
        final String keyComponent = this.metadata.recordComponentKey(clazz, componentValue);
        return (C) Fn.poolThread(WfPool.POOL_COMPONENT, () -> {
            final C instance = Ut.instance(clazz);
            instance.bind(Ut.toJObject(componentValue))
                // Level 1, Record for Transfer
                // Level 2, Todo / Linkage for Movement
                .bind(this.metadata);
            return instance;
        }, keyComponent);       // Critical Key Pool for different record configuration
    }

    public MetaInstance metadata() {
        Objects.requireNonNull(this.metadata);
        return this.metadata;
    }
}
