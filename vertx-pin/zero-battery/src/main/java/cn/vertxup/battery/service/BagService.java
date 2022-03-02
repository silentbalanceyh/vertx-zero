package cn.vertxup.battery.service;

import cn.vertxup.battery.domain.tables.daos.BBagDao;
import cn.vertxup.battery.domain.tables.daos.BBlockDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.battery.cv.BkCv;
import io.vertx.up.atom.Refer;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BagService implements BagStub {
    @Override
    public Future<JsonArray> fetchBag(final String appId) {
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.APP_ID, appId);

        final Refer bagRef = new Refer();
        return Ux.Jooq.on(BBagDao.class).fetchJAsync(condition)
            .compose(bagRef::future)

            .compose(response -> Ux.Jooq.on(BBlockDao.class).fetchJAsync(condition))
            // Block Processing
            .compose(Ut.ifJArray(
                KName.Flow.UI_STYLE,
                KName.Flow.UI_CONFIG,
                BkCv.License.LIC_IDENTIFIER,
                BkCv.License.LIC_MENU
            ))
            .compose(blockArray -> {
                final ConcurrentMap<String, JsonArray> blocks = Ut.elementGroup(blockArray, KName.App.BAG_ID);
                final JsonArray response = bagRef.get();
                Ut.itJArray(response).forEach(json -> {
                    final String bagKey = json.getString(KName.KEY);
                    if (blocks.containsKey(bagKey)) {
                        json.put(KName.App.BLOCK, blocks.get(bagKey));
                    } else {
                        json.put(KName.App.BLOCK, new JsonArray());
                    }
                });
                return Ux.future(response);
            });
    }
}
