package cn.vertxup.atom.service;

import io.vertx.tp.atom.cv.AoCache;

public class DataService implements DataStub {
    @Override
    public Boolean cleanDataAtom(final String key) {
        AoCache.POOL_MODELS.remove(key);
        // AoCache.POOL_ATOM.remove(key);
        return Boolean.TRUE;
    }
}
