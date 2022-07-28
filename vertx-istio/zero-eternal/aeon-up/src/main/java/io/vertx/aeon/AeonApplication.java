package io.vertx.aeon;

import io.vertx.aeon.atom.HSwitcher;
import io.vertx.aeon.atom.iras.HAeon;
import io.vertx.aeon.atom.iras.HBoot;
import io.vertx.aeon.eon.HEnv;
import io.vertx.aeon.exception.heart.AeonConfigureException;
import io.vertx.aeon.exception.heart.AeonEnvironmentException;
import io.vertx.aeon.exception.heart.ClusterRequiredException;
import io.vertx.aeon.runtime.AeonEnvironment;
import io.vertx.aeon.specification.boot.HOn;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.runtime.ZeroApplication;
import io.vertx.up.runtime.ZeroHeart;
import io.vertx.up.util.Ut;

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
        // Zero Environment Initialize
        ZeroAnno.meditate();
        // Start Container
        new AeonApplication(clazz).run(args);
    }

    private Future<Boolean> configure(final HAeon aeon, final Vertx vertx) {
        final List<Future<Boolean>> futures = new ArrayList<>();

        // HOn Processing
        final HBoot boot = aeon.inBoot();

        // HBoot -> HOn
        final HOn up = boot.pick(HOn.class, vertx);
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

        AeonEnvironment.initialize(aeon);
        // Error-50003
        final String workspace = System.getenv(HEnv.ZERO_AEON);
        Fn.out(Ut.isNil(workspace), AeonEnvironmentException.class, this.upClazz);
    }
}
