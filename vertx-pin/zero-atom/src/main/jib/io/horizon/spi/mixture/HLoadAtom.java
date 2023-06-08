package io.horizon.spi.mixture;

import io.horizon.uca.boot.KPivot;
import io.macrocosm.specification.app.HAmbient;
import io.macrocosm.specification.program.HArk;
import io.modello.specification.action.HLoad;
import io.modello.specification.atom.HAtom;
import io.vertx.mod.atom.cv.AoCache;
import io.vertx.mod.atom.modeling.Model;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.up.exception.web._404ModelNotFoundException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HLoadAtom implements HLoad {

    @Override
    public HAtom atom(final String appName, final String identifier) {
        try {

            final HAmbient ambient = KPivot.running();
            final HArk ark = ambient.running(appName);
            // ark.app().connect(Ao.toNS(appName));
            final String unique = ark.cached(identifier);
            final AoPerformer performer = AoPerformer.getInstance(appName);
            final Model model = AoCache.CC_MODEL.pick(() -> performer.fetch(identifier), unique);
            return new DataAtom(model);
        } catch (final _404ModelNotFoundException ignored) {
            /*
             * 这里的改动主要基于动静态模型同时操作导致，如果可以找到Model则证明模型存在于系统中，这种
             * 情况下可直接初始化DataAtom走标准流程，否则直接返回null引用，使得系统无法返回正常模型，
             * 但不影响模型本身的执行。
             */
            return null;
        }
    }
}
