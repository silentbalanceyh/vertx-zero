package cn.vertxup.rbac.service.batch;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<String, IdcRole> ROLE = new ConcurrentHashMap<>();

    ConcurrentMap<String, IdcStub> STUBS = new ConcurrentHashMap<>();
}
