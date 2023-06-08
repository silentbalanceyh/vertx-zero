package io.vertx.aeon.uca.alive;

import io.aeon.eon.HPath;
import io.horizon.specification.storage.HFS;
import io.macrocosm.atom.boot.KRepo;
import io.macrocosm.eon.em.EmCloud;
import io.macrocosm.specification.nc.HAeon;
import io.macrocosm.specification.nc.HWrapper;
import io.macrocosm.specification.program.HNova;
import io.vertx.core.Future;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentMap;

/**
 * 代码库的链接过程
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AeonNovae extends AbstractNovae {

    @Override
    @SuppressWarnings("all")
    public Future<Boolean> configure(final HAeon aeon) {
        /*
         * 三个环境的相互交互
         * kidd     -> 生产环境
         * kinect   -> 开发环境
         * kzero    -> 默认环境（出厂设置）
         * 操作步骤：
         * -- CGit, vertx-zero-cloud 库
         * -- AGit, xxx-cloud 库
         * -- ADir, xxx-cloud 工作目录（本地或存储空间上）
         *
         * 统一步骤说明：
         * 1）检查 kzero 中的 path 定义目录，假设：/runtime，计算最终目录：/runtime/kzero
         * -- 直接从 vertx-zero-cloud 拉取，目录为：        /platform/cn/kzero
         * -- （默认）直接提取环境变量 ZERO_AEON 中的数据
         * 2）从 uri 定义中的值拉取最新版代码，并将公有云中 /platform/$ZA_LANG/kzero 的内容拷贝到目标 path 中同步
         * -- （环境变量）语言使用 ZA_LANG
         *
         * 3）使用同样的方式初始化 kinect（合并模式），只是环境变量切换成 ZK_APP -> ZERO_AEON，ZK_APP 默认是 ZERO_AEON
         * 4）将合并结果拷贝到    kidd  （生产环境）
         * 5）验证 .git 仓库是合法的，并且已经是"最新的"
         */

        configureRuntime(aeon);
        /*
         * 注：此处的执行操作是配置，不做初始化动作，以检查为主，初始化会有另外的接口来执行
         * 1. 初始化工作目录，并保证工作目录中的内容是最新
         * 2. 拷贝配置并执行
         * 3. 检查最终配置
         * 4. 执行配置封存（Git提交）
         *
         *          kzero                kidd                kinect
         * 第一步      o                   o                    x
         * 第二步
         */
        final ConcurrentMap<EmCloud.Runtime, KRepo> repo = aeon.repo();
        final HWrapper boot = aeon.boot();
        final HNova nova = boot.starter(HNova.class, this.vertx);
        return nova.configure(repo);
    }


    private void configureRuntime(final HAeon aeon) {
        // 工作目录：/var/tmp/zero-aeon/
        final HFS fs = HFS.common();
        fs.mkdir(aeon.workspace());


        // 提取 Kinect 运行库
        final KRepo repo = aeon.repo(EmCloud.Runtime.kinect);
        // 构造默认目录
        final String name = aeon.name();
        // Build
        final String mod = Ut.ioPath(repo.getPath(), HPath.KIND);
        final String modPath = Ut.ioPath(mod, name);

        // （保留）模块配置目录：ZK_APP/kind/<aeon/name>/
        fs.mkdir(modPath);
    }
}
