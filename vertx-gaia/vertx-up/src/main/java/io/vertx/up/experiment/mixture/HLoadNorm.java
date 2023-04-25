package io.vertx.up.experiment.mixture;

import io.aeon.experiment.specification.power.KApp;
import io.aeon.specification.app.HES;
import io.horizon.specification.modeler.HAtom;
import io.horizon.specification.modeler.HModel;
import io.vertx.up.exception.web._404ModelNotFoundException;
import io.vertx.up.exception.web._409IdentifierConflictException;
import io.vertx.up.experiment.shape.internal.NormAtom;
import io.vertx.up.experiment.shape.internal.NormModel;
import io.vertx.up.uca.cache.Cc;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HLoadNorm implements HLoad {
    private static final Cc<String, HModel> CC_MODEL = Cc.open();

    @Override
    public HAtom atom(final String appName, final String identifier) {
        try {
            /*
             * KApp building based on `appName` here
             * Internal Object to store application information
             * - sigma
             * - language
             * - appName
             * - ns
             */
            final KApp app = HES.connect(appName);

            // Fetch HModel
            final String unique = app.keyUnique(identifier);
            final HModel model = CC_MODEL.pick(() -> new NormModel(app, identifier), unique);

            // Fetch HAtom
            // final HAtom atom = new NormAtom(app, model);
            // Remove Following Logs
            // LOGGER.info("Model ( Norm ) Information：<namespace>.<identifier> = {0}", unique);
            return new NormAtom(app, model);
        } catch (final _404ModelNotFoundException | _409IdentifierConflictException ignored) {
            /*
             * 这里的改动主要基于动静态模型同时操作导致，如果可以找到Model则证明模型存在于系统中，这种
             * 情况下可直接初始化DataAtom走标准流程，否则直接返回null引用，使得系统无法返回正常模型，
             * 但不影响模型本身的执行。
             */
            return null;
        }
    }
}
