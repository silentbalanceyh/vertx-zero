package io.vertx.aeon.uca.alive;

import io.vertx.aeon.atom.iras.HAeon;
import io.vertx.core.Future;

/**
 * 代码库的链接过程
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AeonNovae extends AbstractNovae {

    @Override
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
        return null;
    }
}
