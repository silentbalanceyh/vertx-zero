package io.vertx.up.eon.configure;

import io.horizon.eon.VOption;

/**
 * @author lang : 2023-05-29
 */
interface YmlExtension extends VOption.component {
    String __KEY = "extension";
    String REGION = "region";
    String AUDITOR = "auditor";
    String ATOM = "atom";
    String ETCD = "etcd";

    interface region extends VOption.component {
        interface config {
            String PREFIX = "prefix";
        }
    }

    interface auditor extends VOption.component {
        interface config {
            String INCLUDE = "include";
            String EXCLUDE = "exclude";
        }
    }
}
