package io.vertx.up;

import io.vertx.core.Vertx;
import io.vertx.up.runtime.Runner;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.runtime.ZeroApplication;
import io.vertx.up.runtime.ZeroHeart;
import io.vertx.up.uca.web.anima.*;
import io.vertx.up.util.Ut;

/**
 * Vertx Application entry
 * 1) VertxApplication: start up zero framework in `application` mode. ( Service | Application )
 * 2) MicroApplication: start up zero framework in `micro` mode. ( Service & Gateway )
 * 3) Five scanners for critical components starting up
 */
public class VertxApplication extends ZeroApplication {

    private VertxApplication(final Class<?> clazz) {
        super(clazz);
    }

    public static void run(final Class<?> clazz, final Object... args) {
        /*
         * Before launcher, start package scanning for preparing metadata
         * This step is critical because it's environment core preparing steps.
         * 1) Before vert.x started, the system must be scanned all to capture some metadata classes instead.
         * 2) PackScan will scan all classes to capture Annotation information
         * 3) For zero extension module, although it's not in ClassLoader, we also need to scan dependency library
         *    to capture zero extension module annotation
         *
         * Because static {} initializing will be triggered when `ZeroAnno` is called first time, to avoid
         * some preparing failure, here we replaced `static {}` with `prepare()` calling before any instance
         * of VertxApplication/MicroApplication.
         */
        // Zero Environment Initialize
        ZeroAnno.meditate();
        /*
         * Then the container could start
         */
        if (ZeroHeart.isGateway()) {
            /*
             * Api Gateway:
             * `Micro` mode only
             * Current zero node will run as api gateway
             */
            // Run vertx application.
            new MicroApplication(clazz).run(args);
        } else {
            /*
             * Standard application
             * 1. In `Micro` mode, it will run as service node.
             * 2. In `Standalone` mode, it will run as application.
             */
            new VertxApplication(clazz).run(args);
        }
    }

    @Override
    protected void ready() {
    }

    @Override
    protected void runInternal(final Vertx vertx, final Object... args) {
        /*
         * Async initializing to replace the original extension
         * Data initializing
         */
        ZeroHeart.initExtension(vertx).onSuccess(res -> {
            if (res) {
                /* 1.Find Agent for deploy **/
                Runner.run(() -> {
                    final Scatter<Vertx> scatter = Ut.singleton(AgentScatter.class);
                    scatter.connect(vertx);
                }, "agent-runner");

                /* 2.Find Worker for deploy **/
                Runner.run(() -> {
                    final Scatter<Vertx> scatter = Ut.singleton(WorkerScatter.class);
                    scatter.connect(vertx);
                }, "worker-runner");

                /* 3.Initialize Infix **/
                Runner.run(() -> {
                    // Infix
                    final Scatter<Vertx> scatter = Ut.singleton(InfixScatter.class);
                    scatter.connect(vertx);
                }, "infix-afflux-runner");

                /* 4.Rule started **/
                Runner.run(() -> {
                    final Scatter<Vertx> scatter = Ut.singleton(CodexScatter.class);
                    scatter.connect(vertx);
                }, "codex-engine-runner");
            }
        }).onFailure(error -> {
            // Error Happened
            error.printStackTrace();
            this.logger().jvm(error);
        });
    }
}
