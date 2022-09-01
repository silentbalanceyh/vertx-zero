package io.vertx.up.runtime;

import io.vertx.core.Vertx;
import io.vertx.up.Launcher;
import io.vertx.up.annotations.Up;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.web.ZeroLauncher;
import io.vertx.up.uca.web.anima.*;
import io.vertx.up.util.Ut;
import io.vertx.zero.exception.UpClassArgsException;
import io.vertx.zero.exception.UpClassInvalidException;

import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Starter of 3 Applications
 * - VertxApplication               ( Standard )
 * - MicroApplication               ( Legacy Micro Service )
 * - AeonApplication                ( Native Cloud )
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class ZeroApplication {

    protected final Class<?> upClazz;
    protected ConcurrentMap<String, Annotation> annotationMap = new ConcurrentHashMap<>();

    protected ZeroApplication(final Class<?> clazz) {
        // Must not be null
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
        this.upClazz = clazz;
        this.annotationMap.putAll(Anno.get(clazz));
        /*
         * Zero specification definition for @Up here.
         * The input class must annotated with @Up instead of other description
         */
        Fn.out(!this.annotationMap.containsKey(Up.class.getName()), UpClassInvalidException.class, this.getClass(),
            null == this.upClazz ? null : this.upClazz.getName());
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    /*
     * 「可重写」Vertx实例启动之前的流程
     * - startEra()
     **/
    protected void runBefore() {
        ZeroArcane.startEra();
    }

    public void run(final Object... args) {
        // Check for basic preparing
        this.runBefore();
        // Execute the Vertx Running for initialized
        final Launcher<Vertx> launcher = Ut.singleton(ZeroLauncher.class);
        launcher.start(vertx -> this.runAfter(vertx, args));
    }

    /*
     * 「不可重写」Vertx实例启动之后的流程
     * - startElite
     * - startEdge
     * - startEnroll
     **/
    private void runAfter(final Vertx vertx, final Object... args) {
        // Elite
        ZeroArcane.startElite(vertx);
        // Extension
        ZeroArcane.startEdge(vertx)
            // Enroll
            .compose(nil -> ZeroArcane.startEnroll(vertx))
            .onSuccess(initialized -> {
                if (initialized) {
                    // Internal
                    this.runInternal(vertx, args);
                }
            })
            .onFailure(error -> {
                // Error Happened
                error.printStackTrace();
                this.logger().jvm(error);
            });
    }

    protected void runInternal(final Vertx vertx, final Object... args) {
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
}
