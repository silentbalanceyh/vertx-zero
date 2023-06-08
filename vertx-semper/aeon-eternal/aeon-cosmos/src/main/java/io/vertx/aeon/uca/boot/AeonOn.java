package io.vertx.aeon.uca.boot;

import io.horizon.specification.typed.TEvent;
import io.macrocosm.specification.nc.HAeon;
import io.macrocosm.specification.nc.HWrapper;
import io.macrocosm.specification.program.HNovae;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.unity.Ux;

import java.util.Objects;

import static io.aeon.refine.Ho.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AeonOn implements TEvent<HAeon, Boolean> {
    private transient Vertx vertx;

    @Override
    public AeonOn bind(final Vertx vertx) {
        this.vertx = vertx;
        return this;
    }

    @Override
    public Future<Boolean> configure(final HAeon aeon) {
        final HWrapper boot = aeon.boot();
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

        // HExtension -> HNovae
        final HNovae novae = boot.starter(HNovae.class, this.vertx);
        if (Objects.isNull(novae)) {
            LOG.Aeon.warn(this.getClass(), "Alive components have not been defined, " +
                "Kidd/Kinect/KZero architecture has been Disabled.");
            return Ux.futureF();
        } else {
            return novae.configure(aeon);
        }
    }
}
