package io.horizon.spi.extension;

import io.vertx.up.uca.cache.Cc;

interface Pool {


    Cc<String, Init> CC_INIT = Cc.open();

    Cc<String, Prerequisite> CC_PREREQUISITE = Cc.open();
}
