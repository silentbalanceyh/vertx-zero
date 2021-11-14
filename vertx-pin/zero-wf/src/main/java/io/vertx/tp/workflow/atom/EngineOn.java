package io.vertx.tp.workflow.atom;

import cn.vertxup.workflow.domain.tables.pojos.WFlow;
import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.init.WfPin;
import io.vertx.tp.workflow.uca.component.*;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class EngineOn {
    private final transient WFlow workflow;

    private EngineOn(final WFlow workflow) {
        this.workflow = workflow;
    }

    public static EngineOn connect(final JsonObject workflow) {
        final String definitionKey = workflow.getString(KName.CODE);
        Objects.requireNonNull(definitionKey);
        /*
         * Thread pool here.
         */
        return Fn.poolThread(WfPool.POOL_ENGINE, () -> {
            final WFlow flow = WfPin.getFlow(definitionKey);
            return new EngineOn(flow);
        }, definitionKey);
    }

    public Transfer componentStart() {
        return this.component(this.workflow::getStartComponent, this.workflow.getStartConfig(), TransferEmpty::new);
    }

    public Transfer componentGenerate() {
        return this.component(this.workflow::getGenerateComponent, this.workflow.getGenerateConfig(), TransferEmpty::new);
    }

    public Movement componentRun() {
        return this.component(this.workflow::getRunComponent, this.workflow.getRunConfig(), MovementEmpty::new);
    }

    private <C extends Behaviour> C component(final Supplier<String> componentCls, final String componentValue,
                                              final Supplier<C> defaultCls) {
        final C instance;
        final Class<?> clazz = Ut.clazz(componentCls.get(), null);
        if (Objects.isNull(clazz)) {
            instance = defaultCls.get();
        } else {
            instance = Ut.instance(clazz);
        }
        if (Objects.nonNull(instance)) {
            instance.bind(Ut.toJObject(componentValue));
        }
        return instance;
    }
}
