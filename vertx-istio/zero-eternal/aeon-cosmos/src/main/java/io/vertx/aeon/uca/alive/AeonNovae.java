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
         * Type = MIN（公共模式）
         * -- 在这种模式下，
         */
        return null;
    }
}
