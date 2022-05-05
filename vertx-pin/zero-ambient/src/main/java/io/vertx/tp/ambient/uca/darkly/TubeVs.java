package io.vertx.tp.ambient.uca.darkly;

import cn.vertxup.ambient.domain.tables.pojos.XActivityRule;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.experiment.mixture.HAtom;
import io.vertx.up.uca.compare.Vs;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class TubeVs extends AbstractTube {
    @Override
    public Future<JsonObject> traceAsync(final JsonObject data, final XActivityRule rule) {
        final HAtom atom = this.atom(rule);
        final Vs vs = atom.vs();
        // Processing the data
        final JsonObject dataN = Ut.aiDataN(data);
        final JsonObject dataO = Ut.aiDataO(data);

        return Ux.future(data);
    }
}
