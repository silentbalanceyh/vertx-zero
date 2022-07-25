package io.vertx.start;

import io.vertx.aeon.AeonApplication;
import io.vertx.up.annotations.Up;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Up
public class AeonAgent {
    public static void main(final String[] args) {
        // 切换启动器
        AeonApplication.run(AeonAgent.class);
    }
}
