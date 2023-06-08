package io.vertx.mod.ambient.uca.digital;

import cn.vertxup.ambient.domain.tables.daos.XTabularDao;
import io.horizon.eon.VValue;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractAide implements Aide {

    // --------------- fetchArray method ------------------

    /* X_TABULAR */
    protected Future<JsonArray> fetchDict(final JsonObject criteria) {
        return Ux.Jooq.on(XTabularDao.class).fetchAsync(criteria)
            .compose(Ux::futureA)
            .compose(Fn.ofJArray(KName.METADATA));
    }

    // --------------- condition building -----------------
    protected JsonObject condApp(final String appId, final JsonArray types, final String code) {
        final JsonObject criteria = Ux.whereAnd();
        criteria.put(KName.APP_ID, appId);
        this.condition(criteria, types, code);
        return criteria;
    }

    protected JsonObject condApp(final String appId, final String type, final String code) {
        return this.condApp(appId, new JsonArray().add(type), code);
    }

    protected JsonObject condSigma(final String sigma, final JsonArray types, final String code) {
        final JsonObject criteria = Ux.whereAnd();
        criteria.put(KName.SIGMA, sigma);
        this.condition(criteria, types, code);
        return criteria;
    }

    protected JsonObject condSigma(final String sigma, final String type, final String code) {
        return this.condSigma(sigma, new JsonArray().add(type), code);
    }

    // -------------- private criteria --------------------
    private void condition(final JsonObject criteria, final JsonArray types, final String code) {
        Objects.requireNonNull(criteria);
        final JsonArray typeArray = Ut.valueJArray(types);
        criteria.put(KName.ACTIVE, Boolean.TRUE);
        if (VValue.ONE == typeArray.size()) {
            final String firstArg = typeArray.getString(VValue.IDX);
            // ACTIVE = TRUE AND TYPE = ?
            criteria.put(KName.TYPE, firstArg);
            if (Ut.isNotNil(code)) {
                /*
                 * Conflict to multi types, this code logical is
                 * available when you fetch one record only
                 *
                 * ACTIVE = TRUE AND TYPE = ? AND CODE = ?
                 * */
                criteria.put(KName.CODE, code);
            }
        } else {
            // ACTIVE = TRUE AND TYPE IN (?, ?)
            criteria.put(KName.TYPE + ",i", types);
        }
    }
}
