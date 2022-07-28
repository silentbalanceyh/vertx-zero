package io.vertx.aeon.uca.boot;

import io.vertx.aeon.atom.iras.HAeon;
import io.vertx.aeon.atom.iras.HBoot;
import io.vertx.aeon.refine.HLog;
import io.vertx.aeon.specification.boot.HOn;
import io.vertx.aeon.specification.program.HNovae;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AeonOn implements HOn {
    private transient Vertx vertx;

    @Override
    @SuppressWarnings("unchecked")
    public HOn bind(final Vertx vertx) {
        this.vertx = vertx;
        return this;
    }

    @Override
    public Future<Boolean> configure(final HAeon aeon) {
        final HBoot boot = aeon.inBoot();
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
         * RTEAeon标识客户自有环境
         */

        // HBoot -> HNovae
        final HNovae novae = boot.pick(HNovae.class, this.vertx);
        if (Objects.isNull(novae)) {
            HLog.warnAeon(this.getClass(), "Alive components have not been defined, " +
                "Kidd/Kinect/KZero architecture has been Disabled.");
        }
        return novae.configure(aeon);
    }
}
