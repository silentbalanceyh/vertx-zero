package io.vertx.mod.ambient.uca.digital;

import cn.vertxup.ambient.domain.tables.daos.XTabularDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AideSigma extends AbstractAide {
    /*
     * Field = APP_ID
     */
    @Override
    public Future<JsonArray> fetch(final String field, final JsonArray types) {
        return this.fetchDict(this.condSigma(field, types, null));
    }

    @Override
    public Future<JsonObject> fetch(final String field, final String type, final String code) {
        return Ux.Jooq.on(XTabularDao.class)
            .fetchOneAsync(this.condSigma(field, type, code))
            .compose(Ux::futureJ).compose(Fn.ofJObject(KName.METADATA));
    }
}
