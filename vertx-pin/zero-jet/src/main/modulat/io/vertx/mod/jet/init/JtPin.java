package io.vertx.mod.jet.init;

import io.horizon.uca.boot.KPivot;
import io.horizon.uca.log.Annal;
import io.macrocosm.specification.app.HRegistry;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.mod.jet.atom.JtConfig;
import io.vertx.mod.ke.refine.Ke;

import java.util.concurrent.ConcurrentMap;

import static io.vertx.mod.jet.refine.Jt.LOG;

/*
 * Configuration of zero here
 * This extension configuration is different from other extension
 * The json config must be set in `vertx-jet.yml` or other tp extension
 */
public class JtPin implements HRegistry.Mod<Vertx> {
    private static final Annal LOGGER = Annal.get(JtPin.class);
    private static final JtConfig CONFIG = null;

    public static JtConfig getConfig() {
        return CONFIG;
    }

    public static ConcurrentMap<String, ServiceEnvironment> serviceEnvironment() {
        return JtConfiguration.serviceEnvironment();
    }

    @Override
    public Future<Boolean> registryAsync(final Vertx container, final HArk ark) {
        Ke.banner("「Πίδακας δρομολογητή」- ( Api )");
        LOG.Init.info(this.getClass(), "JtConfiguration...");
        JtConfiguration.registry(KPivot.running());
        LOG.Init.info(this.getClass(), "HAmbient Environment Start...");
        return JtConfiguration.init(container, KPivot.running());
    }
}
