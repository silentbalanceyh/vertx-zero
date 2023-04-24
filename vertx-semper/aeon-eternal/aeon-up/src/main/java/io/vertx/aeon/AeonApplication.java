package io.vertx.aeon;

import io.aeon.atom.HSwitcher;
import io.aeon.atom.config.HPlot;
import io.aeon.atom.iras.HAeon;
import io.aeon.atom.iras.HBoot;
import io.aeon.exception.heart.AeonConfigureException;
import io.aeon.exception.heart.AeonEnvironmentException;
import io.aeon.specification.boot.HOn;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.runtime.ZeroApplication;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 启动器
 * <p>
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
    protected void runBefore() {
        // 优先调用父类启动流程一
        super.runBefore();
        // 开始启动 Aeon环境
        final HAeon aeon = HSwitcher.aeon();

        // Error-50001, aeon不能为null，并且必须带有plot属性
        Fn.out(Objects.isNull(aeon) || Objects.isNull(aeon.inPlot()),
                AeonConfigureException.class, this.upClazz);

        // Error-50003
        final HPlot plot = aeon.inPlot();
        Fn.out(Ut.isNil(plot.getCloud()), AeonEnvironmentException.class, this.upClazz);
    }
}
