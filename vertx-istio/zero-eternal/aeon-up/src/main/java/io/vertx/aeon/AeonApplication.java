package io.vertx.aeon;

import io.vertx.aeon.atom.HSwitcher;
import io.vertx.aeon.atom.iras.HAeon;
import io.vertx.aeon.atom.iras.HBoot;
import io.vertx.aeon.component.boot.AeonOn;
import io.vertx.aeon.exception.heart.AeonConfigureException;
import io.vertx.aeon.exception.heart.ClusterRequiredException;
import io.vertx.aeon.specification.boot.HOn;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.runtime.ZeroApplication;
import io.vertx.up.runtime.ZeroHeart;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 启动器
 *
 * 1 - 云环境自检
 * 2 - 低代码环境对接
 * 3 - VertxApplication应用启动
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AeonApplication extends ZeroApplication {

    private AeonApplication(final Class<?> clazz) {
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
        // Start Container
        new AeonApplication(clazz).run(args);
    }

    private Future<Boolean> configure(final HAeon aeon, final Vertx vertx) {
        final List<Future<Boolean>> futures = new ArrayList<>();

        // HOn Processing
        final HBoot boot = aeon.boot();
        final HOn up = boot.pickOn(AeonOn.class).bind(vertx);
        futures.add(up.configure(aeon));

        return Fn.combineB(futures);
    }

    @Override
    protected void runInternal(final Vertx vertx, final Object... args) {
        final HAeon aeon = HSwitcher.aeon();
        // HUp 接口（启动检查）
        this.configure(aeon, vertx).onComplete(res -> {
            if (res.succeeded()) {
                // Aeon 启动流程（准备工作）
                super.runInternal(vertx, args);
            } else {
                // Aeon 启动失败
                final Throwable error = res.cause();
                if (Objects.nonNull(error)) {
                    error.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void ready() {
        final HAeon aeon = HSwitcher.aeon();

        // Error-50001
        Fn.out(Objects.isNull(aeon), AeonConfigureException.class, this.upClazz);
        // Error-50002
        Fn.out(!ZeroHeart.isCluster(), ClusterRequiredException.class, this.upClazz);
    }
}
