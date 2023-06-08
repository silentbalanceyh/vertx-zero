package io.vertx.mod.ambient.init;

import cn.vertxup.ambient.service.file.DocBStub;
import cn.vertxup.ambient.service.file.DocBuilder;
import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.app.HRegistry;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.mod.ambient.atom.AtConfig;
import io.vertx.up.boot.di.DiPlugin;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import static io.vertx.mod.ambient.refine.At.LOG;

/*
 * Prefix + c + feature keyword
 */
public class AtInit implements HRegistry.Mod<Vertx> {

    private static final DiPlugin PLUGIN = DiPlugin.create(AtInit.class);

    @Override
    public Future<Boolean> registryAsync(final Vertx container, final HArk ark) {
        final AtConfig config = AtConfiguration.getConfig();
        final boolean disabled = Ut.isNil(config.getFileIntegration());
        if (disabled) {
            LOG.Init.info(this.getClass(), "Document Platform Disabled !!");
            return Ux.futureF();
        }
        // 此处提前调用 initialize 方法，此方法保证无副作用的多次调用即可
        final DocBStub docStub = PLUGIN.createSingleton(DocBuilder.class);
        // Here mapApp function extract `appId`
        final HApp app = ark.app();
        final String appId = app.appId(); // Ut.valueString(appJ, KName.KEY);
        return docStub.initialize(appId, config.getFileIntegration()).compose(initialized -> {
            LOG.Init.info(this.getClass(), "AppId = {0}, Directory Size = {1}", appId, String.valueOf(initialized.size()));
            return Ux.futureT();
        });
    }
}
