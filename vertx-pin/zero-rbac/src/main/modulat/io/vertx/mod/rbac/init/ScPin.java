package io.vertx.mod.rbac.init;

import io.horizon.uca.boot.KPivot;
import io.macrocosm.specification.app.HRegistry;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.Vertx;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.mod.rbac.atom.ScConfig;

import static io.vertx.mod.rbac.refine.Sc.LOG;

/*
 * Init Infusion for `initAsync` static life
 */
public class ScPin implements HRegistry.Mod<Vertx> {


    public static ScConfig getConfig() {
        return ScConfiguration.getConfig();
    }

    /* 新版模块注册器 */
    @Override
    public Boolean registry(final Vertx vertx, final HArk ark) {
        Ke.banner("「Ακριβώς」- Rbac ( Sc )");
        LOG.Init.info(ScPin.class, "ScConfiguration...");
        ScConfiguration.registry(KPivot.running());
        return Boolean.TRUE;
    }
}
