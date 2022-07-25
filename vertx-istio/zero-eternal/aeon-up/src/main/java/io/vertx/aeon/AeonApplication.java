package io.vertx.aeon;

import io.vertx.aeon.atom.HSwitcher;
import io.vertx.aeon.uca.HLog;
import io.vertx.up.VertxApplication;

/**
 * 启动器
 *
 * 1 - 云环境自检
 * 2 - 低代码环境对接
 * 3 - VertxApplication应用启动
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AeonApplication {

    public static void run(final Class<?> clazz, final Object... args) {
        if (HSwitcher.isAeon()) {
            // Aeon 启动流程（准备工作）
            VertxApplication.run(clazz, args);
        } else {
            // Zero 原始流程
            HLog.warnAeon(AeonApplication.class, "Zero Cloud Environment Failed Passed, to Standard!");
            VertxApplication.run(clazz, args);
        }
    }
}
