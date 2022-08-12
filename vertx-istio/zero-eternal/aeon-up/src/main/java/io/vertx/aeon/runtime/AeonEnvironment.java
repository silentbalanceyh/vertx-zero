package io.vertx.aeon.runtime;

import io.vertx.aeon.atom.iras.HAeon;
import io.vertx.aeon.eon.HEnv;
import io.vertx.aeon.refine.HLog;
import io.vertx.up.runtime.ZeroHeart;

import java.util.Arrays;

/**
 * 「环境变量选择器」
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AeonEnvironment {
    /*
     * 环境变量选择器：
     * 1. IDEA运行一定会选择 WORK_DIR 工作目录，该工作目录按照 ZERO 标准是当前服务运行目录
     * -- 检查该目录下是否存在 .env.development 环境变量
     * 2. 如果不存在则检查发布的容器中是否存在相关环境变量（Docker / Podman 生产注入），实则检查系统环境变量
     * 3. 上述三者都不存在则直接以 zapp.yml 中指定 `environment` 部分提取环境变量文件
     */
    public static void initialize(final HAeon aeon) {
        // 开发才会出现的环境变量
        ZeroHeart.initEnvironment();

        // 最终环境变量报表
        final StringBuilder builder = new StringBuilder();
        Arrays.stream(HEnv.REQUIRED).forEach(name -> {
            final String value = System.getenv(name);
            builder.append("\n\t").append(name).append(" = ").append(value);
        });
        HLog.infoAeon(AeonEnvironment.class, "Environment Variables: {0}\n", builder.toString());
    }
}
