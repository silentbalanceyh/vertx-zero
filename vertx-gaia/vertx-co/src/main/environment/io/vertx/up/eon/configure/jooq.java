package io.vertx.up.eon.configure;

import io.horizon.eon.VOption;

/**
 * @author lang : 2023-05-29
 */
interface YmlJooq {
    String __KEY = "jooq";
    String ORBIT = "orbit";     // 历史库
    String PROVIDER = "provider";   // 正常库

    interface orbit extends VOption.database {

    }

    interface provider extends VOption.database {

    }
}
