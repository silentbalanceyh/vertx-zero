package io.vertx.tp.rbac.acl.rapid;

import io.vertx.tp.rbac.cv.em.RegionType;
import io.horizon.uca.cache.Cc;

interface Pool {
    Cc<RegionType, Dwarf> CC_DWARF = Cc.open();

    Cc<String, Dwarf> CC_ADDON = Cc.openThread();
}