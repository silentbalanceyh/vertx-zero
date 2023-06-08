package cn.vertxup.battery.service;

import cn.vertxup.battery.domain.tables.daos.BBagDao;
import cn.vertxup.battery.domain.tables.daos.BBlockDao;
import cn.vertxup.battery.domain.tables.pojos.BBag;
import cn.vertxup.battery.domain.tables.pojos.BBlock;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.battery.atom.PowerApp;
import io.vertx.mod.battery.uca.configure.Combiner;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public class BagArgService implements BagArgStub {
    private static final ConcurrentMap<String, Combiner> POOL_COMBINER = new ConcurrentHashMap<>();

    @Override
    public Future<JsonObject> fetchBagConfig(final String bagAbbr) {
        Objects.requireNonNull(bagAbbr);
        final JsonObject condition = Ux.whereAnd("nameAbbr", bagAbbr);
        final UxJooq jq = Ux.Jooq.on(BBagDao.class);
        return jq.<BBag>fetchOneAsync(condition).compose(bag -> {
            /* Check if root by parentId */
            if (Objects.isNull(bag)) {
                return Ux.futureJ();
            }
            if (Objects.isNull(bag.getParentId())) {
                /* Multi Bag Information */
                final JsonObject criteria = Ux.whereAnd();
                criteria.put(KName.PARENT_ID, bag.getKey());
                criteria.put(KName.ACTIVE, Boolean.TRUE);
                return jq.<BBag>fetchAsync(criteria).compose(query -> {
                    final ConcurrentMap<String, BBag> map = Ut.elementMap(query, BBag::getNameAbbr);
                    final Combiner<BBag, ConcurrentMap<String, BBag>> combiner = Combiner.forBag();
                    return combiner.configure(bag, map);
                });
            } else {
                /* Single Bag Information */
                final JsonObject response = Ut.toJObject(bag.getUiConfig());
                final Combiner<JsonObject, BBag> combiner = Combiner.outBag();
                return combiner.configure(response, bag);
            }
        });
    }

    @Override
    public Future<JsonObject> fetchBag(final String bagAbbr) {
        Objects.requireNonNull(bagAbbr);
        final JsonObject condition = Ux.whereAnd("nameAbbr", bagAbbr);
        return Ux.Jooq.on(BBagDao.class).<BBag>fetchOneAsync(condition).compose(bag -> {
            if (Objects.isNull(bag)) {
                return Ux.futureJ();
            }
            return this.seekBlocks(bag).compose(blocks -> {
                final Combiner<BBag, List<BBlock>> combiner = Combiner.forBlock();
                return combiner.configure(bag, blocks);
            });
        });
    }


    @Override
    public Future<JsonObject> saveBag(final String bagId, final JsonObject data) {
        Objects.requireNonNull(bagId);
        return Ux.Jooq.on(BBagDao.class).<BBag>fetchByIdAsync(bagId)
            // Cache Processing
            .compose(Fn.ofJObject(bag -> this.saveConfigure(bag, data)));
    }

    @Override
    public Future<JsonObject> saveBagBy(String nameAbbr, JsonObject data) {
        Objects.requireNonNull(nameAbbr);
        return Ux.Jooq.on(BBagDao.class).<BBag>fetchOneAsync("nameAbbr", nameAbbr)
            // Cache Processing
            .compose(Fn.ofJObject(bag -> this.saveConfigure(bag, data)));
    }

    private Future<JsonObject> saveConfigure(final BBag bag, final JsonObject data) {
        // Cache flush
        final BlockStub blockStub = Ut.singleton(BlockService.class);
        return this.seekBlocks(bag)
            // Parameters Store Code Logical
            .compose(blocks -> blockStub.saveParameters(blocks, data))
            // Refresh Cache of appId
            .compose(config -> PowerApp.flush(bag.getAppId())
                .compose(nil -> Ux.future(config)));
    }

    @Override
    public Future<List<BBlock>> seekBlocks(final BBag input) {
        return Ux.future(input).compose(bag -> {
            final JsonObject condition = Ux.whereAnd();
            if (Objects.isNull(bag.getParentId())) {
                final JsonObject criteria = Ux.whereAnd();
                criteria.put(KName.PARENT_ID, bag.getKey());
                criteria.put(KName.ACTIVE, Boolean.TRUE);
                return Ux.Jooq.on(BBagDao.class).<BBag>fetchAsync(criteria).compose(bags -> {
                    final Set<String> keys = Ut.elementSet(bags, BBag::getKey);
                    condition.put(KName.App.BAG_ID + ",i", Ut.toJArray(keys));
                    return Ux.future(condition);
                });
            } else {
                condition.put(KName.App.BAG_ID, bag.getKey());
                return Ux.future(condition);
            }
        }).compose(condition -> {
            if (Objects.isNull(condition)) {
                return Ux.futureL();
            } else {
                return Ux.Jooq.on(BBlockDao.class).fetchAsync(condition);
            }
        });
    }
}
