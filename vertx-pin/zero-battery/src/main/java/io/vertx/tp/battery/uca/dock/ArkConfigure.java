package io.vertx.tp.battery.uca.dock;

import cn.vertxup.battery.domain.tables.daos.BBagDao;
import cn.vertxup.battery.domain.tables.pojos.BBag;
import io.vertx.aeon.eon.em.TypeBy;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;
import io.vertx.tp.battery.refine.Bk;
import io.vertx.tp.ke.cv.em.TypeBag;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class ArkConfigure extends AbstractArk {
    @Override
    public Future<ClusterSerializable> modularize(final String appId, final TypeBy by) {
        final JsonObject condition = this.buildQr(appId, TypeBag.EXTENSION, by);
        condition.put(KName.PARENT_ID + ",n", null);
        Bk.Log.infoChannel(this.getClass(), "Modulat condition = {0}", condition.encode());
        return Ux.Jooq.on(BBagDao.class).<BBag>fetchAsync(condition).compose(bags -> {
            final ConcurrentMap<String, Future<JsonObject>> futures = new ConcurrentHashMap<>();
            bags.forEach(bag -> {
                final JsonObject uiConfig = Ut.toJObject(bag.getUiConfig());
                final String configKey = Ut.valueString(uiConfig, KName.STORE);
                if (Ut.notNil(configKey)) {
                    futures.put(configKey, this.configureBag(bag));
                }
            });
            return Fn.combineM(futures);
        }).compose(map -> Ux.future(Ut.toJObject(map)));
    }
}
