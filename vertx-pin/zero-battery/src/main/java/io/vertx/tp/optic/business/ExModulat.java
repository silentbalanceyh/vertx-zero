package io.vertx.tp.optic.business;

import cn.vertxup.battery.domain.tables.daos.BBagDao;
import cn.vertxup.battery.domain.tables.pojos.BBag;
import cn.vertxup.battery.domain.tables.pojos.BBlock;
import cn.vertxup.battery.service.BagArgService;
import cn.vertxup.battery.service.BagArgStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.battery.refine.Bk;
import io.vertx.tp.battery.uca.configure.Combiner;
import io.vertx.tp.optic.feature.Modulat;
import io.vertx.up.atom.record.Apt;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ExModulat implements Modulat {
    @Override
    public Future<JsonObject> extension(final JsonObject appJson) {
        final String key = appJson.getString(KName.KEY);
        return Ux.future(Apt.create(appJson.copy(), this.extension(key).result()).dataI());
    }

    @Override
    public Future<JsonObject> extension(final String appId) {
        Objects.requireNonNull(appId);
        return this.fetchModule(appId).compose(map -> {
            final JsonObject appJson = new JsonObject();
            appJson.put(KName.KEY, appId);
            map.forEach(appJson::put);
            return Ux.future(appJson);
        });
    }

    private Future<ConcurrentMap<String, JsonObject>> fetchModule(final String appId) {
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.TYPE, "EXTENSION");
        condition.put(KName.APP_ID, appId);
        condition.put(KName.PARENT_ID + ",n", null);
        Bk.Log.infoChannel(this.getClass(), "Modulat condition = {0}", condition.encode());
        return Ux.Jooq.on(BBagDao.class).<BBag>fetchAsync(condition).compose(bags -> {
            final ConcurrentMap<String, Future<JsonObject>> futures = new ConcurrentHashMap<>();
            bags.forEach(bag -> {
                final JsonObject uiConfig = Ut.toJObject(bag.getUiConfig());
                final String field = uiConfig.getString(KName.STORE);
                if (Ut.notNil(field)) {
                    futures.put(field, this.fetchData(bag));
                }
            });
            return Ux.thenCombine(futures);
        });
    }

    private Future<JsonObject> fetchData(final BBag bag) {
        final BagArgStub stub = Ut.singleton(BagArgService.class);
        return stub.seekBlocks(bag).compose(blocks -> {
            final Combiner<BBag, List<BBlock>> combiner = Combiner.forBlock();
            return combiner.configure(bag, blocks);
        });
    }
}
