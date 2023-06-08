package io.horizon.uca.boot;

import io.horizon.eon.VMessage;
import io.horizon.eon.VSpec;
import io.horizon.eon.em.app.OsType;
import io.horizon.uca.log.LogAs;
import io.horizon.util.HUt;

import java.util.Properties;
import java.util.concurrent.ConcurrentMap;

/**
 * 「内置环境变量准备」
 * 开发环境综述，用于处理开发环境专用的启动器，在 KLauncher 中会被调用，主要检查
 * .env.development 文件，初始化当前环境的系统环境变量，执行完成后处理下一步骤
 *
 * @author lang : 2023-05-30
 */
class KEnvironment {
    /**
     * 环境变量初始化验证，内置启动会被 {@link io.horizon.uca.boot.KLauncher} 调用
     */
    static void initialize() {
        /*
         * 判断是否开启了开发环境，如果开启了开发环境，那么就会读取 .env.development 文件
         * 加载文件中的环境变量到系统层（只适用于开发）
         */
        if (HUt.ioExist(VSpec.Boot._ENV_DEVELOPMENT)) {
            // 1. 环境变量设置
            final OsType os = HUt.envOs();
            LogAs.Boot.warn(KEnvironment.class, VMessage.KEnvironment.DEVELOPMENT,
                os.name(), VSpec.Boot._ENV_DEVELOPMENT);
            final Properties properties = HUt.ioProperties(VSpec.Boot._ENV_DEVELOPMENT);
            /*
             * 开发环境需要带上启动参数，否则会报错，这里是为了解决 JDK 9 以上版本的问题
             * --add-opens java.base/java.util=ALL-UNNAMED
             * --add-opens java.base/java.lang=ALL-UNNAMED
             */
            final ConcurrentMap<String, String> written = HUt.envOut(properties);


            // 2. 环境变量打印
            final String environments = HUt.envString(written);
            LogAs.Boot.info(KEnvironment.class, VMessage.KEnvironment.ENV, environments);
        }
    }
}
