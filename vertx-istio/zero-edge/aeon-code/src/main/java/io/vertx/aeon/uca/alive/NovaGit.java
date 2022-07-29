package io.vertx.aeon.uca.alive;

import io.vertx.aeon.atom.iras.HRepo;
import io.vertx.aeon.eon.em.RTEAeon;
import io.vertx.core.Future;
import io.vertx.tp.plugin.git.GitClient;
import io.vertx.tp.plugin.git.GitInfix;
import io.vertx.up.unity.Ux;

import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class NovaGit extends AbstractNova {

    @Override
    public Future<Boolean> configure(final ConcurrentMap<RTEAeon, HRepo> repoMap) {
        /*
         * 1. （公库）下载代码到 /var/tmp/zero-aeon/kzero 中
         *    （私库）下载代码到 /var/tmp/zero-aeon/kidd 中
         *    逆向检查 kinect 库的原始 uri 读取 Git 库
         * 2. （公库）更新                -- 工作空间（临时）
         *    （私库）更新                -- 工作空间（临时）
         *    （运行库）更新              -- 标准运行库
         * 3. 配置发放
         *    - 公库 /platform/<ZA_LANG>/kzero --> 运行库 /kzero（框架更新）
         *    - 模块化检查
         */
        final HRepo zeroP = repoMap.get(RTEAeon.kzero);
        final GitClient zeroC = GitInfix.createClient(this.vertx, zeroP);
        return zeroC.connectAsync().compose(nil -> Ux.futureT());
    }
}
