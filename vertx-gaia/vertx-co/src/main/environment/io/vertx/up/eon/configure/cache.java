package io.vertx.up.eon.configure;

import io.horizon.eon.VOption;

/**
 * @author lang : 2023-05-29
 */
interface YmlCache {
    String __KEY = "cache";
    String L1 = "l1";
    String L2 = "l2";
    String L3 = "l3";

    interface l1 extends VOption.component {
        String OPTIONS = "options";
        String MATRIX = "matrix";
        String WORKER = "worker";
        String ADDRESS = "address";
    }
}
