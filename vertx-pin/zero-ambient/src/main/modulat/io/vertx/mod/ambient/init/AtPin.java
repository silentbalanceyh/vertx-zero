package io.vertx.mod.ambient.init;

import io.horizon.spi.extension.Init;
import io.horizon.spi.extension.Prerequisite;
import io.horizon.uca.boot.KPivot;
import io.macrocosm.specification.app.HRegistry;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.Vertx;
import io.vertx.mod.ambient.atom.AtConfig;
import io.vertx.mod.ke.refine.Ke;

import static io.vertx.mod.ambient.refine.At.LOG;

public class AtPin implements HRegistry.Mod<Vertx> {

    public static AtConfig getConfig() {
        return AtConfiguration.getConfig();
    }

    public static Init getInit() {
        return AtConfiguration.getInit(getConfig().getInitializer());
    }

    public static Init getLoader() {
        return AtConfiguration.getInit(getConfig().getLoader());
    }

    public static Prerequisite getPrerequisite() {
        return AtConfiguration.getPrerequisite();
    }

    /* 新版模块注册器 */
    @Override
    public Boolean registry(final Vertx vertx, final HArk ark) {
        Ke.banner("「περιβάλλων」- Ambient ( At )");
        LOG.Init.info(AtPin.class, "AtConfiguration...");
        AtConfiguration.registry(KPivot.running());
        return Boolean.TRUE;
    }
}
