package io.vertx.up.eon;

import io.horizon.eon.VValue;

/**
 * @author lang : 2023/4/24
 */
public interface KWeb {

    String JOB_PREFIX = "jobs";

    interface DFT extends VValue.DFT {
        String VERTX_GROUP = "__VERTX_ZERO__";

    }
}
