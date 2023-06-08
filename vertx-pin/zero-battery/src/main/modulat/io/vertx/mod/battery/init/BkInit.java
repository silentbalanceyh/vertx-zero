package io.vertx.mod.battery.init;

import io.horizon.spi.modeler.ExModulat;
import io.horizon.spi.modeler.Modulat;
import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.app.HRegistry;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.unity.Ux;

import java.util.Objects;

import static io.vertx.mod.battery.refine.Bk.LOG;

public class BkInit implements HRegistry.Mod<Vertx> {

    @Override
    public Future<Boolean> registryAsync(final Vertx container, final HArk ark) {
        final HApp app = ark.app();
        final String appKey = app.appId(); // Ut.valueString(appJ, KName.KEY);
        if (Objects.isNull(appKey)) {
            LOG.Init.warn(this.getClass(), "App Id = null, ignored initialized!!");
            return Ux.futureF();
        }
        final Modulat modulat = new ExModulat();
        return modulat.extension(app.option()).compose(nil -> Ux.futureT());
    }
}
