package io.vertx.tp.ambient.uca.dict;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.em.GlossaryType;
import io.vertx.up.util.Ut;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class DpmTool {

    static final ConcurrentMap<GlossaryType, Dpm> POOL_DPM = new ConcurrentHashMap<GlossaryType, Dpm>() {
        {
            this.put(GlossaryType.ASSIST, Ut.instance(DpmAssist.class));
            this.put(GlossaryType.CATEGORY, Ut.instance(DpmCategory.class));
            this.put(GlossaryType.TABULAR, Ut.instance(DpmTabular.class));
            this.put(GlossaryType.DAO, Ut.instance(DpmDao.class));
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
            condition.put(Strings.EMPTY, Boolean.TRUE);
        }
        return condition;
    }
}
