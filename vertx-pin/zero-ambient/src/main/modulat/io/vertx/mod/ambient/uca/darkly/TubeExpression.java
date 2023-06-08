package io.vertx.mod.ambient.uca.darkly;

import cn.vertxup.ambient.domain.tables.pojos.XActivityRule;
import io.modello.specification.atom.HAtom;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.uca.differ.Schism;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TubeExpression extends AbstractTube {
    @Override
    public Future<JsonObject> traceAsync(final JsonObject data, final XActivityRule rule) {
        /*
         * No default condition checking here, execute workflow
         * activity generation directly, it's different from TubeAtom
         */
        return this.newActivity(data, rule).compose(activity -> {
            /*
             * For Activity Generation
             * 1) Extract `HAtom` for model
             * 2) Extract data to ( NEW / OLD ) data twins ( Refactor )
             * 3) Build default Activity JsonObject and ActivityChange
             */
            final HAtom atom = this.atom(rule);
            /*
             * - modelId
             */
            activity.setModelId(atom.identifier());
            final Schism diffJ = Schism.diffJ(atom);
            final JsonObject dataN = Ut.aiDataN(data);
            final JsonObject dataO = Ut.aiDataO(data);

            return diffJ.diffAsync(dataO, dataN, () -> Ux.future(activity));
        });
    }
}
