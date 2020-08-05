package io.vertx.tp.rbac.acl.rapid;

import io.vertx.tp.rbac.cv.em.RegionType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<RegionType, DataDwarf> DWARF_POOL =
            new ConcurrentHashMap<>();
}
