package io.vertx.up;

import io.vertx.core.Vertx;
import io.vertx.tp.plugin.shared.MapInfix;
import io.vertx.up.annotations.Up;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.Anno;
import io.vertx.up.runtime.Runner;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.runtime.ZeroHeart;
import io.vertx.up.uca.registry.Uddi;
import io.vertx.up.uca.registry.UddiRegistry;
import io.vertx.up.uca.web.ZeroLauncher;
import io.vertx.up.uca.web.anima.*;
import io.vertx.up.util.Ut;
import io.vertx.zero.exception.UpClassArgsException;
import io.vertx.zero.exception.UpClassInvalidException;

import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentMap;

/**
 * Vertx Application entry
 * 1) VertxApplication: start up zero framework in `application` mode. ( Service | Application )
 * 2) MicroApplication: start up zero framework in `micro` mode. ( Service & Gateway )
 * 3) Five scanners for critical components starting up
 */
public class VertxApplication {

    private static final Annal LOGGER = Annal.get(VertxApplication.class);

    private transient final Class<?> clazz;

    private final transient ConcurrentMap<String, Annotation> annotationMap;

    private VertxApplication(final Class<?> clazz) {
        /*
         * Although the input `clazz` is not important, but zero container require the input
         * clazz mustn't be null, for future usage such as plugin extension for it.
         */
        Fn.out(null == clazz, UpClassArgsException.class, this.getClass());

        /*
         * Stored clazz information
         * 1. clazz stored
         * 2. annotation extraction from Annotation[] -> Annotation Map
         */
        this.clazz = clazz;
        this.annotationMap = Anno.get(clazz);

        /*
         * Zero specification definition for @Up here.
         * The input class must annotated with @Up instead of other description
         */

        Fn.out(!this.annotationMap.containsKey(Up.class.getName()), UpClassInvalidException.class, this.getClass(),
            null == this.clazz ? null : this.clazz.getName());
    }

    public static void run(final Class<?> clazz, final Object... args) {
        Fn.onRun(() -> {
            /*
             * Class definition predicate
             */
            ensureEtcd(clazz);

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
            ZeroAnno.prepare();

            /*
             * To avoid getPackages issue here
             * Move to InitScatter here
             */
            ZeroHeart.init();
            /*
             * Then the container could start
             */
            if (ZeroHeart.isGateway()) {
                /*
                 * Api Gateway:
                 * `Micro` mode only
                 * Current zero node will run as api gateway
                 */
                MicroApplication.run(clazz);
            } else {
                /*
                 * Standard application
                 * 1. In `Micro` mode, it will run as service node.
                 * 2. In `Standalone` mode, it will run as application.
                 */
                new VertxApplication(clazz).run(args);
            }
        }, LOGGER);
    }

    private static void ensureEtcd(final Class<?> clazz) {
        /*
         * Whether startup etcd environment
         * 1) Etcd environment depend on `vertx-micro.yml`
         * 2) If it's start up, zero container must check etcd configuration and try to connect
         * 3) Zero will initialize etcd nodes information for current micro environment.
         */
        if (ZeroHeart.isEtcd()) {
            /*
             * Zero UddiRegistry processing
             */
            final UddiRegistry registry = Uddi.registry(VertxApplication.class);
            registry.initialize(clazz);
        }
    }

    private void run(final Object... args) {

        final Launcher<Vertx> launcher = Ut.singleton(ZeroLauncher.class);
        launcher.start(vertx -> {
            if (ZeroHeart.isShared()) {
                /*
                 * Map infix initialized first to fix
                 * Boot issue here to enable map infix ( SharedMap will be used widely )
                 * It means that the MapInfix should started twice for safe usage in future
                 *
                 * In our production environment, only MapInfix plugin booting will cost some time
                 * to be ready, it may take long time to be ready after container started
                 * In this kind of situation, Zero container start up MapInfix internally first
                 * to leave more time to be prepared.
                 */
                MapInfix.init(vertx);
            }

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
        });
    }
}
