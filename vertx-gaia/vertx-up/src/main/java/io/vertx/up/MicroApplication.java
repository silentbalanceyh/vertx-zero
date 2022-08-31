package io.vertx.up;

import io.vertx.core.Vertx;
import io.vertx.tp.error.RpcPreparingException;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.Runner;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.runtime.ZeroApplication;
import io.vertx.up.runtime.ZeroHeart;
import io.vertx.up.uca.registry.Uddi;
import io.vertx.up.uca.registry.UddiRegistry;
import io.vertx.up.uca.web.anima.DetectScatter;
import io.vertx.up.uca.web.anima.InfixScatter;
import io.vertx.up.uca.web.anima.PointScatter;
import io.vertx.up.uca.web.anima.Scatter;
import io.vertx.up.util.Ut;

/**
 * Vertx Application begin launcher for api gateway.
 * It's only used in Micro Service mode.
 */
public class MicroApplication extends ZeroApplication {

    MicroApplication(final Class<?> clazz) {
        super(clazz);
    }

    public static void run(final Class<?> clazz, final Object... args) {
        // Zero Environment Initialize
        ZeroAnno.meditate();
        // Micro Container
        new MicroApplication(clazz).run(args);
    }

    @Override
    protected void runPre() {
        // Check etcd server status, IPC Only
        Fn.outUp(!ZeroHeart.isEtcd(), this.logger(), RpcPreparingException.class, this.getClass());
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
            final UddiRegistry registry = Uddi.registry(MicroApplication.class);
            registry.initialize(this.upClazz);
        }
    }

    @Override
    protected void runInternal(final Vertx vertx, final Object... args) {
        /* 1.Find Agent for deploy **/
        Runner.run(() -> {
            final Scatter<Vertx> scatter = Ut.singleton(PointScatter.class);
            scatter.connect(vertx);
        }, "gateway-runner");
        /* 2.Find Worker for deploy **/
        Runner.run(() -> {
            final Scatter<Vertx> scatter = Ut.singleton(DetectScatter.class);
            scatter.connect(vertx);
        }, "detect-runner");
        /* 3.Initialize Infix **/
        Runner.run(() -> {
            // Infix For Api Gateway
            final Scatter<Vertx> scatter = Ut.singleton(InfixScatter.class);
            scatter.connect(vertx);
        }, "infix-afflux-runner");
    }
}
