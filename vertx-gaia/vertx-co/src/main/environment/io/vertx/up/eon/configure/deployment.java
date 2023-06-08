package io.vertx.up.eon.configure;

import io.horizon.eon.VName;

/**
 * @author lang : 2023-05-29
 */
interface YmlDeployment {
    String __KEY = "deployment";
    String MODE = "mode";
    String OPTIONS = VName.OPTIONS;
    String DELIVERY = "delivery";
}
