package io.vertx.mod.is.init;

import io.horizon.uca.boot.KPivot;
import io.macrocosm.specification.app.HRegistry;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.Vertx;
import io.vertx.mod.is.atom.IsConfig;
import io.vertx.mod.ke.refine.Ke;

import static io.vertx.mod.is.refine.Is.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IsPin implements HRegistry.Mod<Vertx> {

    public static IsConfig getConfig() {
        return IsConfiguration.getConfig();
    }

    /* 模块注册器 */
    @Override
    public Boolean registry(final Vertx container, final HArk ark) {
        Ke.banner("「Ολοκλήρωση」- Integration ( Is )");
        LOG.Init.info(IsPin.class, "IsConfiguration...");
        IsConfiguration.registry(KPivot.running());
        return true;
    }
}
