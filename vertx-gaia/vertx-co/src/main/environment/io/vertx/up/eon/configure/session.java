package io.vertx.up.eon.configure;

import io.horizon.eon.VName;

/**
 * @author lang : 2023-05-29
 */
interface YmlSession {
    String CONFIG = VName.CONFIG;

    interface config {
        String CATEGORY = VName.CATEGORY;
        String STORE = VName.STORE;
        String OPTIONS = VName.OPTIONS;
    }
}
