package io.vertx.tp.optic.mixture;

import io.vertx.tp.atom.cv.AoCache;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.exception.web._404ModelNotFoundException;
import io.vertx.up.experiment.mixture.HAtom;
import io.vertx.up.experiment.mixture.HLoad;
import io.vertx.up.experiment.specification.power.KApp;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HLoadAtom implements HLoad {

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
            final KApp app = KApp.context(appName).bind(Ao.toNS(appName));
            // H3H.CC_APP.pick(() -> new KApp(appName).bind(Ao.toNS(appName)), appName);

            // Fetch HModel
            final String unique = app.keyUnique(identifier); // Model.namespace(appName) + "-" + identifier;
            final AoPerformer performer = AoPerformer.getInstance(appName);
            final Model model = AoCache.CC_MODEL.pick(() -> performer.fetch(identifier), unique);

            // Fetch HAtom
            // final DataAtom atom = new DataAtom(app, model);
            // Remove following Log
            // Ao.infoAtom(DataAtom.class, AoMsg.DATA_ATOM, unique, model.toJson().encode());
            return new DataAtom(app, model);
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
