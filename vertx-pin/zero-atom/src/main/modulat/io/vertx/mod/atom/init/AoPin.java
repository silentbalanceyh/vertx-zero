package io.vertx.mod.atom.init;

import io.horizon.uca.boot.KPivot;
import io.macrocosm.specification.app.HRegistry;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.Vertx;
import io.vertx.mod.atom.modeling.config.AoConfig;
import io.vertx.mod.ke.refine.Ke;

import static io.vertx.mod.atom.refine.Ao.LOG;

public class AoPin implements HRegistry.Mod<Vertx> {
    public static AoConfig getConfig() {
        return AoConfiguration.getConfig();
    }

    /** 模块注册器 */
    @Override
    public Boolean registry(final Vertx container, final HArk ark) {
        Ke.banner("「διαμορφωτής」- Atom ( Ao )");
        LOG.Init.info(AoPin.class, "AoConfiguration...");
        AoConfiguration.registry(KPivot.running());
        return true;
    }
}
