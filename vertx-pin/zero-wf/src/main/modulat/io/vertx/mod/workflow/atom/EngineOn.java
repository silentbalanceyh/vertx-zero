package io.vertx.mod.workflow.atom;

import cn.vertxup.workflow.cv.WfPool;
import cn.vertxup.workflow.domain.tables.pojos.WFlow;
import io.vertx.mod.workflow.atom.configuration.MetaInstance;
import io.vertx.mod.workflow.atom.runtime.WRequest;
import io.vertx.mod.workflow.error._404WorkflowNullException;
import io.vertx.mod.workflow.init.WfPin;
import io.vertx.mod.workflow.uca.central.Behaviour;
import io.vertx.mod.workflow.uca.coadjutor.Stay;
import io.vertx.mod.workflow.uca.coadjutor.StayCancel;
import io.vertx.mod.workflow.uca.coadjutor.StayClose;
import io.vertx.mod.workflow.uca.coadjutor.StaySave;
import io.vertx.mod.workflow.uca.component.*;
import io.vertx.up.atom.extension.KFlow;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vertx.mod.workflow.refine.Wf.LOG;

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
        LOG.Web.info(EngineOn.class, "The system will detect `{0}` workflow.", definitionKey);
        return WfPool.CC_ENGINE.pick(() -> {
            final WFlow flow = WfPin.getFlow(definitionKey);
            /* Defined Exception throw out because of configuration data */
            Fn.out(Objects.isNull(flow), _404WorkflowNullException.class, EngineOn.class, definitionKey);
            return new EngineOn(flow);
        }, definitionKey);
    }

    public static EngineOn connect(final WRequest request) {
        final KFlow key = request.workflow();
        /*
         * {
         *     "definitionKey": "",
         *     "definitionId": "",
         *     "instanceId": "",
         *     "taskId": ""
         * }
         */
        return connect(key.definitionKey());
    }

    /*
     * Here are EngineOn structure for component management, each item refer to
     * one RESTful api here.
     *
     * 1. /up/flow/start
     *    1.1) 「Movement」runComponent       ->        ( MovementEmpty )
     *          componentRun()
     *    1.2) 「Transfer」startComponent     ->        ( TransferEmpty )
     *          componentStart()
     *
     * 2. /up/flow/saving
     *    2.1) 「Movement」( Fixed )          ->        ( MovementStay )
     *          stayMovement()
     *    2.2) 「Stay」    ( Fixed )          ->        ( StaySave )
     *          stayDraft()
     *
     * 3. /up/flow/complete
     *    3.1) 「Movement」runComponent       ->        ( MovementEmpty )
     *          componentRun()
     *    3.2) 「Transfer」generateComponent  ->        ( TransferStandard )
     *          componentGenerate()
     *
     * 4. /up/flow/cancel
     *    4.1) 「Movement」( Fixed )          ->        ( MovementStay )
     *          stayMovement()
     *    4.2) 「Stay」    ( Fixed )          ->        ( StayCancel )
     *          stayCancel()
     *
     * 5. /up/flow/close
     *    5.1) 「Movement」( Fixed )          ->        ( MovementStay )
     *          stayMovement()
     *    5.2) 「Stay」    ( Fixed )          ->        ( StayClose )
     *          stayClose()
     *
     * For different mode usage here
     *    <MODE>        runComponent            generateComponent           startComponent
     *
     * - Standard       MovementNext            TransferStandard            TransferStart
     * - Fork/Join      MovementForkNext        TransferForkStandard        TransferForkStart
     * - Multi          MovementMultiNext       TransferMultiStandard       TransferMultiStart
     */
    // ----------------------- Configured Component -------------------------
    public Transfer componentStart() {
        return this.component(this.workflow::getStartComponent,
            this.workflow.getStartConfig(),
            () -> Ut.singleton(TransferEmpty.class));
    }

    public Transfer componentGenerate() {
        return this.component(this.workflow::getGenerateComponent,
            this.workflow.getGenerateConfig(),
            this::transferStandard);
    }

    public Movement componentRun() {
        return this.component(this.workflow::getRunComponent,
            this.workflow.getRunConfig(),
            () -> Ut.singleton(MovementEmpty.class));
    }

    // ----------------------- Fixed Save -------------------------
    public Movement stayMovement() {
        return this.component(MovementStay.class, null);
    }

    public Stay stayDraft() {
        return this.component(StaySave.class, null);
    }

    public Stay stayCancel() {
        return this.component(StayCancel.class, null);
    }

    public Stay stayClose() {
        return this.component(StayClose.class, null);
    }

    // ----------------------- Private Method -------------------------
    private Transfer transferStandard() {
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
        /*
         * Here are component pool based on keyComponent, the format is as following:
         * className + ConfigRecord ( hashCode ) + componentValue ( hashCode Optional )
         * - authorizedComponent
         *   authorized on user to check who could do actions
         *
         * - generateComponent
         *   close previous todo record / open new todo record
         *
         * - runComponent
         *   run current todo record / processing
         *
         * - startComponent
         *   start new workflow and generate new start todo record
         *
         * - endComponent
         *   end workflow of closing ticket
         */
        return (C) WfPool.CC_COMPONENT.pick(() -> {
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
