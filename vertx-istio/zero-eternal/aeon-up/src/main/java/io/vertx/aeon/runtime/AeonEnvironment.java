package io.vertx.aeon.runtime;

import io.vertx.aeon.atom.iras.HAeon;
import io.vertx.aeon.eon.HEnv;
import io.vertx.aeon.eon.HPath;
import io.vertx.aeon.eon.em.TypeOs;
import io.vertx.aeon.refine.HLog;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

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
        final TypeOs osType = TypeOs.from(System.getProperty("os.name"));
        // 开发才会出现的环境变量
        initialize(osType);

        // 最终环境变量报表
        final StringBuilder builder = new StringBuilder();
        Arrays.stream(HEnv.REQUIRED).forEach(name -> {
            final String value = System.getenv(name);
            builder.append("\n\t").append(name).append(" = ").append(value);
        });
        HLog.infoAeon(AeonEnvironment.class, "Environment Variables: {0}\n", builder.toString());
    }

    @SuppressWarnings("all")
    private static void initialize(final TypeOs osType) {
        Fn.safeJvm(() -> {
            if (Ut.ioExist(HPath.ENV_DEVELOPMENT)) {
                HLog.warnAeon(AeonEnvironment.class, "OS = {0},  `{1}` file has been found! DEVELOPMENT connected.",
                    osType.name(), HPath.ENV_DEVELOPMENT);
                if (TypeOs.MAC_OS == osType) {
                    final Properties properties = Ut.ioProperties(HPath.ENV_DEVELOPMENT);
                    final Enumeration<String> it = (Enumeration<String>) properties.propertyNames();
                    while (it.hasMoreElements()) {
                        final String key = it.nextElement();
                        final String value = properties.getProperty(key);
                        // .env.development （环境变量黑科技）
                        final Map<String, String> env = System.getenv();
                        final Field field = env.getClass().getDeclaredField("m");
                        field.setAccessible(true);
                        ((Map<String, String>) field.get(env)).put(key, value);
                    }
                }
            }
        });
    }
}
