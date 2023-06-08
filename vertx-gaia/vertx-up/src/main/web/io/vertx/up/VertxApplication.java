package io.vertx.up;

import io.horizon.runtime.Runner;
import io.horizon.uca.boot.KLauncher;
import io.macrocosm.specification.config.HConfig;
import io.vertx.core.Vertx;
import io.vertx.up.boot.anima.*;
import io.vertx.up.runtime.ZeroArcane;
import io.vertx.up.util.Ut;

/**
 * 标准启动器，直接启动 Vertx 实例处理 Zero 相关的业务逻辑
 */
public class VertxApplication {

    public static void run(final Class<?> clazz, final String... args) {
        // 构造启动器容器
        final KLauncher<Vertx> container = KLauncher.create(clazz, args);
        container.start((vertx, config) ->

            ZeroArcane.start(vertx, config, VertxApplication::runComponent));
    }

    private static void runComponent(final Vertx vertx, final HConfig config) {
        /* 1.Find Agent for deploy **/
        Runner.run(() -> {
            final Scatter<Vertx> scatter = Ut.singleton(AgentScatter.class);
            scatter.connect(vertx, config);
        }, "component-agent");

        /* 2.Find Worker for deploy **/
        Runner.run(() -> {
            final Scatter<Vertx> scatter = Ut.singleton(WorkerScatter.class);
            scatter.connect(vertx, config);
        }, "component-worker");

        /* 3.Initialize Infusion **/
        Runner.run(() -> {
            // Infusion
            final Scatter<Vertx> scatter = Ut.singleton(InfixScatter.class);
            scatter.connect(vertx, config);
        }, "component-infix");

        /* 4.Rule started **/
        Runner.run(() -> {
            final Scatter<Vertx> scatter = Ut.singleton(CodexScatter.class);
            scatter.connect(vertx, config);
        }, "component-codex");
    }
}
