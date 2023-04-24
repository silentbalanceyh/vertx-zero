package io.vertx.aeon.uca.alive;

import io.aeon.atom.iras.HRepo;
import io.horizon.constant.em.cloud.RTEAeon;
import io.vertx.core.Future;
import io.vertx.tp.plugin.git.GitClient;
import io.vertx.tp.plugin.git.GitInfix;
import io.vertx.up.atom.Refer;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

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
        final Refer refKinect = new Refer();
        // kinect 客户端
        final HRepo zeroP = repoMap.get(RTEAeon.kinect);
        final GitClient zeroC = GitInfix.createClient(this.vertx, zeroP);
        // 连接 kzero / kidd
        return this.connect(repoMap)

            // Search Current Git And Call Pull
            .compose(nil -> zeroC.searchAsync())
            .compose(refKinect::future)
            .compose(zeroC::pullAsync)

            // Normalize Building on different directories
            .compose(nil -> this.normalize(repoMap))
            // Commit & Push
            .compose(nil -> this.finish(zeroC, refKinect.get()));
    }

    private Future<Boolean> finish(final GitClient client, final Git git) {
        final Status status = client.status(git);
        if (status.isClean()) {
            // No Changes
            return Ux.futureT();
        } else {
            // Add
            client.add(git);
            // Commit
            client.commit(git, "Git commit in configure");
            // Push
            return client.pushAsync(git, true).compose(Ux::futureT);
        }
    }

    /*
     * 代码下载
     * 1. 公库
     * 2. 私库
     * 全部下载到 /var/tmp/zero-aeon/ 工作空间中
     *
     * /kzero
     * /kidd
     * /kinect（私库）
     *
     * 如果不存在该库则     git clone
     * 如果已存在该库则     git pull
     */
    private Future<Boolean> connect(final ConcurrentMap<RTEAeon, HRepo> repoMap) {
        // kidd / kzero 下载
        final Function<RTEAeon, Future<Boolean>> connectFn = (runtime) -> {
            final HRepo zeroP = repoMap.get(runtime);
            final GitClient zeroC = GitInfix.createClient(this.vertx, zeroP);
            return zeroC.connectAsync(true).compose(nil -> Ux.futureT());
        };
        final List<Future<Boolean>> futures = new ArrayList<>();
        futures.add(connectFn.apply(RTEAeon.kzero));
        futures.add(connectFn.apply(RTEAeon.kidd));
        return Fn.combineB(futures);
    }
}
