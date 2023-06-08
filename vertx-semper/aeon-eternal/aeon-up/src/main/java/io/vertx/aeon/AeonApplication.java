package io.vertx.aeon;

/**
 * 启动器
 * <p>
 * 1 - 云环境自检
 * 2 - 低代码环境对接
 * 3 - VertxApplication应用启动
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AeonApplication {

    public static void run(final Class<?> clazz, final String... args) {
        // Zero Environment Initialize
        //        ZeroAnno.configure();
        //        // Start Container
        //        new AeonApplication(clazz).run(args);
    }

    //    private Future<Boolean> configure(final HAeon aeon, final Vertx vertx) {
    //        //        final List<Future<Boolean>> futures = new ArrayList<>();
    //        //
    //        //        // HOn Processing
    //        //        final HWrapper boot = aeon.boot();
    //        //
    //        //        // HExtension -> HOn
    //        //        final HOn up = boot.starter(HOn.class, vertx);
    //        //        futures.add(up.configure(aeon));
    //        //
    //        //        return Fn.combineB(futures);
    //    }
    //
    //    protected void runInternal(final Vertx vertx, final Object... args) {
    //        final HAeon aeon = KSwitcher.aeon();
    //        // HUp 接口（启动检查）
    //        this.configure(aeon, vertx).onComplete(res -> {
    //            if (res.succeeded()) {
    //                // Aeon 启动流程（准备工作）
    //                super.runInternal(vertx, args);
    //            } else {
    //                // Aeon 启动失败
    //                final Throwable error = res.cause();
    //                if (Objects.nonNull(error)) {
    //                    error.printStackTrace();
    //                }
    //            }
    //        });
    //    }

    protected void runBefore() {
        // 优先调用父类启动流程一
        ////        super.runBefore();
        //        // 开始启动 Aeon环境
        //        final HAeon aeon = KSwitcher.aeon();
        //
        //        // Error-50001, aeon不能为null，并且必须带有plot属性
        //        Fn.out(Objects.isNull(aeon) || Objects.isNull(aeon.plot()),
        //            AeonConfigureException.class, this.upClazz);
        //
        //        // Error-50003
        //        final KPlot plot = aeon.plot();
        //        Fn.out(Ut.isNil(plot.getCloud()), AeonEnvironmentException.class, this.upClazz);
    }
}
