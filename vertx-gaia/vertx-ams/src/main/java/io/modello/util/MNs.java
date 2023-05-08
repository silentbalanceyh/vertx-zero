package io.modello.util;

import io.horizon.spi.modeler.AtomNs;
import io.horizon.util.HUt;

/**
 * @author lang : 2023-05-08
 */
class MNs {
    private MNs() {
    }

    static String namespace(final String appName, final String identifier) {
        // 严格模式处理
        final AtomNs atomNs = HUt.service(AtomNs.class, true);
        if (HUt.isNil(identifier)) {
            return atomNs.ns(appName);
        } else {
            return atomNs.ns(appName, identifier);
        }
    }
}
