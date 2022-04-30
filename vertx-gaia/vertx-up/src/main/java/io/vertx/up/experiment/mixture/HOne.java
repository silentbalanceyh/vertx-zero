package io.vertx.up.experiment.mixture;

import io.vertx.core.MultiMap;
import io.vertx.up.experiment.specification.KModule;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HOne<T> {

    /*
     * This method is for different combining operation between two
     * KModules
     * 1) The main KModule      module
     * 2) The joined KModule    connect
     */
    T combine(KModule module, KModule connect, MultiMap headers);

    default T combine(final KModule module, final MultiMap configuration) {
        return this.combine(module, null, configuration);
    }

    default T combine(final KModule module, final KModule connect) {
        return this.combine(module, connect, MultiMap.caseInsensitiveMultiMap());
    }
}
