package io.vertx.mod.rbac.acl.rapid;

import io.horizon.uca.cache.Cc;
import io.vertx.mod.rbac.cv.em.RegionType;

interface Pool {
    Cc<RegionType, Dwarf> CC_DWARF = Cc.open();

    Cc<String, Dwarf> CC_ADDON = Cc.openThread();
}