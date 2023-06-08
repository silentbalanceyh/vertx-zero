package cn.vertxup.atom.service;

import io.vertx.mod.atom.cv.AoCache;
import io.vertx.mod.atom.modeling.Model;

import java.util.concurrent.ConcurrentMap;

public class DataService implements DataStub {
    @Override
    public Boolean cleanDataAtom(final String key) {
        final ConcurrentMap<String, Model> cdModel = AoCache.CC_MODEL.store();
        cdModel.remove(key);
        // AoCache.POOL_ATOM.remove(key);
        return Boolean.TRUE;
    }
}
