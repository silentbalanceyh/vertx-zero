package io.vertx.mod.ambient.uca.dict;

import io.horizon.eon.VString;
import io.horizon.eon.em.EmDict;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class DpmTool {

    static final ConcurrentMap<EmDict.Type, Dpm> POOL_DPM = new ConcurrentHashMap<EmDict.Type, Dpm>() {
        {
            this.put(EmDict.Type.ASSIST, Ut.instance(DpmAssist.class));
            this.put(EmDict.Type.CATEGORY, Ut.instance(DpmCategory.class));
            this.put(EmDict.Type.TABULAR, Ut.instance(DpmTabular.class));
            this.put(EmDict.Type.DAO, Ut.instance(DpmDao.class));
        }
    };

    /**
     * Build condition for `X_CATEGORY, X_TABULAR` etc.
     *
     * @param params  {@link MultiMap} The parameters map that came from vert.x
     * @param typeSet {@link Set<String>} The definition of dict source.
     *
     * @return {@link JsonObject} Return to json data with criteria format
     */
    static JsonObject condition(final MultiMap params, final Set<String> typeSet) {
        /* Result */
        final JsonObject condition = new JsonObject();
        /* Sigma for each application */
        final String sigma = params.get(KName.SIGMA);
        condition.put(KName.SIGMA, sigma);

        /* Types */
        if (!typeSet.isEmpty()) {
            condition.put(KName.TYPE + ",i", Ut.toJArray(typeSet));
            condition.put(VString.EMPTY, Boolean.TRUE);
        }
        return condition;
    }
}
