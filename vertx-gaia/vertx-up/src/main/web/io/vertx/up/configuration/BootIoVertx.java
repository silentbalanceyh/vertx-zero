package io.vertx.up.configuration;

import io.horizon.specification.boot.HLauncher;
import io.horizon.spi.BootIo;
import io.macrocosm.specification.config.HBoot;
import io.macrocosm.specification.config.HEnergy;
import io.vertx.up.MixerLauncher;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author lang : 2023-05-30
 */
public class BootIoVertx implements BootIo {

    @Override
    public <T> HLauncher<T> launcher() {
        /*
         * 混合启动器，可开启两种启动模式
         * 1. Micro：微服务模式
         * 2. Zero：单机模式
         */
        final BootStore store = BootStore.singleton();
        final Class<?> launcher = store.boot().launcher();
        return Ut.singleton(Objects.isNull(launcher) ? MixerLauncher.class : launcher);
    }

    @Override
    public HEnergy energy(final Class<?> target, final String[] args) {
        // 内置实现配置，Vertx直接从本身实现中处理
        final BootStore store = BootStore.singleton(target, args);
        // 内置绑定
        final HBoot boot = store.boot();
        return boot.energy();
    }
}
