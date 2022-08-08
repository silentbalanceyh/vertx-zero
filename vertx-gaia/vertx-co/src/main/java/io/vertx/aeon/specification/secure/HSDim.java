package io.vertx.aeon.specification.secure;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.aeon.specification.action.HEvent;
import io.vertx.core.json.JsonObject;
import io.vertx.up.experiment.mixture.HAtom;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HSDim extends HEvent<HPermit, JsonObject> {
    default HSDim bind(final HAtom atom) {
        return this;
    }
}
