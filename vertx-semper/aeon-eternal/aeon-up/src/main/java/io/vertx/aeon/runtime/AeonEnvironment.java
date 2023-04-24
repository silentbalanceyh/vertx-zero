package io.vertx.aeon.runtime;

import io.aeon.atom.iras.HAeon;
import io.aeon.refine.HLog;
import io.vertx.up.runtime.env.Macrocosm;

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
        // 最终环境变量报表
        final String content = Macrocosm.envContent();
        HLog.infoAeon(AeonEnvironment.class, "Aeon Environment Variables: {0}\n", content);
    }
}
