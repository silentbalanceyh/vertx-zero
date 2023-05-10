package io.vertx.tp.ambient.init;

import cn.vertxup.ambient.service.file.DocBStub;
import cn.vertxup.ambient.service.file.DocBuilder;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.tp.ambient.atom.AtConfig;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.di.DiPlugin;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import static io.vertx.tp.ambient.refine.At.LOG;

/*
 * Prefix + c + feature keyword
 */
public class AtInit {

    private static final Annal LOGGER = Annal.get(AtInit.class);

    private static final DiPlugin PLUGIN = DiPlugin.create(AtInit.class);

    public static Future<Boolean> initDocument(final Vertx vertx) {
        return Ke.mapApp(appJ -> {
            final AtConfig config = AtConfiguration.getConfig();
            final boolean disabled = Ut.isNil(config.getFileIntegration());
            if (disabled) {
                LOG.Init.info(LOGGER, "Document Platform Disabled !!");
                return Ux.futureF();
            }
            // 此处提前调用 initialize 方法，此方法保证无副作用的多次调用即可
            final DocBStub docStub = PLUGIN.createComponent(DocBuilder.class);
            // Here mapApp function extract `appId`
            final String appId = Ut.valueString(appJ, KName.KEY);
            return docStub.initialize(appId, config.getFileIntegration()).compose(initialized -> {
                LOG.Init.info(LOGGER, "AppId = {0}, Directory Size = {1}", appId, String.valueOf(initialized.size()));
                return Ux.futureT();
            });
        }, (result) -> Ux.future(result.stream().allMatch(item -> item)));
    }
}
