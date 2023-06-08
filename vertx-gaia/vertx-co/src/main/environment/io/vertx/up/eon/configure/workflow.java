package io.vertx.up.eon.configure;

import io.horizon.eon.VOption;

/**
 * @author lang : 2023-05-29
 */
interface YmlWorkflow {
    String __KEY = "workflow";

    String NAME = "name";
    String BUILT_IN = "builtIn";
    String RESOURCE = "resource";
    String DATABASE = "database";

    interface database extends VOption.database {

    }
}
