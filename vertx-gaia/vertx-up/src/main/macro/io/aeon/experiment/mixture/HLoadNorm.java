package io.aeon.experiment.mixture;

import io.aeon.experiment.shape.internal.NormAtom;
import io.aeon.experiment.shape.internal.NormModel;
import io.horizon.uca.boot.KPivot;
import io.horizon.uca.cache.Cc;
import io.macrocosm.specification.app.HAmbient;
import io.macrocosm.specification.program.HArk;
import io.modello.specification.action.HLoad;
import io.modello.specification.atom.HAtom;
import io.modello.specification.atom.HModel;
import io.vertx.up.exception.web._404ModelNotFoundException;
import io.vertx.up.exception.web._409IdentifierConflictException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HLoadNorm implements HLoad {
    private static final Cc<String, HModel> CC_MODEL = Cc.open();

    @Override
    public HAtom atom(final String appName, final String identifier) {
        try {
            final HAmbient ambient = KPivot.running();
            final HArk ark = ambient.running(appName);
            final String unique = ark.cached(identifier);
            final HModel model = CC_MODEL.pick(() -> new NormModel(ark, identifier), unique);
            return new NormAtom(model);
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
