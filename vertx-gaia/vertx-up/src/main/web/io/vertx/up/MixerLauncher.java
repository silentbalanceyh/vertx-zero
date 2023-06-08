package io.vertx.up;

import io.horizon.eon.em.EmApp;
import io.horizon.specification.boot.HLauncher;
import io.macrocosm.specification.boot.HOff;
import io.macrocosm.specification.boot.HOn;
import io.macrocosm.specification.config.HBoot;
import io.macrocosm.specification.config.HConfig;
import io.vertx.core.Vertx;
import io.vertx.up.configuration.BootStore;
import io.vertx.up.util.Ut;

import java.util.function.Consumer;

/**
 * @author lang : 2023-05-30
 */
public class MixerLauncher implements HLauncher<Vertx> {
    private static final BootStore STORE = BootStore.singleton();
    private transient final HLauncher<Vertx> micro;
    private transient final HLauncher<Vertx> zero;

    public MixerLauncher() {
        this.micro = Ut.singleton(MicroLauncher.class);
        this.zero = Ut.singleton(ZeroLauncher.class);
    }

    @Override
    public <T extends HConfig> void start(final HOn<T> on, final Consumer<Vertx> server) {
        final HBoot boot = STORE.boot();
        if (EmApp.Type.APPLICATION == boot.app()) {
            this.zero.start(on, server);
        } else {
            this.micro.start(on, server);
        }
    }

    @Override
    public <T extends HConfig> void stop(final HOff<T> off, final Consumer<Vertx> server) {
        final HBoot boot = STORE.boot();
        if (EmApp.Type.APPLICATION == boot.app()) {
            this.zero.stop(off, server);
        } else {
            this.micro.stop(off, server);
        }
    }
}
