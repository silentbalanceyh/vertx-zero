package cn.vertxup.atom.service;

import io.vertx.tp.atom.cv.AoCache;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.up.uca.cache.Cd;

public class DataService implements DataStub {
    @Override
    public Boolean cleanDataAtom(final String key) {
        final Cd<String, Model> cdModel = AoCache.CC_MODEL.store();
        cdModel.clear(key);
        // AoCache.POOL_ATOM.remove(key);
        return Boolean.TRUE;
    }
}
