package cn.vertxup.ambient.service.application;

import cn.vertxup.ambient.domain.tables.daos.XMenuDao;
import cn.vertxup.ambient.domain.tables.daos.XMenuMyDao;
import cn.vertxup.ambient.domain.tables.pojos.XMenuMy;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.refine.At;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MenuService implements MenuStub {

    @Override
    public Future<JsonArray> fetchByApp(final String appId) {
        return Ux.Jooq.on(XMenuDao.class)
            .fetchJAsync(KName.APP_ID, appId)
            // metadata field extraction
            .compose(Ut.ifJArray(KName.METADATA));
    }

    @Override
    public Future<JsonArray> fetchMy(final JsonObject condition) {
        condition.put(Strings.EMPTY, Boolean.TRUE);
        At.infoFlow(this.getClass(), "My menu condition: {0}", condition.encode());
        return Ux.Jooq.on(XMenuMyDao.class).fetchJAsync(condition);
    }

    /*
     * {
     *      "owner": "xxx",
     *      "page": "",
     *      "position": "",
     *      "type": ""
     *      "menus": [
     *      ]
     * }
     */
    @Override
    public Future<JsonArray> saveMy(final JsonObject condition, final JsonArray data) {
        At.infoFlow(this.getClass(), "My menu saving: {0}, data = {1}",
            condition.encode(), data.encode());
        final UxJooq jooq = Ux.Jooq.on(XMenuMyDao.class);
        return jooq.deleteByAsync(condition).compose(removed -> {
            final List<XMenuMy> menus = Ux.fromJson(data, XMenuMy.class);
            return jooq.insertJAsync(menus);
        });
    }
}
