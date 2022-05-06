package io.vertx.tp.ambient.uca.darkly;

import cn.vertxup.ambient.domain.tables.pojos.XActivityRule;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.em.TubeType;
import io.vertx.tp.ambient.refine.At;
import io.vertx.up.experiment.mixture.HAtom;
import io.vertx.up.uca.compare.Vs;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

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
        final String field = rule.getRuleField();
        Objects.requireNonNull(field);
        if (vs.isChange(dataO.getValue(field), dataN.getValue(field), field)) {
            final Tube tube = Tube.instance(TubeType.ATOM);
            return tube.traceAsync(data, rule);
        } else {
            At.infoFlow(this.getClass(), "The field = {0} of Atom (  identifier = {1} ) has not been changed!",
                field, atom.identifier());
            return Ux.future(data);
        }
    }
}
